package com.lk.soccer.engine.intentions;

import static com.lk.engine.common.d2.Vector2D.vec2DDistanceSq;

import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.Goal;
import com.lk.engine.soccer.elements.players.Player;

public class SoccerLogic {
	public boolean canShoot(final Player<?> player, final Ball ball, final Goal goal) {
		return vec2DDistanceSq(ball.pos(), player.pos()) < player.getParams().getKickingDistanceSq();
	}
}
