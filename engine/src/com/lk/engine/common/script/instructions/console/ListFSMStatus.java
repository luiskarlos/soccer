package com.lk.engine.common.script.instructions.console;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;
import com.lk.engine.common.debug.JsonDebug;
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
		final Debug debug = new JsonDebug();
		debug.openArray("debug");
		for (final StateMachine fsm : enviroment.getFSMS()) {
			if (fsm.getOwner() instanceof Debuggable) {
				debug.addToArray((Debuggable) fsm.getOwner());
			} else {
				debug.put(fsm.getOwner().toString(), "not debuggable");
			}
    }
		debug.closeArray();

		logger.log(Level.SEVERE, "\n" + debug.toString());
		return Active.No;
	}

	@Override
	public String toString() {
		return "listVariables";
	}
}
