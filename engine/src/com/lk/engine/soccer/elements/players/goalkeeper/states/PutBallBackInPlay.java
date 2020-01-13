/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.goalkeeper.states;

import static com.lk.engine.common.d2.Vector2D.sub;
import static com.lk.engine.common.d2.Vector2D.vec2DNormalize;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.misc.CppToJava.ObjectRef;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.Player;

public class PutBallBackInPlay implements State {
	private final Telegraph telegraph;
	private final Referee referee;

	public PutBallBackInPlay(final Telegraph telegraph, final Referee referee) {
		this.telegraph = telegraph;
		this.referee = referee;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		// let the team know that the keeper is in control
		telegraph.post(new TelegramPackage(Message.CONTROLING_PLAYER, player));
		telegraph.post(new TelegramPackage(Message.GO_HOME));
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();
		Player<?> receiver = null;
		final Vector2D ballTarget = new Vector2D();
		final ObjectRef<Player<?>> receiverRef = new ObjectRef<Player<?>>(receiver);

		// test if there are players further forward on the field we might
		// be able to pass to. If so, make a pass.
		if (player.team().findPass(player, receiverRef, ballTarget, player.getParams().getMaxPassingForce(),
		    player.getParams().getMinPassDistance())) {
			receiver = (Player<?>) receiverRef.get();
			// make the pass
			player.ball().kick(vec2DNormalize(sub(ballTarget, player.ball().pos())), player.getParams().getMaxPassingForce());

			// goalkeeper no longer has ball
			referee.setGoalKeeperHasBall(false);

			// let the receiving player know the ball's comin' at him
			telegraph.post(new TelegramPackage(0, player.Id(), receiver.Id(), Message.RECEIVE_BALL, ballTarget));

			// go back to tending the goal
			stateMachine.changeTo(TendGoal.class);

			return;
		}

		player.setVelocity(new Vector2D());
	}

	@Override
	public void exit(final StateMachine stateMachine) {
	}
}
