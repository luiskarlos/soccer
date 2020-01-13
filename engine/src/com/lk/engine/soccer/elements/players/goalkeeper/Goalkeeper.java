/**
 * Desc:   class to implement a goalkeeper agent
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.goalkeeper;

import static com.lk.engine.common.core.EntityFunctionTemplates.enforceNonPenetrationContraint;
import static com.lk.engine.common.d2.Vector2D.div;
import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DDistanceSq;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.console.params.GoalkeeperParams;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.Players;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.team.Team;

public class Goalkeeper extends Player<GoalkeeperParams> implements StateMachineOwner {
	private StateMachine stateMachine;

	// this vector is updated to point towards the ball and is used when
	// rendering the goalkeeper (instead of the underlaying vehicle's heading)
	// to ensure he always appears to be watching the ball
	private Vector2D lookAt = new Vector2D();
	private final FieldPlayingArea playingArea;
	private final Referee referee;

	// ----------------------------- ctor ------------------------------------
	// -----------------------------------------------------------------------
	public Goalkeeper(final GoalkeeperParams params, final Telegraph telegraph, final Team homeTeam,
	    final int homeRegion, final Players players, final PlayRegions regions, final FieldPlayingArea playingArea,
	    final Referee referee, final Ball ball) {
		super(params, telegraph, homeTeam, homeRegion, new Vector2D(0, 1), new Vector2D(0.0, 0.0),
		    Player.PlayerRole.GOALKEEPER, players, regions, ball);
		this.playingArea = playingArea;
		this.referee = referee;
	}

	/**
	 * @return true if the ball can be grabbed by the goalkeeper
	 */
	public boolean ballWithinKeeperRange() {
		return (vec2DDistanceSq(pos(), ball().pos()) < getParams().getKeeperInBallRangeSq());
	}

	@Override
	public boolean inHomeRegion() {
		return regions.get(homeRegion).inside(pos(), Region.RegionModifier.NORMAL);
	}

	// these must be implemented
	@Override
	public Active update() {
		// run the logic for the current state
		stateMachine.update();

		// calculate the combined force from each steering behavior
		final Vector2D SteeringForce = steering.calculate();

		// Acceleration = Force/Mass
		final Vector2D Acceleration = div(SteeringForce, mass());
		// update velocity
		velocity.add(Acceleration);

		// make sure player does not exceed maximum velocity
		velocity.truncate(maxSpeed());

		// update the position
		position.add(velocity);

		// enforce a non-penetration constraint if desired
		if (getParams().isNonPenetrationConstraint()) {
			enforceNonPenetrationContraint(this, players.getPlayers());
		}

		// update the heading if the player has a non zero velocity
		if (!velocity.isZero()) {
			heading = vec2DNormalize(velocity);
			side = heading.perp();
		}

		// look-at vector always points toward the ball
		if (!referee.goalKeeperHasBall()) {
			lookAt = vec2DNormalize(sub(ball().pos(), pos()));
		}

		return Active.Yes;
	}

	/**
	 * @return true if the ball comes close enough for the keeper to consider
	 *         intercepting
	 */
	public boolean ballWithinRangeForIntercept() {
		return (vec2DDistanceSq(team().goal().center(), ball().pos()) <= getParams().getInterceptRangeSq());
	}

	/**
	 * @return true if the keeper has ventured too far away from the goalmouth
	 */
	public boolean tooFarFromGoalMouth() {
		return (vec2DDistanceSq(pos(), getRearInterposeTarget()) > getParams().getInterceptRangeSq());
	}

	/**
	 * this method is called by the Intercept state to determine the spot along
	 * the goalmouth which will act as one of the INTERPOSE targets (the other is
	 * the ball). the specific point at the goal line that the keeper is trying to
	 * cover is flexible and can move depending on where the ball is on the field.
	 * To achieve this we just scale the ball's y value by the ratio of the goal
	 * width to playingfield width
	 */
	public Vector2D getRearInterposeTarget() {
		final double xPosTarget = team().goal().center().x;

		final double yPosTarget = playingArea.getArea().center().y - team().goal().getWidth() * 0.5
		    + (ball().pos().y * team().goal().getWidth()) / playingArea.getArea().height();

		return new Vector2D(xPosTarget, yPosTarget);
	}

	@Override
	public StateMachine getFSM() {
		return stateMachine;
	}

	@Override
	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public Vector2D lookAt() {
		return new Vector2D(lookAt);
	}

	public void setLookAt(final Vector2D v) {
		lookAt = new Vector2D(v);
	}

}