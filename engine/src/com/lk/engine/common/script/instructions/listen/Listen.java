package com.lk.engine.common.script.instructions.listen;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;
import com.lk.engine.common.script.instructions.Instruction;

public class Listen extends Instruction {
	// private final List<On> events = new ArrayList<>();
	// private String entityName;

	public Listen() {
		super("");
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		/*
		 * final List<Player<?>> entities = enviroment.getPlayers(entityName); for
		 * (Player<?> p : entities) { for (On on : events) { if (on.getTrigger() ==
		 * Trigger.ENTER) { p.getFSM().registerOnEnter(on.getState(),
		 * on.getExecutable()); } else if (on.getTrigger() == Trigger.EXIT) {
		 * p.getFSM().registerOnExit(on.getState(), on.getExecutable()); } else if
		 * (on.getTrigger() == Trigger.CHANGE) {
		 * p.getFSM().registerOnChange(on.getState(), on.getTarget(),
		 * on.getExecutable()); } } }/*
		 */
		return Active.No;
	}

	@Override
	public String toString() {
		return "listen";
	}

	public static class On {
		private final Trigger trigger;
		private final State state;
		private final State target;
		private final Executable executable;

		public On(Trigger trigger, State state, Executable executable, State target) {
			super();
			this.trigger = trigger;
			this.state = state;
			this.executable = executable;
			this.target = target;
		}

		public State getTarget() {
			return target;
		}

		public Executable getExecutable() {
			return executable;
		}

		public Trigger getTrigger() {
			return trigger;
		}

		public State getState() {
			return state;
		}
	}
}
