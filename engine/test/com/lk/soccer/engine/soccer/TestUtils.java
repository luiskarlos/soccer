package com.lk.soccer.engine.soccer;

import java.io.IOException;
import java.io.InputStream;

import com.lk.engine.soccer.script.ScriptParser;
import com.lk.engine.soccer.script.instructions.Block;
import com.lk.soccer.engine.soccer.script.ScriptParserTest;

public class TestUtils {
	public static String loadFile(final Class<?> clazz, final String name) {
		try {
			final InputStream stream = clazz.getResourceAsStream(name);
			final byte[] data = new byte[stream.available()];
			stream.read(data);
			stream.close();
			final String script = new String(data);
			return script;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Block loadScript(final String file) {
		final String script = TestUtils.loadFile(ScriptParserTest.class, file);
		final ScriptParser parser = new ScriptParser();
		final Block block = parser.parse(script);
		return block;
	}
}
