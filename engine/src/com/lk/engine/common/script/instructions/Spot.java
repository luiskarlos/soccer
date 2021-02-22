package com.lk.engine.common.script.instructions;

import com.lk.engine.common.console.ParamAccess;
import com.lk.engine.common.console.ParamHandler;
import com.lk.engine.common.core.Region;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;

public class Spot extends Instruction {
	private Region spot; // Environment global var.
	private final double left;
	private final double top;
	private final double width;
	private final double height;

	public Spot(final String name, final String left, final String top, final String width, final String height) {
		super(name);
		this.left = Double.parseDouble(left);
		this.top = Double.parseDouble(top);
		this.width = Double.parseDouble(width);
		this.height = Double.parseDouble(height);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		defineVar(enviroment);
		spot.set(left, top, left + width, top + height);
		return Active.No;
	}

	private void defineVar(final Environment enviroment) {
		if (spot == null) {
			spot = new Region(left, top, left + width, top + height);
			final ParamHandler handler = new ParamHandler(getElement(enviroment), spot, new ParamAccess() {
				@Override
				public void setValue(final String val) {
					final String[] values = val.split(" ");
					final double left = Double.parseDouble(values[0]);
					final double top = Double.parseDouble(values[1]);
					final double right = Double.parseDouble(values[2]);
					final double bottom = Double.parseDouble(values[3]);

					spot.set(left, top, left + right, top + bottom);
				}

				@Override
				public String getValue() {
					return spot.left() + " " + spot.top() + " " + spot.right() + " " + spot.bottom();
				}
			});
			enviroment.setVariable(getElement(enviroment), handler);
		}

		spot = enviroment.getVariable(getElement(enviroment)).container(Region.class);
	}

	@Override
	public String toString() {
		return "Spot " + super.toString() + " " + spot.left() + " " + spot.top() + " " + spot.right() + " " + spot.bottom();
	}
}
