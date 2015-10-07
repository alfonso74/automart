package com.orendel.epl.model;

public enum FontSize {
	/** 20.3 cpi */
	ONE(1, 20.3),
	/** 16.9 cpi */
	TWO(2, 16.9),
	/** 14.5 cpi */
	THREE(3, 14.5),
	/** 12.7 cpi */
	FOUR(4, 12.7),
	/** 5.6 cpi */
	FIVE(5, 5.6),
	;
	
	private int code;
	private double cpi;
	
	private FontSize(int code, double cpi) {
		this.code = code;
		this.cpi = cpi;
	}
	
	public int getCode() {
		return code;
	}
	
	public double getCPI() {
		return cpi;
	}

}
