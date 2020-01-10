/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.team.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.goalkeeper.states.ReturnHome;
import com.lk.engine.soccer.elements.players.states.ReturnToHomeRegion;
import com.lk.engine.soccer.elements.referee.Referee;
import com.lk.engine.soccer.elements.team.Team;

public class PrepareForKickOff extends StateAdapter {
	public static final String NAME = "PrepareForKickOff";
	
	//private final Referee referee;

	public PrepareForKickOff(final Referee referee) {
		super(NAME);
		//this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		// reset key player pointers
		// team.setControllingPlayer(null);
		team.setSupportingPlayer(null);
		team.setReceiver(null);
		team.setPlayerClosestToBall(null);
		team.prepareForKickoff();
		team.changeFieldPlayersTo(ReturnToHomeRegion.NAME);
		team.changeGoalKeeperTo(ReturnHome.NAME);
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Team team = stateMachine.getOwner();
		if (team.allPlayersAtHome()) {
			stateMachine.changeTo(WaitForReferee.NAME);
		}
		
		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		//referee.setGameOn();
	}
}