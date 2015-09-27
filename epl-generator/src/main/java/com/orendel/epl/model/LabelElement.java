package com.orendel.epl.model;

public interface LabelElement {

	int getWidthForDpi(int dpi);

	int getxPosition();

	void setxPosition(int xPosition);

	int getyPosition();

	void setyPosition(int yPosition);
	
	String getEpl();

}