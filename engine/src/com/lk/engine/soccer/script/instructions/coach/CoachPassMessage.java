package com.lk.engine.soccer.script.instructions.coach;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.telegraph.Message;
import com.lk.engine.common.telegraph.TelegramPackage;
import com.lk.engine.soccer.elements.coach.message.PassInstruction;
import com.lk.engine.soccer.script.Enviroment;
import com.lk.engine.soccer.script.Evaluator;
import com.lk.engine.soccer.script.Executable;
import com.lk.engine.soccer.script.instructions.Instruction;

public class CoachPassMessage extends Instruction implements Executable {
	private final PassInstruction passInstruction;

	public CoachPassMessage(final String name, final Vector2D pos) {
		super(name);
		passInstruction = new PassInstruction(name, pos);
	}

	@Override
	public Active execute(Evaluator evaluator, Enviroment enviroment) {
		enviroment.getMessageDispatcher().post(new TelegramPackage(Message.COACH_PASS_INSTRUCTION, passInstruction));
		return Active.No;
	}

	@Override
	public String toString() {
		return "pass " + passInstruction.getTeamName() + " " + passInstruction.getPos().toString();
	}
}
