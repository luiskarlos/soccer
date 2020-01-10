/**
 *  In this state the GP will attempt to intercept the ball using the
 *  PURSUIT steering behavior, but he only does so so long as he remains
 *  within his home region.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.fsm.Idle;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.referee.Referee;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PickupBall extends StateAdapter {
	public static final String NAME = "PickupBall";
	private Logger logger = Logger.getLogger("Game");
	
	//private final Referee referee;

	public PickupBall(final Referee referee) {
		super(NAME);
		//this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().seekOn();
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final FieldPlayer player = stateMachine.getOwner();

		if (player.ballInPickupRange()) {
			player.steering().seekOff();
			player.ball().trap();
			player.mount(0, player.ball());
			stateMachine.changeTo(Idle.NAME);
			return State.Status.INTERRUPTIBLE;
		} else {
			player.steering().setTarget(player.ball().pos());
		}
		
		logger.log(Level.SEVERE, player.ball().pos().toString());
		return State.Status.NO_INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {

	}
}
