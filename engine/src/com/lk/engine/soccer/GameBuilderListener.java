package com.lk.engine.soccer;

import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.referee.Referee;
import com.lk.engine.soccer.elements.team.Team;

public interface GameBuilderListener {
	void onCreated(Team soccerTeam);

	void onCreated(Goalkeeper goalkeeper);

	void onCreated(FieldPlayer fieldPlayer);

	void onCreated(Ball ball);

	void onCreated(Referee soccerPitch);

	void onCreated(PlayRegions playRegions);

}
