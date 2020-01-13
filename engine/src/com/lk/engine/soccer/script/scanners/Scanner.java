package com.lk.engine.soccer.script.scanners;

import com.lk.engine.soccer.script.Executable;
import com.lk.engine.soccer.script.ScriptParser.Tokens;

public interface Scanner {
	boolean isNext(final Tokens tokens);

	Executable scan(final Tokens tokens);
}
