/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;

public class ReturnToHomeRegion implements State {
	private final Referee referee;

	public ReturnToHomeRegion(final Referee referee) {
		this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final FieldPlayer player = stateMachine.getOwner();
		player.steering().arriveOn();

		if (!player.homeRegion().inside(player.steering().target(), Region.RegionModifier.HALF_SIZE)) {
			player.steering().setTarget(player.homeRegion().center());
		}
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		if (referee.gameOn()) {
			// if the ball is nearer this player than any other team member &&
			// there is not an assigned receiver && the goalkeeper does not gave
			// the ball, go chase it
			if (player.isClosestTeamMemberToBall() && (player.team().receiver() == null) && !referee.goalKeeperHasBall()) {
				stateMachine.changeTo(ChaseBall.class);
				return;
			}
			// if game is on and close enough to home, change state to wait and set
			// the
			// player target to his current position.(so that if he gets jostled out
			// of
			// position he can move back to it)
			else if (player.homeRegion().inside(player.pos(), Region.RegionModifier.HALF_SIZE)) {
				player.steering().setTarget(player.pos());
				stateMachine.changeTo(Wait.class);
			}
		}
		// if game is not on the player must return much closer to the center of his
		// home region
		else if (!referee.gameOn() && player.atTarget()) {
			stateMachine.changeTo(Wait.class);
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOff();
	}

}
