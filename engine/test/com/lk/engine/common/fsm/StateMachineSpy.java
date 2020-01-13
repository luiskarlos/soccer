package com.lk.engine.common.fsm;

import com.google.gwt.event.shared.EventBus;
import com.lk.engine.common.injector.Provider;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class StateMachineSpy extends StateMachine {

	public StateMachineSpy(final StateMachineOwner owner, final Provider<Evaluator> evaluator, final EventBus eventBus) {
	  super(owner, evaluator, eventBus);
  }

	@Override
  public void changeTo(String newState) {
	  super.changeTo(newState);
  }

	@Override
  public void changeTo(String newState, Executable onExit, Object data) {
	  super.changeTo(newState, onExit, data);
  }
}
