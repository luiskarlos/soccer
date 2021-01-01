package com.lk.engine.common.core;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.misc.Active;

public class UpdateManager {
	private final List<Updatable> uneatable = new ArrayList<>();
	private final List<Updatable> tmpUneatable = new ArrayList<>();

	public void add(final Updatable updatable) {
		uneatable.add(updatable);
	}

	public void update(long time, int delta) {
		tmpUneatable.clear();
		tmpUneatable.addAll(uneatable);
		for (final Updatable u : tmpUneatable) {
			if (u.update(time, delta) == Active.No)
				uneatable.remove(u);
		}
	}
}
