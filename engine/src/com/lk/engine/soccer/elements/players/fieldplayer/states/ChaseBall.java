/**
 * 
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;

public class ChaseBall implements State {
	public ChaseBall() {
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final FieldPlayer player = stateMachine.getOwner();
		player.steering().seekOn();
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		if (player.canKickball()) {
			stateMachine.changeTo(KickBall.class);
			return;
		}

		if (player.isClosestTeamMemberToBall()) {
			player.steering().setTarget(player.ball().pos());
			return;
		}

		stateMachine.changeTo(ReturnToHomeRegion.class);
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().seekOff();
	}
}
