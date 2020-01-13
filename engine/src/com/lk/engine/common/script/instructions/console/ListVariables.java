package com.lk.engine.common.script.instructions.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class ListVariables implements Executable {
	private static Logger logger = Logger.getLogger("ListVariables");
	
  private final String p;
  
	public ListVariables(String search) {
		if (search.isEmpty() || search.equals("*"))
			p = "";
		else
		  p = search;
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		final List<String> vars = enviroment.listVariables();

		final List<String> varsValue = new ArrayList<String>();
		for (String var : vars) {
	    if (var.startsWith(p)) {
	    	if (enviroment.getVariable(var).getParamAccess() != null)
	    	  varsValue.add(var + " = " + enviroment.getVariable(var).getParamAccess().getValue());
	    	else
	    		varsValue.add(var + " = null");
	    }
    }
		
		Collections.sort(vars);
		StringBuffer buffer = new StringBuffer();
		for (String var : varsValue) {
			buffer.append("  ").append(var.toString()).append('\n');
    }

		logger.log(Level.SEVERE, "\n" + buffer.toString());		
		return Active.No;
	}

	@Override
	public String toString() {
		return "listVariables";
	}
}
