/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import static com.lk.engine.common.d2.Transformation.vec2DRotateAroundOrigin;
import static com.lk.engine.common.misc.NumUtils.QUARTER_PI;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.players.Player;

public class Dribble implements State {
	private final Telegraph telegraph;

	public Dribble(final Telegraph telegraph) {
		this.telegraph = telegraph;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		// let the team know this player is controlling
		telegraph.post(new TelegramPackage(Message.CONTROLING_PLAYER, player));
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		final double dot = player.team().goal().facing().dot(player.heading());

		// if the ball is between the player and the home goal, it needs to swivel
		// the ball around by doing multiple small kicks and turns until the player
		// is facing in the correct direction
		if (dot < 0) {
			// the player's heading is going to be rotated by a small amount (Pi/4)
			// and then the ball will be kicked in that direction
			final Vector2D direction = player.heading();

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

		// the player has kicked the ball so he must now change state to follow it
		stateMachine.changeTo(ChaseBall.class);

		return;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
	}

}
