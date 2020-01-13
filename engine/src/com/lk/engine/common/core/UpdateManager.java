package com.lk.engine.common.core;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.misc.Active;

public class UpdateManager {
	private final List<Updatable> updatables = new ArrayList<Updatable>();
	private final List<Updatable> tmpUpdatables = new ArrayList<Updatable>();

	public void add(final Updatable updatable) {
		updatables.add(updatable);
	}

	public void update() {
		tmpUpdatables.clear();
		tmpUpdatables.addAll(updatables);
		for (final Updatable u : tmpUpdatables) {
			if (u.update() == Active.No)
				updatables.remove(u);
		}
	}
}
