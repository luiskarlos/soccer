/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;
import static com.lk.engine.common.telegraph.Message.RECEIVE_BALL;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.misc.CppToJava.ObjectRef;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;

public class KickBall implements State {
	private final Telegraph telegraph;
	private final RandomGenerator random;
	private final Referee referee;

	public KickBall(final Telegraph telegraph, final RandomGenerator random, final Referee referee) {
		this.telegraph = telegraph;
		this.random = random;
		this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final FieldPlayer player = stateMachine.getOwner();
		// let the team know this player is controlling
		telegraph.post(new TelegramPackage(Message.CONTROLING_PLAYER, player));

		// the player can only make so many kick attempts per second.
		if (!player.isReadyForNextKick()) {
			stateMachine.changeTo(ChaseBall.class);
		}
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();

		// calculate the dot product of the vector pointing to the ball
		// and the player's heading
		final Vector2D toBall = sub(player.ball().pos(), player.pos());
		final double dot = player.heading().dot(vec2DNormalize(toBall));
		final Vector2D ballTarget = new Vector2D();
		// the dot product is used to adjust the shooting force. The more
		// directly the ball is ahead, the more forceful the kick
		final double shootPower = player.getParams().getMaxShootingForce() * dot;

		// if a shot is possible, this vector will hold the position along the
		// opponent's goal line the player should aim for.
		
		// if it is determined that the player could score a goal from this position
		// OR if he should just kick the ball anyway, the player will attempt to
		// make the shot
		if (player.team().canShoot(player.ball().pos(), shootPower, ballTarget)
		    || random.nextDouble() < player.getParams().getChanceAttemptsPotShot()) {
			attemptToTarget(stateMachine, ballTarget, shootPower);
			return;
		}

		// cannot kick the ball if the goalkeeper is in possession or if it is
		// behind the player or if there is already an assigned receiver. So just
		// continue chasing the ball
		// Goaly has ball / ball behind player
		if (player.team().receiver() != null || referee.goalKeeperHasBall() || (dot < 0)) {
			stateMachine.changeTo(ChaseBall.class);
			return;
		}/**/

		/* Attempt a shot at the goal */

		if (player.team().getCoach().hasPassIndication()) {
			attemptToTarget(stateMachine, player.team().getCoach().consumePassIndication(), shootPower);
			stateMachine.changeTo(ChaseBall.class);
			return;
		}

		/* Attempt a pass to a player */
		final double passPower = player.getParams().getMaxPassingForce() * dot;
		final ObjectRef<Player<?>> receiverRef = new ObjectRef<Player<?>>();
		// test if there are any potential candidates available to receive a pass
		if (player.isThreatened()
		    && player.team().findPass(player, receiverRef, ballTarget, passPower, player.getParams().getMinPassDistance())) {
			attemptToPass(stateMachine, ballTarget, passPower, receiverRef);
			return;
		} // cannot shoot or pass, so dribble the ball upfield
		else {
			player.findSupport();
			stateMachine.changeTo(Dribble.class);
		}
	}

	private void attemptToTarget(final StateMachine stateMachine, Vector2D target, final double force) {
		final Player<?> player = stateMachine.getOwner();

		final double distance = target.distanceSq(player.ball().pos());
		final double maxDistance = 200 * 200;
		final double percentage = Math.min(distance, maxDistance) / maxDistance;

		final double modulatedForce = force * percentage;

		// add some noise to the kick. We don't want players who are
		// too accurate! The amount of noise can be adjusted by altering
		// Prm.PlayerKickingAccuracy
		target = player.ball().addNoiseToKick(player, player.ball().pos(), target);
		// this is the direction the ball will be kicked in
		final Vector2D kickDirection = sub(target, player.ball().pos());
		player.ball().kick(kickDirection, modulatedForce);

		// change state
		stateMachine.changeTo(Wait.class);

		player.findSupport();
	}

	private void attemptToPass(final StateMachine stateMachine, Vector2D ballTarget, final double power,
	    final ObjectRef<Player<?>> receiverRef) {
		final Player<?> player = stateMachine.getOwner();
		final Player<?> receiver = (Player<?>) receiverRef.get();
		// add some noise to the kick
		ballTarget = player.ball().addNoiseToKick(player, player.ball().pos(), ballTarget);

		final Vector2D KickDirection = sub(ballTarget, player.ball().pos());
		player.ball().kick(KickDirection, power);

		// let the receiver know a pass is coming
		telegraph.post(new TelegramPackage(0, player.Id(), receiver.Id(), RECEIVE_BALL, ballTarget));

		// the player should wait at his current position unless instruced
		// otherwise
		stateMachine.changeTo(Wait.class);

		player.findSupport();
	}

	@Override
	public void exit(final StateMachine stateMachine) {
	}

}
