package com.lk.soccer.engine.intentions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.Goal;
import com.lk.engine.soccer.elements.players.Player;

@RunWith(PowerMockRunner.class)
public class GoalIntentionTest {
	@Mock
	private Player<?> player;
	@Mock
	private Ball ball;
	@Mock
	private Goal goal;
	@Mock
	private PlayerKnowledge knowledge;

	@Test
	public void test_0_intension() {
		final GoalIntention goalIntention = new GoalIntention();
		final Intention intention = goalIntention.process(player, knowledge);

		assertEquals(0, intention.value());
	}

	@Test
	public void test_full_intension() {
		final GoalIntention goalIntention = new GoalIntention();
		final Intention intention = goalIntention.process(player, knowledge);

		assertEquals(99, intention.value());
	}

}
