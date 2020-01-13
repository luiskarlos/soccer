package com.lk.engine.soccer.injector;

import java.util.HashMap;
import java.util.Map;

import com.lk.engine.common.fsm.Idle;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.coach.states.CoachGlobalState;
import com.lk.engine.soccer.elements.players.fieldplayer.states.ChaseBall;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Dribble;
import com.lk.engine.soccer.elements.players.fieldplayer.states.GlobalPlayerState;
import com.lk.engine.soccer.elements.players.fieldplayer.states.KickBall;
import com.lk.engine.soccer.elements.players.fieldplayer.states.ReceiveBall;
import com.lk.engine.soccer.elements.players.fieldplayer.states.ReturnToHomeRegion;
import com.lk.engine.soccer.elements.players.fieldplayer.states.SupportAttacker;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Wait;
import com.lk.engine.soccer.elements.players.goalkeeper.states.GlobalKeeperState;
import com.lk.engine.soccer.elements.players.goalkeeper.states.InterceptBall;
import com.lk.engine.soccer.elements.players.goalkeeper.states.PutBallBackInPlay;
import com.lk.engine.soccer.elements.players.goalkeeper.states.ReturnHome;
import com.lk.engine.soccer.elements.players.goalkeeper.states.TendGoal;
import com.lk.engine.soccer.elements.players.states.Walk;
import com.lk.engine.soccer.elements.team.states.Attacking;
import com.lk.engine.soccer.elements.team.states.Defending;
import com.lk.engine.soccer.elements.team.states.PrepareForKickOff;
import com.lk.engine.soccer.elements.team.states.TeamGlobalState;
import com.lk.engine.soccer.injector.Injector.Factory;
import com.lk.engine.soccer.injector.Injector.SingletonFactory;

public class StateBuilder {
	private final Injector injector;

	public StateBuilder(final Injector injector) {
		this.injector = injector;
	}

	Map<Class<?>, Factory> addStates() {
		final Map<Class<?>, Factory> instances = new HashMap<Class<?>, Factory>();
		addCoachStates(instances);
		addTeamStates(instances);
		addFieldPlayerStates(instances);
		addGoalkeeperStats(instances);
		addGenericStates(instances);
		return instances;
	}

	private void addCoachStates(final Map<Class<?>, Factory> instances) {
		instances.put(CoachGlobalState.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new CoachGlobalState();
			}
		});
	}

	private void addGenericStates(final Map<Class<?>, Factory> instances) {
		instances.put(Idle.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return Idle.instance();
			}
		});

		instances.put(Walk.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new Walk();
			}
		});
	}

	private void addTeamStates(final Map<Class<?>, Factory> instances) {
		instances.put(Attacking.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new Attacking();
			}
		});

		instances.put(Defending.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new Defending();
			}
		});

		instances.put(PrepareForKickOff.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new PrepareForKickOff(injector.get(Referee.class));
			}
		});

		instances.put(TeamGlobalState.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new TeamGlobalState();
			}
		});

	}

	private void addFieldPlayerStates(final Map<Class<?>, Factory> instances) {
		instances.put(ChaseBall.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ChaseBall();
			}
		});

		instances.put(Dribble.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new Dribble(injector.get(Telegraph.class));
			}
		});

		instances.put(GlobalPlayerState.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new GlobalPlayerState();
			}
		});

		instances.put(KickBall.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new KickBall(injector.get(Telegraph.class), injector.get(RandomGenerator.class), injector
				    .get(Referee.class));
			}
		});

		instances.put(ReceiveBall.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ReceiveBall(injector.get(Telegraph.class), injector.get(RandomGenerator.class), injector
				    .get(FieldPlayingArea.class));
			}
		});

		instances.put(ReturnToHomeRegion.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ReturnToHomeRegion(injector.get(Referee.class));
			}
		});

		instances.put(SupportAttacker.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new SupportAttacker();
			}
		});

		instances.put(Wait.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new Wait(injector.get(Referee.class));
			}
		});

	}

	private void addGoalkeeperStats(final Map<Class<?>, Factory> instances) {
		instances.put(GlobalKeeperState.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new GlobalKeeperState();
			}
		});

		instances.put(InterceptBall.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new InterceptBall(injector.get(Referee.class));
			}
		});

		instances.put(ReturnHome.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ReturnHome();
			}
		});

		instances.put(TendGoal.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new TendGoal(injector.get(Referee.class));
			}
		});

		instances.put(PutBallBackInPlay.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new PutBallBackInPlay(injector.get(Telegraph.class), injector.get(Referee.class));
			}
		});

	}

}
