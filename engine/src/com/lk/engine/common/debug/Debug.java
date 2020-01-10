package com.lk.engine.common.debug;

import com.lk.engine.common.d2.Vector2D;

public interface Debug {
	
	Debug put(String key, Debuggable debuggable);
	
	Debug put(String key, String value);
	
	Debug put(String key, double value);
	
	Debug put(String key, int value);
	
	Debug openArray(String key);
	
	Debug addToArray(Debuggable values);

	Debug addToArray(String values);
	
	Debug addToArray(double values);
	
	Debug closeArray();

	Debug put(String key, Vector2D vector);

	
}
