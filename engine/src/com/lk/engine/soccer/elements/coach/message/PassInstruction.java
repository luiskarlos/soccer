package com.lk.engine.soccer.elements.coach.message;

import com.lk.engine.common.d2.Vector2D;

public class PassInstruction {
	private final String teamName;
	private final Vector2D pos;

	public PassInstruction(final String teamName, final Vector2D pos) {
		this.teamName = teamName;
		this.pos = pos;
	}

	public Vector2D getPos() {
		return pos;
	}

	public String getTeamName() {
		return teamName;
	}
}
