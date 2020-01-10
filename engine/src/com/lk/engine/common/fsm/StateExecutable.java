package com.lk.engine.common.fsm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.injector.Provider;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.Executable;

public class StateExecutable implements State {
	private List<State> beforeExecute = Collections.emptyList();
	private List<State> afterExecute = Collections.emptyList();
	private List<Executable> onEnterExec = Collections.emptyList();
	private List<Executable> onExitExec = Collections.emptyList();
  private Provider<Evaluator> evaluator;

	private final State state;
	
	public StateExecutable(State state, Provider<Evaluator> eval) {
		this.state = state;
		this.evaluator = eval;
  }
	
	@Override
  public String getName() {	  
	  return state.getName();
  }
	
	private <T> List<T> checkNewList(List<T> list) {
		if (list.equals(Collections.emptyList()))
			return new ArrayList<T>();
		return list;
	}
	
	public void onEnterExec(Executable executable) {
		onEnterExec = checkNewList(onEnterExec);
		onEnterExec.add(executable);
	}
	
	public void onExitExec(Executable executable) {
		onExitExec = checkNewList(onEnterExec);
		onExitExec.add(executable);
	}
	
	public void beforeExecute(List<State> e) {
	  beforeExecute = checkNewList(beforeExecute);
		beforeExecute.addAll(e);
	}
	
	public void afterExecute(List<State> e) {
		afterExecute = checkNewList(afterExecute);
		afterExecute.addAll(e);
	}

	@Override
  public void debug(Debug debug) {
		state.debug(debug);
	}

	@Override
  public Check check(StateMachine stateMachine) {
		return state.check(stateMachine);
  }

	@Override
  public void enter(StateMachine stateMachine) {
		state.enter(stateMachine);
		exec(onEnterExec);
  }
 /**
  * TODO: this is loosing one turn, because it is changing the state and not executing? 
  */
	@Override
  public State.Status execute(StateMachine stateMachine, Object data) {
		if (check(stateMachine, beforeExecute) == Check.NO) {
			State.Status status = state.execute(stateMachine, data);
			if (status == State.Status.INTERRUPTIBLE) {
				check(stateMachine, afterExecute);
			}
			return status;
		}
		return State.Status.INTERRUPTIBLE;
  }

	@Override
  public void exit(StateMachine stateMachine) {
		state.exit(stateMachine);
		exec(onExitExec);
  }
	
	private Check check(StateMachine stateMachine, List<State> executables) {
		if (executables.size() > 0) {			
			for (State s : executables) {
				if (s.check(stateMachine) == Check.APPLY) {
					stateMachine.changeTo(s);
					return Check.APPLY;
				}
			}
		}
		return Check.NO;
	}
	
	private Check exec(List<Executable> executable) {
		for (Executable e : executable) {
			evaluator.get().eval(e);
		}
		return Check.NO;
	}
}
