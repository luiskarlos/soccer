package com.lk.engine.common.console;


public class ParamAccessObject implements ParamAccess {

	private final Object obj;
	
	public ParamAccessObject(Object obj) {
	  this.obj = obj;
  }
	
	@Override
  public void setValue(String val) {
		throw new RuntimeException("ParamObjects can not be change!");
  }

	@Override
  public String getValue() {
	  return obj.toString();
  }
	
	public Object getObject() {
		return obj;
	}
}
