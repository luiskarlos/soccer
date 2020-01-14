/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import static com.lk.engine.common.d2.Transformation.vec2DRotateAroundOrigin;
import static com.lk.engine.common.misc.NumUtils.QUARTER_PI;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.states.ReturnToHomeRegion;
import com.lk.engine.soccer.elements.referee.Referee;

public class Dribble extends StateAdapter {
	public static final String NAME = "Dribble";
	private final Telegraph telegraph;
	private final Referee referee;

	public Dribble(final Referee referee, final Telegraph telegraph) {
		super(NAME);
		this.referee = referee;
		this.telegraph = telegraph;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		// let the team know this player is controlling
		telegraph.post(new TelegramPackage(Message.CONTROLLING_PLAYER, player));
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		final double dot = player.team().goal().facing().dot(player.heading());

		if (referee.goalKeeperHasBall()) {
			stateMachine.changeTo(ReturnToHomeRegion.NAME);
			return State.Status.INTERRUPTIBLE;
		}
		// if the ball is between the player and the home goal, it needs to swivel
		// the ball around by doing multiple small kicks and turns until the player
		// is facing in the correct direction
		if (dot < 0) {
			// the player's heading is going to be rotated by a small amount (Pi/4)
			// and then the ball will be kicked in that direction
			//TODO: esto estaba horrible check
			final Vector2D direction = new Vector2D(player.heading());

			// calculate the sign (+/-) of the angle between the player heading and
			// the
			// facing direction of the goal so that the player rotates around in the
			// correct direction
			final double angle = QUARTER_PI * -1 * player.team().goal().facing().sign(player.heading());

			vec2DRotateAroundOrigin(direction, angle);

			// this value works well whjen the player is attempting to control the
			// ball and turn at the same time
			final double KickingForce = player.getParams().getShortDribbleForce();

			player.ball().kick(direction, KickingForce);
		} // kick the ball down the field
		else {
			player.ball().kick(player.team().goal().facing(), player.getParams().getMaxDribbleForce());
		}

		return State.Status.INTERRUPTIBLE;
	}
}
