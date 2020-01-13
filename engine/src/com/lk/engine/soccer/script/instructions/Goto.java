package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class Goto extends Instruction {
	public Goto(final String name) {
		super(name);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		final Block block = enviroment.getBlock(getElement(enviroment));
		evaluator.eval(block);
		return Active.No;
	}

	@Override
	public String toString() {
		return "goto " + super.toString();
	}
}