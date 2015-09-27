package com.orendel.epl.model;

public enum BarcodeType {
	
	Code128(1);
	
	private int code;
	
	private BarcodeType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

}
