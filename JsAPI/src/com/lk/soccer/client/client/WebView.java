package com.lk.soccer.client.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.lk.engine.common.core.MovingEntity;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.script.instructions.coach.CoachPassMessage;
import com.lk.engine.common.script.js.JsActions;
import com.lk.engine.soccer.Game;
import com.lk.engine.soccer.GameBuilderListener;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.referee.Referee;
import com.lk.engine.soccer.elements.team.Team;
import jsinterop.annotations.JsMethod;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebView implements EntryPoint {
	private static final int width = 700;
	private static final int height = 400;
	private final Map<String, MovingEntity<?>> entities = new HashMap<>();

	private final Game game = new Game();
	private final Logger logger = Logger.getLogger("Game");

	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(e -> logger.log(Level.SEVERE, e.getMessage(), e));
		game.setListener(jsListener);
		//loadScript(0,"webview/spawn-players.js");
	}

	private void loadScript(final int pos, final String ... lib) {
		ScriptInjector
				.fromUrl(lib[pos])
				.setCallback(new Callback<Void, Exception>() {

				  public void onFailure(Exception reason) {
              logger.log(Level.SEVERE, "Error loading /js/" + lib[pos], reason);
            }

            public void onSuccess(Void result) {
              if (pos+1 < lib.length) {
                loadScript(pos+1, lib);
              } else {
                registerEngine();
                addConsole();
                game.start();
              }

              logger.log(Level.SEVERE, "Loaded /js/" + lib[pos]);
            }
        })
        .inject();
	}

	public void click(int posx, int posy) {
		logger.log(Level.SEVERE, "Click " + posx + " " + posy);
		game.eval(new CoachPassMessage("red", posx, posy));
	}

	@JsMethod
	public JsActions actions() {
		return game.actions();
  }

	public native void registerEngine() /*-{
		var that = this;
		$doc.actions = that.@com.lk.soccer.client.client.WebView::actions()();
		debugger;
		$doc.engine = $doc.engine || $wnd["engine"] || {};

		$doc.gameSpawnPlayers($doc.actions);

		$doc.engine.update = $entry(function() {
			that.@com.lk.soccer.client.client.WebView::update()();
		});

		$doc.engine.click = $entry(function(x, y) {
			that.@com.lk.soccer.client.client.WebView::click(II)(x, y);
		});

 	}-*/;

	private native void onUpdate(String entity, double x, double y, double angle, double speed)/*-{
	  $doc.engine.updatePosition(entity, x, y, angle, speed);
	}-*/;

	public void update() {
		game.update(0, 0);
		for (Entry<String, MovingEntity<?>> set : entities.entrySet()) {
	    onUpdate(
        set.getKey(),
			  set.getValue().pos().x(),
				set.getValue().pos().y(),
				angle(set.getValue()),
        set.getValue().speed()
			);
    }
	}

	private final Vector2D imageDirection = new Vector2D(0, -1);
	private double angle(MovingEntity<?> entity) {
		double headingAngle = Math.acos(-entity.heading().y());
		headingAngle = entity.heading().sign(imageDirection) < 0 ? headingAngle : -headingAngle;
		if (Double.isNaN(headingAngle)) {
			headingAngle = 0;
		}
		return headingAngle;
	}

	GameBuilderListener listener = new GameBuilderListener() {
		@Override
		public void onCreated(PlayRegions playRegions) {
			logger.log(Level.SEVERE, "PlayRegions: ");
		}

		@Override
		public void onCreated(Referee soccerPitch) {
		}

		@Override
		public void onCreated(Ball ball) {
			entities.put("ball", ball);
		}

		@Override
		public void onCreated(FieldPlayer fieldPlayer) {
			addPlayer(fieldPlayer);
		}

		@Override
		public void onCreated(Goalkeeper goalkeeper) {
			addPlayer(goalkeeper);
		}

		@Override
		public void onCreated(Team soccerTeam) {
		}

		private void addPlayer(Player<?> player) {
			logger.log(Level.SEVERE, "Player loaded..." + player.getName());
			entities.put(player.getName(), player);
		}
	};

	GameBuilderListener jsListener = new GameBuilderListener() {
		@Override
		public void onCreated(PlayRegions playRegions) {
			logger.log(Level.SEVERE, "PlayRegions:");
		}

		@Override
		public void onCreated(Referee soccerPitch) {
		}

		@Override
		public void onCreated(Ball ball) {
			entities.put("ball", ball);
			jsAddBall(ball.pos().x(), ball.pos().x(), angle(ball));
		}

		private native void jsAddBall(double x, double y, double angle)/*-{
      $doc.engine.addBall(x, y, angle);
    }-*/;

		@Override
		public void onCreated(FieldPlayer player) {
			entities.put(player.getName(), player);
			jsAddPlayer(player.team().name(), player.getName(), player.pos().x(), player.pos().y(), angle(player));
		}

		private native void jsAddPlayer(String team, String entity, double x, double y, double angle)/*-{
      $doc.engine.addPlayer(team, entity, x, y, angle);
    }-*/;

		@Override
		public void onCreated(Goalkeeper player) {
			entities.put(player.getName(), player);
			jsAddGoalkeeper(player.team().name(), player.getName(), player.pos().x(), player.pos().y(), angle(player));
		}

		private native void jsAddGoalkeeper(String team, String entity, double x, double y, double angle)/*-{
      $doc.engine.addGoalkeeper(team, entity, x, y, angle);
    }-*/;

		@Override
		public void onCreated(Team soccerTeam) {
		}
	};

	private void addConsole() {
    // Let's make an 80x50 text area to go along with the other two.
    final TextArea ta = TextArea.wrap(Document.get().getElementById("console"));

    addButton("eval", event -> game.eval(ta.getText()));
    addButton("fsm", event -> game.eval("fsm.status"));
    addButton("start", event -> game.eval("change referee to referee.superviseGame"));
	}

	private void addButton(String name, ClickHandler handler) {
        Button btn = Button.wrap(Document.get().getElementById(name));
        btn.addClickHandler(handler);
	}
}
