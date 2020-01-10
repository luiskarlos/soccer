package com.lk.engine.common.script.instructions;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateExecutable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class OnEnter extends Instruction {
	private String to;
	
	public OnEnter(State state, Executable executable) {
		super("");
		if (!(state instanceof StateExecutable)) {
			throw new RuntimeException("State is not StateExecutable: '" + state.getName() + "'  ");
		}
		
		((StateExecutable)state).onEnterExec(executable);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		return Active.No;
	}

	@Override
	public String toString() {
		return to;
	}
}
