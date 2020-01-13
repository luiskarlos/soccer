package com.lk.engine.soccer;

import static com.lk.engine.soccer.console.ParamNames.SOCCERPITCH;
import static com.lk.engine.soccer.console.ParamNames.SYSTEM;

import com.lk.engine.common.core.UpdateManager;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.Telegram;
import com.lk.engine.common.telegraph.TelegramCheckin;
import com.lk.engine.common.telegraph.TelegramHandler;
import com.lk.engine.common.telegraph.Telegraph;
import com.lk.engine.common.time.PrecisionTimer;
import com.lk.engine.soccer.console.Parameters;
import com.lk.engine.soccer.console.params.SoccerPitchParams;
import com.lk.engine.soccer.console.params.SystemParams;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.team.Team;
import com.lk.engine.soccer.injector.Injector;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;
import com.lk.engine.soccer.script.ScriptParser;
import com.lk.engine.soccer.script.ScriptParserListener;
import com.lk.engine.soccer.script.instructions.Block;

public class Game {
	private GameBuilderListener gameBuilderListener;
	private GameListener gameListener;
	private final Injector injector = new Injector();
	private final ScriptParser parser = injector.get(ScriptParser.class);
	private final PrecisionTimer timer;

	private final TelegramHandler msgHandler = new TelegramHandler() {
		@Override
		public Processed handle(Telegram telegram) {
			if (gameBuilderListener != null) {
				if (telegram.getExtraInfo() instanceof FieldPlayer)
					gameBuilderListener.onCreated((FieldPlayer) telegram.getExtraInfo());
				else
					gameBuilderListener.onCreated((Goalkeeper) telegram.getExtraInfo());

				return Processed.YES;
			}
			return Processed.NO;
		}
	};

	public Game() {
		final SystemParams systemParams = injector.get(Parameters.class).getContainer(SYSTEM);
		timer = new PrecisionTimer(systemParams.getFrameRate());

		parser.setListener(new ScriptParserListener() {
			@Override
			public void onBlock(final String name, final Block block) {
				injector.get(Enviroment.class).registerBlock(name, block);
			}
		});
		injector.get(Telegraph.class).checkin(Message.NEW_PLAYER, msgHandler);
	}

	public void setListener(final GameBuilderListener gameBuilderListener) {
		this.gameBuilderListener = gameBuilderListener;
	}

	public void setListener(final GameListener gameListener) {
		this.gameListener = gameListener;
	}

	public int getWidth() {
		final SoccerPitchParams params = injector.get(Parameters.class).getContainer(SOCCERPITCH);
		return params.getFieldWidth();
	}

	public int getHeight() {
		final SoccerPitchParams params = injector.get(Parameters.class).getContainer(SOCCERPITCH);
		return params.getFieldHeight();
	}

	public void eval(String script) {
		injector.get(Evaluator.class).eval(parser.parse(script));
	}

	public void start() {

		injector.get(RandomGenerator.class).setSeed(0);

		final Team red = injector.get("red.team", Team.class);
		final Team blue = injector.get("blue.team", Team.class);

		gameBuilderListener.onCreated(red);
		gameBuilderListener.onCreated(blue);
		gameBuilderListener.onCreated(injector.get(Referee.class));
		gameBuilderListener.onCreated(injector.get(Ball.class));
		gameBuilderListener.onCreated(injector.get(PlayRegions.class));

		timer.start();
	}

	public boolean readForNextFrame() {
		return timer.readyForNextFrame();
	}

	public void update() {
		// if (timer.readyForNextFrame())
		{
			if (gameListener != null)
				gameListener.onUpdateStart();

			injector.get(UpdateManager.class).update();

			if (gameListener != null)
				gameListener.onUpdateEnd();
		}
	}

	public void checkin(final TelegramCheckin handlers) {
		injector.get(Telegraph.class).checking(handlers);
	}
}
