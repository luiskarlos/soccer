package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class None extends Instruction {
	public static final None NONE = new None();

	private None() {
		super("");
	}

	@Override
	public Active execute(Evaluator evaluator, Enviroment enviroment) {
		return Active.No;
	}

	@Override
	public String toString() {
		return "None";
	}
}
