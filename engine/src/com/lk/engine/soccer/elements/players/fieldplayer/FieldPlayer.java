/**
 *   Desc:   Derived from a PlayerBase, this class encapsulates a player
 *           capable of moving around a soccer pitch, kicking, dribbling,
 *           shooting etc
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer;

import static com.lk.engine.common.d2.Transformation.vec2DRotateAroundOrigin;
import static com.lk.engine.common.d2.Vector2D.mul;
import static com.lk.engine.common.misc.NumUtils.clamp;

import com.lk.engine.common.console.params.FieldPlayerParams;
import com.lk.engine.common.core.EntityFunctionTemplates;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.common.time.Regulator;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.Players;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Wait;
import com.lk.engine.soccer.elements.players.states.ReturnToHomeRegion;
import com.lk.engine.soccer.elements.team.Team;

public class FieldPlayer extends Player<FieldPlayerParams> {

	// limits the number of kicks a player may take per second
	private final Regulator kickLimiter;

	public FieldPlayer(final FieldPlayerParams params, final Telegraph telegraph, final Team homeTeam,
	    final PlayerRole role, final RandomGenerator random, final Players players, final PlayRegions regions,
	    final Ball ball) {
		super(params, telegraph, homeTeam, 0, new Vector2D(0, 1), new Vector2D(0.0, 0.0), role, players, regions, ball);

		if (getParams().isNonPenetrationConstraint())
			steering.separationOn();

		// set up the kick regulator
		kickLimiter = new Regulator(random, params.getKickFrequency());
	}

	/**
	 * call this to update the player's position and orientation
	 */
	@Override
	public Active update() {
		// run the logic for the current state
		getFSM().update();

		// calculate the combined steering force
		steering.calculate();

		// if no steering force is produced decelerate the player by applying a
		// braking force
		if (steering.force().isZero()) {
			final double BrakingRate = 0.8;
			velocity.mul(BrakingRate);
		}

		// the steering force's side component is a force that rotates the
		// player about its axis. We must limit the rotation so that a player
		// can only turn by PlayerMaxTurnRate rads per update.
		double turningForce = steering.sideComponent();

		turningForce = clamp(turningForce, -getParams().getMaxTurnRate(), maxTurnRate());

		// rotate the heading vector
		vec2DRotateAroundOrigin(heading, turningForce);

		// make sure the velocity vector points in the same direction as
		// the heading vector
		velocity.set(mul(heading, velocity.length()));

		// and recreate side
		side = heading.perp();

		// now to calculate the acceleration due to the force exerted by
		// the forward component of the steering force in the direction
		// of the player's heading
		final Vector2D accel = mul(heading, steering.forwardComponent() / mass());

		velocity.add(accel);

		// make sure player does not exceed maximum velocity
		velocity.truncate(maxSpeed());
		// update the position
		position.add(velocity);

		// enforce a non-penetration constraint if desired
		if (getParams().isNonPenetrationConstraint()) {
			EntityFunctionTemplates.enforceNonPenetrationContraint(this, players.getPlayers());
		}

		return Active.Yes;
	}

	public boolean isReadyForNextKick() {
		return kickLimiter.isReady();
	}

	@Override
	public void updateTargetOfWaiting() {
		if (getFSM().isInState(Wait.NAME) || getFSM().isInState(ReturnToHomeRegion.NAME)) {
			steering().setTarget(homeRegion().center());
		}
	}

}
