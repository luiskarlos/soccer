package com.lk.engine.common.debug;

/**
 * this little class just acts as a sink for any input. Used in place of the
 * Console class when the console is not required
 */
public class ConsoleSink implements OldConsole {
	private static final ConsoleSink instance = new ConsoleSink();

	public ConsoleSink() {
	}

	public static ConsoleSink instance() {
		return instance;
	}

	@Override
	public ConsoleSink print(final Object T) {
		return this;
	}

	@Override
	public OldConsole print(final Object... T) {
		return this;
	}

	@Override
	public boolean isShowInfoEnabled() {
		return false;
	}

	@Override
	public boolean isPlayerStateInfoOn() {
		return false;
	}

	@Override
	public boolean isDebugTeamStatesOn() {
		return false;
	}

	@Override
	public boolean isGoalyStateInfoOn() {
		return false;
	}

	@Override
	public void debugOn() {
	}

	@Override
	public void debugOff() {
	}
}