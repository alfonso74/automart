package com.orendel.epl.model;

public class TextLine implements LabelElement {
	
	private int xPosition;
	private int yPosition;
//	private int width;
	private FontSize fontSize;
	private String content;
	
	private TextLine(FontSize fontSize, String content) {
		this.fontSize = fontSize;
		this.content = content;
	}
	
	private TextLine(int xPosition, int yPosition, FontSize fontSize, String content) {
		this(fontSize, content);
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	public static LabelElement create(FontSize fontSize, String content) {
		LabelElement line = new TextLine(fontSize, content);
		return line;
	}
	
	// helper methods
	
	public String getEpl() {
		StringBuilder sb = new StringBuilder();
		sb.append("A" + this.getxPosition() + ',');
		sb.append(this.getyPosition() + ",");
		sb.append("0,");
		sb.append(fontSize.getCode() + ",");
		sb.append("1,1,N,\"");
		sb.append(content + "\"\n");
		return sb.toString();
	}

	// getters
	
	public FontSize getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(FontSize fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getWidthForDpi(int dpi) {
		int width = (int) (content.length() / fontSize.getCPI() * dpi);
		return width;
	}

	@Override
	public int getxPosition() {
		return xPosition;
	}

	@Override
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	@Override
	public int getyPosition() {
		return yPosition;
	}

	@Override
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	@Override
	public boolean canBeSplitted() {
		return true;
	}
	
}
