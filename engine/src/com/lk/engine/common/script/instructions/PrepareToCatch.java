package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class PrepareToCatch extends Instruction implements Executable {
	public PrepareToCatch(final String name) {
		super(name);
	}

	@Override
	public Active execute(Evaluator evaluator, Enviroment enviroment) {
		System.out.println("Not implemented " + toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "PrepareToCatch " + super.toString();
	}

}