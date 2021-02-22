/**
 */
package com.lk.engine.soccer.elements.players.states;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.fsm.Idle;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Wait;
import com.lk.engine.soccer.elements.referee.Referee;

public class ReturnToHomeRegion extends StateAdapter {
	public static final String NAME = "ReturnToHomeRegion";

	private final Referee referee;
	public ReturnToHomeRegion(final Referee referee) {
		super(NAME);
		this.referee = referee;
	}
	
	@Override
	public Check check(StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		if (!player.team().inControl()) {
			return Check.APPLY;
		}
		return Check.NO;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOn();

		if (!player.homeRegion().inside(player.steering().target(), Region.RegionModifier.HALF_SIZE)) {
			player.steering().setTarget(player.homeRegion().center());
		}
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		
		if (player.atTarget()) {
			player.steering().arriveOff();
			if (referee.gameOn())
				stateMachine.changeTo(Wait.NAME);
			else
				stateMachine.changeTo(Idle.NAME);
		}
		
		return State.Status.INTERRUPTIBLE;
	}
}
