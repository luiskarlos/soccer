package com.lk.engine.common.script;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.instructions.Block;
import com.lk.engine.common.script.instructions.None;

public class Evaluator implements Updatable {
	private final Environment enviroment;

	private final List<Executable> executables = new ArrayList<Executable>();
	private final List<Executable> newExecutables = new ArrayList<Executable>();
	private final List<Evaluator> additionalEvaluators = new ArrayList<Evaluator>();
	private boolean exiting = false;

	private Executable onExit = None.NONE;

	private static Evaluator activeEvaluator = null;

	public Evaluator(final Environment enviroment) {
		this.enviroment = enviroment;
		if (activeEvaluator == null) {
			activeEvaluator = this;
		}
	}

	public Environment getEnvironment() {
	  return enviroment;
  }

	public void eval(final Executable e) {
		if (e != None.NONE)
			newExecutables.add(0, e);
	}

	public void exit(final Executable onExit) {
		executables.clear();
		newExecutables.clear();
		this.onExit = onExit;
		this.exiting = true;
	}

	@Override
	public Active update(long time, int delta) {
		execute();
		executeAdditionalEvaluators(time, delta);
		return isActive();
	}

	private void execute() {
		final Evaluator tmp = activeEvaluator;
		activeEvaluator = this;
		executables.addAll(0, newExecutables);
		newExecutables.clear();
		for (int i = executables.size() - 1; i >= 0; i--) {
			if (executables.isEmpty())
				break;

			final Executable executable = executables.get(i);
			if (executable.execute(this, enviroment) == Active.No) {
				if (executables.isEmpty())
					break;
				executables.remove(i);
			}
		}
		activeEvaluator = tmp												;
	}

	private void executeAdditionalEvaluators(long time, int delta) {
		for (int i = additionalEvaluators.size() - 1; i >= 0; i--) {
			if (additionalEvaluators.get(i).update(time, delta) == Active.No)
				additionalEvaluators.remove(i);
		}
	}

	private Active isActive() {
		if (exiting && onExit != None.NONE) {
			eval(onExit);
			onExit = None.NONE;
		} else if (exiting && executables.isEmpty() && newExecutables.isEmpty() && additionalEvaluators.isEmpty())
			return Active.No;

		return Active.Yes;
	}

	public void newEvaluator(final Block block) {
		final Evaluator newEvaluator = new Evaluator(new EnviromentStack(enviroment));
		newEvaluator.eval(block);
		additionalEvaluators.add(newEvaluator);
	}

	public static Evaluator getActiveEvaluator() {
	  return activeEvaluator;
  }
}
