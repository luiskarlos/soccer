/**
 *  Desc: Definition of a soccer player base class. <del>The player inherits
 *        from the autolist class so that any player created will be 
 *        automatically added to a list that is easily accesible by any
 *        other game objects.</del> (mainly used by the steering behaviors and
 *        player state classes)
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players;

import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DDistanceSq;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;
import static com.lk.engine.common.telegraph.Message.GO_HOME;
import static com.lk.engine.common.telegraph.Message.SUPPORT_ATTACKER;
import static java.lang.Math.abs;

import java.util.LinkedList;
import java.util.List;

import com.lk.engine.common.core.MovingEntity;
import com.lk.engine.common.core.Region;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.console.params.PlayerParams;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.Players;
import com.lk.engine.soccer.elements.team.Team;

abstract public class Player<T extends PlayerParams> extends MovingEntity<T> {
	public enum PlayerRole {
		GOALKEEPER, ATTACKER, DEFENDER
	};

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
	protected List<Vector2D> vecPlayerVB = new LinkedList<Vector2D>();

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
		this.homeRegion = homeRegion;
		this.defaultRegion = homeRegion;
		this.playerRole = role;
		this.ball = ball;

		// setup the vertex buffers and calculate the bounding radius
		final Vector2D player[] = { new Vector2D(-3, 8), new Vector2D(3, 10), new Vector2D(3, -10), new Vector2D(-3, -8) };
		final int numPlayerVerts = player.length;

		for (int vtx = 0; vtx < numPlayerVerts; ++vtx) {
			vecPlayerVB.add(player[vtx]);

			// set the bounding radius to the length of the
			// greatest extent
			if (abs(player[vtx].x) > boundingRadius) {
				boundingRadius = abs(player[vtx].x);
			}

			if (abs(player[vtx].y) > boundingRadius) {
				boundingRadius = abs(player[vtx].y);
			}
		}

		// set up the steering behavior class
		steering = new SteeringBehaviors(this, players, ball());

		// a player's start target is its start position (because it's just waiting)
		steering.setTarget(regions.get(homeRegion).center());
	}

	@Override
	public String toString() {
		return getParams().getName();
	}

	/**
	 * returns true if there is an opponent within this player's comfort zone
	 */
	public boolean isThreatened() {
		// check against all opponents to make sure non are within this player's
		// comfort zone
		for (final Player<?> p : team().opponents().members()) {
			// calculate distance to the player. if dist is less than our
			// comfort zone, and the opponent is infront of the player, return true
			if (positionInFrontOfPlayer(p.pos()) && (vec2DDistanceSq(pos(), p.pos()) < getParams().getComfortZone())) {
				return true;
			}
		}
		return false;
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
		return abs(pos().x - team().opponents().goal().center().x) < abs(team().controllingPlayer().pos().x
		    - team().opponents().goal().center().x);
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
	public boolean positionInFrontOfPlayer(final Vector2D position) {
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
		return abs(pos().x - team().opponents().goal().center().x);
	}

	public double distToHomeGoal() {
		return abs(pos().x - team().goal().center().x);
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

	public void setHomeRegion(final int NewRegion) {
		homeRegion = NewRegion;
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

	public StateMachine getFSM() {
		return null;
	}
	
	public String getName() {
		return getParams().getName();
	}
}
