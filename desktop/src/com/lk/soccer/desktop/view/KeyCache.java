package com.lk.soccer.desktop.view;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyCache {
	private Map<Integer, Boolean> keys;

	KeyCache() {
		keys = new HashMap<Integer, Boolean>();
	}

	public KeyCache(final KeyCache kc) {
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Cloning not allowed");
	}

	public void released(final KeyEvent e) {
		keys.put(e.getKeyCode(), Boolean.FALSE);
	}

	public void pressed(final KeyEvent e) {
		keys.put(e.getKeyCode(), Boolean.TRUE);
	}

	public boolean keyDown(final int key) {
		final Boolean b = keys.get(key);
		if (b == null)
			return false;
		return b;
	}
}