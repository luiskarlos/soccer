/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.fieldplayer.states;

import java.util.List;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateAdapter;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.soccer.elements.players.Player;

public class WaitForStateChange extends StateAdapter {
	public static final String NAME = "WaitForStateChange";

	private static final double TALK_TO_OTHER_PLAYER = 0.01;
	private static final double SEE_THE_BALL = 0.01;

	private final RandomGenerator random;

	public WaitForStateChange(final RandomGenerator random) {
		super(NAME);
		this.random = random;
	}

	@Override
	public State.Status execute(final StateMachine stateMachine, final Object data) {
		final Player<?> player = stateMachine.getOwner();

		if (random.nextDouble() < SEE_THE_BALL) {
			player.trackBall();
		} else if (random.nextDouble() < TALK_TO_OTHER_PLAYER) {
			final List<Player<?>> players = player.team().members();
			final Player<?> randomPlayer = players.get(random.nextInt(players.size()));
			player.rotateHeadingToFacePosition(randomPlayer.pos());
			randomPlayer.rotateHeadingToFacePosition(player.pos());
		}

		return Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(StateMachine stateMachine) {
		final Player<?> player = stateMachine.getOwner();
		player.trackBall();
	}
}
