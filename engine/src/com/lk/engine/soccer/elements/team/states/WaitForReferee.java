/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.team.states;

import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.fieldplayer.states.WaitForStateChange;
import com.lk.engine.soccer.elements.team.Team;

public class WaitForReferee extends StateAdapter {
	public static final String NAME = "WaitForReferee";
	
	public WaitForReferee() {
		super(NAME);
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Team team = stateMachine.getOwner();
		team.changeFieldPlayersTo(WaitForStateChange.NAME);
	}
}