/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;

public class GlobalPlayerState extends StateAdapter {
	public static final String NAME = "GlobalPlayerState";
	
	public GlobalPlayerState() {
		super(NAME);
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		// if a player is in possession and close to the ball reduce his max speed
		if ((player.ballWithinReceivingRange()) && (player.isControllingPlayer())) {
			player.setMaxSpeed(player.getParams().getMaxSpeedWithBall());
		} else {
			player.setMaxSpeed(player.getParams().getMaxSpeedWithoutBall());
		}
		
		return State.Status.INTERRUPTIBLE;
	}
}