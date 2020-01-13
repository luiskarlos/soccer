package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.console.ParamAccess;
import com.lk.engine.soccer.console.ParamHandler;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class Set extends InstructionTarget {
	private String value = "";

	public Set(final String name, final String newValue) {
		super(name, newValue);
		this.value = "";
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		defineVar(enviroment);
		value = getTarget(enviroment);
		return Active.No;
	}

	private void defineVar(final Enviroment enviroment) {
		if (!enviroment.existsVariable(getElement(enviroment))) {
			final ParamHandler handler = new ParamHandler(getElement(enviroment), this, new ParamAccess() {
				@Override
				public void setValue(final String val) {
					value = val;
				}

				@Override
				public String getValue() {
					return value;
				}
			});
			enviroment.setVariable(getElement(enviroment), handler);
		}
	}

	@Override
	public String toString() {
		return "Set " + super.toString();
	}
}
