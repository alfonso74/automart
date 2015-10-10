package com.orendel.epl.model;

/**
 * Barcode narrow bar width in dots.  Normal value is {@link #TWO} dots.
 */
public enum BarcodeNarrowBarWidth {
	/** One dot */
	ONE(1),
	/** Two dots (Default value) */
	TWO(2),
	;

	private int width;
	
	private BarcodeNarrowBarWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
}
