package com.lk.engine.soccer.elements;

import com.lk.engine.common.console.params.SoccerPitchParams;
import com.lk.engine.common.core.Region;

public class FieldPlayingArea {
	private final int x;
	private final int y;

	private final Region area;

	public FieldPlayingArea(final SoccerPitchParams params) {
		x = params.getFieldWidth();
		y = params.getFieldHeight();
		area = new Region(params.getBorderWidth(), params.getBorderHeight(), x - params.getBorderWidth(), y
		    - params.getBorderHeight());
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Region getArea() {
		return area;
	}
}
