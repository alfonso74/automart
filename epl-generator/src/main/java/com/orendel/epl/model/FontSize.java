package com.orendel.epl.model;

public enum FontSize {
	
	ONE(1, 20.3),
	TWO(2, 16.9),
	THREE(3, 14.5),
	FOUR(4, 12.7),
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
