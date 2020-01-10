package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.Evaluator;

public class ListenForMessage extends InstructionTarget {
	public ListenForMessage(final String name, final String target) {
		super(name, target);
	}

	@Override
	public Active execute(Evaluator evaluator, Enviroment enviroment) {
		System.out.println("Not implemented " + toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "ListenForMessage " + super.toString();
	}
}
