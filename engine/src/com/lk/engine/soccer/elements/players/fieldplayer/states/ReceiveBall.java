/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import static java.lang.Math.abs;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.players.Player;

public class ReceiveBall implements State {
	private final Telegraph telegraph;
	private final RandomGenerator random;
	private final FieldPlayingArea playingArea;

	public ReceiveBall(final Telegraph telegraph, final RandomGenerator random, final FieldPlayingArea playingArea) {
		this.telegraph = telegraph;
		this.random = random;
		this.playingArea = playingArea;
	}

	/**
	 * @return true if the player is located in the designated 'hot region' -- the
	 *         area close to the opponent's goal
	 */
	public boolean inHotRegion(Player<?> player) {
		return abs(player.pos().y - player.team().opponents().goal().center().y) < playingArea.getArea().length() / 3.0;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		// let the team know this player is receiving the ball
		player.team().setReceiver(player);

		// this player is also now the controlling player
		telegraph.post(new TelegramPackage(Message.CONTROLING_PLAYER, player));

		// there are two types of receive behavior. One uses ARRIVE to direct
		// the receiver to the position sent by the passer in its telegram. The
		// other uses the PURSUIT behavior to pursue the ball.
		// This statement selects between them dependent on the probability
		// ChanceOfUsingArriveTypeReceiveBehavior, whether or not an opposing
		// player is close to the receiving player, and whether or not the receiving
		// player is in the opponents 'hot region' (the third of the pitch closest
		// to the opponent's goal
		final double passThreatRadius = player.getParams().getPassThreatRadius();

		if ((inHotRegion(player) || random.nextDouble() < player.getParams().getChanceOfUsingArriveTypeReceiveBehavior())
		    && !player.team().isOpponentWithinRadius(player.pos(), passThreatRadius)) {
			player.steering().arriveOn();
		} else {
			player.steering().pursuitOn();
		}

	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		// if the ball comes close enough to the player or if his team lose control
		// he should change state to chase the ball
		if (player.ballWithinReceivingRange() || !player.team().inControl()) {
			stateMachine.changeTo(ChaseBall.class);
			return;
		}

		if (player.steering().pursuitIsOn()) {
			player.steering().setTarget(player.ball().pos());
		}

		// if the player has 'arrived' at the steering target he should wait and
		// turn to face the ball
		if (player.atTarget()) {
			player.steering().arriveOff();
			player.steering().pursuitOff();
			player.trackBall();
			player.setVelocity(new Vector2D(0, 0));
		}
	}

	@Override
	public void exit(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.steering().arriveOff();
		player.steering().pursuitOff();

		player.team().setReceiver(null);
	}
}