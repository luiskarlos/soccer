package com.lk.engine.common.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lk.engine.common.console.Console;
import com.lk.engine.common.console.ParamHandler;
import com.lk.engine.common.console.Parameters;
import com.lk.engine.common.debug.Debug;
import com.lk.engine.common.debug.Debuggable;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.injector.EntityBuilder;
import com.lk.engine.common.injector.Provider;
import com.lk.engine.common.script.instructions.Block;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.Players;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.team.Team;

public class Environment implements Players, Debuggable {
	protected final EntityBuilder entityBuilder;

	protected final Parameters parameters;
	private final Map<String, Block> blocks = new HashMap<String, Block>();

	private final Map<String, Player<?>> players = new HashMap<String, Player<?>>();
	private final Map<String, Team> teams = new HashMap<String, Team>();
	private final Map<String, State> states = new HashMap<String, State>();
	private Console console;

	private final Telegraph telegraph;
	protected final Provider<List<StateMachine>> fsms;

	public Environment(final EntityBuilder entityBuilder, final Parameters parameters,
                     final Console console, final Telegraph telegraph, final Provider<List<StateMachine>> fsms) {
		this.entityBuilder = entityBuilder;
		this.parameters = parameters;
		this.console = console;
		this.telegraph = telegraph;
		this.fsms = fsms;
	}

	@Override
  public void debug(Debug debug) {
		for (Entry<String, Team> set : teams.entrySet()) {
	    debug.put(set.getKey(), set.getValue());
    }
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
	
	public void registerStates(final Map<String, State> states) {
		this.states.putAll(states);
	}
	
	public State getState(String name) {
		final State state = states.get(name);
		if (state == null) {
			throw new RuntimeException("State not found: " + name);
		}
		return state;
	}
	
	public Map<String, State> getStates() {
	  return states;
  }

	public ParamHandler getVariable(final String name) {
		return parameters.getParamHandler(name);
	}
	
	/*
	 * Return the list of available variables. 
	 */
	public List<String> listVariables() {
		return parameters.keys();
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
		final List<Player<?>> players = new ArrayList<Player<?>>(20);
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

	public List<StateMachine> getFSMS() {
	  return fsms.get();
  }

	
}
