/**
 * State machine class. Inherit from this class and create some 
 * states to give your agents FSM functionality
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.fsm;

import java.util.HashMap;
import java.util.Map;

import com.lk.engine.soccer.injector.States;
import com.lk.engine.soccer.script.Evaluator;
import com.lk.engine.soccer.script.Executable;
import com.lk.engine.soccer.script.instructions.None;

public class StateMachine {
	private final StateMachineOwner owner;
	private final States states;

	private State currentState = Idle.instance();
	private State previousState = Idle.instance();
	private State globalState = Idle.instance();

	private boolean changingState = false;
	private Evaluator evaluator;
	private Executable onExit = None.NONE;
	private final Map<Class<? extends State>, Object> extraData = new HashMap<Class<? extends State>, Object>();

	public StateMachine(final StateMachineOwner owner, final States states) {
		this.owner = owner;
		this.states = states;
	}

	public void setCurrentState(final State s) {
		currentState = s;
	}

	public void setGlobalState(final State s) {
		globalState = s;
	}

	public void setPreviousState(final State s) {
		previousState = s;
	}

	public void update() {
		globalState.execute(this, null);
		currentState.execute(this, extraData.get(currentState.getClass()));
	}

	public void changeTo(final Class<? extends State> newState) {
		extraData.remove(newState);
		changeTo(states.get(newState));
	}

	public void changeTo(final Class<? extends State> newState, final Evaluator evaluator, final Executable onExit,
	    final Object data) {
		extraData.remove(newState);
		if (data != null)
			extraData.put(newState, data);

		changeTo(states.get(newState));
		this.onExit = onExit;
		this.evaluator = evaluator;
	}

	public void exit() {
		changeTo(Idle.instance());
	}

	// change to a new state
	private void changeTo(final State pNewState) {
		if (changingState) {
			throw new RuntimeException("changeTo: Reentrant call from exit method is not allowed!");
		}

		previousState = currentState;

		System.out.println("sfm: " + owner.getName() + " " + getStateName(currentState) + " => " + getStateName(pNewState));
		
		changingState = true;
		currentState.exit(this);
		changingState = false;

		currentState = pNewState;
		currentState.enter(this);
		extraData.remove(previousState);

		if (onExit != None.NONE) {
			evaluator.eval(onExit);
			onExit = None.NONE;
		}
	}

	// change state back to the previous state
	public void revertToPreviousState() {
		changeTo(previousState);
	}

	public boolean isInState(final Class<? extends State> clazz) {
		return currentState.getClass() == clazz;
	}

	// returns true if the current state's type is equal to the type of the
	// class passed as a parameter.
	public boolean isInState(final State st) {
		return currentState.getClass() == st.getClass();
	}

	public State currentState() {
		return currentState;
	}

	public State globalState() {
		return globalState;
	}

	public State previousState() {
		return previousState;
	}

	public String getCurrentStateName() {
		return getStateName(currentState);
	}
	
	// only ever used during debugging to grab the name of the current state
	private String getStateName(State state) {
		final String[] s = state.getClass().getName().split("\\.");
		return s[s.length - 1];
	}

	@SuppressWarnings("unchecked")
	public <T> T getOwner() {
		return (T) owner;
	}
}