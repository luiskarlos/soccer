package com.lk.engine.common.console;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultConsole implements Console {

	private static Logger logger = Logger.getLogger("Console");
	
	@Override
	public boolean isActive(Level level) {
		return true;
	}

	@Override
	public void println(Level level, Object[] params) {
		final StringBuilder buffer = new StringBuilder(level.toString());
		for (final Object object : params) {
			buffer.append(' ').append(object.toString());
		}
		
		logger.log(level, buffer.toString());
	}

}
