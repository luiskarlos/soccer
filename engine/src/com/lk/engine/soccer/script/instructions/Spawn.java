package com.lk.engine.soccer.script.instructions;

import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.elements.players.Player.PlayerRole;
import com.lk.engine.soccer.injector.EntityBuilder;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;

public class Spawn extends Instruction {
	private final String teamName;
	private final PlayerRole role;
	private final String playerName;
	private final double x;
	private final double y;

	public Spawn(final String element, final String team, final String role, final String playerName, final String x,
	    final String y) {
		super(element);
		this.teamName = team.toLowerCase();
		this.role = PlayerRole.valueOf(role.toUpperCase());
		this.playerName = playerName;
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Enviroment enviroment) {
		final EntityBuilder builder = enviroment.getEntityBuilder();
		if (role == PlayerRole.GOALKEEPER)
			builder.newGoalkeeper(enviroment, teamName, playerName, x, y);
		else
			builder.newFieldPlayer(enviroment, teamName, playerName, role, x, y);
		return Active.No;
	}

	@Override
	public String toString() {
		return "Spawn " + super.toString() + " " + teamName + " " + role + " " + playerName + " " + x + " " + y;
	}
}
