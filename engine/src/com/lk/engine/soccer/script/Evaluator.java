package com.lk.engine.soccer.script;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.script.instructions.Block;
import com.lk.engine.soccer.script.instructions.None;

public class Evaluator implements Updatable {
	private final Enviroment enviroment;

	private final List<Executable> executables = new ArrayList<Executable>();
	private final List<Executable> newExecutables = new ArrayList<Executable>();
	private final List<Evaluator> additionalEvaluators = new ArrayList<Evaluator>();
	private Executable onExit = None.NONE;
	private boolean exiting = false;

	public Evaluator(final Enviroment enviroment) {
		this(null, enviroment);
	}

	public Evaluator(final Evaluator parent, final Enviroment enviroment) {
		this.enviroment = enviroment;
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
	public Active update() {
		execute();
		executeAdditionalEvaluators();
		return isActive();
	}

	private void execute() {
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
	}

	private void executeAdditionalEvaluators() {
		for (int i = additionalEvaluators.size() - 1; i >= 0; i--) {
			if (additionalEvaluators.get(i).update() == Active.No)
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
		final Evaluator newEvaluator = new Evaluator(this, new EnviromentStack(enviroment));
		newEvaluator.eval(block);
		additionalEvaluators.add(newEvaluator);
	}

}
