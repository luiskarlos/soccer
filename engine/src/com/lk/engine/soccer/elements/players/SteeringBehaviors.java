/**
 *
 *  Desc:   class to encapsulate steering behaviors for a soccer player
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players;

import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.div;
import static com.lk.engine.common.d2.Vector2D.mul;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.lk.engine.common.core.EntityFunctionTemplates;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.Players;

public class SteeringBehaviors implements Debuggable {
	private final Player<?> player;
	private final Ball ball;
	private final Set<Behavior> behaviors = new HashSet<Behavior>();

	// the steering force created by the combined effect of all the selected
	// behaviors
	private final Vector2D steeringForce = new Vector2D();
	// the current target (usually the ball or predicted ball position)
	private final Vector2D target = new Vector2D();
	// the distance the player tries to INTERPOSE from the target
	private double interposeDist;

	@Override
	public void debug(Debug debug) {
		debug.put("behaviors",
				behaviors
						.stream()
						.map(behavior -> behavior.name())
						.collect(Collectors.joining(", "))
		);
	}

	private enum Behavior {
		SEEK() {
			@Override
			Vector2D process(final SteeringBehaviors steeringBehaviors) {
				return steeringBehaviors.seek();
			}
		},
		ARRIVE() {
			@Override
			Vector2D process(final SteeringBehaviors steeringBehaviors) {
				return steeringBehaviors.arrive();
			}
		},
		SEPARATION() {
			@Override
			Vector2D process(final SteeringBehaviors steeringBehaviors) {
				return steeringBehaviors.separation();
			}
		},
		PURSUIT() {
			@Override
			Vector2D process(final SteeringBehaviors steeringBehaviors) {
				return steeringBehaviors.pursuit();
			}
		},
		INTERPOSE() {
			@Override
			Vector2D process(final SteeringBehaviors steeringBehaviors) {
				return steeringBehaviors.interpose();
			}
		};

		abstract Vector2D process(final SteeringBehaviors steeringBehaviors);
	}

	private final Players players;

	// Arrive makes use of these to determine how quickly a vehicle
	// should decelerate to its target
	private enum Deceleration {
		SLOW(3), NORMAL(2), FAST(1);
		private final int dec;

		Deceleration(final int d) {
			this.dec = d;
		}

		public int value() {
			return dec;
		}
	}

	public SteeringBehaviors(final Player<?> agent, final Players players, final Ball ball) {
		this.player = agent;
		this.players = players;
		this.ball = ball;
	}

	private Vector2D seek() {
		return seek(target);
	}

	/**
	 * Given a target, this behaviors returns a steering force which will allign
	 * the agent with the target and move the agent in the desired direction
	 */
	private Vector2D seek(final Vector2D target) {
		final Vector2D DesiredVelocity = vec2DNormalize(mul(sub(target, player.pos()), player.maxSpeed()));
		return (sub(DesiredVelocity, player.velocity()));
	}

	private Vector2D arrive() {
		return arrive(target, Deceleration.FAST);
	}

	/**
	 * This behaviors is similar to SEEK but it attempts to ARRIVE at the target
	 * with a zero velocity
	 */
	private Vector2D arrive(final Vector2D targetPos, final Deceleration deceleration) {
		final Vector2D toTarget = sub(targetPos, player.pos());

		// calculate the distance to the target
		final double dist = toTarget.length();

		if (dist > 0) {
			// because Deceleration is enumerated as an int, this value is required
			// to provide fine tweaking of the deceleration..
			final double DecelerationTweaker = 0.3;

			// calculate the speed required to reach the target given the desired
			// deceleration
			double speed = dist / (deceleration.value() * DecelerationTweaker);

			// make sure the velocity does not exceed the max
			speed = Math.min(speed, player.maxSpeed());

			// from here proceed just like Seek except we don't need to normalize
			// the ToTarget vector because we have already gone to the trouble
			// of calculating its length: dist.
			final Vector2D DesiredVelocity = mul(toTarget, speed / dist);

			return sub(DesiredVelocity, player.velocity());
		}

		return new Vector2D(0, 0);
	}

	private Vector2D pursuit() {
		return pursuit(ball);
	}

	/**
	 * This behaviors predicts where its prey will be and seeks to that location
	 * This behaviors creates a force that steers the agent towards the ball
	 */
	private Vector2D pursuit(final Ball ball) {
		final Vector2D ToBall = sub(ball.pos(), player.pos());

		// the lookahead time is proportional to the distance between the ball
		// and the pursuer;
		double LookAheadTime = 0.0;

		if (ball.speed() != 0.0) {
			LookAheadTime = ToBall.length() / ball.speed();
		}

		// calculate where the ball will be at this time in the future
		target.set(ball.futurePosition(LookAheadTime));

		// now SEEK to the predicted future position of the ball
		return arrive(target, Deceleration.FAST);
	}

	/**
	 *
	 * this calculates a force repelling from the other neighbors
	 */
	protected Vector2D separation() {
		// iterate through all the neighbors and calculate the vector from them
		final Vector2D steeringForce = new Vector2D();

		EntityFunctionTemplates.enforceNonPenetrationContraint(player, players.getPlayers());
		for (final Player<?> p : findNeighbour()) {
			// make sure this agent isn't included in the calculations and that
			// the agent is close enough
			final Vector2D toAgent = sub(player.pos(), p.pos());

			// scale the force inversely proportional to the agents distance
			// from its neighbor.
			steeringForce.add(div(vec2DNormalize(toAgent), toAgent.length()));
		}

		return steeringForce.mul(player.getParams().getSeparationCoefficient());
	}

	private Vector2D interpose() {
		return interpose(ball.pos(), target, interposeDist);
	}

	/**
	 * Given an opponent and an object position this method returns a force that
	 * attempts to position the agent between them
	 */
	private Vector2D interpose(final UVector2D objectPos, final Vector2D target, final double distFromTarget) {
		return arrive(add(target, mul(vec2DNormalize(sub(objectPos, target)), distFromTarget)), Deceleration.NORMAL);
	}

	/**
	 * tags any vehicles within a predefined radius
	 */
	private Set<Player<?>> findNeighbour() {
		final Set<Player<?>> tagged = new HashSet<>();
		for (final Player<?> p : players.getPlayers()) {
			// work in distance squared to avoid sqrts
			// final Vector2D to = sub(player.pos(), p.pos());

			final Vector2D toEntity = sub(player.pos(), p.pos());
			final double distFromEachOther = toEntity.length();

			// if this distance is smaller than the sum of their radii then this
			// entity must be moved away in the direction parallel to the
			// ToEntity vector
			final double amountOfOverLap = p.bRadius() + player.bRadius() - distFromEachOther;

			if (amountOfOverLap > 0)// to.lengthSq() <
															// p.getParams().getSeparationCoefficient())
			{
				tagged.add(p);
			}
		}/**/

		tagged.remove(player);
		return tagged;
	}

	/**
	 * This function calculates how much of its max steering force the vehicle has
	 * left to apply and then applies that amount of the force to add.
	 */
	private boolean accumulateForce(final Vector2D sf, final Vector2D forceToAdd) {
		// first calculate how much steering force we have left to use
		final double magnitudeSoFar = sf.length();

		final double magnitudeRemaining = player.maxForce() - magnitudeSoFar;

		// return false if there is no more force left to use
		if (magnitudeRemaining <= 0.0) {
			return false;
		}

		// calculate the magnitude of the force we want to add
		double magnitudeToAdd = forceToAdd.length();

		// now calculate how much of the force we can really add
		if (magnitudeToAdd > magnitudeRemaining) {
			magnitudeToAdd = magnitudeRemaining;
		}

		// add it to the steering force
		sf.add(mul(vec2DNormalize(forceToAdd), magnitudeToAdd));

		return true;
	}

	/**
	 * this method calls each active steering behaviors and acumulates their
	 * forces until the max steering force magnitude is reached at which time the
	 * function returns the steering force accumulated to that point
	 */
	private Vector2D sumForces() {
		final Vector2D force = new Vector2D();
		for (final Behavior type : behaviors) {
			force.add(type.process(this));
			if (!accumulateForce(steeringForce, force)) {
				return steeringForce;
			}
		}
		return steeringForce;
	}

	/**
	 * calculates the overall steering force based on the currently active
	 * steering behaviors.
	 */
	public UVector2D calculate() {
		// reset the force
		steeringForce.zero();

		// this will hold the value of each individual steering force
		steeringForce.set(sumForces());

		// make sure the force doesn't exceed the vehicles maximum allowable
		steeringForce.truncate(player.maxForce());

		return steeringForce;
	}

	/**
	 * calculates the component of the steering force that is parallel with the
	 * vehicle heading
	 */
	public double forwardComponent() {
		return player.heading().dot(steeringForce);
	}

	/**
	 * calculates the component of the steering force that is perpendicuar with
	 * the vehicle heading
	 */
	public double sideComponent() {
		return player.side().dot(steeringForce) * player.maxTurnRate();
	}

	public Vector2D force() {
		return steeringForce;
	}

	public UVector2D target() {
		return target;
	}

	public void setTarget(final UVector2D t) {
		target.set(t);
	}

	public double interposeDistance() {
		return interposeDist;
	}

	public void setInterposeDistance(final double d) {
		interposeDist = d;
	}

	public void seekOn() {
		behaviors.add(Behavior.SEEK);
	}

	public void arriveOn() {
		behaviors.add(Behavior.ARRIVE);
	}

	public void pursuitOn() {
		behaviors.add(Behavior.PURSUIT);
	}

	public void separationOn() {
		behaviors.add(Behavior.SEPARATION);
	}

	public void interposeOn(final double d) {
		behaviors.add(Behavior.INTERPOSE);
		interposeDist = d;
	}

	public void seekOff() {
		behaviors.remove(Behavior.SEEK);
	}

	public void arriveOff() {
		behaviors.remove(Behavior.ARRIVE);
	}

	public void pursuitOff() {
		behaviors.remove(Behavior.PURSUIT);
	}

	public void separationOff() {
		behaviors.remove(Behavior.SEPARATION);
	}

	public void interposeOff() {
		behaviors.remove(Behavior.INTERPOSE);
	}

	public boolean seekIsOn() {
		return behaviors.contains(Behavior.SEEK);
	}

	public boolean arriveIsOn() {
		return behaviors.contains(Behavior.ARRIVE);
	}

	public boolean pursuitIsOn() {
		return behaviors.contains(Behavior.PURSUIT);
	}

	public boolean separationIsOn() {
		return behaviors.contains(Behavior.SEPARATION);
	}

	public boolean interposeIsOn() {
		return behaviors.contains(Behavior.INTERPOSE);
	}

	public Vector2D steeringForce() {
		return steeringForce;
	}

	public Player<?> player() {
		return player;
	}
}
