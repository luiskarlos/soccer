package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;

public class IsListenenForMessage extends InstructionTarget {
	public IsListenenForMessage(final String name, final String msg) {
		super(name, msg);
	}

	@Override
	public Active execute(Evaluator evaluator, Environment enviroment) {
		System.out.println("Not implemented " + toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "IsListenenForMessage " + super.toString();
	}

}
