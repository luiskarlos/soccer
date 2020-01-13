package com.lk.engine.common.console;

import java.util.logging.Level;

public interface Console {

	boolean isActive(final Level level);

	void println(final Level level, final Object[] params);
}
