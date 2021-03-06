package com.lk.engine.common.script.instructions;

import com.lk.engine.common.console.ParamAccess;
import com.lk.engine.common.console.ParamHandler;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;

public class Set extends InstructionTarget {
	private String value = "";

	public Set(final String name, final String newValue) {
		super(name, newValue);
		this.value = "";
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		defineVar(enviroment);
		value = getTarget(enviroment);
		enviroment.getVariable(getElement(enviroment)).getParamAccess().setValue(value);
		return Active.No;
	}

	private void defineVar(final Environment enviroment) {
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
