package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;
import com.lk.engine.soccer.script.Executable;

public class SendGlobalMessage extends Instruction implements Executable {
	public SendGlobalMessage(final String name) {
		super(name);
	}

	@Override
	public Active execute(Evaluator evaluator, Enviroment enviroment) {
		System.out.println("Not implemented " + toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "SendGlobalMessage " + super.toString();
	}
}
