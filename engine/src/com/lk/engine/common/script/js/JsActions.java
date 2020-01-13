package com.lk.engine.common.script.js;

import com.google.gwt.event.shared.EventBus;
import com.lk.engine.common.core.BaseGameEntity;
import com.lk.engine.common.core.EntityManager;
import com.lk.engine.common.core.MovingEntity;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateExecutable;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.injector.EntityBuilder;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.tools.CsvSerializer;
import com.lk.engine.soccer.elements.players.Player;
import jsinterop.annotations.JsType;

import java.util.Arrays;
import java.util.stream.Collectors;

@JsType
public class JsActions {
  private final EventBus eventBus;
  private final EntityManager entityManager;
  private final Environment environment;

  private final CSV csv = new CSV();

  public JsActions(EventBus eventBus, EntityManager entityManager, Environment environment) {
    this.eventBus = eventBus;
    this.entityManager = entityManager;
    this.environment = environment;
  }

  public CSV csv() {
    return csv;
  }

  public void load(String data) {
    final CsvSerializer.CsvLoadEvent event = new CsvSerializer.CsvLoadEvent(data);
    eventBus.fireEvent(event);
  }

  public void spawn(final String team, final String roleName, final String playerName, final int kickoffRegion) {
    final EntityBuilder builder = environment.getEntityBuilder();
    final Player.PlayerRole role = Player.PlayerRole.valueOf(roleName.toUpperCase());

    builder.newPlayer(environment, team, role, playerName, kickoffRegion);
  }

  public void before(String stateName, String ... to) {
    final State state = environment.getState(stateName);
    ((StateExecutable)state).beforeExecute(Arrays
        .stream(to)
        .map(environment::getState)
        .collect(Collectors.toList()));
  }

  public void after(String stateName, String ... to) {
    final State state = environment.getState(stateName);
    ((StateExecutable)state).afterExecute(Arrays
        .stream(to)
        .map(environment::getState)
        .collect(Collectors.toList()));
  }

  public void change(String entityName, String stateName) {
    final State state = environment.getState(stateName);
    final StateMachineOwner owner = (StateMachineOwner) entity(entityName);
    owner.getFSM().changeTo(state);
  }

  public BaseGameEntity entity(String name) {
    return entityManager.getEntityByName(name);
  }

  @JsType
  public class CSV {
    public void load(String data) {
      final CsvSerializer.CsvLoadEvent event = new CsvSerializer.CsvLoadEvent(data);
      eventBus.fireEvent(event);
    }
  }
}
