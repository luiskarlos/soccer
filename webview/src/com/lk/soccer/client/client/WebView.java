package com.lk.soccer.client.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.lk.engine.soccer.Game;
import com.lk.engine.soccer.GameBuilderListener;
import com.lk.engine.soccer.elements.Ball;
import com.lk.engine.soccer.elements.PlayRegions;
import com.lk.engine.soccer.elements.Referee;
import com.lk.engine.soccer.elements.players.Player;
import com.lk.engine.soccer.elements.players.fieldplayer.FieldPlayer;
import com.lk.engine.soccer.elements.players.goalkeeper.Goalkeeper;
import com.lk.engine.soccer.elements.team.Team;
import com.lk.engine.soccer.elements.team.Team.TeamColor;
import com.lk.soccer.client.client.images.Images;
import com.lk.soccer.client.client.script.Scripts;
import com.lk.soccer.client.common.telegrams.TelegramBuilder;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebView implements EntryPoint {
	private static final String holderId = "canvasholder";
	private static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this demo.";
	private static final int height = 400;
	private static final int width = 700;

	private final List<Renderable> renderables = new ArrayList<Renderable>();

	private final Game game = new Game();
	private Canvas canvas;
	private Canvas backBuffer;
	// canvas size, in px

	private final CssColor redrawColor = CssColor.make("rgb(0, 100, 0)");
	private Context2d context;
	private Context2d backBufferContext;
	private final Images images = Images.INSTANCE;

	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				e.printStackTrace();
				System.out.println(e);
			}
		});

		game.setListener(listener);
		canvas = Canvas.createIfSupported();
		backBuffer = Canvas.createIfSupported();
		if (canvas == null) {
			RootPanel.get(holderId).add(new Label(upgradeMessage));
			return;
		}

		// init the canvases
		canvas.setWidth(game.getWidth() + "px");
		canvas.setHeight(game.getHeight() + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		backBuffer.setCoordinateSpaceWidth(width);
		backBuffer.setCoordinateSpaceHeight(height);
		RootPanel.get(holderId).add(canvas);

		canvas.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent e) {
				game.eval("pass red " + e.getX() + ", " + e.getY());
			}
		});

		context = canvas.getContext2d();
		backBufferContext = backBuffer.getContext2d();

		renderables.add(new CanvasField(getImage(images.field().getSafeUri())));
		final CanvasCoach coach = new CanvasCoach();
		renderables.add(coach);
		
		game.checkin(new TelegramBuilder(coach));
		game.eval(Scripts.INSTANCE.spawnPlayers().getText());
		game.start();

		Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
			@Override
			public boolean execute() {
				game.update();

				backBufferContext.setFillStyle(redrawColor);
				backBufferContext.fillRect(0, 0, width, height);

				for (final Renderable r : renderables) {
					r.render(backBufferContext);
				}

				context.drawImage(backBufferContext.getCanvas(), 0, 0);
				return true;
			}
		}, 12);
	}

	public void addRenderable(Renderable renderable) {
		renderables.add(renderable);
	}

	private ImageElement getImage(SafeUri imageResource) {
		return (ImageElement) new Image(imageResource).getElement().cast();
	}

	GameBuilderListener listener = new GameBuilderListener() {
		@Override
		public void onCreated(PlayRegions playRegions) {
		}

		@Override
		public void onCreated(Referee soccerPitch) {
		}

		@Override
		public void onCreated(Ball ball) {
			renderables.add(new CanvasBall(ball));
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
			ImageElement image = getImage(images.bluePlayer().getSafeUri());
			if (player.team().color() == TeamColor.RED)
				image = getImage(images.redPlayer().getSafeUri());

			renderables.add(new CanvasFieldPlayer(image, player));
		}
	};
}
