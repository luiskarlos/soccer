/**
 * State machine class. Inherit from this class and create some 
 * states to give your agents FSM functionality
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.common.fsm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;
import com.lk.engine.common.injector.Provider;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;
import com.lk.engine.common.script.instructions.None;

public class StateMachine implements Debuggable {
	private final StateMachineOwner owner;
	private final Provider<Evaluator> evaluator;
	
	private State currentState = Idle.instance();
	private State previousState = Idle.instance();
	private State globalState = Idle.instance();

	private boolean changingState = false;
	private Executable onExit = None.NONE;
	private Map<String, Object> extraData = Collections.emptyMap();

	public StateMachine(final StateMachineOwner owner, Provider<Evaluator> evaluator) {
		this.owner = owner;
		this.evaluator = evaluator;
	}
	
	@Override
	public void debug(Debug debug) {
		debug.put("owner", owner.getName());
		debug.put("currentState", currentState);
		debug.put("previousState", previousState);
		debug.put("globalState", globalState);
		debug.put("type", "StateMachine");
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
		currentState.execute(this, extraData.get(currentState.getName()));
	}

	public void changeTo(final String newState, final Executable onExit, final Object data) {
		extraData.remove(newState);
		if (data != null) {
			if (extraData.equals(Collections.emptyMap()))
				extraData = new HashMap<String, Object>();
			extraData.put(newState, data);
		}

		changeTo(newState);
		
		this.onExit = onExit;
	}

	public void exit() {
		changeTo(Idle.instance());
	}
	
	public void changeTo(final String newState) {
		changeTo(evaluator.get().getEnviroment().getState(newState));
	}

	// change to a new state
	public void changeTo(final State pNewState) {
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
			evaluator.get().eval(onExit);
			onExit = None.NONE;
		}
	}

	// change state back to the previous state
	public void revertToPreviousState() {
		changeTo(previousState);
	}

	public boolean isInState(String name) {
		return currentState.getName() == name;
	}

	// returns true if the current state's type is equal to the type of the
	// class passed as a parameter.
	public boolean isInState(final State st) {
		return isInState(st.getName());
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
		return s[s.length - 1] + "." + state.getName();
	}

	@SuppressWarnings("unchecked")
	public <T> T getOwner() {
		return (T) owner;
	}
}