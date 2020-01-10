package com.lk.engine.common.script.scanners;

import com.lk.engine.common.script.Executable;
import com.lk.engine.common.script.ScriptParser.Tokens;

public interface Scanner {
	boolean isNext(final Tokens tokens);

	Executable scan(final Tokens tokens);
}
