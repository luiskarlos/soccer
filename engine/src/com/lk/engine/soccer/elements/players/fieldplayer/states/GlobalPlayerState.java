/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;

public class GlobalPlayerState implements State {
	public GlobalPlayerState() {
	}

	@Override
	public void enter(final StateMachine stateMachine) {
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		// if a player is in possession and close to the ball reduce his max speed
		if ((player.ballWithinReceivingRange()) && (player.isControllingPlayer())) {
			player.setMaxSpeed(player.getParams().getMaxSpeedWithBall());
		} else {
			player.setMaxSpeed(player.getParams().getMaxSpeedWithoutBall());
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
	}
}