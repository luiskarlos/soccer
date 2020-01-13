package com.lk.engine.soccer.script;

import com.lk.engine.soccer.script.instructions.Block;

public interface ScriptParserListener {
	void onBlock(final String name, final Block block);
}
