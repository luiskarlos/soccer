package com.lk.engine.common.tools;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * CsvLoad events to position game entities
 */
public class Csv {

  public static GwtEvent.Type<LoaderEventHandler> TYPE = new GwtEvent.Type<>();
  private final EventBus eventBus;

  Csv(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void loadCsvStatus(String row) {
    final Stream<String> data = Arrays.stream(row.split(","));
    final EntityInfo ballInfo = new EntityInfo(data.findFirst().get(), read(data), read(data), Optional.empty());
    fire(ballInfo);
    for(;;) {
      final Optional<EntityInfo> player = readPlayerInfo(data);
      if (player.isPresent()) {
        fire(player.get());
      } else {
        return;
      }
    }
  }

  private Optional<EntityInfo> readPlayerInfo(Stream<String> data) {
    Optional<String> name = data.findFirst();
    if (name.isPresent())
      return Optional.of(new EntityInfo(name.get(), read(data), read(data), readStr(data)));
    else
      return Optional.empty();
  }

  private UVector2D read(Stream<String> data) {
    return new Vector2D(readDouble(data), readDouble(data));
  }

  private double readDouble(Stream<String> data) {
    return Double.parseDouble(data.findFirst().get());
  }

  private Optional<String> readStr(Stream<String> data) {
    return data.findFirst();
  }

  private void fire(EntityInfo entityInfo) {
    eventBus.fireEvent(new EntityInfoEvent(this, entityInfo));
  }
}

class EntityInfo {
  final String name;
  final UVector2D position;
  final UVector2D heading;
  final Optional<String> state;

  public EntityInfo(String name, UVector2D position, UVector2D heading, Optional<String> state) {
    this.name = name;
    this.position = position;
    this.heading = heading;
    this.state = state;
  }
}

class EntityInfoEvent extends GwtEvent<LoaderEventHandler> {

  private final EntityInfo entityInfo;

  public EntityInfoEvent(Csv loader, EntityInfo entityInfo) {
    this.entityInfo = entityInfo;
    setSource(loader);
  }

  public EntityInfo getEntityInfo() {
    return entityInfo;
  }

  @Override
  public Type<LoaderEventHandler> getAssociatedType() {
    return Csv.TYPE;
  }

  @Override
  protected void dispatch(LoaderEventHandler handler) {
    handler.onLoad(this);
  }
}


interface LoaderEventHandler extends EventHandler {

  void onLoad(EntityInfoEvent event);

}

