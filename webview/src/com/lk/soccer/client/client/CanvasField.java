package com.lk.soccer.client.client;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;

public class CanvasField implements Renderable {
	private final ImageElement field;

	public CanvasField(ImageElement field) {
		this.field = field;
	}

	@Override
	public boolean render(final Context2d context) {
		context.save();

		context.translate(0, 0);
		context.drawImage(field, 0, 0);

		context.restore();

		return true;
	}

}
