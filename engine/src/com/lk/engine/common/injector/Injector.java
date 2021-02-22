package com.lk.engine.common.injector;

import static com.lk.engine.common.console.ParamNames.GOAL;
import static com.lk.engine.common.console.ParamNames.SOCCERPITCH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.lk.engine.common.console.Console;
import com.lk.engine.common.console.DefaultConsole;
import com.lk.engine.common.console.ParamAccessBuilder;
import com.lk.engine.common.console.ParamAccessObject;
import com.lk.engine.common.console.ParamHandler;
import com.lk.engine.common.console.ParamNames;
import com.lk.engine.common.console.Parameters;
import com.lk.engine.common.console.params.BallParams;
import com.lk.engine.common.console.params.GoalParams;
import com.lk.engine.common.console.params.SoccerPitchParams;
import com.lk.engine.common.console.params.TeamParams;
import com.lk.engine.common.core.BaseGameEntity;
import com.lk.engine.common.core.EntityManager;
import com.lk.engine.common.core.RenderManager;
import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.core.UpdateManager;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.debug.ConsoleSink;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.js.JsActions;
import com.lk.engine.common.tools.CsvSerializer;
import com.lk.engine.common.debug.OldConsole;
import com.lk.engine.common.fsm.State;
import com.lk.engine.common.fsm.StateMachine;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.ScriptParser;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.common.time.FrameCounter;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.FieldMarkLines;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.Goal;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.coach.Coach;
import com.lk.engine.soccer.elements.coach.states.CoachGlobalState;
import com.lk.engine.soccer.elements.coach.telegrams.CoachTelegramBuilder;
import com.lk.engine.soccer.elements.referee.Referee;
import com.lk.engine.soccer.elements.team.Team;
import com.lk.engine.soccer.elements.team.Team.TeamColor;
import com.lk.engine.soccer.elements.team.states.TeamGlobalState;
import com.lk.engine.soccer.elements.team.telegram.TeamTelegramBuilder;

public class Injector {
	private final EventBus eventBus = GWT.create(SimpleEventBus.class);
	private JsActions jsActions;

	private final Map<Class<?>, Factory> instances = new HashMap<Class<?>, Factory>();
	private final Map<String, Factory> named = new HashMap<String, Factory>();
	private Map<String, State> states = new HashMap<String, State>();
	private List<StateMachine> fsms = new ArrayList<StateMachine>();


	public Injector() {

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

		instances.put(ScriptParser.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				return new ScriptParser(get(Environment.class), eventBus);
			}
		});

		instances.put(Evaluator.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				final Evaluator evaluator = new Evaluator(get(Environment.class));
				get(UpdateManager.class).add(evaluator);
				return new Provider<Evaluator>() {
					@Override
          public Evaluator get() {
	          return Evaluator.getActiveEvaluator();
          }
				};
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

		instances.put(Environment.class, new SingletonFactory() {
			@Override
			public Object newInstance() {
				final Telegraph dispatcher = get(Telegraph.class);
				final Provider<List<StateMachine>> pFsms = new Provider<List<StateMachine>>() {
					@Override
					public List<StateMachine> get() {
						return fsms;
					}
				};

				Environment env = new Environment(get(EntityBuilder.class), get(Parameters.class), get(Console.class),
				    dispatcher, pFsms);
				return env;
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
				final Environment enviroment = get(Environment.class);
				return newCoach(enviroment, TeamColor.BLUE);
			}
		});

		named.put("red.coach", new SingletonFactory() {
			@Override
			public Object newInstance() {
				final Environment enviroment = get(Environment.class);
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
				final Referee referee = new Referee(params, get(FieldPlayingArea.class), get(FieldMarkLines.class), get(Ball.class), get(
				    "red.team", Team.class), get("blue.team", Team.class));
				newStateMachine(referee, TeamGlobalState.NAME);
				final Environment enviroment = get(Environment.class);
				enviroment.setVariable(referee.getName(), new ParamHandler(referee.getName(), enviroment, new ParamAccessObject(referee)));
				register(enviroment, referee);
				return referee;
			}
		});

		Map<String, Factory> statesFactory = new StateBuilder(this).addStates();
		named.putAll(statesFactory);

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
				final Ball ball = new Ball(params, new Vector2D(343, 195), markLines, random);

				register(get(Environment.class), ball);
				return ball;
			}
		});

		for (Entry<String, Factory> set : statesFactory.entrySet()) {
	    states.put(set.getKey(), (State)set.getValue().instance());
    }
		get(Environment.class).registerStates(states);

		jsActions = new JsActions(eventBus, get(EntityManager.class), get(Environment.class));

		final Console c = get(Console.class);
		c.println(Level.INFO, new String[] { c.toString() });
		new CsvSerializer(
        get("blue.team", Team.class),
        get("red.team", Team.class),
        get(Ball.class),
        eventBus);
	}

	private Coach newCoach(Environment enviroment, TeamColor color) {
		final Telegraph telegraph = get(Telegraph.class);

		final Coach coach = new Coach(telegraph, color);

		newStateMachine(coach, CoachGlobalState.NAME);
		new CoachTelegramBuilder(coach).checkin(telegraph);
		register(enviroment, coach);

		return coach;
	}

	public List<StateMachine> getFsms() {
	  return fsms;
  }

	public StateMachine newStateMachine(StateMachineOwner owner, String globalState) {
		final StateMachine stateMachine = new StateMachine(owner, getProvider(Evaluator.class), eventBus);
		owner.setStateMachine(stateMachine);
		stateMachine.setGlobalState((State) named.get(globalState).instance());
		fsms.add(stateMachine);   //TODO: FMS is now updatable, add to updatables
		return stateMachine;
	}

	public JsActions getJsActions() {
		return jsActions;
	}

	public void register(final Environment enviroment, final Object object) {
		if (object instanceof BaseGameEntity) {
			final EntityManager entityManager = get(EntityManager.class);
			entityManager.registerEntity((BaseGameEntity) object);
		}

		if (object instanceof Updatable) {
			final UpdateManager updateManager = get(UpdateManager.class);
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

	@SuppressWarnings("unchecked")
	public <T> Provider<T> getProvider(final Class<T> clazz) {
		if (instances.containsKey(clazz))
			return (Provider<T>) instances.get(clazz).instance();
		else
			throw new RuntimeException("Injector: instance not found: " + clazz.getName());
	}

	private Team newTeam(TeamColor color) {
		final Environment environment = get(Environment.class);
		final TeamParams params = get(Parameters.class).getContainer(ParamNames.TEAM);
		final RandomGenerator random = get(RandomGenerator.class);
		final Goal goal = get(color.name().toLowerCase() + "Goal", Goal.class);
		final FieldPlayingArea playingArea = get(FieldPlayingArea.class);
		final Ball ball = get(Ball.class);
		final Coach coach = get(color.name().toLowerCase() + ".coach", Coach.class);
		final Telegraph dispatcher = get(Telegraph.class);

		final Team team = new Team(params, dispatcher, goal, color, random, playingArea, ball, coach);

		newStateMachine(team, TeamGlobalState.NAME);
		dispatcher.checking(new TeamTelegramBuilder(team));

		environment.registerTeam(team);
		register(environment, team);

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
