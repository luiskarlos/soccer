package com.lk.soccer.engine.soccer.script;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lk.engine.soccer.script.ScriptParser;
import com.lk.engine.soccer.script.instructions.Block;
import com.lk.engine.soccer.script.instructions.ListenForMessage;
import com.lk.engine.soccer.script.instructions.Set;
import com.lk.engine.soccer.script.instructions.Spawn;
import com.lk.engine.soccer.script.instructions.WalkTo;
import com.lk.soccer.engine.soccer.TestUtils;

public class ScriptParserTest {
	@Test
	public void emptyBlock() {
		final ScriptParser parser = new ScriptParser();
		final Block block = parser.parse("named { }");

		assertEquals(1, block.getInstructions().size());
		assertEquals(Block.class, block.getInstructions().get(0).getClass());
		assertEquals("named {...} 0", ((Block) block.getInstructions().get(0)).toString());
	}

	@Test
	public void anonymousBlock() {
		final ScriptParser parser = new ScriptParser();
		final Block block = parser.parse("{ }");

		assertEquals(1, block.getInstructions().size());
		assertEquals(Block.class, block.getInstructions().get(0).getClass());
		assertEquals("anonymous {...} 0", ((Block) block.getInstructions().get(0)).toString());
	}

	@Test
	public void spawn() {
		final ScriptParser parser = new ScriptParser();
		final Block block = parser.parse("spawn red.player1 flag1");

		assertEquals(1, block.getInstructions().size());
		assertEquals(Spawn.class, block.getInstructions().get(0).getClass());
		assertEquals("Spawn red.player1 flag1", ((Spawn) block.getInstructions().get(0)).toString());
	}

	@Test
	public void set() {
		final ScriptParser parser = new ScriptParser();
		final Block block = parser.parse("set red.player1.name carlitos");

		assertEquals(1, block.getInstructions().size());
		assertEquals(Set.class, block.getInstructions().get(0).getClass());
		assertEquals("Set red.player1.name carlitos", ((Set) block.getInstructions().get(0)).toString());
	}

	@Test
	public void onExit() {
		final ScriptParser parser = new ScriptParser();
		final Block block = parser.parse("walk red.goalkeeper to red.goal onExit ListenForMessage red.goalkeeper shoot");

		assertEquals(1, block.getInstructions().size());
		assertEquals(WalkTo.class, block.getInstructions().get(0).getClass());

		final WalkTo walkTo = (WalkTo) block.getInstructions().get(0);
		assertEquals("WalkTo red.goalkeeper red.goal", walkTo.toString());
		assertEquals(ListenForMessage.class, walkTo.getOnExit().getClass());

		final ListenForMessage msg = (ListenForMessage) walkTo.getOnExit();
		assertEquals("ListenForMessage red.goalkeeper shoot", msg.toString());
	}

	@Test
	public void parsePenalty() {
		final String script = TestUtils.loadFile(ScriptParserTest.class, "test.script");

		final ScriptParser parser = new ScriptParser();
		final Block block = parser.parse(script);

		assertEquals(1, block.getInstructions().size());
	}
}
