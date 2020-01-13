package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.common.d2.Wall2D;

public class GdiWall2D implements Renderable {
	private final Wall2D wall2D;
	private final Cgdi gdi;

	public GdiWall2D(Cgdi cgdi, Wall2D wall2D) {
		this.wall2D = wall2D;
		this.gdi = cgdi;
	}

	public boolean render() {
		gdi.line(wall2D.from(), wall2D.to());

		// render the normals if rqd
		if (true) // renderNormals)
		{
			final int MidX = (int) ((wall2D.from().x + wall2D.to().x) / 2);
			final int MidY = (int) ((wall2D.from().y + wall2D.to().y) / 2);

			gdi.line(MidX, MidY, (int) (MidX + (wall2D.normal().x * 5)), (int) (MidY + (wall2D.normal().y * 5)));
		}

		return true;
	}
}
