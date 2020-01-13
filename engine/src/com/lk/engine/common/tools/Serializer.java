package com.lk.engine.common.tools;

import com.lk.engine.soccer.elements.players.Player;

public interface Serializer {

  void restore(Player<?> player);

}
