package com.lk.engine.common.core;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {
	private final List<Renderable> renderables = new ArrayList<Renderable>();

	public void push(final Renderable renderable) {
		renderables.add(0, renderable);
	}

	public void add(final Renderable renderable) {
		renderables.add(renderable);
	}

	public void render() {
		for (final Renderable u : renderables) {
			u.render();
		}
	}
}
