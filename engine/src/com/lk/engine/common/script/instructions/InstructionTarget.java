package com.lk.engine.common.script.instructions;

import com.lk.engine.common.script.Environment;

public abstract class InstructionTarget extends Instruction {
	private final String target;

	public InstructionTarget(final String element, final String target) {
		super(element);
		this.target = target;
	}

	public String getTarget(final Environment enviroment) {
		return getVariableName(target, enviroment);
	}

	@Override
	public String toString() {
		return super.toString() + " " + target;
	}

}
