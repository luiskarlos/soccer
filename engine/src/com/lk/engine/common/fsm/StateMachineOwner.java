package com.lk.engine.common.fsm;

public interface StateMachineOwner {
	
	String getName();
	
	void setStateMachine(final StateMachine stateMachine);

	StateMachine getFSM();
}
