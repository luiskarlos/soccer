package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class CatchPenalty extends Instruction {
	public CatchPenalty(final String name) {
		super(name);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		System.out.println("Not implemented " + toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "CatchPenalty " + super.toString();
	}
}
