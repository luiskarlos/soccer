package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.soccer.elements.team.SupportSpotCalculator;

public class GdiSupportSpotCalculator implements Renderable {
	private final SupportSpotCalculator supportSpotCalculator;
	private final Cgdi gdi;

	public GdiSupportSpotCalculator(Cgdi cgdi, SupportSpotCalculator supportSpotCalculator) {
		this.supportSpotCalculator = supportSpotCalculator;
		this.gdi = cgdi;
	}

	public boolean render() {
		gdi.hollowBrush();
		gdi.greyPen();

		for (int spt = 0; spt < supportSpotCalculator.spots().size(); ++spt) {
			gdi.circle(supportSpotCalculator.spots().get(spt).pos(), supportSpotCalculator.spots().get(spt).score());
		}

		if (supportSpotCalculator.getBestSupportingSpot() != null) {
			gdi.greenPen();
			gdi.circle(supportSpotCalculator.bestSupportingSpot().pos(), supportSpotCalculator.bestSupportingSpot().score());
		}
		return true;
	}
}
