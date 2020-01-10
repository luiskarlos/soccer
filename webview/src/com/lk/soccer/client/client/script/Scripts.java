package com.lk.soccer.client.client.script;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface Scripts extends ClientBundle {
	public static final Scripts INSTANCE = GWT.create(Scripts.class);

	@Source("com/lk/engine/soccer/script/spawn-players.script")
	TextResource spawnPlayers();

	@Source("square.script")
	TextResource squarePattern();

}
