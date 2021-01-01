/**
 *  Desc: Class to implement a soccer ball. This class inherits from
 *        MovingEntity and provides further functionality for collision
 *        testing and position prediction.
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements;

import static com.lk.engine.common.d2.Geometry.DistanceToRayPlaneIntersection;
import static com.lk.engine.common.d2.Geometry.lineIntersection2D;
import static com.lk.engine.common.d2.Geometry.whereIsPoint;
import static com.lk.engine.common.d2.Geometry.SpanType.PLANE_BACK_SIDE;
import static com.lk.engine.common.d2.Transformation.vec2DRotateAroundOrigin;
import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.div;
import static com.lk.engine.common.d2.Vector2D.mul;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DDistance;
import static com.lk.engine.common.d2.Vector2D.vec2DDistanceSq;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;
import static java.lang.Math.sqrt;

import java.util.List;

import com.lk.engine.common.console.params.BallParams;
import com.lk.engine.common.core.MovingEntity;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.d2.Wall2D;
import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.soccer.elements.players.Player;

public class Ball extends MovingEntity<BallParams> implements Debuggable {
	// keeps a record of the ball's position at the last update
	private final Vector2D oldPos;
	// a local reference to the Walls that make up the pitch boundary
	private final FieldMarkLines markLines;
	private final RandomGenerator random;

	@Override
  public void debug(Debug debug) {
		super.debug(debug);
		debug.put("oldPos", oldPos);
		debug.put("type", "Ball");
  }

	public Ball(final BallParams params, final UVector2D pos, final FieldMarkLines markLines, RandomGenerator random) {
		// set up the base class
		super(params, pos, new Vector2D(0, 1), new Vector2D(1.0, 1.0));
		this.oldPos = new Vector2D(pos);
		this.random = random;
		this.markLines = markLines;
	}

	/**
	 * tests to see if the ball has collided with a ball and reflects the ball's
	 * velocity accordingly
	 */
	void testCollisionWithWalls(final List<Wall2D> walls) {
		// test ball against each wall, find out which is closest
		int idxClosest = -1;

		final Vector2D VelNormal = vec2DNormalize(velocity);

		Vector2D IntersectionPoint = new Vector2D(); /* CollisionPoint */

		double distToIntersection = Double.MAX_VALUE;

		/**
		 * iterate through each wall and calculate if the ball intersects. If it
		 * does then store the index into the closest intersecting wall
		 */
		for (int w = 0; w < walls.size(); ++w) {
			// assuming a collision if the ball continued on its current heading
			// calculate the point on the ball that would hit the wall. This is
			// simply the wall's NORMAL(inversed) multiplied by the ball's radius
			// and added to the balls center (its position)
			final Vector2D thisCollisionPoint = sub(pos(), (mul(walls.get(w).normal(), bRadius())));

			// calculate exactly where the collision point will hit the plane
			if (whereIsPoint(thisCollisionPoint, walls.get(w).from(), walls.get(w).normal()) == PLANE_BACK_SIDE) {
				final double distToWall = DistanceToRayPlaneIntersection(thisCollisionPoint, walls.get(w).normal(), walls
				    .get(w).from(), walls.get(w).normal());
				IntersectionPoint = add(thisCollisionPoint, (mul(distToWall, walls.get(w).normal())));
			} else {
				final double distToWall = DistanceToRayPlaneIntersection(thisCollisionPoint, VelNormal, walls.get(w).from(),
				    walls.get(w).normal());
				IntersectionPoint = add(thisCollisionPoint, (mul(distToWall, VelNormal)));
			}

			// check to make sure the intersection point is actually on the line
			// segment
			boolean onLineSegment = false;

			if (lineIntersection2D(walls.get(w).from(), walls.get(w).to(),
			    sub(thisCollisionPoint, mul(walls.get(w).normal(), 20.0)),
			    add(thisCollisionPoint, mul(walls.get(w).normal(), 20.0)))) {
				onLineSegment = true;
			}

			// Note, there is no test for collision with the end of a line segment

			// now check to see if the collision point is within range of the
			// velocity vector. [work in distance squared to avoid sqrt] and if it
			// is the closest hit found so far.
			// If it is that means the ball will collide with the wall sometime
			// between this time step and the next one.
			final double distSq = vec2DDistanceSq(thisCollisionPoint, IntersectionPoint);

			if ((distSq <= velocity.lengthSq()) && (distSq < distToIntersection) && onLineSegment) {
				distToIntersection = distSq;
				idxClosest = w;
				// CollisionPoint = IntersectionPoint;
			}
		}// next wall

		// to prevent having to calculate the exact time of collision we
		// can just check if the velocity is opposite to the wall NORMAL
		// before reflecting it. This prevents the case where there is overshoot
		// and the ball gets reflected back over the line before it has completely
		// reentered the playing area.
		if ((idxClosest >= 0) && VelNormal.dot(walls.get(idxClosest).normal()) < 0) {
			velocity.reflect(walls.get(idxClosest).normal());
		}
	}

	/**
	 * updates the ball physics, tests for any collisions and adjusts the ball's
	 * velocity accordingly
	 */

	@Override
	public Active update(long time, int delta) { //TODO: update to consider delta
		// keep a record of the old position so the goal::scored method
		// can utilize it for goal testing
		oldPos.set(pos()); //TODO: this is update even if the pos did not change

		// Test for collisions
		testCollisionWithWalls(markLines.getLineMarks());

		// Simulate Prm.Friction. Make sure the speed is positive first though
		if (velocity.lengthSq() > getParams().getFriction() * getParams().getFriction()) {
			velocity.add(mul(vec2DNormalize(velocity), getParams().getFriction()));
			position.add(velocity);
			// update heading
			heading.set(vec2DNormalize(velocity));
		}

		return Active.Yes;
	}

	/**
	 * applys a force to the ball in the direction of heading. Truncates the new
	 * velocity to make sure it doesn't exceed the max allowable.
	 */
	public void kick(final UVector2D dir, final double force) {
		// ensure direction is normalized
		final Vector2D normalDir = new Vector2D(dir);
		normalDir.normalize();

		// calculate the acceleration
		final Vector2D acceleration = div(mul(normalDir, force), getParams().getMass());

		// update the velocity
		velocity.set(acceleration);
	}

	/**
	 * Given a force and a distance to cover given by two vectors, this method
	 * calculates how long it will take the ball to travel between the two points
	 * TODO: check not used parameters?
	 */
	public double timeToCoverDistance(final UVector2D A, final UVector2D B, final double force) {
		// this will be the velocity of the ball in the next time step *if*
		// the player was to make the pass.
		final double speed = force / getParams().getMass();

		// calculate the velocity at B using the equation
		//
		// v^2 = u^2 + 2as
		//

		// first calculate s (the distance between the two positions)
		final double distanceToCover = vec2DDistance(A, B);

		final double term = speed * speed + 2.0 * distanceToCover * getParams().getFriction();

		// if (u^2 + 2as) is negative it means the ball cannot reach point B.
		if (term <= 0.0) {
			return -1.0;
		}

		final double v = sqrt(term);

		// it IS possible for the ball to reach B and we know its speed when it
		// gets there, so now it's easy to calculate the time using the equation
		//
		// t = v-u
		// ---
		// a
		//
		return (v - speed) / getParams().getFriction();
	}

	/**
	 * given a time this method returns the ball position at that time in the
	 * future
	 */
	public Vector2D futurePosition(final double time) {
		// using the equation s = ut + 1/2at^2, where s = distance, a = friction
		// u=start velocity

		// calculate the ut term, which is a vector
		final Vector2D ut = mul(velocity, time);

		// calculate the 1/2at^2 term, which is scalar
		final double half_a_t_squared = 0.5 * getParams().getFriction() * time * time;

		// turn the scalar quantity into a vector by multiplying the value with
		// the normalized velocity vector (because that gives the direction)
		final Vector2D scalarToVector = mul(half_a_t_squared, vec2DNormalize(velocity));

		// the predicted position is the balls position plus these two terms
		return add(pos(), ut).add(scalarToVector);
	}

	/**
	 * this is used by players and goalkeepers to 'trap' a ball -- to stop it
	 * dead. That player is then assumed to be in possession of the ball and
	 * m_pOwner is adjusted accordingly
	 */
	public void trap() {
		velocity.zero();
	}

	public UVector2D oldPos() {
		return oldPos;
	}

	/**
	 * positions the ball at the desired location and sets the ball's velocity to
	 * zero
	 */
	public void placeAtPosition(final Vector2D newPos) {
		oldPos.set(pos());
		setPos(newPos);
		velocity.zero();
	}

	/**
	 * this can be used to vary the accuracy of a player's kick. Just call it
	 * prior to kicking the ball using the ball's position and the ball target as
	 * parameters.
	 */
	public Vector2D addNoiseToKick(final Player<?> player, final UVector2D ballPos, final UVector2D target) {
		final double displacement = (Math.PI - Math.PI * player.getParams().getKickingAccuracy()) * random.randomClamped();
		final Vector2D toTarget = sub(target, ballPos);
		vec2DRotateAroundOrigin(toTarget, displacement);

		return add(toTarget, ballPos);
	}


}
