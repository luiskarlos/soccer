package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.core.Renderable;

public class GdiRegion implements Renderable {
	private final Region region;
	private final Cgdi gdi;

	public GdiRegion(Cgdi cgdi, Region region) {
		this.gdi = cgdi;
		this.region = region;
	}

	@Override
	public boolean render() {
		gdi.hollowBrush();
		gdi.greenPen();
		gdi.rect(region.left(), region.top(), region.right(), region.bottom());

		gdi.textColor(Cgdi.green);
		gdi.textAtPos(region.center(), String.valueOf(region.ID()));

		/*
		 * if (showID) {
		 * 
		 * }/*
		 */

		return true;
	}
}
