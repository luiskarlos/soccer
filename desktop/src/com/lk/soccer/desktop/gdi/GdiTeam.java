package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.soccer.elements.team.Team;
import com.lk.engine.soccer.elements.team.Team.TeamColor;

public class GdiTeam implements Renderable {
	private final Team soccerTeam;
	private final Cgdi gdi;

	public GdiTeam(Cgdi cgdi, Team soccerTeam) {
		this.gdi = cgdi;
		this.soccerTeam = soccerTeam;
	}

	@Override
	public boolean render() {
		// show the controlling team and player at the top of the display
		if (soccerTeam.getParams().isShowControllingTeam()) {
			gdi.textColor(Cgdi.white);
			if (soccerTeam.inControl()) {
				gdi.textAtPos(20, 3, soccerTeam.color().name() + " in Control");
				gdi.textAtPos(soccerTeam.getPlayingArea().getX() - 150, 3, "Controlling Player: "
				    + soccerTeam.controllingPlayer().Id());
			}
		}

		// render the sweet spots
		if (soccerTeam.getParams().isShowSupportSpots() && soccerTeam.inControl()) {
			new GdiSupportSpotCalculator(gdi, soccerTeam.supportSpotCalc()).render();
		}

		if (soccerTeam.getParams().isShowTeamState()) {
			if (soccerTeam.color() == TeamColor.RED) {
				gdi.textColor(Cgdi.red);
				gdi.textAtPos(160, 20, soccerTeam.getFSM().getCurrentStateName());
			} else {
				gdi.textAtPos(160, soccerTeam.getPlayingArea().getY() - 40, soccerTeam.getFSM().getCurrentStateName());
			}
		}

		if (soccerTeam.getParams().isShowSupportingPlayersTarget()) {
			if (soccerTeam.supportingPlayer() != null) {
				gdi.blueBrush();
				gdi.redPen();
				gdi.circle(soccerTeam.supportingPlayer().steering().target(), 4);
			}
		}

		return true;
	}
}
