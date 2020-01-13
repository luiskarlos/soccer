package com.lk.engine.common.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;

public class Define extends Instruction {
	private final Block block;

	public Define(Block block) {
		super("");
		this.block = block;
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		enviroment.registerBlock(block.getElement(enviroment), block);
		return Active.No;
	}

	@Override
	public String toString() {
		return "Define " + super.toString() +" "+ block.toString();
	}
}
