package com.lk.engine.common.script;

import com.lk.engine.common.misc.Active;

public interface Executable {
	
	Active execute(final Evaluator evaluator, final Environment enviroment);

}
