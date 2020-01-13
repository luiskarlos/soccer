/**
 *  Desc:   A SoccerPitch is the main game object. It owns instances of
 *          two soccer teams, two goals, the playing area, the ball
 *          etc. This is the root class for all the game updates and
 *          renders etc
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements;

import com.lk.engine.common.core.Updatable;
import com.lk.engine.common.misc.Active;
import com.lk.engine.soccer.console.params.SoccerPitchParams;
import com.lk.engine.soccer.elements.team.Team;

public class Referee implements Updatable {
	private final FieldPlayingArea fieldPlayingArea;
	private final FieldMarkLines markLines;

	private final Ball ball;
	private final Team redTeam;
	private final Team blueTeam;
	// true if a goal keeper has possession
	private boolean goalKeeperHasBall;
	// true if the game is in play. Set to false whenever the players
	// are getting ready for kickoff
	private boolean gameOn = false;
	// set true to pause the motion
	private boolean paused;

	private final SoccerPitchParams params;

	public Referee(final SoccerPitchParams params, final FieldPlayingArea fieldPlayingArea,
	    final FieldMarkLines markLines, final Ball ball, final Team blue, final Team red) {
		this.params = params;
		this.fieldPlayingArea = fieldPlayingArea;
		this.markLines = markLines;
		this.ball = ball;
		this.blueTeam = blue;
		this.redTeam = red;

		// make sure each team knows who their opponents are
		redTeam.setOpponents(blueTeam);
		blueTeam.setOpponents(redTeam);
	}

	/**
	 * this demo works on a fixed frame rate (60 by default) so we don't need to
	 * pass a time_elapsed as a parameter to the game entities
	 */
	@Override
	public Active update() {
		// if a goal has been detected reset the pitch ready for kickoff
		// TODO: cuando anota
		/*
		 * if (blueTeam.goal().scored(ball) || redTeam.goal().scored(ball)) { gameOn
		 * = false;
		 * 
		 * // reset the ball ball.placeAtPosition(new Vector2D(xClient / 2.0,
		 * yClient / 2.0));
		 * 
		 * // get the teams ready for kickoff
		 * redTeam.getFSM().changeTo(PrepareForKickOff.class);
		 * blueTeam.getFSM().changeTo(PrepareForKickOff.class);
		 * 
		 * messageDispatcher.dispatchMsg(SEND_MSG_IMMEDIATELY, SENDER_ID_IRRELEVANT,
		 * ALL, Message.GO_HOME, null); }/*
		 */

		return Active.Yes;
	}

	public void togglePause() {
		paused = !paused;
	}

	public boolean paused() {
		return paused;
	}

	public boolean goalKeeperHasBall() {
		return goalKeeperHasBall;
	}

	public void setGoalKeeperHasBall(final boolean b) {
		goalKeeperHasBall = b;
	}

	public Ball ball() {
		return ball;
	}

	public boolean gameOn() {
		return gameOn;
	}

	public void setGameOn() {
		gameOn = true;
	}

	public void setGameOff() {
		gameOn = false;
	}

	public Team blueTeam() {
		return blueTeam;
	}

	public Team redTeam() {
		return redTeam;
	}

	public SoccerPitchParams getParams() {
		return params;
	}

	public FieldPlayingArea playingArea() {
		return fieldPlayingArea;
	}

	public FieldMarkLines getMarkLines() {
		return markLines;
	}
}
