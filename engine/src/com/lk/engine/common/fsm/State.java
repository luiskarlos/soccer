/**
 * abstract base class to define an interface for a state
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.fsm;

public interface State {
	// this will execute when the state is entered
	void enter(final StateMachine stateMachine);

	// this is the state's NORMAL update function
	void execute(final StateMachine stateMachine, final Object data);

	// this will execute when the state is exited. (My word, isn't
	// life full of surprises... ;o))
	void exit(final StateMachine stateMachine);
}
