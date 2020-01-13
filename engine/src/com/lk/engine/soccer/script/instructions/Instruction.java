package com.lk.engine.soccer.script.instructions;

import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Executable;

public abstract class Instruction implements Executable {
	private final String element;
	private Executable onExit = None.NONE;

	protected Instruction(final String element) {
		this.element = element;
	}

	public Executable getOnExit() {
		return onExit;
	}

	public void setOnExit(final Executable onExit) {
		this.onExit = onExit;
	}

	public String getElement(final Enviroment enviroment) {
		return getVariableName(element, enviroment);
	}

	protected String getVariableName(final String name, final Enviroment enviroment) {
		String resolvedName = name;
		final String realName = resolvedName.substring(1);
		for (; resolvedName.startsWith("$");) {
			resolvedName = enviroment.getVariable(realName).getParamAccess().getValue();
		}
		return resolvedName;
	}

	@Override
	public String toString() {
		return element;
	}
}
