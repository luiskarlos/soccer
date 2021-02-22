package com.lk.engine.common.script.instructions;

import com.lk.engine.common.injector.EntityBuilder;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.soccer.elements.players.Player.PlayerRole;

public class Spawn extends Instruction {
	private final String teamName;
	private final PlayerRole role;
	private final String playerName;
	private final int kickoffRegion;

	public Spawn(final String element, final String team, final String role, final String playerName, final int kickoffRegion) {
		super(element);
		this.teamName = team.toLowerCase();
		this.role = PlayerRole.valueOf(role.toUpperCase());
		this.playerName = playerName;
		this.kickoffRegion = kickoffRegion;
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		final EntityBuilder builder = enviroment.getEntityBuilder();
	  builder.newPlayer(enviroment, teamName, role, playerName, kickoffRegion);
		return Active.No;
	}

	@Override
	public String toString() {
		return "Spawn " + super.toString() + " " + teamName + " " + role + " " + playerName + " " +kickoffRegion;
	}
}
