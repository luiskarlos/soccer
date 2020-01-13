package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.console.Console;
import com.lk.engine.soccer.console.Console.Level;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class Echo extends Instruction {
	private final Console.Level level;
	private final String[] strings;
	private final Object[] object;

	public Echo(final Level level, final String[] strings) {
		super("");
		this.level = level;
		this.strings = strings;
		this.object = new Object[strings.length];
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		if (enviroment.getConsole().isActive(level)) {
			for (int i = 0; i < strings.length; i++) {
				String val = strings[i];
				if (val.startsWith("$") || enviroment.existsVariable(val))
					val = getVariableName(val, enviroment);

				object[i] = val;
			}
			enviroment.getConsole().println(level, object);
		}
		return Active.No;
	}

	@Override
	public String toString() {
		return "exit";
	}
}
