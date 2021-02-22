package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;

public class Exit extends Instruction {
	public Exit() {
		super("");
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		evaluator.exit(getOnExit());
		return Active.No;
	}

	@Override
	public String toString() {
		return "exit";
	}
}
