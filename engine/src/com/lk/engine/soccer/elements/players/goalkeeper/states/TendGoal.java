/**
 *
 *  This is the main state for the goalkeeper. When in this state he will
 *  move left to right across the goalmouth using the 'INTERPOSE' steering
 *  behavior to put himself between the ball and the back of the net.
 *
 *  If the ball comes within the 'goalkeeper range' he moves out of the
 *  goalmouth to attempt to intercept it. (see next state)
 *
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.goalkeeper.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;

public class TendGoal implements State {
	private final Referee referee;

	public TendGoal(final Referee referee) {
		this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Goalkeeper player = stateMachine.getOwner();
		// turn INTERPOSE on
		player.steering().interposeOn(player.getParams().getTendingDistance());

		// INTERPOSE will position the agent between the ball position and a target
		// position situated along the goal mouth. This call sets the target
		player.steering().setTarget(player.getRearInterposeTarget());
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Goalkeeper player = stateMachine.getOwner();
		// the rear INTERPOSE target will change as the ball's position changes
		// so it must be updated each update-step
		player.steering().setTarget(player.getRearInterposeTarget());

		// if the ball comes in range the keeper traps it and then changes state
		// to put the ball back in play
		if (player.ballWithinKeeperRange()) {
			player.ball().trap();
			referee.setGoalKeeperHasBall(true);
			stateMachine.changeTo(PutBallBackInPlay.class);
			return;
		}

		// if ball is within a predefined distance, the keeper moves out from
		// position to try and intercept it.
		if (player.ballWithinRangeForIntercept() && !player.team().inControl()) {
			stateMachine.changeTo(InterceptBall.class);
		}

		// if the keeper has ventured too far away from the goal-line and there
		// is no threat from the opponents he should move back towards it
		if (player.tooFarFromGoalMouth() && player.team().inControl()) {
			stateMachine.changeTo(ReturnHome.class);
			return;
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().interposeOff();
	}

}
