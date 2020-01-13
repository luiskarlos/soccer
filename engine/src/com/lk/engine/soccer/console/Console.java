package com.lk.engine.soccer.console;

public interface Console {
	enum Level {
		MSG, INFO, WARNING, ERROR,
	}

	boolean isActive(final Level level);

	void println(final Level level, final Object[] params);
}
