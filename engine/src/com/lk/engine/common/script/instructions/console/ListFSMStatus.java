package com.lk.engine.common.script.instructions.console;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class ListFSMStatus implements Executable {
	private static Logger logger = Logger.getLogger("fsm.status");
	
	public ListFSMStatus() {
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		final StringBuffer buffer = new StringBuffer();
		for (final StateMachine fsm : enviroment.getFSMS()) {
			buffer.append("  ").append(fsm.getOwner().toString()).append(" in ").append(fsm.getCurrentStateName()).append('\n');
    }

		logger.log(Level.SEVERE, "\n" + buffer.toString());		
		return Active.No;
	}

	@Override
	public String toString() {
		return "listVariables";
	}
}
