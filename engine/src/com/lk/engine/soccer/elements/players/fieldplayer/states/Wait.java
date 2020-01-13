/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;

public class Wait implements State {
	private final Referee referee;

	public Wait(final Referee referee) {
		this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		// if the game is not on make sure the target is the center of the player's
		// home region. This is ensure all the players are in the correct positions
		// ready for kick off
		if (!referee.gameOn()) {
			player.steering().setTarget(player.homeRegion().center());
		}
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		
		if (player.isClosestTeamMemberToBall()) {
			stateMachine.changeTo(ChaseBall.class);
			return;
		}
		
		// if the player has been jostled out of position, get back in position
		if (!player.atTarget()) {
			player.steering().arriveOn();
			return;
		} else {
			player.steering().arriveOff();
			player.setVelocity(new Vector2D(0, 0));
			// the player should keep his eyes on the ball!
			player.trackBall();
		}

		// if this player's team is controlling AND this player is not the ATTACKER
		// AND is further up the field than the ATTACKER he should request a pass.
		if (player.team().inControl() && (!player.isControllingPlayer()) && player.isAheadOfAttacker()) {
			player.team().RequestPass((FieldPlayer) player);
			return;
		}

		if (referee.gameOn()) {
			// if the ball is nearer this player than any other team member AND
			// there is not an assigned receiver AND neither goalkeeper has
			// the ball, go chase it
			if (player.isClosestTeamMemberToBall() && player.team().receiver() == null && !referee.goalKeeperHasBall()) {
				stateMachine.changeTo(ChaseBall.class);
				return;
			}
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
	}

}