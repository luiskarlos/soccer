package com.lk.engine.common.script.instructions;

import java.util.List;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateExecutable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.Evaluator;

public class Before extends Instruction {
	private String to;
	
	public Before(String entity, State state, String check, List<State> target) {
		super("");
		if (!(state instanceof StateExecutable)) {
			throw new RuntimeException("State is not StateExecutable: '" + state.getName() + "'  ");
		}
		
		if (!entity.equals("player")) {
			throw new RuntimeException("Unsuported entity: '" + entity + "'  " + toString());
		}
		((StateExecutable)state).beforeExecute(target);
		to = "before " + entity + " " + state.getName() + " check [" + target + "]";
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
