package com.lk.engine.common.fsm;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class StateMachineEvents {

  public static class StateChanged extends GwtEvent<StateChanged.Handler> {
    public static GwtEvent.Type<StateChanged.Handler> TYPE = new GwtEvent.Type<>();

    private final StateMachine stateMachine;

    public StateChanged(StateMachine stateMachine) {
      this.stateMachine = stateMachine;
      setSource(stateMachine);
    }

    public StateMachine getStateMachine() {
      return stateMachine;
    }

    @Override
    public Type<Handler> getAssociatedType() {
      return TYPE;
    }

    @Override
    protected void dispatch(Handler handler) {
      handler.on(this);
    }

    public interface Handler extends EventHandler {
      void on(StateChanged event);
    }
  }


}
