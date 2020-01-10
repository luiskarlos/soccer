package com.lk.engine.common.injector;

import com.lk.engine.common.console.params.FieldPlayerParams;
import com.lk.engine.common.console.params.GoalkeeperParams;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.script.Enviroment;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.players.Player.PlayerRole;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.fieldplayer.states.GlobalPlayerState;
import com.lk.engine.soccer.elements.players.fieldplayer.telegrams.FieldPlayerTelegramBuilder;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.players.goalkeeper.states.GlobalKeeperState;
import com.lk.engine.soccer.elements.players.goalkeeper.telegrams.GoalkeeperTelegramBuilder;
import com.lk.engine.soccer.elements.referee.Referee;
import com.lk.engine.soccer.elements.team.Team;

public class EntityBuilder {
	private final Injector injector;

	public EntityBuilder(final Injector injector) {
		this.injector = injector;
	}
	
	public void newPlayer(final Enviroment enviroment, final String team, final PlayerRole role, 
			                  final String playerName, final int kickoffRegion) {
		if (role == PlayerRole.GOALKEEPER)
			newGoalkeeper(enviroment, team, playerName, kickoffRegion);
		else
			newFieldPlayer(enviroment, team, playerName, role, kickoffRegion);
	}

	public FieldPlayer newFieldPlayer(final Enviroment enviroment, final String teamName, final String playerName,
	  final PlayerRole role, final int kickoffRegion) {
		final FieldPlayerParams params = enviroment.getVariable(playerName).container(FieldPlayerParams.class);
		final RandomGenerator random = injector.get(RandomGenerator.class);
		final Team team = injector.get(teamName, Team.class);
		final PlayRegions regions = injector.get(PlayRegions.class);
		final Ball ball = injector.get(Ball.class);
		final Telegraph telegraph = injector.get(Telegraph.class);

		final FieldPlayer fieldPlayer = new FieldPlayer(params, telegraph, team, role, random, enviroment, regions, ball);
		fieldPlayer.getParams().setKickoffRegion(kickoffRegion);
		fieldPlayer.gotoKickoff();

		injector.newStateMachine(fieldPlayer, GlobalPlayerState.NAME);
		injector.register(enviroment, fieldPlayer);

		new FieldPlayerTelegramBuilder(fieldPlayer).checkin(telegraph);

		enviroment.registerPlayer(playerName, fieldPlayer);

		telegraph.post(new TelegramPackage(Message.NEW_PLAYER, fieldPlayer));

		return fieldPlayer;
	}

	public Goalkeeper newGoalkeeper(final Enviroment enviroment, final String teamName, final String playerName,
	    final int kickoffRegion) {
		final GoalkeeperParams params = enviroment.getVariable(playerName).container(GoalkeeperParams.class);
		final Team team = injector.get(teamName, Team.class);
		final FieldPlayingArea playingArea = injector.get(FieldPlayingArea.class);
		final PlayRegions regions = injector.get(PlayRegions.class);
		final Ball ball = injector.get(Ball.class);
		final Referee referee = injector.get(Referee.class);
		final Telegraph telegraph = injector.get(Telegraph.class);

		final Goalkeeper goalkeeper = new Goalkeeper(params, telegraph, team, 0, enviroment, regions, playingArea, referee,
		    ball);
		goalkeeper.getParams().setKickoffRegion(kickoffRegion);
		goalkeeper.gotoKickoff();

		injector.newStateMachine(goalkeeper, GlobalKeeperState.NAME);
		injector.register(enviroment, goalkeeper);

		new GoalkeeperTelegramBuilder(goalkeeper).checkin(telegraph);

		enviroment.registerPlayer(playerName, goalkeeper);

		telegraph.post(new TelegramPackage(Message.NEW_PLAYER, goalkeeper));

		return goalkeeper;
	}
}
