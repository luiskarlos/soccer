package com.lk.engine.common.debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lk.engine.common.d2.Vector2D;
import com.lk.engine.common.misc.NumUtils;

public class JsonDebug implements Debug {
	
	private static final Item NULL = new ItemString("null");
	
	private ItemObject root = new ItemObject();
	private ItemArray array;

	public void toString(StringBuilder buffer) {
		root.toString(buffer);
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		root.toString(buffer);
		return buffer.toString();
	}
	
	@Override
  public Debug put(String key, Debuggable serializable) {
	  root.put(key, toJson(serializable));
	  return this;
  }

	@Override
  public Debug put(String key, String value) {
		root.put(key, toJson(value));
		return this;
  }
	
	@Override
  public Debug put(String key, int value) {
		root.put(key, toJson(value));
		return this;
  }
	
	@Override
  public Debug put(String key, double value) {
		root.put(key, toJson(value));
		return this;
  }
	
	@Override
	public Debug openArray(String key) {
		array = new ItemArray();
		root.put(key, array);
		return this;
	}
	@Override
  public Debug addToArray(Debuggable value) {
    array.addItem(toJson(value));
		return this;
  }
	
	@Override
  public Debug addToArray(String value) {
    array.addItem(toJson(value));
		return this;
  }
	
	@Override
  public Debug addToArray(double value) {
    array.addItem(toJson(value));
		return this;
  }
	
	@Override
  public Debug closeArray() {
    array = null;
		return this;
  }
	
	private Item toJson(Debuggable serializable) {
		if (serializable != null) {
			JsonDebug json = new JsonDebug();
			serializable.debug(json);
			return json.root;
		} else {
			return NULL;
		}	  
  }
	
	private Item toJson(String value) {
	  return new ItemString(value);
  }
	
	private Item toJson(int value) {
		return new ItemNumber(value+"");
  }
	
	private Item toJson(double value) {
		return new ItemNumber(NumUtils.toString(value, 2));
  }

	@Override
  public Debug put(String key, Vector2D vector) {
		final ItemArray itemArray = new ItemArray();
		if (vector != null) {
			itemArray.addItem(toJson(vector.x)).addItem(toJson(vector.y));
		}
		root.put(key, itemArray);
	  return this;
  }
}

interface Item {
	void toString(StringBuilder buffer);
}

class ItemObject implements Item {
	private List<String> order = new ArrayList<String>();
  private Map<String, Item> properties = new HashMap<String, Item>();
	
	public void put(String key, Item value) {
		order.add(key);
		properties.put(key, value);
	}

	@Override
  public void toString(StringBuilder buffer) {
	  buffer.append('{');
	  for (String key : order) {
	    buffer.append('"').append(key).append('"').append(':');
	    properties.get(key).toString(buffer);
	    buffer.append(',');
    }
	  if (order.size() > 0) {
	  	buffer.setLength(buffer.length()-1);
	  }
	  buffer.append('}');
  }
}

class ItemString implements Item {
	private String value;
	
	public ItemString(final String value) {
		this.value = value;
	}
	
	@Override
  public void toString(StringBuilder buffer) {
		buffer.append('"').append(value).append('"');
	}
}

class ItemNumber implements Item {
	private String value;
	
	public ItemNumber(final String value) {
		this.value = value;
	}
	
	@Override
  public void toString(StringBuilder buffer) {
		buffer.append(value);
	}
}
class ItemArray implements Item {
	private List<Item> value = new ArrayList<Item>();
	
	public ItemArray() {
	}
	
	public ItemArray addItem(Item item) {
		value.add(item);
		return this;
	}
	
	@Override
  public void toString(StringBuilder buffer) {
		buffer.append('[');
	  for (Item item : value) {
	  	item.toString(buffer);
	    buffer.append(',');
    }
	  if (value.size() > 0) {
	  	buffer.setLength(buffer.length()-1);
	  }
	  buffer.append(']');
	}
}