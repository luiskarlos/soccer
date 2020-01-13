/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.team.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.team.Team;

public class PrepareForKickOff implements State {
	private final Referee referee;

	public PrepareForKickOff(final Referee referee) {
		this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		// reset key player pointers
		// team.setControllingPlayer(null);
		team.setSupportingPlayer(null);
		team.setReceiver(null);
		team.setPlayerClosestToBall(null);
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Team team = stateMachine.getOwner();
		// if both teams in position, start the game
		if (team.allPlayersAtHome() && team.opponents().allPlayersAtHome()) {
			stateMachine.changeTo(Defending.class);
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		referee.setGameOn();
	}

}