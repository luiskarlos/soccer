/**
 *  Desc:   class to define a team of soccer playing agents. A SoccerTeam
 *          contains several field players and one goalkeeper. A SoccerTeam
 *          is implemented as a finite state machine and has states for
 *          attacking, defending, and KickOff.
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.team;

import static com.lk.engine.common.d2.Geometry.getTangentPoints;
import static com.lk.engine.common.d2.Transformation.pointToLocalSpace;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DDistanceSq;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gwt.core.client.GWT;
import com.lk.engine.common.console.params.TeamParams;
import com.lk.engine.common.core.BaseGameEntity;
import com.lk.engine.common.core.Named;
import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.Goal;
import com.lk.engine.soccer.elements.coach.Coach;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Wait;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.team.states.Defending;

public class Team extends BaseGameEntity implements Updatable, StateMachineOwner, Debuggable, Named {
	public enum TeamColor {
		BLUE, RED
	};

	private final int id = BaseGameEntity.getNextValidID();
	private StateMachine stateMachine;
	private final TeamColor color;
	private final List<Player<?>> players = new ArrayList<Player<?>>(5);
	private Player<?> goalKeeper;
	private final Goal goal;
	private Team opponents;

	private Player<?> controllingPlayer;
	private Player<?> supportingPlayer;
	private Player<?> receivingPlayer;
	private Player<?> playerClosestToBall;
	private double distSqToBallOfClosestPlayer;
	private final SupportSpotCalculator supportSpotCalc;

	private final Telegraph telegraph;
	private final TeamParams params;
	private final RandomGenerator random;
	private final FieldPlayingArea playingArea;
	private final Ball ball;
	private final Coach coach;

	public Team(final TeamParams params, final Telegraph telegraph, final Goal homeGoal, final TeamColor color,
	    final RandomGenerator random, final FieldPlayingArea playingArea, final Ball ball, final Coach coach) {
		this.telegraph = telegraph;
		this.params = params;
		this.goal = homeGoal;
		this.color = color;
		this.random = random;
		this.playingArea = playingArea;
		this.ball = ball;
		this.coach = coach;

		// create the sweet spot calculator
		supportSpotCalc = new SupportSpotCalculator(params, params.getSupportSpotsX(), params.getSupportSpotsY(), this,
		    random, playingArea);
	}

	@Override
  public void debug(Debug debug) {
		debug.put("team", color.name());
		debug.put("type", "Team");
		debug.put("fsm", stateMachine);
		debug.put("controllingPlayer", controllingPlayer);
		debug.put("supportingPlayer", supportingPlayer);
		debug.put("receivingPlayer", receivingPlayer);
		debug.put("playerClosestToBall", playerClosestToBall);

		debug.openArray("players");
		for (Player<?> player : players) {
	    debug.addToArray(player);
    }
		debug.closeArray();
  }

	@Override
	public void setStateMachine(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public StateMachine getFSM() {
		return stateMachine;
	}

	public Ball ball() {
		return ball;
	}

	public FieldPlayingArea getPlayingArea() {
		return playingArea;
	}

	public void addPlayer(final Player<?> player) {
		players.add(player);
    if (player instanceof Goalkeeper) {
    	goalKeeper = player;
    }
	}

	public void attack() {
		for (Player<?> player : players) {
	    player.attack();
    }
  	updateTargetsOfWaitingPlayers();
	}

	public void defend() {
		for (Player<?> player : players) {
	    player.defence();
    }
		updateTargetsOfWaitingPlayers();
	}

	public void prepareForKickoff() {
		for (Player<?> player : players) {
	    player.gotoKickoff();
    }
		updateTargetsOfWaitingPlayers();
  }

	public void startPlaying() {
		changeTo(Defending.NAME);
		changeFieldPlayersTo(Wait.NAME);
		//playerClosestToBall.changeTo(ChaseBall.NAME);
	}

	/**
	 * called each frame. Sets m_pClosestPlayerToBall to point to the player
	 * closest to the ball.
	 */
	private void calculateClosestPlayerToBall() {
		double closestSoFar = Double.MAX_VALUE;

		for (final Player<?> cur : players) {
			// calculate the dist. Use the squared value to avoid sqrt
			final double dist = vec2DDistanceSq(cur.pos(), ball().pos());

			// keep a record of this value for each player
			cur.setDistSqToBall(dist);

			if (dist < closestSoFar) {
				closestSoFar = dist;
				playerClosestToBall = cur;
			}
		}

		distSqToBallOfClosestPlayer = closestSoFar;
	}

	/**
	 * iterates through each player's update function and calculates frequently
	 * accessed info
	 */
	@Override
	public Active update(long time, int delta) { //TODO: update to consider delta
		// this information is used frequently so it's more efficient to
		// calculate it just once each frame
		calculateClosestPlayerToBall();

		// the team state machine switches between attack/defense behavior. It
		// also handles the 'kick off' state where a team must return to their
		// kick off positions before the whistle is blown
		stateMachine.update(time, delta);

		return Active.Yes;
	}

	/**
	 * Given a ball position, a kicking power and a reference to a vector2D this
	 * function will sample random positions along the opponent's goal- mouth and
	 * check to see if a goal can be scored if the ball was to be kicked in that
	 * direction with the given power. If a possible shot is found, the function
	 * will immediately return true, with the target position stored in the vector
	 * ShotTarget.
	 *
	 * returns true if player has a clean shot at the goal and sets ShotTarget to
	 * a normalized vector pointing in the direction the shot should be made. Else
	 * returns false and sets heading to a zero vector
	 */
	private final Vector2D trash = new Vector2D();
	public boolean canShoot(final UVector2D ballPos, final double power) {
		return canShoot(ballPos, power, trash);
	}

	public boolean canShoot(final UVector2D ballPos, final double power, final Vector2D shotTarget) {
		// the number of randomly created shot targets this method will test
		int numAttempts = params.getAttemptsToFindValidStrike();

		while (numAttempts-- > 0) {
			// choose a random position along the opponent's goal mouth. (making
			// sure the ball's radius is taken into account)
			shotTarget.set(opponents.goal().center());

			// the y value of the shot position should lay somewhere between two
			// goalposts (taking into consideration the ball diameter)
			final int minYVal = (int) (opponents.goal().leftPost().y() + ball().bRadius());
			final int maxYVal = (int) (opponents.goal().rightPost().y() - ball().bRadius());

			shotTarget.y = random.randInt(minYVal, maxYVal);

			// make sure striking the ball with the given power is enough to drive
			// the ball over the goal line.
			final double time = ball().timeToCoverDistance(ballPos, shotTarget, power);

			// if it is, this shot is then tested to see if any of the opponents can
			// intercept it.
			if (time >= 0 && isPassSafeFromAllOpponents(ballPos, shotTarget, null, power)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * The best pass is considered to be the pass that cannot be intercepted by an
	 * opponent and that is as far forward of the receiver as possible If a pass
	 * is found, the receiver's address is returned in the reference, 'receiver'
	 * and the position the pass will be made to is returned in the reference
	 * 'PassTarget'
	 */
	public Player<?> findPass(final Player<?> passer, final Vector2D passTarget,
	    final double power, final double MinPassingDistance) {
		assert (passTarget != null);

		double closestToGoalSoFar = Double.MAX_VALUE;
		final Vector2D target = new Vector2D();

		// iterate through all this player's team members and calculate which
		// one is in a position to be passed the ball
		for (final Player<?> c : members()) {
			// make sure the potential receiver being examined is not this player
			// and that it is further away than the minimum pass distance
			if ((c != passer) && (vec2DDistanceSq(passer.pos(), c.pos()) > MinPassingDistance * MinPassingDistance)) {
				if (getBestPassToReceiver(passer, c, target, power)) {
					// if the pass target is the closest to the opponent's goal line found
					// so far, keep a record of it
					final double dist2Goal = abs(target.x - opponents.goal().center().x());

					if (dist2Goal < closestToGoalSoFar) {
						closestToGoalSoFar = dist2Goal;
						// and the target
						passTarget.set(target);
						return c;
					}
				}
			}
		}// next team member

		return null;
	}

	/**
	 * Three potential passes are calculated. One directly toward the receiver's
	 * current position and two that are the tangents from the ball position to
	 * the circle of radius 'range' from the receiver. These passes are then
	 * tested to see if they can be intercepted by an opponent and to make sure
	 * they terminate within the playing area. If all the passes are invalidated
	 * the function returns false. Otherwise the function returns the pass that
	 * takes the ball closest to the opponent's goal area.
	 */
	public boolean getBestPassToReceiver(final Player<?> passer, final Player<?> receiver, final Vector2D passTarget,
	    final double power) {
		assert (passTarget != null);
		// first, calculate how much time it will take for the ball to reach
		// this receiver, if the receiver was to remain motionless
		final double time = ball().timeToCoverDistance(ball().pos(), receiver.pos(), power);

		// return false if ball cannot reach the receiver after having been
		// kicked with the given power
		if (time < 0) {
			return false;
		}

		// the maximum distance the receiver can cover in this time
		double InterceptRange = time * receiver.maxSpeed();

		// Scale the intercept range
		final double ScalingFactor = 0.3;
		InterceptRange *= ScalingFactor;

		// now calculate the pass targets which are positioned at the intercepts
		// of the tangents from the ball to the receiver's range circle.
		final Vector2D ip1 = new Vector2D(), ip2 = new Vector2D();

		getTangentPoints(receiver.pos(), InterceptRange, ball().pos(), ip1, ip2);

		final UVector2D passes[] = { ip1, receiver.pos(), ip2 };
		final int numPassesToTry = passes.length;

		// this pass is the best found so far if it is:
		//
		// 1. Further upfield than the closest valid pass for this receiver
		// found so far
		// 2. Within the playing area
		// 3. Cannot be intercepted by any opponents

		double closestSoFar = Double.MAX_VALUE;
		boolean bResult = false;

		for (int pass = 0; pass < numPassesToTry; ++pass) {
			final double dist = abs(passes[pass].x() - opponents.goal().center().x());

			if ((dist < closestSoFar) && playingArea.getArea().inside(passes[pass])
			    && isPassSafeFromAllOpponents(ball().pos(), passes[pass], receiver, power)) {
				closestSoFar = dist;
				passTarget.set(passes[pass]);
				bResult = true;
			}
		}

		return bResult;
	}

	/**
	 * test if a pass from positions 'from' to 'target' kicked with force
	 * 'PassingForce'can be intercepted by an opposing player
	 */
	public boolean isPassSafeFromOpponent(final UVector2D from, final UVector2D target, final Player<?> receiver,
	    final Player<?> opp, final double PassingForce) {
		// move the opponent into local space.
		final Vector2D toTarget = sub(target, from);
		final Vector2D toTargetNormalized = vec2DNormalize(toTarget);

		final Vector2D localPosOpp = pointToLocalSpace(opp.pos(), toTargetNormalized, toTargetNormalized.perp(), from);

		// if opponent is behind the kicker then pass is considered okay(this is
		// based on the assumption that the ball is going to be kicked with a
		// velocity greater than the opponent's max velocity)
		if (localPosOpp.x < 0) {
			return true;
		}

		// if the opponent is further away than the target we need to consider if
		// the opponent can reach the position before the receiver.
		if (vec2DDistanceSq(from, target) < vec2DDistanceSq(opp.pos(), from)) {
			if (receiver != null) {
				return (vec2DDistanceSq(target, opp.pos()) > vec2DDistanceSq(target, receiver.pos()));
			} else {
				return true;
			}
		}

		// calculate how long it takes the ball to cover the distance to the
		// position orthogonal to the opponents position
		final double timeForBall = ball().timeToCoverDistance(new Vector2D(0, 0), new Vector2D(localPosOpp.x, 0),
		    PassingForce);

		// now calculate how far the opponent can run in this time
		final double reach = opp.maxSpeed() * timeForBall + ball().bRadius() + opp.bRadius();

		// if the distance to the opponent's y position is less than his running
		// range plus the radius of the ball and the opponents radius then the
		// ball can be intercepted
		return !(abs(localPosOpp.y) < reach);
	}

	/**
	 * tests a pass from position 'from' to position 'target' against each member
	 * of the opposing team. Returns true if the pass can be made without getting
	 * intercepted
	 */
	public boolean isPassSafeFromAllOpponents(final UVector2D from, final UVector2D target, final Player<?> receiver,
	    final double passingForce) {
		for (final Player<?> pb : opponents().members()) {
			if (!isPassSafeFromOpponent(from, target, receiver, pb, passingForce)) {
				// debugOn();
				return false;
			}
		}

		return true;
	}

	/**
	 * returns true if an opposing player is within the radius of the position
	 * given as a par ameter
	 */
	public boolean isOpponentWithinRadius(final UVector2D pos, final double rad) {
		for (final Player<?> pb : opponents().members()) {
			if (vec2DDistanceSq(pos, pb.pos()) < rad * rad) {
				return true;
			}
		}
		return false;
	}

	/**
	 * this tests to see if a pass is possible between the requester and the
	 * controlling player. If it is possible a message is sent to the controlling
	 * player to pass the ball asap.
	 */
	public void RequestPass(final FieldPlayer requester) {
		// maybe put a restriction here
		if (random.nextDouble() > 0.1) {
			return;
		}

		if (isPassSafeFromAllOpponents(controllingPlayer().pos(), requester.pos(), requester, requester.getParams()
		    .getMaxPassingForce())) {
			// tell the player to make the pass
			// let the receiver know a pass is coming
			telegraph.post(new TelegramPackage(0, requester.Id(), controllingPlayer().Id(), Message.PASS_TO_ME, requester));
		}
	}

	/**
	 * calculate the closest player to the SupportSpot
	 */
	public Player<?> determineBestSupportingAttacker() {
		double closestSoFar = Double.MAX_VALUE;
		Player<?> bestPlayer = null;

		for (final Player<?> pb : players) {
			// only attackers utilize the BestSupportingSpot
			if ((pb.role() == Player.PlayerRole.ATTACKER) && (pb != controllingPlayer)) {
				// calculate the dist. Use the squared value to avoid sqrt
				final double dist = vec2DDistanceSq(pb.pos(), supportSpotCalc.getBestSupportingSpot());

				// if the distance is the closest so far and the player is not a
				// goalkeeper and the player is not the one currently controlling
				// the ball, keep a record of this player
				if ((dist < closestSoFar)) {
					closestSoFar = dist;
					bestPlayer = pb;
				}
			}
		}

		return bestPlayer;
	}

	public List<Player<?>> members() {
		return players;
	}

	public Goal goal() {
		return goal;
	}

	public Team opponents() {
		return opponents;
	}

	public void setOpponents(final Team opps) {
		opponents = opps;
	}

	public TeamColor color() {
		return color;
	}

	@Deprecated
	public void setPlayerClosestToBall(final Player<?> plyr) {
		playerClosestToBall = plyr;
	}

	public Player<?> playerClosestToBall() {
		return playerClosestToBall;
	}

	public double closestDistToBallSq() {
		return distSqToBallOfClosestPlayer;
	}

	public UVector2D getSupportSpot() {
		return supportSpotCalc.getBestSupportingSpot();
	}

	public Player<?> supportingPlayer() {
		return supportingPlayer;
	}

	@Deprecated
	public void setSupportingPlayer(final Player<?> plyr) {
		supportingPlayer = plyr;
	}

	public Player<?> receiver() {
		return receivingPlayer;
	}

	@Deprecated
	public void setReceiver(final Player<?> plyr) {
		receivingPlayer = plyr;
	}

	public Player<?> controllingPlayer() {
		if (controllingPlayer == null)
			return playerClosestToBall;

		return controllingPlayer;
	}

	public void setControlling(final Player<?> plyr) {
		controllingPlayer = plyr;

		// rub it in the opponents faces!
		opponents().lostControl();
	}/**/

	public boolean inControl() {
		return controllingPlayer != null;
	}

	public void lostControl() {
		controllingPlayer = null;
	}

	public Player<?> getPlayerFromID(final int id) {
		for (final Player<?> pb : players) {
			if (pb.Id() == id) {
				return pb;
			}
		}

		return null;
	}

	public void determineBestSupportingPosition() {
		supportSpotCalc.determineBestSupportingPosition();
	}

	public void updateTargetsOfWaitingPlayers() {
		for (final Player<?> pb : players) {
			pb.updateTargetOfWaiting();
		}
	}

	/**
	 * @return false if any of the team are not located within their home region
	 */
	public boolean allPlayersAtHome() {
		for (final Player<?> pb : players) {
			if (pb.inHomeRegion() == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return Named of the team ("Red" or "Blue")
	 */
	public String name() {
		return color.name();
	}

	public SupportSpotCalculator supportSpotCalc() {
		return supportSpotCalc;
	}

	public TeamParams getParams() {
		return params;
	}

	@Override
	public String toString() {
		return color.name();
	}

	public int Id() {
		return id;
	}

	public Coach getCoach() {
		return coach;
	}

	@Override
	public String getName() {
	  return name();
	}

	public void changeFieldPlayersTo(String name) {
	  for (Player<?> player : players) {
	  	if (player != goalKeeper) {
	  		player.changeTo(name);
	  	}
    }
  }

	public void changeGoalKeeperTo(String name) {
		goalKeeper.changeTo(name);
  }

	public void changeTo(String name) {
	  getFSM().changeTo(name);
  }

}
