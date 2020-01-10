package com.lk.engine.common.injector;

import java.util.HashMap;
import java.util.Map;

import com.lk.engine.common.fsm.Idle;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateExecutable;
import com.lk.engine.common.injector.Injector.Factory;
import com.lk.engine.common.injector.Injector.SingletonFactory;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.coach.states.CoachGlobalState;
import com.lk.engine.soccer.elements.players.fieldplayer.states.ChaseBall;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Dribble;
import com.lk.engine.soccer.elements.players.fieldplayer.states.GlobalPlayerState;
import com.lk.engine.soccer.elements.players.fieldplayer.states.KickBall;
import com.lk.engine.soccer.elements.players.fieldplayer.states.PickupBall;
import com.lk.engine.soccer.elements.players.fieldplayer.states.ReceiveBall;
import com.lk.engine.soccer.elements.players.fieldplayer.states.SupportAttacker;
import com.lk.engine.soccer.elements.players.fieldplayer.states.Wait;
import com.lk.engine.soccer.elements.players.fieldplayer.states.WaitForStateChange;
import com.lk.engine.soccer.elements.players.goalkeeper.states.GlobalKeeperState;
import com.lk.engine.soccer.elements.players.goalkeeper.states.InterceptBall;
import com.lk.engine.soccer.elements.players.goalkeeper.states.PutBallBackInPlay;
import com.lk.engine.soccer.elements.players.goalkeeper.states.ReturnHome;
import com.lk.engine.soccer.elements.players.goalkeeper.states.TendGoal;
import com.lk.engine.soccer.elements.players.states.ReturnToHomeRegion;
import com.lk.engine.soccer.elements.players.states.Walk;
import com.lk.engine.soccer.elements.referee.Referee;
import com.lk.engine.soccer.elements.referee.states.KickOff;
import com.lk.engine.soccer.elements.referee.states.SuperviseGame;
import com.lk.engine.soccer.elements.team.states.Attacking;
import com.lk.engine.soccer.elements.team.states.Defending;
import com.lk.engine.soccer.elements.team.states.PrepareForKickOff;
import com.lk.engine.soccer.elements.team.states.TeamGlobalState;
import com.lk.engine.soccer.elements.team.states.WaitForReferee;

public class StateBuilder {
	private final Injector injector;

	public StateBuilder(final Injector injector) {
		this.injector = injector;
	}

	Map<String, Factory> addStates() {
		final Map<String, Factory> instances = new HashMap<String, Factory>();
		addRefereeStates(instances);
		addCoachStates(instances);
		addTeamStates(instances);
		addFieldPlayerStates(instances);
		addGoalkeeperStats(instances);
		addGenericStates(instances);
		return instances;
	}

	private void addCoachStates(final Map<String, Factory> instances) {
		instances.put(CoachGlobalState.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new CoachGlobalState());
			}
		});
	}

	private void addGenericStates(final Map<String, Factory> instances) {
		instances.put(Idle.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(Idle.instance());
			}
		});

		instances.put(Walk.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new Walk());
			}
		});
	}

	private void addTeamStates(final Map<String, Factory> instances) {
		instances.put(WaitForReferee.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new WaitForReferee());
			}
		});
		
		instances.put(Attacking.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new Attacking());
			}
		});

		instances.put(Defending.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new Defending());
			}
		});

		instances.put(PrepareForKickOff.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new PrepareForKickOff(injector.get(Referee.class)));
			}
		});

		instances.put(TeamGlobalState.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new TeamGlobalState());
			}
		});
	}
	
	private void addRefereeStates(final Map<String, Factory> instances) {
		instances.put(KickOff.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new KickOff());
			}
		});

		instances.put(com.lk.engine.soccer.elements.referee.states.PrepareForKickOff.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new com.lk.engine.soccer.elements.referee.states.PrepareForKickOff());
			}
		});
		
		instances.put(SuperviseGame.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new SuperviseGame());
			}
		});
	}
	
	private void addFieldPlayerStates(final Map<String, Factory> instances) {
		instances.put(WaitForStateChange.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new WaitForStateChange(injector.get(RandomGenerator.class)));
			}
		});
		
		instances.put(PickupBall.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new PickupBall(injector.get(Referee.class)));
			}
		});
		
		instances.put(ChaseBall.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new ChaseBall(injector.get(Referee.class)));
			}
		});

		instances.put(Dribble.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new Dribble(injector.get(Referee.class), injector.get(Telegraph.class)));
			}
		});

		instances.put(GlobalPlayerState.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new GlobalPlayerState());
			}
		});

		instances.put(KickBall.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new KickBall(injector.get(Telegraph.class), injector.get(RandomGenerator.class), injector
				    .get(Referee.class)));
			}
		});

		instances.put(ReceiveBall.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new ReceiveBall(injector.get(Telegraph.class), injector.get(RandomGenerator.class), injector
				    .get(FieldPlayingArea.class)));
			}
		});

		instances.put(ReturnToHomeRegion.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				ReturnToHomeRegion state = new ReturnToHomeRegion(injector.get(Referee.class));
				return  wrap(state);
			}
		});

		instances.put(SupportAttacker.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new SupportAttacker());
			}
		});

		instances.put(Wait.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new Wait(injector.get(Referee.class)));
			}
		});

	}

	private void addGoalkeeperStats(final Map<String, Factory> instances) {
		instances.put(GlobalKeeperState.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new GlobalKeeperState());
			}
		});

		instances.put(InterceptBall.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new InterceptBall(injector.get(Referee.class)));
			}
		});

		instances.put(ReturnHome.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new ReturnHome());
			}
		});

		instances.put(TendGoal.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new TendGoal(injector.get(Referee.class)));
			}
		});

		instances.put(PutBallBackInPlay.NAME, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return  wrap(new PutBallBackInPlay(injector.get(Telegraph.class), injector.get(Referee.class)));
			}
		});
	}
	
	private State wrap(State state) {
		return new StateExecutable(state, injector.getProvider(Evaluator.class));
	}
}
