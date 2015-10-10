package com.orendel.epl.model;

public class Barcode implements LabelElement {
	
	private BarcodeType type;
	private int narrowBarWidth;
	private int height;
	private String content;
	
	private int xPosition;
	private int yPosition;
	
	
	public Barcode(BarcodeType type, BarcodeNarrowBarWidth narrowBarWidth, int height, String content) {
		this.type = type;
		this.narrowBarWidth = narrowBarWidth.getWidth();
		this.height = height;
		this.content = content;
	}
	
	// helper methods
	
	public String getEpl() {
		StringBuilder sb = new StringBuilder();
		sb.append("B" + this.getxPosition() + ',');
		sb.append(this.getyPosition() + ",");
		sb.append("0,");
		sb.append(type.getCode() + ",");
		sb.append(narrowBarWidth + ",");
		sb.append("0,");
		sb.append(height + ",");
		sb.append("N,\"");
		sb.append(content + "\"\n");
		return sb.toString();
	}
	
	public BarcodeType getType() {
		return type;
	}
	
	public int getNarrowBarWidth() {
		return narrowBarWidth;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getCode() {
		return content;
	}
	
	public int getDataCharsCount() {
		boolean isNumeric = true;
		for (int n = 0; n < content.length(); n++) {
			if (content.charAt(n) < '0' || content.charAt(n) > '9') {
				isNumeric = false;
				break;
			}
		}
		int charCount = 0;
		if (isNumeric && content.length() > 3) {
			charCount = content.length() / 2;
			if (content.length() % 2 != 0) {
				charCount += 2;
			}
		} else {
			charCount = content.length();
		}
		return charCount;
	}

	@Override
	public int getWidthForDpi(int dpi) {
		int charCount = getDataCharsCount();
		int width = (int) ((11 * charCount + 35) * narrowBarWidth);
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
		return false;
	}

	@Override
	public String getContent() {
		return content;
	}

}
