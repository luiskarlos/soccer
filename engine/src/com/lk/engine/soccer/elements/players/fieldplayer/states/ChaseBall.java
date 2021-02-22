/**
 * 
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.referee.Referee;

public class ChaseBall extends StateAdapter {
	public static final String NAME = "ChaseBall";
	
	private final Referee referee;

	public ChaseBall(final Referee referee) {
		super(NAME);
		this.referee = referee;
	}

	@Override
	public Check check(final StateMachine stateMachine) {  	
  	if (referee.gameOn()) {
			// if the ball is nearer this player than any other team member AND
			// there is not an assigned receiver AND neither goalkeeper has
			// the ball, go chase it
  		final FieldPlayer player = stateMachine.getOwner();
			if (!referee.goalKeeperHasBall() &&
					(player.team().receiver() == player || 
					 (player.team().receiver() == null && player.isClosestTeamMemberToBall()))) {
				return Check.APPLY;
			}
		}
  	return Check.NO;
  }
	
	@Override
	public void enter(final StateMachine stateMachine) {
		final FieldPlayer player = stateMachine.getOwner();
		player.steering().seekOn();
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		if (player.isClosestTeamMemberToBall()) {
			player.steering().setTarget(player.ball().pos());
			return State.Status.NO_INTERRUPTIBLE;
		}
		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().seekOff();
	}
}
