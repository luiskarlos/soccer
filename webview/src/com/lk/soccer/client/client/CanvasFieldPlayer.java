package com.lk.soccer.client.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.elements.players.Player;

public class CanvasFieldPlayer implements Renderable {
	private final ImageElement image;

	private final Player<?> player;
	private final double halfW;
	private final double halfH;
	private final Vector2D imageDirection = new Vector2D(0, -1);

	public CanvasFieldPlayer(final ImageElement image, final Player<?> player) {
		this.player = player;
		this.image = image;

		this.halfH = -image.getHeight() / 2;
		this.halfW = -image.getWidth() / 2;
	}

	@Override
	public boolean render(final Context2d context) {
		context.save();

		context.translate(player.pos().x, player.pos().y);
		context.rotate(angle());
		context.drawImage(image, halfW, halfH);

		context.restore();

		return true;
	}

	private double angle() {

		double headingAngle = Math.acos(-player.heading().y);

		headingAngle = player.heading().sign(imageDirection) < 0 ? headingAngle : -headingAngle;
		if (Double.isNaN(headingAngle)) {
			headingAngle = 0;
		}
		return headingAngle;
	}
}
