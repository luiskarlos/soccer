package com.lk.engine.common.script.instructions;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lk.engine.common.console.ParamAccessObject;
import com.lk.engine.common.console.ParamHandler;
import com.lk.engine.common.fsm.StateMachineOwner;
import com.lk.engine.common.misc.Active;
import com.lk.engine.common.script.Environment;
import com.lk.engine.common.script.Evaluator;
import com.lk.engine.soccer.elements.players.Player;

public class Change extends InstructionTarget {
	private static Logger logger = Logger.getLogger("Change");
	
	public Change(final String name, final String state) {
		super(name, state);
	}

	@Override
	public Active execute(final Evaluator evaluator, final Environment enviroment) {
		final String state = getTarget(enviroment);
		final List<Player<?>> players = enviroment.getPlayers(getElement(enviroment));

		if (players.isEmpty()) {
			StateMachineOwner owner = enviroment.getTeam(getElement(enviroment));
			if (owner != null)
				owner.getFSM().changeTo(state, getOnExit(), null);
			else {//Migrar todo a esta forma.
				final ParamHandler var = enviroment.getVariable(getElement(enviroment));
				if (var != null && var.getParamAccess() instanceof ParamAccessObject) {
					final Object obj = ((ParamAccessObject)var.getParamAccess()).getObject();
					if (obj instanceof StateMachineOwner) {
						((StateMachineOwner) obj).getFSM().changeTo(state);
					}
				} else {
					logger.log(Level.SEVERE, "StateMachineOwner not found - " + getElement(enviroment));
				}				
			}
		} else {
			for (Player<?> p : players) {
				p.getFSM().changeTo(state, getOnExit(), null);/**/
			}
		}

		return Active.No;
	}

	@Override
	public String toString() {
		return "Change " + super.toString();
	}
}
