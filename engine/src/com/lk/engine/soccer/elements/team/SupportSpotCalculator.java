/**
 *  Desc:   Class to determine the best spots for a suppoting soccer
 *          player to move to.
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.team;

import static com.lk.engine.common.d2.Vector2D.vec2DDistance;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.console.params.TeamParams;
import com.lk.engine.common.core.Region;
import com.lk.engine.common.d2.UVector2D;
import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.RandomGenerator;
import com.lk.engine.common.time.Regulator;
import com.lk.engine.soccer.elements.FieldPlayingArea;
import com.lk.engine.soccer.elements.team.Team.TeamColor;

//------------------------------------------------------------------------
public class SupportSpotCalculator {
	// a data structure to hold the values and positions of each spot
	public class SupportSpot {
		Vector2D pos;
		double score;

		SupportSpot(final UVector2D pos, final double value) {
			this.pos = new Vector2D(pos);
			this.score = value;
		}

		public UVector2D pos() {
			return pos;
		}

		public double score() {
			return score;
		}
	}

	private final Team team;
	private final List<SupportSpot> spots = new ArrayList<SupportSpot>(50);
	// a pointer to the highest valued spot from the last update
	private SupportSpot bestSupportingSpot;
	// this will regulate how often the spots are calculated (default is
	// one update per second)
	private final Regulator regulator;
	private final TeamParams params;

	public SupportSpotCalculator(final TeamParams params, final int numX, final int numY, final Team team,
	    RandomGenerator random, final FieldPlayingArea playingArea) {
		this.team = team;
		this.params = params;
		final Region playingField = playingArea.getArea();

		// calculate the positions of each sweet spot, create them and
		// store them in spots
		final double heightOfSSRegion = playingField.height() * 0.8;
		final double widthOfSSRegion = playingField.width() * 0.9;
		final double sliceX = widthOfSSRegion / numX;
		final double sliceY = heightOfSSRegion / numY;

		final double left = playingField.left() + (playingField.width() - widthOfSSRegion) / 2.0 + sliceX / 2.0;
		final double right = playingField.right() - (playingField.width() - widthOfSSRegion) / 2.0 - sliceX / 2.0;
		final double top = playingField.top() + (playingField.height() - heightOfSSRegion) / 2.0 + sliceY / 2.0;

		for (int x = 0; x < (numX / 2) - 1; ++x) {
			for (int y = 0; y < numY; ++y) {
				double a = x * sliceX;
				if (team.color() == TeamColor.BLUE) {
					a = left + a;
				} else {
					a = right - a;
				}
				spots.add(new SupportSpot(new Vector2D(a, top + y * sliceY), 0.0));
			}
		}

		// create the regulator
		regulator = new Regulator(random, params.getSupportSpotUpdateFreq());
	}

	/**
	 * this method iterates through each possible spot and calculates its score.
	 * TODO: investigar si se puede hacer mejor getSpotPassSafeScore
	 */
	public UVector2D determineBestSupportingPosition() {
		// only update the spots every few frames
		if (!regulator.isReady() && bestSupportingSpot != null) {
			return bestSupportingSpot.pos;
		}

		// reset the best supporting spot
		bestSupportingSpot = null;

		double bestScoreSoFar = 0.0;

		for (final SupportSpot curSpot : spots) {
			// first remove any previous score. (the score is set to one so that
			// the viewer can see the positions of all the spots if he has the
			// aids turned on)
			curSpot.score = 1.0;

			// Test 1. is it possible to make a safe pass from the ball's position
			// to this position?
			if (team.isPassSafeFromAllOpponents(team.controllingPlayer().pos(), curSpot.pos, null, team.controllingPlayer()
			    .getParams().getMaxPassingForce())) {
				curSpot.score += params.getSpotPassSafeScore();
			}

			// Test 2. Determine if a goal can be scored from this position.
			if (team.canShoot(curSpot.pos, team.controllingPlayer().getParams().getMaxShootingForce())) {
				curSpot.score += params.getSpotCanScoreFromPositionScore();
			}

			// Test 3. calculate how far this spot is away from the controlling
			// player. The further away, the higher the score. Any distances further
			// away than OptimalDistance pixels do not receive a score.
			if (team.supportingPlayer() != null) { // TODO: nema tu byt
																						 // team.ControllingPlayer()??

				final double optimalDistance = 200.0;
				final double dist = vec2DDistance(team.controllingPlayer().pos(), curSpot.pos);
				final double temp = abs(optimalDistance - dist);

				if (temp < optimalDistance) {
					// normalize the distance and add it to the score
					curSpot.score += params.getSpotDistFromControllingPlayerScore() * (optimalDistance - temp) / optimalDistance;
				}
			}

			// check to see if this spot has the highest score so far
			if (curSpot.score > bestScoreSoFar) {
				bestScoreSoFar = curSpot.score;
				bestSupportingSpot = curSpot;
			}
		}

		return bestSupportingSpot.pos;
	}

	public SupportSpot bestSupportingSpot() {
		if (bestSupportingSpot == null) {
			determineBestSupportingPosition();
		}
		return bestSupportingSpot;
	}

	/**
	 * returns the best supporting spot if there is one. If one hasn't been
	 * calculated yet, this method calls DetermineBestSupportingPosition and
	 * returns the result.
	 */
	public UVector2D getBestSupportingSpot() {
		if (bestSupportingSpot != null) {
			return bestSupportingSpot.pos;
		} else {
			return determineBestSupportingPosition();
		}
	}

	public List<SupportSpot> spots() {
		return spots;
	}
}
