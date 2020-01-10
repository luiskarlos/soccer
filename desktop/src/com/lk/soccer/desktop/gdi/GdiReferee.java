package com.lk.soccer.desktop.gdi;

import com.lk.engine.common.core.Region;
import com.lk.engine.common.core.Renderable;
import com.lk.engine.common.d2.Wall2D;
import com.lk.engine.soccer.elements.referee.Referee;

public class GdiReferee implements Renderable {
	private final Referee referee;
	private final Cgdi gdi;

	public GdiReferee(Cgdi cgdi, Referee referee) {
		this.referee = referee;
		this.gdi = cgdi;
	}

	@Override
	public boolean render() {
		// draw the grass
		gdi.darkGreenPen();
		gdi.darkGreenBrush();

		int xClient = referee.playingArea().getX();
		int yClient = referee.playingArea().getY();

		gdi.rect(0, 0, xClient, yClient);

		// render the goals
		gdi.hollowBrush();
		gdi.redPen();

		final Region playingArea = referee.playingArea().getArea();
		gdi.rect(playingArea.left(), (yClient - referee.blueTeam().goal().getWidth()) / 2, playingArea.left() + 40, yClient
		    - (yClient - referee.blueTeam().goal().getWidth()) / 2);

		gdi.bluePen();
		gdi.rect(playingArea.right(), (yClient - referee.blueTeam().goal().getWidth()) / 2, playingArea.right() - 40,
		    yClient - (yClient - referee.blueTeam().goal().getWidth()) / 2);

		// render the pitch markings
		gdi.whitePen();
		gdi.circle(playingArea.center(), playingArea.width() * 0.125);
		gdi.line(playingArea.center().x(), playingArea.top(), playingArea.center().x(), playingArea.bottom());
		gdi.whiteBrush();
		gdi.circle(playingArea.center(), 2.0);

		// the ball
		gdi.whitePen();
		gdi.whiteBrush();

		// render the walls
		gdi.whitePen();
		for (final Wall2D w : referee.getMarkLines().getLineMarks()) {
			new GdiWall2D(gdi, w).render();
		}/**/

		// show the score
		gdi.textColor(Cgdi.red);
		gdi.textAtPos((xClient / 2) - 50, yClient - 18, "Red: " + referee.blueTeam().goal().numGoalsScored());

		gdi.textColor(Cgdi.blue);
		gdi.textAtPos((xClient / 2) + 10, yClient - 18, "Blue: " + referee.redTeam().goal().numGoalsScored());

		return true;
	}
}
