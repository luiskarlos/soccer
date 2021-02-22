package com.lk.engine.common.script.instructions;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;
/**
 * Block of instructions
 */
public class Block extends Instruction implements Executable {
	private final List<Executable> instructions = new ArrayList<Executable>();

	public Block(final String name) {
		super(name);
	}

	public void addInstruction(final Executable stateInstruction) {
		this.instructions.add(stateInstruction);
	}

	public List<Executable> getInstructions() {
		return instructions;
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		for (final Executable e : instructions) {
			evaluator.eval(e);
		}
		return Active.No;
	}

	@Override
	public String toString() {
		return super.toString() + " {...} " + instructions.size();
	}

}
