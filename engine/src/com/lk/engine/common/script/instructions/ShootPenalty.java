package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class ShootPenalty extends Instruction implements Executable {
	public ShootPenalty(final String name) {
		super(name);
	}

	@Override
	public Active execute(Evaluator evaluator, Environment enviroment) {
		System.out.println("Not implemented " + toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "ShootPenalty " + super.toString();
	}
}
