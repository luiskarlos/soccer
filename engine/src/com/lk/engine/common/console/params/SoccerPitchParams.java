package com.lk.engine.common.console.params;

public class SoccerPitchParams {
	private boolean drawRegions = true;
	private int fieldWidth = 700;
	private int fieldHeight = 400;
	private int borderWidth = 20;
	private int borderHeight = 20;

	public boolean isDrawRegions() {
		return drawRegions;
	}

	public void setDrawRegions(boolean drawRegions) {
		this.drawRegions = drawRegions;
	}

	public int getFieldWidth() {
		return fieldWidth;
	}

	public void setFieldWidth(int fieldWidth) {
		this.fieldWidth = fieldWidth;
	}

	public int getFieldHeight() {
		return fieldHeight;
	}

	public void setFieldHeight(int fieldHeight) {
		this.fieldHeight = fieldHeight;
	}

	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getBorderHeight() {
		return borderHeight;
	}

	public void setBorderHeight(int borderHeight) {
		this.borderHeight = borderHeight;
	}
}
