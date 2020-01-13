package com.lk.engine.common.fsm;


public class Idle extends StateAdapter {
	public static final String NAME = "Idle";
	
	private static final Idle idle = new Idle();

	private Idle() {
		super(NAME);
	}

	public static Idle instance() {
		return idle;
	}

}
