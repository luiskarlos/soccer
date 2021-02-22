package com.lk.engine.soccer.elements;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.d2.Wall2D;

public class FieldMarkLines {
	private final List<Wall2D> lineMarks = new ArrayList<Wall2D>(10);

	public FieldMarkLines(FieldPlayingArea playingArea, Goal redGoal, Goal blueGoal) {
		final Vector2D topLeft = new Vector2D(playingArea.getArea().left(), playingArea.getArea().top());
		final Vector2D topRight = new Vector2D(playingArea.getArea().right(), playingArea.getArea().top());
		final Vector2D bottomRight = new Vector2D(playingArea.getArea().right(), playingArea.getArea().bottom());
		final Vector2D bottomLeft = new Vector2D(playingArea.getArea().left(), playingArea.getArea().bottom());

		lineMarks.add(new Wall2D(bottomLeft, redGoal.rightPost()));
		lineMarks.add(new Wall2D(redGoal.leftPost(), topLeft));
		lineMarks.add(new Wall2D(topLeft, topRight));
		lineMarks.add(new Wall2D(topRight, blueGoal.leftPost()));
		lineMarks.add(new Wall2D(blueGoal.rightPost(), bottomRight));
		lineMarks.add(new Wall2D(bottomRight, bottomLeft));
	}

	public List<Wall2D> getLineMarks() {
		return lineMarks;
	}
}
