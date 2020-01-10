package com.lk.engine.common.fsm;

import com.lk.engine.common.debug.Debug;

public class StateAdapter implements State {
	
	private String name;

	protected StateAdapter(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
	  return name;
	}
	
	public Check check(final StateMachine stateMachine) {
		return Check.NO;
	}
	
	@Override
	public void enter(StateMachine stateMachine) {
	}

	@Override
	public State.Status execute(StateMachine stateMachine, Object data) {
		return State.Status.INTERRUPTIBLE;
	}

	@Override
	public void exit(StateMachine stateMachine) {
	}

	@Override
  public void debug(Debug debug) {
	  debug.put("name", name);
	  debug.put("type", "State");
  }
}
