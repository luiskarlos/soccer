package com.lk.engine.soccer.script.scanners;

import com.lk.engine.soccer.script.Executable;
import com.lk.engine.soccer.script.ScriptParser.Tokens;

public interface BuildExecutable {
	Executable newExec(final Tokens tokens);
}
