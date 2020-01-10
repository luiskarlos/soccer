/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.states;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;

public class Chase extends StateAdapter {
	public static final String NAME = "Chase";
	
	public Chase() {
		super(NAME);
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().seekOn();
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		/*
		 * final Player<?> player = owner.getOwner(); if
		 * (player.ballWithinKickingRange()) {
		 * owner.getFSM().changeTo(KickBall.class); return; }
		 * 
		 * if (player.isClosestTeamMemberToBall()) {
		 * player.steering().setTarget(player.ball().pos()); return; }
		 * 
		 * owner.getFSM().changeTo(ReturnToHomeRegion.class);/*
		 */
		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().seekOff();
	}
}
