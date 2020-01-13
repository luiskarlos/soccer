package com.lk.soccer.client.common.gui;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.elements.players.Player;

public interface CoachRender {
	
  public void setPassMark(final Vector2D passMark);
  
  public void setPassFrom(final Player<?> player);
}
