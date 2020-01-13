/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;

public class SupportAttacker implements State {
	public SupportAttacker() {
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOn();
		player.steering().setTarget(player.team().getSupportSpot());
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		
		if (player.isClosestTeamMemberToBall()) {
			stateMachine.changeTo(ChaseBall.class);
			return;
		}

		// if his team loses control go back home
		if (!player.team().inControl()) {
			stateMachine.changeTo(ReturnToHomeRegion.class);
			return;
		}

		// if the best supporting spot changes, change the steering target
		if (player.team().getSupportSpot() != player.steering().target()) {
			player.steering().setTarget(player.team().getSupportSpot());

			player.steering().arriveOn();
		}

		// if this player has a shot at the goal AND the ATTACKER can pass
		// the ball to him the ATTACKER should pass the ball to this player
		if (player.team().canShoot(player.pos(), player.getParams().getMaxShootingForce())) {
			player.team().RequestPass((FieldPlayer) player);
		}


		// if this player is located at the support spot and his team still have
		// possession, he should remain still and turn to face the ball
		if (player.atTarget()) {
			player.steering().arriveOff();

			// the player should keep his eyes on the ball!
			player.trackBall();

			player.setVelocity(new Vector2D(0, 0));

			// if not threatened by another player request a pass
			if (!player.isThreatened()) {
				player.team().RequestPass((FieldPlayer) player);
			}
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		// set supporting player to null so that the team knows it has to
		// determine a new one.
		player.team().setSupportingPlayer(null);

		player.steering().arriveOff();
	}
}
