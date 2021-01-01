package com.lk.engine.soccer.elements.players;

import com.lk.engine.common.console.params.PlayerParams;
import com.lk.engine.common.core.MovingEntity;
import com.lk.engine.common.core.Named;
import com.lk.engine.common.core.Region;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.Players;
import com.lk.engine.soccer.elements.team.Team;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.lk.engine.common.d2.Vector2D.*;
import static com.lk.engine.common.telegraph.Message.GO_HOME;
import static com.lk.engine.common.telegraph.Message.SUPPORT_ATTACKER;
import static java.lang.Math.abs;

abstract public class Player<T extends PlayerParams>
		extends MovingEntity<T>
		implements StateMachineOwner, Named {

	public enum PlayerRole {
		GOALKEEPER, ATTACKER, DEFENDER
	}

	private StateMachine stateMachine;

	// this player's role in the team
	protected PlayerRole playerRole;
	// a pointer to this player's team
	protected Team team;
	// the steering behaviors
	protected SteeringBehaviors steering;
	// the region that this player is assigned to.
	protected int homeRegion;
	// the region this player moves to before kickoff
	protected int defaultRegion;
	// the distance to the ball (in squared-space). This value is queried
	// a lot so it's calculated once each time-step and stored here.
	protected double distSqToBall = Double.MAX_VALUE;
	// the vertex buffer
	protected List<Vector2D> vecPlayerVB = new LinkedList<>();

	protected final Telegraph telegraph;
	protected final Players players;
	protected final PlayRegions regions;
	protected final Ball ball;

	public Player(final T params, final Telegraph telegraph, final Team team, final int homeRegion,
	    final Vector2D heading, final Vector2D velocity, final PlayerRole role, final Players players,
	    final PlayRegions regions, final Ball ball) {
		super(params, regions.get(homeRegion).center(), velocity, heading);
		this.regions = regions;
		this.telegraph = telegraph;
		this.team = team;
		this.players = players;
		this.defaultRegion = homeRegion;
		this.playerRole = role;
		this.ball = ball;

		// setup the vertex buffers and calculate the bounding radius
		final Vector2D[] player = {
				new Vector2D(-3, 8),
				new Vector2D(3, 10),
				new Vector2D(3, -10),
				new Vector2D(-3, -8)
		};

		for (Vector2D vector2D : player) {
			vecPlayerVB.add(vector2D);

			// set the bounding radius to the length of the
			// greatest extent
			if (abs(vector2D.x) > boundingRadius) {
				boundingRadius = abs(vector2D.x);
			}

			if (abs(vector2D.y) > boundingRadius) {
				boundingRadius = abs(vector2D.y);
			}
		}

		// set up the steering behavior class
		steering = new SteeringBehaviors(this, players, ball());

		// a player's start target is its start position (because it's just waiting)
		steering.setTarget(regions.get(homeRegion).center());
	}

	@Override
  public void debug(Debug debug) {
		debug.put("name", getName());
		super.debug(debug);
		debug.put("steering", steering);
		debug.put("fsn", getFSM());
  }

	@Override
	public String toString() {
		return getParams().getName();
	}

	/**
	 * @return true if the ball can be grabbed by the goalkeeper
	 */
	public boolean ballInPickupRange() {
		return (vec2DDistanceSq(pos(), ball().pos()) < getParams().getBallPickupRangeSq());
	}

	/**
	 * returns true if there is an opponent within this player's comfort zone
	 */
	public boolean isThreatened() {
		// check against all opponents to make sure non are within this player's
		// comfort zone
		return team()
				.opponents()
				.members()
				.stream()
				.anyMatch(opponent ->
						positionInFrontOfPlayer(opponent.pos()) &&
						vec2DDistanceSq(pos(), opponent.pos()) < getParams().getComfortZone());
	}

	/**
	 * rotates the player to face the ball
	 */
	public void trackBall() {
		rotateHeadingToFacePosition(ball().pos());
	}

	/**
	 * sets the player's heading to point at the current target
	 */
	public void trackTarget() {
		setHeading(vec2DNormalize(sub(steering().target(), pos())));
	}

	/**
	 * determines the player who is closest to the SupportSpot and messages him to
	 * tell him to change state to SupportAttacker
	 */
	public void findSupport() {
		// if there is no support we need to find a suitable player.
		if (team().supportingPlayer() == null) {
			final Player<?> bestSupportPly = team().determineBestSupportingAttacker();
			team().setSupportingPlayer(bestSupportPly);
			telegraph.post(new TelegramPackage(0, Id(), team().supportingPlayer().Id(), SUPPORT_ATTACKER, null));
			return;
		}

		final Player<?> bestSupportPly = team().determineBestSupportingAttacker();

		// if the best player available to support the ATTACKER changes, update
		// the pointers and send messages to the relevant players to update their
		// states
		if (bestSupportPly != null && (bestSupportPly != team().supportingPlayer())) {
			if (team().supportingPlayer() != null) {
				telegraph.post(new TelegramPackage(0, Id(), team().supportingPlayer().Id(), GO_HOME, null));
			}

			team().setSupportingPlayer(bestSupportPly);
			telegraph.post(new TelegramPackage(0, Id(), team().supportingPlayer().Id(), SUPPORT_ATTACKER, null));
		}
	}

	/**
	 * @return true if the ball is within kicking range
	 */
	public boolean canKickball() {
		return vec2DDistanceSq(ball().pos(), pos()) < getParams().getKickingDistanceSq();
	}

	/**
	 * @return true if a ball comes within range of a receiver
	 */
	public boolean ballWithinReceivingRange() {
		return (vec2DDistanceSq(pos(), ball().pos()) < getParams().getBallWithinReceivingRangeSq());
	}

	/**
	 * @return true if the player is located within the boundaries of his home
	 *         region
	 */
	public boolean inHomeRegion() {
		return regions.get(homeRegion).inside(pos(), Region.RegionModifier.HALF_SIZE);
	}

	/**
	 *
	 * @return true if this player is ahead of the ATTACKER
	 */
	public boolean isAheadOfAttacker() {
		return abs(pos().x() - team().opponents().goal().center().x()) < abs(team().controllingPlayer().pos().x()
		    - team().opponents().goal().center().x());
	}

	// returns true if a player is located at the designated support spot
	// bool AtSupportSpot()const;
	/**
	 * @return true if the player is located at his steering target
	 */
	public boolean atTarget() {
		return (vec2DDistanceSq(pos(), steering().target()) < getParams().getPlayerInTargetRangeSq());
	}

	/**
	 * @return true if the player is the closest player in his team to the ball
	 */
	public boolean isClosestTeamMemberToBall() {
		return team().playerClosestToBall() == this;
	}

	/**
	 * @param position
	 * @return true if the point specified by 'position' is located in front of
	 *         the player
	 */
	public boolean positionInFrontOfPlayer(final UVector2D position) {
		final Vector2D ToSubject = sub(position, pos());
		return ToSubject.dot(heading()) > 0;
	}

	/**
	 * @return true if the player is the closest player on the pitch to the ball
	 */
	public boolean isClosestPlayerOnPitchToBall() {
		return isClosestTeamMemberToBall() && (distSqToBall() < team().opponents().closestDistToBallSq());
	}

	/**
	 * @return true if this player is the controlling player
	 */
	public boolean isControllingPlayer() {
		return team().controllingPlayer() == this;
	}

	public PlayerRole role() {
		return playerRole;
	}/**/

	public double distSqToBall() {
		return distSqToBall;
	}

	public void setDistSqToBall(final double val) {
		distSqToBall = val;
	}

	/**
	 * Calculate distance to opponent's/home goal. Used frequently by the passing
	 * methods
	 */
	public double distToOppGoal() {
		return abs(pos().x() - team().opponents().goal().center().x());
	}

	public double distToHomeGoal() {
		return abs(pos().x() - team().goal().center().x());
	}

	public void setDefaultHomeRegion() {
		homeRegion = defaultRegion;
	}

	public Ball ball() {
		return ball;
	}

	public SteeringBehaviors steering() {
		return steering;
	}

	public Region homeRegion() {
		return regions.get(homeRegion);
	}

	public void attack() {
		homeRegion = getParams().getAttackRegion();
	}

	public void gotoKickoff() {
		homeRegion = getParams().getKickoffRegion();
	}

	public void defence() {
		homeRegion = getParams().getDefenseRegion();
	}


	public Team team() {
		return team;
	}

	/**
	 * binary predicates for std::sort (see CanPassForward/Backward)
	 */
	static public boolean sortByDistanceToOpponentsGoal(final Player<?> p1, final Player<?> p2) {
		return (p1.distToOppGoal() < p2.distToOppGoal());
	}

	static public boolean sortByReversedDistanceToOpponentsGoal(final Player<?> p1, final Player<?> p2) {
		return (p1.distToOppGoal() > p2.distToOppGoal());
	}

	public void updateTargetOfWaiting() {
	}

	public List<Vector2D> vecPlayerVB() {
		return vecPlayerVB;
	}

	public String getName() {
		return getParams().getName();
	}

	@Override
	public StateMachine getFSM() {
		return stateMachine;
	}

	@Override
	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public void changeTo(String name) {
	  this.stateMachine.changeTo(name);
  }

}
