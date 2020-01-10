package com.lk.soccer.engine.soccer;

import java.io.IOException;
import java.io.InputStream;

import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.ScriptParser;
import com.lk.engine.common.script.instructions.Block;
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
		final ScriptParser parser = new ScriptParser(new Enviroment(null, null, null, null, null));
		final Block block = parser.parse(script);
		return block;
	}
}
