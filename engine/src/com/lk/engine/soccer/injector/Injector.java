package com.lk.engine.soccer.injector;

import static com.lk.engine.soccer.console.ParamNames.GOAL;
import static com.lk.engine.soccer.console.ParamNames.SOCCERPITCH;

import java.util.HashMap;
import java.util.Map;

import com.lk.engine.common.core.BaseGameEntity;
import com.lk.engine.common.core.EntityManager;
import com.lk.engine.common.core.RenderManager;
import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.core.UpdateManager;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.debug.ConsoleSink;
import com.lk.engine.common.debug.OldConsole;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.common.time.FrameCounter;
import com.lk.engine.soccer.console.Console;
import com.lk.engine.soccer.console.DefaultConsole;
import com.lk.engine.soccer.console.ParamAccessBuilder;
import com.lk.engine.soccer.console.ParamNames;
import com.lk.engine.soccer.console.Parameters;
import com.lk.engine.soccer.console.params.BallParams;
import com.lk.engine.soccer.console.params.GoalParams;
import com.lk.engine.soccer.console.params.SoccerPitchParams;
import com.lk.engine.soccer.console.params.TeamParams;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.FieldMarkLines;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.Goal;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.coach.Coach;
import com.lk.engine.soccer.elements.coach.states.CoachGlobalState;
import com.lk.engine.soccer.elements.coach.telegrams.CoachTelegramBuilder;
import com.lk.engine.soccer.elements.team.Team;
import com.lk.engine.soccer.elements.team.Team.TeamColor;
import com.lk.engine.soccer.elements.team.states.TeamGlobalState;
import com.lk.engine.soccer.elements.team.telegram.TeamTelegramBuilder;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;
import com.lk.engine.soccer.script.ScriptParser;

public class Injector {
	private final Map<Class<?>, Factory> instances = new HashMap<Class<?>, Factory>();
	private final Map<String, Factory> named = new HashMap<String, Factory>();

	public Injector() {
		instances.put(ScriptParser.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ScriptParser();
			}
		});

		instances.put(States.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new States(Injector.this);
			}
		});

		instances.put(RandomGenerator.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new RandomGenerator();
			}
		});

		instances.put(ParamAccessBuilder.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ParamAccessBuilder();
			}
		});

		instances.put(Parameters.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new Parameters(get(ParamAccessBuilder.class).buildAccessors());
			}
		});

		instances.put(OldConsole.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ConsoleSink();
			}
		});

		instances.put(EntityManager.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new EntityManager();
			}
		});

		instances.put(FrameCounter.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new FrameCounter();
			}
		});

		instances.put(Telegraph.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new Telegraph(get(FrameCounter.class));
			}
		});

		instances.put(UpdateManager.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new UpdateManager();
			}
		});

		instances.put(RenderManager.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new RenderManager();
			}
		});

		instances.put(Console.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new DefaultConsole();
			}
		});

		final Map<Class<?>, Factory> states = new StateBuilder(this).addStates();
		instances.putAll(states);

		instances.put(Enviroment.class, new SingletonFactory() {
			@SuppressWarnings("unchecked")
			@Override
			public Object newInstance() {
				final Map<String, Class<State>> statesMap = new HashMap<String, Class<State>>();
				for (final Class<?> state : states.keySet()) {
					statesMap.put(stateName(state), (Class<State>) state);
				}
				final Telegraph dispatcher = get(Telegraph.class);
				return new Enviroment(get(EntityBuilder.class), get(Parameters.class), statesMap, get(Console.class),
				    dispatcher);
			}
		});

		instances.put(Evaluator.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				final Evaluator evaluator = new Evaluator(get(Enviroment.class));
				get(UpdateManager.class).add(evaluator);
				return evaluator;
			}
		});

		instances.put(EntityBuilder.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new EntityBuilder(Injector.this);
			}
		});

		instances.put(FieldPlayingArea.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				final SoccerPitchParams params = get(Parameters.class).getContainer(SOCCERPITCH);
				return new FieldPlayingArea(params);
			}
		});

		instances.put(PlayRegions.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new PlayRegions(get(FieldPlayingArea.class));
			}
		});

		named.put("blue.coach", new SingletonFactory() {
			@Override
			public Object newInstance() {
				final Enviroment enviroment = get(Enviroment.class);
				return newCoach(enviroment, TeamColor.BLUE);
			}
		});

		named.put("red.coach", new SingletonFactory() {
			@Override
			public Object newInstance() {
				final Enviroment enviroment = get(Enviroment.class);
				return newCoach(enviroment, TeamColor.RED);
			}
		});

		named.put("blue.team", new SingletonFactory() {
			@Override
			public Object newInstance() {
				return newTeam(TeamColor.BLUE);
			}
		});

		named.put("red.team", new SingletonFactory() {
			@Override
			public Object newInstance() {
				return newTeam(TeamColor.RED);
			}
		});

		instances.put(Referee.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				final SoccerPitchParams params = get(Parameters.class).getContainer(SOCCERPITCH);
				return new Referee(params, get(FieldPlayingArea.class), get(FieldMarkLines.class), get(Ball.class), get(
				    "red.team", Team.class), get("blue.team", Team.class));
			}
		});

		named.put("redGoal", new SingletonFactory() {
			@Override
			public Object newInstance() {
				final GoalParams params = get(Parameters.class).getContainer(GOAL);
				final FieldPlayingArea playingArea = get(FieldPlayingArea.class);
				final double left = playingArea.getArea().left();
				final int cy = playingArea.getY();
				return new Goal(params, new Vector2D(left, (cy - params.getWidth()) / 2), new Vector2D(left, cy
				    - (cy - params.getWidth()) / 2), new Vector2D(1, 0));
			}
		});

		named.put("blueGoal", new SingletonFactory() {
			@Override
			public Object newInstance() {
				final GoalParams params = get(Parameters.class).getContainer(GOAL);
				final FieldPlayingArea playingArea = get(FieldPlayingArea.class);
				final double right = playingArea.getArea().right();
				final int cy = playingArea.getY();
				return new Goal(params, new Vector2D(right, (cy - params.getWidth()) / 2), new Vector2D(right, cy
				    - (cy - params.getWidth()) / 2), new Vector2D(-1, 0));
			}
		});

		instances.put(FieldMarkLines.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				final FieldPlayingArea playingArea = get(FieldPlayingArea.class);
				final Goal blueGoal = get("blueGoal", Goal.class);
				final Goal redGoal = get("redGoal", Goal.class);
				return new FieldMarkLines(playingArea, redGoal, blueGoal);
			}
		});

		instances.put(Ball.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				final BallParams params = get(Parameters.class).getContainer(ParamNames.BALL);
				final FieldMarkLines markLines = get(FieldMarkLines.class);
				final RandomGenerator random = get(RandomGenerator.class);
				final Ball ball = new Ball(params, new Vector2D(200, 200), markLines, random);

				register(get(Enviroment.class), ball);
				return ball;
			}
		});
	}

	private Coach newCoach(Enviroment enviroment, TeamColor color) {
		final Telegraph telegraph = get(Telegraph.class);

		final Coach coach = new Coach(telegraph, color);

		newStateMachine(coach, CoachGlobalState.class);
		new CoachTelegramBuilder(coach).checkin(telegraph);
		register(enviroment, coach);

		return coach;
	}

	private String stateName(final Class<?> state) {
		final String[] s = state.getName().split("\\.");
		final String name = s[s.length - 1];
		return name.toLowerCase();
	}/**/

	public StateMachine newStateMachine(StateMachineOwner owner, Class<? extends State> globalState) {
		final States states = get(States.class);

		final StateMachine stateMachine = new StateMachine(owner, states);
		owner.setStateMachine(stateMachine);
		stateMachine.setGlobalState(states.get(globalState));
		return stateMachine;
	}

	public void register(final Enviroment enviroment, final Object object) {
		final EntityManager entityManager = get(EntityManager.class);
		final UpdateManager updateManager = get(UpdateManager.class);

		if (object instanceof BaseGameEntity) {
			entityManager.registerEntity((BaseGameEntity) object);
		}

		if (object instanceof Updatable) {
			updateManager.add((Updatable) object);
		}

	}

	@SuppressWarnings("unchecked")
	public <T> T get(String name, final Class<T> clazz) {
		if (named.containsKey(name))
			return (T) named.get(name).instance();
		else
			throw new RuntimeException("Injector: instance named " + name + " not found: " + clazz.getName());
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> clazz) {
		if (instances.containsKey(clazz))
			return (T) instances.get(clazz).instance();
		else
			throw new RuntimeException("Injector: instance not found: " + clazz.getName());
	}

	private Team newTeam(TeamColor color) {
		final Enviroment enviroment = get(Enviroment.class);
		final TeamParams params = get(Parameters.class).getContainer(ParamNames.TEAM);
		final RandomGenerator random = get(RandomGenerator.class);
		final Goal goal = get(color.name().toLowerCase() + "Goal", Goal.class);
		final FieldPlayingArea playingArea = get(FieldPlayingArea.class);
		final Ball ball = get(Ball.class);
		final Coach coach = get(color.name().toLowerCase() + ".coach", Coach.class);
		final Telegraph dispatcher = get(Telegraph.class);

		final Team team = new Team(params, dispatcher, goal, color, random, playingArea, ball, coach);

		newStateMachine(team, TeamGlobalState.class);
		dispatcher.checking(new TeamTelegramBuilder(team));

		enviroment.registerTeam(team);
		register(enviroment, team);

		return team;
	}

	interface Factory {
		public Object instance();
	}

	static abstract class SingletonFactory implements Factory {
		private Object obj;

		@Override
		public Object instance() {
			if (obj == null)
				obj = newInstance();

			return obj;
		}

		public abstract Object newInstance();
	}

}
