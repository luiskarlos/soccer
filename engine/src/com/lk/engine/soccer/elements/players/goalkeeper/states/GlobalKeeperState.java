/**
 * @author Petr (http://www.sallyx.org/)
 */
package com.lk.engine.soccer.elements.players.goalkeeper.states;

import com.lk.engine.common.fsm.StateAdapter;

public class GlobalKeeperState extends StateAdapter {
	public static final String NAME = "GlobalKeeperState";
	
	public GlobalKeeperState() {
		super(NAME);
	}
}