package com.lk.soccer.engine.intentions;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lk.engine.common.console.params.PlayerParams;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.players.Player;

@RunWith(PowerMockRunner.class)
public class PlayerKnowledgecanShootToGoalTest {
	@Mock
	private Player<?> player;
	@Mock
	private Ball ball;
	@Mock
	private PlayerParams params;
	@Mock
	private RandomGenerator random;

	//private PlayerKnowledge knowledge;

	@Before
	public void setup() {
		//knowledge = new PlayerKnowledge(player, params, random);

		when(ball.pos()).thenReturn(new Vector2D(10, 10));
		when(params.getKickingDistanceSq()).thenReturn(10.0);
	}

	@Test
	public void canNotKickTheBallIfNull() {

	}

}
