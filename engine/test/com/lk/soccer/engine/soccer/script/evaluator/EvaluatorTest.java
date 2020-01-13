package com.lk.soccer.engine.soccer.script.evaluator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.fsm.State;
import com.lk.engine.soccer.console.Console;
import com.lk.engine.soccer.console.DefaultConsole;
import com.lk.engine.soccer.console.ParamHandler;
import com.lk.engine.soccer.console.Parameters;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;
import com.lk.engine.soccer.script.ScriptParser;
import com.lk.engine.soccer.script.instructions.Block;
import com.lk.soccer.engine.soccer.TestUtils;

public class EvaluatorTest {
	public Enviroment newEnviroment() {
		final Parameters parameters = new Parameters(new ArrayList<ParamHandler>());
		final Map<String, Class<State>> states = new HashMap<String, Class<State>>();
		return new Enviroment(null, parameters, states, new DefaultConsole(), null);
	}

	@Test
	public void run_test_script() {
		final Enviroment enviroment = newEnviroment();
		final Block block = TestUtils.loadScript("test.script");
		eval(enviroment, block);
	}

	@Test
	public void create_spot() {
		final Enviroment enviroment = newEnviroment();
		eval(enviroment, "spot a 1,1 1 1");
		assertEquals(true, enviroment.existsVariable("a"));
		assertEquals(1, enviroment.getVariable("a").container(Region.class).left(), 0.001);
		assertEquals(1, enviroment.getVariable("a").container(Region.class).top(), 0.001);

		eval(enviroment, "spot a 10,10 1 1");

		assertEquals(true, enviroment.existsVariable("a"));
		assertEquals(10, enviroment.getVariable("a").container(Region.class).left(), 0.001);
		assertEquals(10, enviroment.getVariable("a").container(Region.class).top(), 0.001);
	}

	@Test
	public void variable() {
		final Enviroment enviroment = newEnviroment();
		eval(enviroment, "set a name");
		assertEquals(true, enviroment.existsVariable("a"));
		assertEquals("name", enviroment.getVariable("a").getParamAccess().getValue());

		eval(enviroment, "set b $a");
		assertEquals("name", enviroment.getVariable("b").getParamAccess().getValue());
		assertEquals("name", enviroment.getVariable("a").getParamAccess().getValue());

		eval(enviroment, "set $a weird");
		assertEquals("weird", enviroment.getVariable("name").getParamAccess().getValue());
	}

	@Test
	public void echo() {
		final Enviroment enviroment = newEnviroment();
		final TestConsle console = new TestConsle();
		enviroment.setConsole(console);

		console.setExpected(Console.Level.MSG, new String[] { "my", "echo" });
		eval(enviroment, "echo \"my echo\"");

		eval(enviroment, "set val 12");
		console.setExpected(Console.Level.MSG, new String[] { "my", "echo", "12" });
		eval(enviroment, "echo \"my echo $val\"");

		console.setExpected(Console.Level.INFO, new String[] { "my", "echo", "12" });
		eval(enviroment, "echo info \"my echo $val\"");

	}

	private void eval(final Enviroment enviroment, final String script) {
		final Block block = new ScriptParser().parse(script);
		eval(enviroment, block);
	}

	private void eval(final Enviroment enviroment, final Block block) {
		final Evaluator evaluator = new Evaluator(enviroment);
		evaluator.eval(block);

		for (int i = 0; i < 10; i++) {
			evaluator.update();
		}
	}

	private class TestConsle implements Console {
		private Level level;
		private String[] expected;

		@Override
		public void println(Level level, Object[] params) {
			assertEquals(this.level, level);
			assertEquals(this.expected.length, params.length);
			for (int i = 0; i < params.length; i++) {
				assertEquals(expected[i], params[i]);
			}
		}

		@Override
		public boolean isActive(Level level) {
			return true;
		}

		public void setExpected(Level level, String[] expected) {
			this.level = level;
			this.expected = expected;
		}
	}
}
