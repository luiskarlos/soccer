package com.lk.soccer.engine.intentions;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.soccer.console.params.PlayerParams;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.players.Player;

@RunWith(PowerMockRunner.class)
public class PlayerKnowledgeKickTheBallTest {
	@Mock
	private Player<?> player;
	@Mock
	private Ball ball;
	@Mock
	private PlayerParams params;
	@Mock
	private RandomGenerator random;

	private PlayerKnowledge knowledge;

	@Before
	public void setup() {
		knowledge = new PlayerKnowledge(player, params, random);

		when(ball.pos()).thenReturn(new Vector2D(10, 10));
		when(params.getKickingDistanceSq()).thenReturn(10.0);
	}

	@Test
	public void canKit() {
		when(player.pos()).thenReturn(new Vector2D(100, 100));
		when(player.heading()).thenReturn(new Vector2D(0, 1));

		System.out.println(degrees(knowledge.angle(new Vector2D(0, 10))));
		System.out.println(degrees(knowledge.angle(new Vector2D(10, 0))));
		System.out.println(degrees(knowledge.angle(new Vector2D(-10, 0))));
		System.out.println(degrees(knowledge.angle(new Vector2D(0, -10))));
	}

	private double degrees(double radians) {
		System.out.println("-------------");
		System.out.println(radians);
		return radians * (180.0 / Math.PI);
	}

	@Test
	public void canNotKickTheBallIfNull() {
		knowledge.setBall(null);
		assertEquals(false, knowledge.canKickTheBall());
	}

	@Test
	public void theBallIsNotInKickDistance() {
		when(player.pos()).thenReturn(new Vector2D(100, 100));

		knowledge.setBall(ball);
		assertEquals(false, knowledge.canKickTheBall());
	}

	@Test
	public void theBallIsInKickDistance() {
		when(player.pos()).thenReturn(new Vector2D(11, 11));

		knowledge.setBall(ball);
		assertEquals(true, knowledge.canKickTheBall());
	}

}
