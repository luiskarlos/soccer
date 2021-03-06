package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;

public class None extends Instruction {
	public static final None NONE = new None();

	private None() {
		super("");
	}

	@Override
	public Active execute(Evaluator evaluator, Environment enviroment) {
		return Active.No;
	}

	@Override
	public String toString() {
		return "None";
	}
}
