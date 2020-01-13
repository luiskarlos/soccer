package com.lk.engine.soccer.script.scanners;

import com.lk.engine.soccer.script.Executable;
import com.lk.engine.soccer.script.ScriptParser.Tokens;

public class ExecutableScanner implements Scanner {
	private final String id;
	private final BuildExecutable executable;

	public ExecutableScanner(final String id, final BuildExecutable executable) {
		this.id = id;
		this.executable = executable;
	}

	@Override
	public boolean isNext(final Tokens tokens) {
		return tokens.isNext(id);
	}

	@Override
	public Executable scan(final Tokens tokens) {
		tokens.nextString();
		return executable.newExec(tokens);
	}
}
