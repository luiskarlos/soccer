/**
 *  Desc:   A base class defining an entity that moves. The entity has 
 *          a local coordinate system and members for defining its
 *          mass and velocity.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.core;

import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;

import com.lk.engine.common.console.params.MovingEntityParams;
import com.lk.engine.common.d2.C2DMatrix;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;

public abstract class MovingEntity<T extends MovingEntityParams> extends BaseGameEntity implements Debuggable {
	protected final Vector2D velocity;
	// a normalized vector pointing in the direction the entity is heading.
	protected final Vector2D heading;
	// a vector perpendicular to the heading vector
	protected Vector2D side;
	private T params;

	public MovingEntity(final T params, final UVector2D position, final UVector2D velocity, final UVector2D heading) {
		super(BaseGameEntity.getNextValidID());
		this.heading = new Vector2D(heading);
		this.velocity = new Vector2D(velocity);
		this.side = this.heading.perp();
		super.setPos(position);
		this.params = params;
		this.boundingRadius = params.getRadius();
	}

	@Override
  public void debug(Debug debug) {
		super.debug(debug);
		debug.put("velocity", velocity);
		debug.put("heading", heading);
		debug.put("side", side);
  }
	
	// accessors
	public UVector2D velocity() {
		return velocity;
	}

	public void setVelocity(final UVector2D newVel) {
		velocity.set(newVel);
	}

	public double mass() {
		return getParams().getMass();
	}

	public Vector2D side() {
		return side;
	}

	public double maxSpeed() {
		return getParams().getMaxSpeed();
	}

	public void setMaxSpeed(final double new_speed) {
		getParams().setMaxSpeed(new_speed);
	}

	public double maxForce() {
		return getParams().getMaxForce();
	}

	public void setMaxForce(final double mf) {
		getParams().setMaxForce(mf);
	}

	public boolean isSpeedMaxedOut() {
		return maxSpeed() * maxSpeed() >= velocity.lengthSq();
	}

	public double speed() {
		return velocity.length();
	}

	public double speedSq() {
		return velocity.lengthSq();
	}

	public UVector2D heading() {
		return heading;
	}

	public double maxTurnRate() {
		return getParams().getMaxTurnRate();
	}

	void setMaxTurnRate(final double val) {
		getParams().setMaxTurnRate(val);
	}

	/**
	 * given a target position, this method rotates the entity's heading and side
	 * vectors by an amount not greater than maxTurnRate until it directly faces
	 * the target.
	 * 
	 * @return true when the heading is facing in the desired direction
	 */
	public boolean rotateHeadingToFacePosition(final UVector2D target) {
		final Vector2D toTarget = vec2DNormalize(sub(target, pos()));

		// first determine the angle between the heading vector and the target
		double angle = Math.acos(heading.dot(toTarget));

		// sometimes heading.Dot(toTarget) == 1.000000002
		if (Double.isNaN(angle)) {
			angle = 0;
		}
		// return true if the player is facing the target
		if (angle < 0.00001) {
			return true;
		}

		// clamp the amount to turn to the max turn rate
		if (angle > maxTurnRate()) {
			angle = maxTurnRate();
		}

		// The next few lines use a rotation matrix to rotate the player's heading
		// vector accordingly
		final C2DMatrix rotationMatrix = new C2DMatrix();

		// notice how the direction of rotation has to be determined when creating
		// the rotation matrix
		rotationMatrix.rotate(angle * heading.sign(toTarget));
		rotationMatrix.transformVector2Ds(heading);
		rotationMatrix.transformVector2Ds(velocity);

		// finally recreate side
		side = heading.perp();

		return false;
	}

	/**
	 * first checks that the given heading is not a vector of zero length. If the
	 * new heading is valid this fumction sets the entity's heading and side
	 * vectors accordingly
	 */
	public void setHeading(final UVector2D new_heading) {
		assert ((new_heading.lengthSq() - 1.0) < 0.00001);

		heading.set(new_heading);

		// the side vector must always be perpendicular to the heading
		side = heading.perp();
	}

	public T getParams() {
		return params;
	}

}