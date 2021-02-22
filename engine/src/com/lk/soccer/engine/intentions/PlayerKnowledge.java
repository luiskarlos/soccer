package com.lk.soccer.engine.intentions;

import static com.lk.engine.common.d2.Transformation.pointToLocalSpace;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DDistanceSq;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.console.params.PlayerParams;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.Goal;
import com.lk.engine.soccer.elements.players.Player;

public class PlayerKnowledge {
	private final Player<?> me;
	private final PlayerParams params;
	private Ball ball;
	private Goal goal;
	private final List<Player<?>> opponents = new ArrayList<Player<?>>(12);
	private final RandomGenerator random;

	public PlayerKnowledge(final Player<?> me, final PlayerParams params, final RandomGenerator random) {
		this.me = me;
		this.params = params;
		this.random = random;
	}

	public double angle(final Vector2D target) {
		final Vector2D toTarget = vec2DNormalize(sub(target, me.pos()));

		// first determine the angle between the heading vector and the target
		double headingAngle = Math.acos(me.heading().dot(toTarget));

		headingAngle = me.heading().sign(target) < 0 ? headingAngle : -headingAngle;
		if (Double.isNaN(headingAngle)) {
			headingAngle = 0;
		}
		return headingAngle;
	}

	public Intention canKickTheBall1() {
		if (ball == null)
			return Intention.ZERO;

		return Intention.ZERO;// vec2DDistanceSq(ball.pos(), me.pos()) <
													// params.getKickingDistanceSq();
	}

	public boolean canKickTheBall() {
		if (ball == null)
			return false;

		return vec2DDistanceSq(ball.pos(), me.pos()) < params.getKickingDistanceSq();
	}

	public boolean canShootToGoal() {
		final double dot = me.heading().dot(Vector2D.vec2DNormalize(me.pos()));

		// the dot product is used to adjust the shooting force. The more
		// directly the ball is ahead, the more forceful the kick
		final double shootPower = params.getMaxShootingForce() * dot;

		return canShoot(me.pos(), shootPower, new Vector2D());
	}

	private boolean canShoot(final UVector2D ballPos, final double power, final Vector2D shotTarget) {
		// the number of randomly created shot targets this method will test
		int numAttempts = params.getAttemptsToFindValidStrike();

		while (numAttempts-- > 0) {
			// choose a random position along the goal mouth. (making
			// sure the ball's radius is taken into account)
			shotTarget.set(goal.center());

			// the y value of the shot position should lay somewhere between two

			// goalposts (taking into consideration the ball diameter)
			final int minYVal = (int) (goal.leftPost().y() + ball.bRadius());
			final int maxYVal = (int) (goal.rightPost().y() - ball.bRadius());

			shotTarget.y = random.randInt(minYVal, maxYVal);

			// make sure striking the ball with the given power is enough to drive
			// the ball over the goal line.
			final double time = ball.timeToCoverDistance(ballPos, shotTarget, power);

			// if it is, this shot is then tested to see if any of the opponents can
			// intercept it.
			if (time >= 0 && isPassSafeFromAllOpponents(ballPos, shotTarget, null, power)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * tests a pass from position 'from' to position 'target' against each member
	 * of the opposing team. Returns true if the pass can be made without getting
	 * intercepted
	 */
	public boolean isPassSafeFromAllOpponents(final UVector2D from, final UVector2D target, final Player<?> receiver,
	    final double passingForce) {
		for (final Player<?> pb : opponents) {
			if (!isPassSafeFromOpponent(from, target, receiver, pb, passingForce)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * test if a pass from positions 'from' to 'target' kicked with force
	 * 'PassingForce'can be intercepted by an opposing player
	 */
	public boolean isPassSafeFromOpponent(final UVector2D from, final UVector2D target, final Player<?> receiver,
	    final Player<?> opp, final double passingForce) {
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
		final double timeForBall = ball.timeToCoverDistance(new Vector2D(0, 0), new Vector2D(localPosOpp.x, 0),
		    passingForce);

		// now calculate how far the opponent can run in this time
		final double reach = opp.maxSpeed() * timeForBall + ball.bRadius() + opp.bRadius();

		// if the distance to the opponent's y position is less than his running
		// range plus the radius of the ball and the opponents radius then the
		// ball can be intercepted
		return !(abs(localPosOpp.y) < reach);
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

}
