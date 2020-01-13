package com.lk.engine.common.fsm;

public class Idle implements State {
	private static final Idle idle = new Idle();

	private Idle() {
	}

	public static Idle instance() {
		return idle;
	}

	@Override
	public void enter(final StateMachine stateMachine) {
	}

	@Override
	public void execute(final StateMachine stateMachine, final Object data) {
	}

	@Override
	public void exit(final StateMachine stateMachine) {
	}
}
