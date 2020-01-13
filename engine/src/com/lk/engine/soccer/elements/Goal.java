/**
 * Desc:  class to define a goal for a soccer pitch. The goal is defined
 *        by two 2D vectors representing the left and right posts.
 *
 *        Each time-step the method Scored should be called to determine
 *        if a goal has been scored.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements;

import static com.lk.engine.common.d2.Geometry.lineIntersection2D;
import static com.lk.engine.common.d2.Vector2D.add;
import static com.lk.engine.common.d2.Vector2D.div;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.soccer.console.params.GoalParams;

public class Goal {
	private Vector2D leftPost;
	private Vector2D rightPost;
	// a vector representing the facing direction of the goal
	private Vector2D facing;
	// the position of the center of the goal line
	private Vector2D center;
	// each time Scored() detects a goal this is incremented
	private int numGoalsScored;

	private final GoalParams params;

	public Goal(final GoalParams params, final Vector2D left, final Vector2D right, final Vector2D facing) {
		this.params = params;

		this.leftPost = left;
		this.rightPost = right;
		this.center = div(add(left, right), 2.0);
		this.numGoalsScored = 0;
		this.facing = facing;
	}

	/**
	 * Given the current ball position and the previous ball position, this method
	 * returns true if the ball has crossed the goal line and increments
	 * numGoalsScored
	 */
	public boolean scored(final Ball ball) {
		if (lineIntersection2D(ball.pos(), ball.oldPos(), leftPost, rightPost)) {
			++numGoalsScored;
			return true;
		}
		return false;
	}

	// -----------------------------------------------------accessor methods
	public Vector2D center() {
		return new Vector2D(center);
	}

	public Vector2D facing() {
		return new Vector2D(facing);
	}

	public Vector2D leftPost() {
		return new Vector2D(leftPost);
	}

	public Vector2D rightPost() {
		return new Vector2D(rightPost);
	}

	public int numGoalsScored() {
		return numGoalsScored;
	}

	public void resetGoalsScored() {
		numGoalsScored = 0;
	}

	public GoalParams getParams() {
		return params;
	}

	public double getWidth() {
		return params.getWidth();
	}
}