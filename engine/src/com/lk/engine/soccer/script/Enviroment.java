package com.lk.engine.soccer.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lk.engine.common.fsm.State;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.console.Console;
import com.lk.engine.soccer.console.ParamHandler;
import com.lk.engine.soccer.console.Parameters;
import com.lk.engine.soccer.elements.Players;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.team.Team;
import com.lk.engine.soccer.injector.EntityBuilder;
import com.lk.engine.soccer.script.instructions.Block;

public class Enviroment implements Players {
	protected final EntityBuilder entityBuilder;

	protected final Parameters parameters;
	private final Map<String, Block> blocks = new HashMap<String, Block>();

	protected final Map<String, Class<State>> states;
	private final Map<String, Player<?>> players = new HashMap<String, Player<?>>();
	private final Map<String, Team> teams = new HashMap<String, Team>();
	private Console console;

	private final Telegraph telegraph;

	public Enviroment(final EntityBuilder entityBuilder, final Parameters parameters,
	    final Map<String, Class<State>> states, final Console console, final Telegraph telegraph) {
		this.entityBuilder = entityBuilder;
		this.parameters = parameters;
		this.states = states;
		this.console = console;
		this.telegraph = telegraph;
	}

	public void setConsole(Console console) {
		this.console = console;
	}

	public Console getConsole() {
		return console;
	}

	public boolean existsVariable(final String name) {
		return parameters.getParamHandler(name) != null;
	}

	public ParamHandler setVariable(final String name, final ParamHandler newValue) {
		return parameters.setVariable(name, newValue);
	}

	public void registerPlayer(final String name, final Player<?> player) {
		players.put(name, player);
	}

	public void registerTeam(final Team team) {
		teams.put(team.name(), team);
	}

	public Class<State> getState(final String name) {
		final Class<State> clazz = states.get(name);
		if (clazz == null)
			throw new RuntimeException("State " + name + " not fond");

		return clazz;
	}

	public ParamHandler getVariable(final String name) {
		return parameters.getParamHandler(name);
	}

	@Override
	public Collection<Player<?>> getPlayers() {
		return this.players.values();
	}

	@Override
	public Player<?> getPlayer(final String name) {
		return this.players.get(name);
	}

	public List<Player<?>> getPlayers(final String name) {
		final List<Player<?>> players = new ArrayList<Player<?>>();
		if (name.endsWith("*")) {
			final String prefix = name.substring(0, name.length() - 1);
			for (final Entry<String, Player<?>> player : this.players.entrySet()) {
				if (player.getKey().startsWith(prefix)) {
					players.add(player.getValue());
				}
			}
		} else if (this.players.containsKey(name))
			players.add(this.players.get(name));

		return players;
	}

	public void registerBlock(final String name, final Block block) {
		blocks.put(name, block);
	}

	public Block getBlock(final String name) {
		return blocks.get(name);
	}

	public EntityBuilder getEntityBuilder() {
		return entityBuilder;
	}

	public Team getTeam(final String teamName) {
		return teams.get(teamName);
	}

	public Telegraph getMessageDispatcher() {
		return this.telegraph;
	}
}
