package com.lk.engine.soccer.elements.players.states;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;

public class Walk extends StateAdapter {
	public static final String NAME = "Walk";
	
	public Walk() {
	  super(NAME);
  }
	
	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOn();
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		final Region target = (Region) data;

		if (target.inside(player.pos(), Region.RegionModifier.NORMAL)) {
			stateMachine.exit();
		} else {
			player.steering().setTarget(target.center());
		}
		
		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOff();
	}

}
