package com.orendel.epl.model;

public class Barcode implements LabelElement {
	
	private BarcodeType type;
	private int narrowBarWidth;
	private int height;
	private String code;
	
	private int xPosition;
	private int yPosition;
//	private int width;
	
	
	public Barcode(BarcodeType type, int narrowBarWidth, int height, String code) {
		this.type = type;
		this.narrowBarWidth = narrowBarWidth;
		this.height = height;
		this.code = code;
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
		sb.append(code + "\"\n");
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
		return code;
	}
	
	public int getDataCharsCount() {
		boolean isNumeric = true;
		for (int n = 0; n < code.length(); n++) {
			if (code.charAt(n) < '0' || code.charAt(n) > '9') {
				isNumeric = false;
				break;
			}
		}
		int charCount = 0;
		if (isNumeric && code.length() > 3) {
			charCount = code.length() / 2;
			if (code.length() % 2 != 0) {
				charCount += 2;
			}
		} else {
			charCount = code.length();
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

}
