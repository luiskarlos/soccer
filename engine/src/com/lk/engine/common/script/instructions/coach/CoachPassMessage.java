package com.lk.engine.common.script.instructions.coach;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.common.script.instructions.Instruction;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.soccer.elements.coach.message.PassInstruction;

public class CoachPassMessage extends Instruction {
	private final PassInstruction passInstruction;

	public CoachPassMessage(final String team, final double x, final double y) {
		super(team);
		passInstruction = new PassInstruction(team, new Vector2D(x, y));
	}
	
	public CoachPassMessage(final String team, final Vector2D pos) {
		super(team);
		passInstruction = new PassInstruction(team, pos);
	}

	@Override
	public Active execute(Evaluator evaluator, Environment enviroment) {
		enviroment.getMessageDispatcher().post(new TelegramPackage(Message.COACH_PASS_INSTRUCTION, passInstruction));
		return Active.No;
	}

	@Override
	public String toString() {
		return "pass " + passInstruction.getTeamName() + " " + passInstruction.getPos().toString();
	}
}
