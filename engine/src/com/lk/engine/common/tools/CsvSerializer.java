package com.lk.engine.common.tools;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.lk.engine.common.console.Console;
import com.lk.engine.common.core.MovingEntity;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.fsm.StateMachineEvents;
import com.lk.engine.common.misc.NumUtils;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CsvSerializer {
  private static Logger logger = Logger.getLogger(CsvSerializer.class.getName());

  public CsvSerializer(Team teamA, Team teamB, Ball ball, EventBus eventBus) {
    eventBus.addHandler(StateMachineEvents.StateChanged.TYPE, event -> {
      String row = new Serializer()
          .serialize(ball)
          .serialize(teamA.members())
          .serialize(teamB.members())
          .toCsv();

      logger.info(row);
    });

    eventBus.addHandler(CsvLoadEvent.TYPE, event ->
      new Loader(new Row(event.getData().split(",")))
        .restore(ball)
        .restore(teamA.members())
        .restore(teamB.members())
    );
  }

  //region Events

  public static class CsvLoadEvent extends GwtEvent<CsvLoadEvent.Handler> {
    private static GwtEvent.Type<CsvLoadEvent.Handler> TYPE = new GwtEvent.Type<>();

    private final String row;

    public CsvLoadEvent(String row) {
      this.row = row;
    }

    public String getData() {
      return row;
    }

    @Override
    public Type<CsvLoadEvent.Handler> getAssociatedType() {
      return TYPE;
    }

    @Override
    protected void dispatch(CsvLoadEvent.Handler handler) {
      handler.on(this);
    }

    public interface Handler extends EventHandler {
      void on(CsvLoadEvent event);
    }
  }

  //endregion

  private static class Serializer {

    private final List<String> data = new ArrayList<>(100);

    public Serializer serialize(List<Player<?>> players) {
      players.stream().forEach(this::serialize);
      return this;
    }

    public Serializer serialize(Ball ball) {
      serialize("ball", ball);
      return this;
    }

    public Serializer serialize(Player<?> player) {
      serialize(player.getName(), player);
      serialize(player.getFSM());
      return this;
    }

    private Serializer serialize(StateMachine fsm) {
      data.add(fsm.currentState().getName());
      return this;
    }

    private Serializer serialize(String name, MovingEntity<?> movingEntity) {
      data.add(name);
      serialize(movingEntity.pos());
      serialize(movingEntity.heading());
      return this;
    }

    private Serializer serialize(UVector2D vec) {
      data.add(NumUtils.toString(vec.x(), 3));
      data.add(NumUtils.toString(vec.y(), 3));
      return this;
    }

    public String toCsv() {
      return data.stream()
          .collect(Collectors.joining(","));
    }
  }

  private static class Row {

    private final String[] data;
    private int index;

    public Row(String[] data) {
      this.data = data;
    }

    public boolean setIndex(String item) {
      for(index = 0; index < data.length; index++)
        if (data[index].equals(item)) {
          return true;
        }
      return false;
    }

    public String next() {
      return data[index++];
    }

    public Double nextDouble() {
      return Double.parseDouble(next());
    }

    public UVector2D nextVector2D() {
      return new Vector2D(nextDouble(), nextDouble());
    }
  }

  private static class Loader {

    private final Row data;

    public Loader(Row data) {
      this.data = data;
    }

    public Loader restore(Ball ball) {
      restore("ball", ball);
      return this;
    }

    public Loader restore(List<Player<?>> players) {
      players.stream().forEach(this::restore);
      return this;
    }

    public Loader restore(Player<?> player) {
      restore(player.getName(), player);
      restore(player.getFSM());
      return this;
    }

    private Loader restore(StateMachine fsm) {
      fsm.setCurrentState(data.next());
      return this;
    }

    private Loader restore(String name, MovingEntity<?> movingEntity) {
      data.setIndex(name);
      data.next();
      movingEntity.setPos(data.nextVector2D());
      movingEntity.setHeading(data.nextVector2D());
      return this;
    }
  }

}














