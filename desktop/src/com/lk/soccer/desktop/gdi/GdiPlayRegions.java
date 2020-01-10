package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.soccer.elements.PlayRegions;

public class GdiPlayRegions implements Renderable {
	private final PlayRegions playRegions;
	private final Cgdi gdi;

	public GdiPlayRegions(Cgdi cgdi, PlayRegions playRegions) {
		this.playRegions = playRegions;
		this.gdi = cgdi;
	}

	@Override
	public boolean render() {
		// if (soccerPitch.getParams().isDrawRegions())
		// render regions
		for (int r = 0; r < playRegions.getRegions().size(); ++r) {
			new GdiRegion(gdi, playRegions.getRegions().get(r)).render();
		}

		return true;
	}
}
