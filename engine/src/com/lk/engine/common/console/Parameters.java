package com.lk.engine.common.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.lk.engine.soccer.elements.team.Team;

public class Parameters {
	private final Map<String, ParamHandler> params;

	public Parameters(final List<ParamHandler> params) {
		this.params = new HashMap<String, ParamHandler>(params.size());
		for (final ParamHandler ph : params) {
			this.params.put(ph.name(), ph);
		}
	}

	public ParamHandler getParamHandler(final String name) {
		return params.get(name);
	}

	public <T> T getContainer(final Class<T> clazz, final String name) {
		if (params.containsKey(name)) {
			return params.get(name).container(clazz);
		} else
			throw new RuntimeException("Container not found: " + name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getContainer(final ParamNames param) {
		return (T) getContainer(param, param.id());
	}

	public <T> T getContainer(final ParamNames param, final Team team, final String postfix) {
		return getContainer(param, paramName(team, postfix));
	}

	public String paramName(final Team team, final String postfix) {
		return team.color().name().toLowerCase() + postfix;
	}

	@SuppressWarnings("unchecked")
	public <T> T getContainer(final ParamNames param, final String name) {
		if (params.containsKey(name)) {
			return (T) params.get(name).container(param.getClazz());
		} else
			throw new RuntimeException("Container not found: " + name);
	}

	public List<String> keys() {
		return new ArrayList<String>(params.keySet());
	}
	
	public Set<Entry<String, ParamHandler>> keysets() {
		return params.entrySet();
	}

	public ParamHandler setVariable(final String name, final ParamHandler newValue) {
		return params.put(name, newValue);
	}
}
