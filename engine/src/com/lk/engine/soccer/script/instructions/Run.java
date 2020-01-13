package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class Run extends Instruction {
	private final Block block;

	public Run(String name, Block block) {
		super(name);
		this.block = block;
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		evaluator.newEvaluator(block);
		return Active.No;
	}

	@Override
	public String toString() {
		return "Run "+super.toString()+" {...} " + block.getInstructions().size();
	}
}
