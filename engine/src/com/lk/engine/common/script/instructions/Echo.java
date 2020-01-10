package com.lk.engine.common.script.instructions;

import java.util.logging.Level;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.Evaluator;

public class Echo extends Instruction {
	private final Level level;
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
		for (int i = 0; i < strings.length; i++) {
			String val = strings[i];
			if (val.startsWith("$") || enviroment.existsVariable(val))
				val = getVariableName(val, enviroment);

			object[i] = val;
		}
		enviroment.getConsole().println(level, object);
		return Active.No;
	}

	@Override
	public String toString() {
		return "exit";
	}
}
