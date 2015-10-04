package com.orendel.epl.model;

import java.util.ArrayList;
import java.util.Collections;
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
		if (element == null) {
			throw new IllegalArgumentException("The 'element' argument can't be null");
		}
		int xPosition = centerHorizontally(element.getWidthForDpi(dpi));
		System.out.println("X: " + xPosition);
		if (xPosition < 5 && element.canBeSplitted()) {
			List<LabelElement> lines = splitTextElement((TextLine) element);
			System.out.println("Splitted lines: " + lines.size());
			xPosition = centerHorizontally(lines.get(0).getWidthForDpi(dpi));
			for (LabelElement e : lines) {
				e.setxPosition(xPosition);
				e.setyPosition(yPosition);
				yPosition += 20;
			}
			elements.addAll(lines);
		} else {
			element.setxPosition(xPosition);
			element.setyPosition(yPosition);
			elements.add(element);
		}
	}
	
	
	public List<LabelElement> getElements() {
		return Collections.unmodifiableList(elements);
	}
	
	
	private int centerHorizontally(int lineWidth) {
		int x = (width - lineWidth) / 2;
		return x;
	}
	
	private List<LabelElement> splitTextElement(TextLine textElement) {
		List<LabelElement> lines = Collections.emptyList();
		String content = textElement.getContent();
		double cpi = textElement.getFontSize().getCPI();
		
		int maxContentLength = (int) (width / cpi);
		
		lines = splitContent(content, textElement.getFontSize(), maxContentLength);
		return lines;
	}
	
	public List<LabelElement> splitContent(String contentToSplit, FontSize fontSize, int maxContentLength) {
		if (contentToSplit == null) {
			throw new IllegalArgumentException("The 'contentToSplit' argument can't be null");
		}
		List<LabelElement> lines = new ArrayList<LabelElement>();
		if (contentToSplit.length() > maxContentLength) {
			String tmpContent = contentToSplit.substring(0, maxContentLength);
			int lastSpace = tmpContent.lastIndexOf(" ");
			if (lastSpace == -1) {
				lastSpace = maxContentLength;
			}
			LabelElement splittedLine = TextLine.create(fontSize, contentToSplit.substring(0, lastSpace));
			lines.add(splittedLine);
			String remainingContent = contentToSplit.substring(lastSpace).trim();
			List<LabelElement> nextLevelLines = splitContent(remainingContent, fontSize, maxContentLength);
			lines.addAll(nextLevelLines);
		} else {
			LabelElement splittedLine = TextLine.create(fontSize, contentToSplit);
			lines.add(splittedLine);
		}
		return lines;
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
