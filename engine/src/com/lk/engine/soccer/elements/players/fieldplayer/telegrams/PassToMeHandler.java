package com.lk.engine.soccer.elements.players.fieldplayer.telegrams;

import static com.lk.engine.common.d2.Vector2D.sub;

import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Wait;

public class PassToMeHandler implements TelegramHandler {
	private final StateMachine stateMachine;

	public PassToMeHandler(final StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public Processed handle(final Telegram telegram) {
		final Player<?> player = stateMachine.getOwner();
		// get the position of the player requesting the pass
		final FieldPlayer receiver = (FieldPlayer) telegram.getExtraInfo();

		// if the ball is not within kicking range or their is already a
		// receiving player, this player cannot pass the ball to the player making
		// the request.
		if (player.team().receiver() != null || !player.canKickball()) {
			return Processed.NO;
		}

		// make the pass
		player.ball().kick(sub(receiver.pos(), player.ball().pos()), player.getParams().getMaxPassingForce());

		// let the receiver know a pass is coming
		stateMachine.changeTo(Wait.NAME);
		player.findSupport();

		return Processed.YES;
	}
}
