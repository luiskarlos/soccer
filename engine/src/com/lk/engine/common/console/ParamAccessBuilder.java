package com.lk.engine.common.console;

import static com.lk.engine.common.console.ParamNames.BALL;
import static com.lk.engine.common.console.ParamNames.BLUE_GOALKEEPER;
import static com.lk.engine.common.console.ParamNames.BLUE_PLAYER;
import static com.lk.engine.common.console.ParamNames.GOAL;
import static com.lk.engine.common.console.ParamNames.RED_GOALKEEPER;
import static com.lk.engine.common.console.ParamNames.RED_PLAYER;
import static com.lk.engine.common.console.ParamNames.SOCCERPITCH;
import static com.lk.engine.common.console.ParamNames.SYSTEM;
import static com.lk.engine.common.console.ParamNames.TEAM;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.console.params.BallParams;
import com.lk.engine.common.console.params.FieldPlayerParams;
import com.lk.engine.common.console.params.GoalParams;
import com.lk.engine.common.console.params.GoalkeeperParams;
import com.lk.engine.common.console.params.MovingEntityParams;
import com.lk.engine.common.console.params.PlayerParams;
import com.lk.engine.common.console.params.SoccerPitchParams;
import com.lk.engine.common.console.params.SystemParams;
import com.lk.engine.common.console.params.TeamParams;

public class ParamAccessBuilder {
	public List<ParamHandler> buildAccessors() {
		final List<ParamHandler> accessors = new ArrayList<ParamHandler>();

		accessors.addAll(goalkeeperPlayer(BLUE_GOALKEEPER.id(), new GoalkeeperParams()));
		accessors.addAll(goalkeeperPlayer(RED_GOALKEEPER.id(), new GoalkeeperParams()));

		for (int i = 1; i <= 4; i++) {
			accessors.addAll(fieldPlayer(BLUE_PLAYER.id() + i, new FieldPlayerParams()));
			accessors.addAll(fieldPlayer(RED_PLAYER.id() + i, new FieldPlayerParams()));
		}

		accessors.addAll(system(SYSTEM.id()));
		accessors.addAll(soccerpitch(SOCCERPITCH.id()));
		accessors.addAll(team(TEAM.id()));
		accessors.addAll(goal(GOAL.id()));
		accessors.addAll(ball(BALL.id()));
		return accessors;
	}

	private List<ParamHandler> system(final String name) {
		final List<ParamHandler> accessors = new ArrayList<ParamHandler>();
		final SystemParams params = new SystemParams();

		accessors.add(new ParamHandler(name, params, null));
		accessors.add(new ParamHandler(name + ".frameRate", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setFrameRate(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getFrameRate());
			}
		}));

		return accessors;
	}

	private List<ParamHandler> team(final String name) {
		final List<ParamHandler> accessors = new ArrayList<ParamHandler>();
		final TeamParams params = new TeamParams();
		accessors.add(new ParamHandler(name, params, null));
		accessors.add(new ParamHandler(name + ".supportSpotsX", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setSupportSpotsX(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getSupportSpotsX());
			}
		}));

		accessors.add(new ParamHandler(name + ".supportSpotsY", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setSupportSpotsY(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getSupportSpotsY());
			}
		}));

		accessors.add(new ParamHandler(name + ".attemptsToFindValidStrike", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setAttemptsToFindValidStrike(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getAttemptsToFindValidStrike());
			}
		}));

		accessors.add(new ParamHandler(name + ".supportSpotUpdateFreq", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setSupportSpotUpdateFreq(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getSupportSpotUpdateFreq());
			}
		}));

		accessors.add(new ParamHandler(name + ".spotPassSafeScore", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setSpotPassSafeScore(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getSpotPassSafeScore());
			}
		}));

		accessors.add(new ParamHandler(name + ".spotCanScoreFromPositionScore", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setSpotCanScoreFromPositionScore(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getSpotCanScoreFromPositionScore());
			}
		}));

		accessors.add(new ParamHandler(name + ".spotDistFromControllingPlayerScore", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setSpotDistFromControllingPlayerScore(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getSpotDistFromControllingPlayerScore());
			}
		}));

		accessors.add(new ParamHandler(name + ".showControllingTeam", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setShowControllingTeam(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isShowControllingTeam());
			}
		}));

		accessors.add(new ParamHandler(name + ".showSupportSpots", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setShowSupportSpots(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isShowSupportSpots());
			}
		}));

		accessors.add(new ParamHandler(name + ".showSupportingPlayersTarget", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setShowSupportingPlayersTarget(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isShowSupportingPlayersTarget());
			}
		}));

		accessors.add(new ParamHandler(name + ".showTeamState", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setShowTeamState(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isShowTeamState());
			}
		}));

		return accessors;
	}

	private List<ParamHandler> soccerpitch(final String name) {
		final List<ParamHandler> accessors = new ArrayList<ParamHandler>();
		final SoccerPitchParams params = new SoccerPitchParams();

		accessors.add(new ParamHandler(name, params, null));
		accessors.add(new ParamHandler(name + ".drawRegions", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setDrawRegions(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isDrawRegions());
			}
		}));

		accessors.add(new ParamHandler(name + ".fieldWidth", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setFieldWidth(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getFieldWidth());
			}
		}));

		accessors.add(new ParamHandler(name + ".fieldHeight", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setFieldHeight(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getFieldHeight());
			}
		}));

		accessors.add(new ParamHandler(name + ".borderWidth", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setBorderWidth(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getBorderWidth());
			}
		}));

		accessors.add(new ParamHandler(name + ".borderHeight", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setBorderHeight(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getBorderHeight());
			}
		}));

		return accessors;
	}

	private List<ParamHandler> goal(final String name) {
		final List<ParamHandler> accessors = new ArrayList<ParamHandler>();
		final GoalParams params = new GoalParams();
		accessors.add(new ParamHandler(name, params, null));
		accessors.add(new ParamHandler(name + ".width", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setWidth(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getWidth());
			}
		}));

		return accessors;
	}

	private List<ParamHandler> ball(final String name) {
		final List<ParamHandler> accessors = new ArrayList<ParamHandler>();
		final BallParams params = new BallParams();
		accessors.add(new ParamHandler(name, params, null));
		accessors.add(mass(name + ".mass", params, 1.0));
		accessors.add(radius(name + ".radius", params, 5.0));
		accessors.add(new ParamHandler(name + ".friction", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setFriction(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getFriction());
			}
		}));

		return accessors;
	}

	private List<ParamHandler> fieldPlayer(final String name, final PlayerParams params) {
		final List<ParamHandler> accessors = new ArrayList<ParamHandler>();
		params.setName(name);
		accessors.add(new ParamHandler(name, params, null));
		accessors.add(mass(name + ".mass", params, 3.0));
		accessors.add(radius(name + ".radius", params, 10.0));
		accessors.add(maxForce(name + ".maxForce", params, 1.0));
		accessors.add(maxTurnRate(name + ".maxTurnRate", params, 0.4));

		accessors.add(new ParamHandler(name + ".name", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setName(val);
			}

			@Override
			public String getValue() {
				return params.getName();
			}
		}));

		accessors.add(new ParamHandler(name + ".maxSpeedWithBall", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxSpeedWithBall(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxSpeedWithBall());
			}
		}));
		
		accessors.add(new ParamHandler(name + ".region.kickoff", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setKickoffRegion((int)parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getKickoffRegion());
			}
		}));
		
		accessors.add(new ParamHandler(name + ".region.attack", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setAttackRegion((int)parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getAttackRegion());
			}
		}));

		accessors.add(new ParamHandler(name + ".region.defence", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setDefenseRegion((int)parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getDefenseRegion());
			}
		}));

		
		accessors.add(new ParamHandler(name + ".maxSpeedWithoutBall", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxSpeedWithoutBall(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxSpeedWithoutBall());
			}
		}));

		accessors.add(new ParamHandler(name + ".comfortZone", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setComfortZone(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getComfortZone());
			}
		}));

		accessors.add(new ParamHandler(name + ".kickingAccuracy", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setKickingAccuracy(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getKickingAccuracy());
			}
		}));

		accessors.add(new ParamHandler(name + ".kickFrequency", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setKickFrequency(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getKickFrequency());
			}
		}));

		accessors.add(new ParamHandler(name + ".nonPenetrationConstraint", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setNonPenetrationConstraint(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isNonPenetrationConstraint());
			}
		}));

		accessors.add(new ParamHandler(name + ".maxDribbleForce", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxDribbleForce(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxDribbleForce());
			}
		}));

		accessors.add(new ParamHandler(name + ".shortDribbleForce", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setShortDribbleForce(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getShortDribbleForce());
			}
		}));

		accessors.add(new ParamHandler(name + ".maxShootingForce", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxShootingForce(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxShootingForce());
			}
		}));

		accessors.add(new ParamHandler(name + ".maxPassingForce", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxPassingForce(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxPassingForce());
			}
		}));

		accessors.add(new ParamHandler(name + ".passThreatRadius", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setPassThreatRadius(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getPassThreatRadius());
			}
		}));

		accessors.add(new ParamHandler(name + ".ballPickupRange", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setBallPickupRange(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getBallPickupRange());
			}
		}));

		accessors.add(new ParamHandler(name + ".playerInTargetRange", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setPlayerInTargetRange(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getPlayerInTargetRange());
			}
		}));

		accessors.add(new ParamHandler(name + ".chanceAttemptsPotShot", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setChanceAttemptsPotShot(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getChanceAttemptsPotShot());
			}
		}));

		accessors.add(new ParamHandler(name + ".minPassDistance", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMinPassDistance(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMinPassDistance());
			}
		}));

		accessors.add(new ParamHandler(name + ".chanceOfUsingArriveTypeReceiveBehavior", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setChanceOfUsingArriveTypeReceiveBehavior(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getChanceOfUsingArriveTypeReceiveBehavior());
			}
		}));

		accessors.add(new ParamHandler(name + ".separationCoefficient", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setSeparationCoefficient(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getSeparationCoefficient());
			}
		}));

		accessors.add(new ParamHandler(name + ".viewDistance", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setViewDistance(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getViewDistance());
			}
		}));

		accessors.add(new ParamHandler(name + ".viewStates", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setViewStates(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isViewStates());
			}
		}));

		accessors.add(new ParamHandler(name + ".viewIDs", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setViewIDs(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isViewIDs());
			}
		}));

		accessors.add(new ParamHandler(name + ".viewSupportSpots", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setViewSupportSpots(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isViewSupportSpots());
			}
		}));

		accessors.add(new ParamHandler(name + ".viewRegions", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setViewRegions(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isViewRegions());
			}
		}));

		accessors.add(new ParamHandler(name + ".showControllingTeam", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setShowControllingTeam(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isShowControllingTeam());
			}
		}));

		accessors.add(new ParamHandler(name + ".viewTargets", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setViewTargets(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isViewTargets());
			}
		}));

		accessors.add(new ParamHandler(name + ".highlightIfThreatened", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setHighlightIfThreatened(Boolean.parseBoolean(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.isHighlightIfThreatened());
			}
		}));

		accessors.add(new ParamHandler(name + ".attemptsToFindValidStrike", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setAttemptsToFindValidStrike(Integer.parseInt(val));
			}

			@Override
			public String getValue() {
				return String.valueOf(params.getAttemptsToFindValidStrike());
			}
		}));

		return accessors;
	}

	private List<ParamHandler> goalkeeperPlayer(final String name, final GoalkeeperParams params) {
		final List<ParamHandler> accessors = fieldPlayer(name, params);
		accessors.add(maxSpeed(name + ".maxSpeed", params, 1.5));
		accessors.add(new ParamHandler(name + ".interceptRange", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setInterceptRange(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getInterceptRange());
			}
		}));

		accessors.add(new ParamHandler(name + ".tendingDistance", params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setTendingDistance(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getTendingDistance());
			}
		}));

		return accessors;
	}

	private ParamHandler mass(final String name, final MovingEntityParams params, final double defaultValue) {
		params.setMass(defaultValue);
		return new ParamHandler(name, params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMass(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMass());
			}
		});
	}

	private ParamHandler radius(final String name, final MovingEntityParams params, final double defaultValue) {
		params.setRadius(defaultValue);
		return new ParamHandler(name, params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setRadius(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getRadius());
			}
		});
	}

	private ParamHandler maxForce(final String name, final MovingEntityParams params, final double defaultValue) {
		params.setMaxForce(defaultValue);
		return new ParamHandler(name, params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxForce(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxForce());
			}
		});
	}

	private ParamHandler maxTurnRate(final String name, final MovingEntityParams params, final double defaultValue) {
		params.setMaxTurnRate(defaultValue);
		return new ParamHandler(name, params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxTurnRate(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxTurnRate());
			}
		});
	}

	private ParamHandler maxSpeed(final String name, final MovingEntityParams params, final double defaultValue) {
		params.setMaxSpeed(defaultValue);
		return new ParamHandler(name, params, new ParamAccess() {
			@Override
			public void setValue(final String val) {
				params.setMaxSpeed(parseDouble(val));
			}

			@Override
			public String getValue() {
				return format(params.getMaxSpeed());
			}
		});
	}

	private double parseDouble(final String val) {
		return Double.parseDouble(val);
	}

	private String format(final double val) {
		return String.valueOf(val);
	}

}
