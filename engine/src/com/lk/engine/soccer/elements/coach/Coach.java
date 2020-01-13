package com.lk.engine.soccer.elements.coach;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.team.Team.TeamColor;

public class Coach implements StateMachineOwner {
	private final TeamColor team;

	private StateMachine stateMachine;
	private Vector2D passIndication = null;
	private final Telegraph telegraph;

	public Coach(final Telegraph telegraph, final TeamColor team) {
		this.team = team;
		this.telegraph = telegraph;
	}

	public Vector2D consumePassIndication() {
		final Vector2D indication = passIndication;
		passIndication = null;
		telegraph.post(new TelegramPackage(Message.COACH_PASS_FULFILLED));
		return indication;
	}

	public boolean hasPassIndication() {
		return passIndication != null;
	}

	public void setPassIndication(final Vector2D passIndication) {
		this.passIndication = passIndication;
	}

	@Override
	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public StateMachine getFSM() {
		return stateMachine;
	}

	public TeamColor getTeamColor() {
		return team;
	}

	@Override
  public String getName() {
	  return "Coach "+ team.toString();
  }
}
