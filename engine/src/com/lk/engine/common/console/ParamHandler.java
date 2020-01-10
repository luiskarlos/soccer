package com.lk.engine.common.console;

public class ParamHandler {
	private final String name;
	private final Object container;
	private final ParamAccess paramAccess;

	public ParamHandler(String name, Object container, ParamAccess paramAccess) {
		this.name = name;
		this.container = container;
		this.paramAccess = paramAccess;
	}

	public String name() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public <T> T container(Class<T> t) {
		return (T) container;
	}

	public ParamAccess getParamAccess() {
		return paramAccess;
	}
}
