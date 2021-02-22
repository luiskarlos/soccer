package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;

public class CatchPenalty extends Instruction {
	public CatchPenalty(final String name) {
		super(name);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		System.out.println("Not implemented " + toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "CatchPenalty " + super.toString();
	}
}
