package com.lk.engine.common.script.scanners;

import com.lk.engine.common.script.Executable;
import com.lk.engine.common.script.ScriptParser.Tokens;
import com.lk.engine.common.script.instructions.Block;

public class BlockScanner implements Scanner {
	@Override
	public boolean isNext(final Tokens tokens) {
		return tokens.isBlockNext();
	}

	@Override
	public Executable scan(final Tokens tokens) {
		final Block block = tokens.nextBlock();
		for (; !tokens.isCloseBlockNext();) {
			block.addInstruction(tokens.nextInstruction());
			if (!tokens.hasNext())
				throw new RuntimeException("Missing { for " + block.toString() + " or an internal block");
		}
		tokens.nextCloseBlock();
		return block;
	}

}
