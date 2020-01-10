package com.lk.soccer.desktop.view;

public interface Decorable {
	enum Decorator {
		HIGHLIGHT, SELECT,
	};

	void add(Decorator... decorator);

	void remove(Decorator... decorator);
}
