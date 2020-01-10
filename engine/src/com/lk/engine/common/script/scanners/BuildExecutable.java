package com.lk.engine.common.script.scanners;

import com.lk.engine.common.script.Executable;
import com.lk.engine.common.script.ScriptParser.Tokens;

public interface BuildExecutable {
	Executable newExec(final Tokens tokens);
}
