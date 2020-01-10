package com.lk.engine.common.console.params;

public class PlayerParams extends MovingEntityParams {
	private String name = "player";

	private double maxSpeedWithBall = 1.2;
	private double maxSpeedWithoutBall = 1.6;

	private double kickingAccuracy = 0.99;
	private double comfortZone = 40.0;

	private double kickingDistance = 6.0;

	private double ballWithinReceivingRange = 10.0;

	private double kickFrequency = 8;

	private boolean nonPenetrationConstraint = false;
	private double separationCoefficient = 10;

	private double maxDribbleForce = 1.5;
	private double shortDribbleForce = 0.8;

	private double maxShootingForce = 4.0;
	private double maxPassingForce = 3.0;

	private double passThreatRadius = 70.0;

	private double ballPickupRange = 10.0;
	private double playerInTargetRange = 10.0;

	private double chanceAttemptsPotShot = 0.2;

	private double minPassDistance = 100.0;

	private double chanceOfUsingArriveTypeReceiveBehavior = 0.9;

	private double viewDistance = 30.0;

	private boolean viewStates = true;
	private boolean viewIDs = true;
	private boolean viewSupportSpots = true;
	private boolean viewRegions = false;
	private boolean showControllingTeam = true;
	private boolean viewTargets = false;
	private boolean highlightIfThreatened = false;

	private int attemptsToFindValidStrike = 5;
	private int attackRegion = 0;
	private int defenseRegion = 0;
	private int kickoffRegion = 0;

	public double getMaxSpeedWithBall() {
		return maxSpeedWithBall;
	}

	public void setMaxSpeedWithBall(final double maxSpeedWithBall) {
		this.maxSpeedWithBall = maxSpeedWithBall;
	}

	public double getMaxSpeedWithoutBall() {
		return maxSpeedWithoutBall;
	}

	public void setMaxSpeedWithoutBall(final double maxSpeedWithoutBall) {
		this.maxSpeedWithoutBall = maxSpeedWithoutBall;
	}

	public double getKickingAccuracy() {
		return kickingAccuracy;
	}

	public void setKickingAccuracy(final double kickingAccuracy) {
		this.kickingAccuracy = kickingAccuracy;
	}

	public double getComfortZone() {
		return comfortZone;
	}

	public void setComfortZone(final double comfortZone) {
		this.comfortZone = comfortZone;
	}

	public double getKickingDistance() {
		return kickingDistance;
	}

	public void setKickingDistance(final double kickingDistance) {
		this.kickingDistance = kickingDistance;
	}

	public double getKickingDistanceSq() {
		return kickingDistance * kickingDistance;
	}

	public double getBallWithinReceivingRange() {
		return ballWithinReceivingRange;
	}

	public void setBallWithinReceivingRange(final double ballWithinReceivingRange) {
		this.ballWithinReceivingRange = ballWithinReceivingRange;
	}

	public double getBallWithinReceivingRangeSq() {
		return ballWithinReceivingRange * ballWithinReceivingRange;
	}

	public double getKickFrequency() {
		return kickFrequency;
	}

	public void setKickFrequency(final double kickFrequency) {
		this.kickFrequency = kickFrequency;
	}

	public boolean isNonPenetrationConstraint() {
		return nonPenetrationConstraint;
	}

	public void setNonPenetrationConstraint(final boolean nonPenetrationConstraint) {
		this.nonPenetrationConstraint = nonPenetrationConstraint;
	}

	public double getMaxDribbleForce() {
		return maxDribbleForce;
	}

	public void setMaxDribbleForce(final double maxDribbleForce) {
		this.maxDribbleForce = maxDribbleForce;
	}

	public double getMaxShootingForce() {
		return maxShootingForce;
	}

	public void setMaxShootingForce(final double maxShootingForce) {
		this.maxShootingForce = maxShootingForce;
	}

	public double getMaxPassingForce() {
		return maxPassingForce;
	}

	public void setMaxPassingForce(final double maxPassingForce) {
		this.maxPassingForce = maxPassingForce;
	}

	public double getChanceAttemptsPotShot() {
		return chanceAttemptsPotShot;
	}

	public void setChanceAttemptsPotShot(final double chanceAttemptsPotShot) {
		this.chanceAttemptsPotShot = chanceAttemptsPotShot;
	}

	public double getMinPassDistance() {
		return minPassDistance;
	}

	public void setMinPassDistance(final double minPassDistance) {
		this.minPassDistance = minPassDistance;
	}

	public double getChanceOfUsingArriveTypeReceiveBehavior() {
		return chanceOfUsingArriveTypeReceiveBehavior;
	}

	public void setChanceOfUsingArriveTypeReceiveBehavior(final double chanceOfUsingArriveTypeReceiveBehavior) {
		this.chanceOfUsingArriveTypeReceiveBehavior = chanceOfUsingArriveTypeReceiveBehavior;
	}

	public double getSeparationCoefficient() {
		return separationCoefficient;
	}

	public void setSeparationCoefficient(final double separationCoefficient) {
		this.separationCoefficient = separationCoefficient;
	}

	public double getViewDistance() {
		return viewDistance;
	}

	public void setViewDistance(final double viewDistance) {
		this.viewDistance = viewDistance;
	}

	public boolean isViewStates() {
		return viewStates;
	}

	public void setViewStates(final boolean viewStates) {
		this.viewStates = viewStates;
	}

	public boolean isViewIDs() {
		return viewIDs;
	}

	public void setViewIDs(final boolean viewIDs) {
		this.viewIDs = viewIDs;
	}

	public boolean isViewSupportSpots() {
		return viewSupportSpots;
	}

	public void setViewSupportSpots(final boolean viewSupportSpots) {
		this.viewSupportSpots = viewSupportSpots;
	}

	public boolean isViewRegions() {
		return viewRegions;
	}

	public void setViewRegions(final boolean viewRegions) {
		this.viewRegions = viewRegions;
	}

	public boolean isShowControllingTeam() {
		return showControllingTeam;
	}

	public void setShowControllingTeam(final boolean showControllingTeam) {
		this.showControllingTeam = showControllingTeam;
	}

	public boolean isViewTargets() {
		return viewTargets;
	}

	public void setViewTargets(final boolean viewTargets) {
		this.viewTargets = viewTargets;
	}

	public boolean isHighlightIfThreatened() {
		return highlightIfThreatened;
	}

	public void setHighlightIfThreatened(final boolean highlightIfThreatened) {
		this.highlightIfThreatened = highlightIfThreatened;
	}

	public double getShortDribbleForce() {
		return shortDribbleForce;
	}

	public void setShortDribbleForce(final double shortDribbleForce) {
		this.shortDribbleForce = shortDribbleForce;
	}

	public double getPassThreatRadius() {
		return passThreatRadius;
	}

	public void setPassThreatRadius(final double passThreatRadius) {
		this.passThreatRadius = passThreatRadius;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public double getBallPickupRange() {
		return ballPickupRange;
	}

	public double getBallPickupRangeSq() {
		return ballPickupRange * ballPickupRange;
	}

	public void setBallPickupRange(double ballPickupRange) {
		this.ballPickupRange = ballPickupRange;
	}

	public double getPlayerInTargetRange() {
		return playerInTargetRange;
	}

	public double getPlayerInTargetRangeSq() {
		return playerInTargetRange * playerInTargetRange;
	}

	public void setPlayerInTargetRange(final double playerInTargetRange) {
		this.playerInTargetRange = playerInTargetRange;
	}

	public void setAttemptsToFindValidStrike(final int attemptsToFindValidStrike) {
		this.attemptsToFindValidStrike = attemptsToFindValidStrike;
	}

	public int getAttemptsToFindValidStrike() {
		return attemptsToFindValidStrike;
	}

	public int getAttackRegion() {
	  return attackRegion;
  }

	public void setAttackRegion(int attackRegion) {
	  this.attackRegion = attackRegion;
  }

	public int getDefenseRegion() {
	  return defenseRegion;
  }

	public void setDefenseRegion(int defenseRegion) {
	  this.defenseRegion = defenseRegion;
  }

	public int getKickoffRegion() {
	  return kickoffRegion;
  }

	public void setKickoffRegion(int kickoffRegion) {
	  this.kickoffRegion = kickoffRegion;
  }

}
