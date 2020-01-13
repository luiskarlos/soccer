package com.lk.engine.common.console.params;

public class TeamParams {
	private int supportSpotsX = 13;
	private int supportSpotsY = 6;
	private int numAttemptsToFindValidStrike = 5; // TODO: pasar este valor a por
																								// jugador
	private int supportSpotUpdateFreq = 1;
	private double spotPassSafeScore = 2.0;
	private double spotCanScoreFromPositionScore = 1.0;
	private double spotDistFromControllingPlayerScore = 2.0;

	private boolean showControllingTeam = true;
	private boolean showSupportSpots = true;

	private boolean showSupportingPlayersTarget = true;
	private boolean showTeamState = true;

	public int getSupportSpotsX() {
		return supportSpotsX;
	}

	public void setSupportSpotsX(int supportSpotsX) {
		this.supportSpotsX = supportSpotsX;
	}

	public int getSupportSpotsY() {
		return supportSpotsY;
	}

	public void setSupportSpotsY(int supportSpotsY) {
		this.supportSpotsY = supportSpotsY;
	}

	public int getAttemptsToFindValidStrike() {
		return numAttemptsToFindValidStrike;
	}

	public void setAttemptsToFindValidStrike(int numAttemptsToFindValidStrike) {
		this.numAttemptsToFindValidStrike = numAttemptsToFindValidStrike;
	}

	public int getSupportSpotUpdateFreq() {
		return supportSpotUpdateFreq;
	}

	public void setSupportSpotUpdateFreq(int supportSpotUpdateFreq) {
		this.supportSpotUpdateFreq = supportSpotUpdateFreq;
	}

	public double getSpotPassSafeScore() {
		return spotPassSafeScore;
	}

	public void setSpotPassSafeScore(double spotPassSafeScore) {
		this.spotPassSafeScore = spotPassSafeScore;
	}

	public double getSpotCanScoreFromPositionScore() {
		return spotCanScoreFromPositionScore;
	}

	public void setSpotCanScoreFromPositionScore(double spotCanScoreFromPositionScore) {
		this.spotCanScoreFromPositionScore = spotCanScoreFromPositionScore;
	}

	public double getSpotDistFromControllingPlayerScore() {
		return spotDistFromControllingPlayerScore;
	}

	public void setSpotDistFromControllingPlayerScore(double spotDistFromControllingPlayerScore) {
		this.spotDistFromControllingPlayerScore = spotDistFromControllingPlayerScore;
	}

	public boolean isShowControllingTeam() {
		return showControllingTeam;
	}

	public void setShowControllingTeam(boolean showControllingTeam) {
		this.showControllingTeam = showControllingTeam;
	}

	public boolean isShowSupportSpots() {
		return showSupportSpots;
	}

	public void setShowSupportSpots(boolean showSupportSpots) {
		this.showSupportSpots = showSupportSpots;
	}

	public boolean isShowSupportingPlayersTarget() {
		return showSupportingPlayersTarget;
	}

	public void setShowSupportingPlayersTarget(boolean showSupportingPlayersTarget) {
		this.showSupportingPlayersTarget = showSupportingPlayersTarget;
	}

	public boolean isShowTeamState() {
		return showTeamState;
	}

	public void setShowTeamState(boolean showTeamState) {
		this.showTeamState = showTeamState;
	}

}
