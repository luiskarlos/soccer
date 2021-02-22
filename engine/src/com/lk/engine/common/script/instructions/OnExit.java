package com.lk.engine.common.script.instructions;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateExecutable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class OnExit extends Instruction {
	private String to;
	
	public OnExit(State state, Executable executable) {
		super("");
		if (!(state instanceof StateExecutable)) {
			throw new RuntimeException("State is not StateExecutable: '" + state.getName() + "'  ");
		}
		
		((StateExecutable)state).onExitExec(executable);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		return Active.No;
	}

	@Override
	public String toString() {
		return to;
	}
}
