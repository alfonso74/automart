package com.orendel.epl.model;

import java.util.ArrayList;
import java.util.List;

public class Label {
	
	private int dpi = 203;
	
	private int width;
	private int height;
	
	private List<LabelElement> elements = new ArrayList<LabelElement>();
	
	
	public Label(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	
	public void addElement(LabelElement element, int xPosition, int yPosition) {
		element.setxPosition(xPosition);
		element.setyPosition(yPosition);
		elements.add(element);
	}
	

	public void addElementCentered(LabelElement element, int yPosition) {
		int xPosition = centerHorizontally(element.getWidthForDpi(dpi));
		element.setxPosition(xPosition);
		element.setyPosition(yPosition);
		elements.add(element);
	}
	
	
	private int centerHorizontally(int lineWidth) {
		int x = (width - lineWidth) / 2;
		return x;
	}
	
	public String getEpl(int numberOfLabelsToPrint) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nN\n");            
	    sb.append("q" + width + "\n");
	    for (LabelElement element : elements) {
	    	sb.append(element.getEpl());
	    }
	    sb.append("\nP" + numberOfLabelsToPrint + ",1\n");
		return sb.toString();
	}
}
