package com.lk.engine.soccer.console;

public class DefaultConsole implements Console {

	@Override
	public boolean isActive(Level level) {
		return true;
	}

	@Override
	public void println(Level level, Object[] params) {
		StringBuilder buffer = new StringBuilder(level.toString());
		for (Object object : params) {
			buffer.append(' ').append(object.toString());
		}
		System.out.println(buffer.toString());
	}

}
