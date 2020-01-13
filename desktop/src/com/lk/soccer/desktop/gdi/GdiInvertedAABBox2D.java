package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Renderable;
import com.lk.engine.common.d2.InvertedAABBox2D;

public class GdiInvertedAABBox2D implements Renderable {
	private final InvertedAABBox2D invertedAABBox2D;
	private final Cgdi gdi;

	public GdiInvertedAABBox2D(Cgdi cgdi, InvertedAABBox2D invertedAABBox2D) {
		this.invertedAABBox2D = invertedAABBox2D;
		this.gdi = cgdi;
	}

	public boolean render() {
		gdi.line((int) invertedAABBox2D.left(), (int) invertedAABBox2D.top(), (int) invertedAABBox2D.right(),
		    (int) invertedAABBox2D.top());
		gdi.line((int) invertedAABBox2D.left(), (int) invertedAABBox2D.bottom(), (int) invertedAABBox2D.right(),
		    (int) invertedAABBox2D.bottom());
		gdi.line((int) invertedAABBox2D.left(), (int) invertedAABBox2D.top(), (int) invertedAABBox2D.left(),
		    (int) invertedAABBox2D.bottom());
		gdi.line((int) invertedAABBox2D.right(), (int) invertedAABBox2D.top(), (int) invertedAABBox2D.right(),
		    (int) invertedAABBox2D.bottom());

		/*
		 * if (RenderCenter) { gdi.circle(center, 5); }/*
		 */
		return true;
	}
}
