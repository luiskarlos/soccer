package com.lk.engine.soccer.injector;

import com.lk.engine.common.fsm.State;

/**
 * Proxy class to provide State instances without expose the entire injector.
 */
public class States {
	private final Injector injector;

	public States(final Injector injector) {
		super();
		this.injector = injector;
	}

	public <T extends State> T get(final Class<T> clazz) {
		return injector.get(clazz);
	}
}
