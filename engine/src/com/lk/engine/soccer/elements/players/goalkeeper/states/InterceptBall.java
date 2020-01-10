/**
 *  In this state the GP will attempt to intercept the ball using the
 *  PURSUIT steering behavior, but he only does so so long as he remains
 *  within his home region.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.goalkeeper.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.referee.Referee;

public class InterceptBall extends StateAdapter {
	public static final String NAME = "InterceptBall";
	
	private final Referee referee;

	public InterceptBall(final Referee referee) {
		super(NAME);
		this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().pursuitOn();
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Goalkeeper player = stateMachine.getOwner();
		// if the goalkeeper moves to far away from the goal he should return to his
		// home region UNLESS he is the closest player to the ball, in which case,
		// he should keep trying to intercept it.
		if (player.tooFarFromGoalMouth() && !player.isClosestPlayerOnPitchToBall()) {
			stateMachine.changeTo(ReturnHome.NAME);
			return State.Status.INTERRUPTIBLE;
		}

		// if the ball becomes in range of the goalkeeper's hands he traps the
		// ball and puts it back in play
		if (player.ballInPickupRange()) {
			player.ball().trap();			
			referee.setGoalKeeperHasBall(true);
			stateMachine.changeTo(PutBallBackInPlay.NAME);
		}

		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().pursuitOff();
	}
}
