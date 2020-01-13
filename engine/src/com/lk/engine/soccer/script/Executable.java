package com.lk.engine.soccer.script;

import com.lk.engine.common.misc.Active;

public interface Executable {
	Active execute(Evaluator evaluator, Enviroment enviroment);

}
