package com.lk.engine.common.script.instructions;

import com.google.gwt.event.shared.EventBus;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;
import com.lk.engine.common.tools.CsvSerializer;

public class CsvLoad extends Instruction implements Executable {
	private final EventBus eventBus;
	private final CsvSerializer.CsvLoadEvent event;

	public CsvLoad(EventBus eventBus, String data) {
		super("csv.load");
		this.eventBus = eventBus;
		this.event = new CsvSerializer.CsvLoadEvent(data);
	}

	@Override
	public Active execute(Evaluator evaluator, Environment environment) {
		eventBus.fireEvent(event);
		return Active.No;
	}

	@Override
	public String toString() {
		return "csv.load " + super.toString();
	}
}
