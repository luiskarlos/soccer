package com.lk.engine.common.script;

import com.lk.engine.common.script.instructions.Block;

public interface ScriptParserListener {
	void onBlock(final String name, final Block block);
}
