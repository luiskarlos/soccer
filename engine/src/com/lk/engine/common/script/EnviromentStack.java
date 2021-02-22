package com.lk.engine.common.script;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.console.ParamHandler;
import com.lk.engine.common.console.Parameters;
import com.lk.engine.common.script.instructions.Block;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.team.Team;

public class EnviromentStack extends Environment {
	private final Environment main;

	public EnviromentStack(final Environment enviroment) {
		super(enviroment.entityBuilder, new Parameters(new ArrayList<ParamHandler>()), enviroment
		    .getConsole(), enviroment.getMessageDispatcher(), enviroment.fsms);
		this.main = enviroment;
	}

	@Override
	public boolean existsVariable(final String name) {
		return super.existsVariable(name) || main.existsVariable(name);
	}

	@Override
	public ParamHandler getVariable(final String name) {
		final ParamHandler handler = super.getVariable(name);
		if (handler == null)
			return main.getVariable(name);

		return handler;
	}

	@Override
	public List<Player<?>> getPlayers(final String name) {
		List<Player<?>> players = super.getPlayers(name);
		if (players.isEmpty())
			players = main.getPlayers(name);

		return players;
	}

	@Override
	public Block getBlock(final String name) {
		final Block block = super.getBlock(name);
		if (block == null)
			return main.getBlock(name);

		return block;
	}

	@Override
	public Team getTeam(final String teamName) {
		final Team team = super.getTeam(teamName);
		if (team == null)
			return main.getTeam(teamName);

		return team;
	}
}
