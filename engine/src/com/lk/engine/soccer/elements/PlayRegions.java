package com.lk.engine.soccer.elements;

import java.util.ArrayList;
import java.util.List;

import com.lk.engine.common.core.Region;

/*
 the playing field is broken up into regions that the team
 can make use of to implement strategies.
 */
public class PlayRegions {
	public static final int NUM_REGIONS_HORIZONTAL = 6;
	public static final int NUM_REGIONS_VETTICAL = 3;

	private final List<Region> regions = new ArrayList<Region>(NUM_REGIONS_HORIZONTAL*NUM_REGIONS_VETTICAL);

	public PlayRegions(final FieldPlayingArea playingArea) {
		createRegions(playingArea, playingArea.getArea().width() / NUM_REGIONS_HORIZONTAL, playingArea.getArea().height()
		    / NUM_REGIONS_VETTICAL);
	}

	private void createRegions(final FieldPlayingArea playingArea, final double width, final double height) {
		// index into the vector
		int idx = NUM_REGIONS_HORIZONTAL * NUM_REGIONS_VETTICAL - 1;
		for (int col = 0; col < NUM_REGIONS_HORIZONTAL; ++col) {
			for (int row = 0; row < NUM_REGIONS_VETTICAL; ++row) {
				regions.add(0, new Region(playingArea.getArea().left() + col * width, playingArea.getArea().top() + row
				    * height, playingArea.getArea().left() + (col + 1) * width, playingArea.getArea().top() + (row + 1)
				    * height, idx));
				--idx;
			}
		}
	}

	public Region get(final int index) {
		return regions.get(index);
	}

	public List<Region> getRegions() {
		return regions;
	}
}
