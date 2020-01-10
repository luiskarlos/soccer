/**
 * abstract base class to define an interface for a state
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.fsm;

import com.lk.engine.common.debug.Debuggable;

public interface State extends Debuggable {
	
	enum Check {
		APPLY, NO
	}
	
	enum Status {
		INTERRUPTIBLE,
		NO_INTERRUPTIBLE
	}
	
	String getName();
	
  /**
   * Check is intended to check if this state must be
   * active
   * @param stateMachine
   */
	Check check(StateMachine stateMachine);
	
	// this will execute when the state is entered
	void enter(final StateMachine stateMachine);

	// this is the state's NORMAL update function
	Status execute(final StateMachine stateMachine, final Object data);

	// this will execute when the state is exited. (My word, isn't
	// life full of surprises... ;o))
	void exit(final StateMachine stateMachine);
}
