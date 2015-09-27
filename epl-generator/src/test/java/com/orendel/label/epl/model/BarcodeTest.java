package com.orendel.label.epl.model;

import org.junit.Test;

import com.orendel.epl.model.Barcode;
import com.orendel.epl.model.BarcodeType;

public class BarcodeTest {
	
	@Test
	public void testGetDataCharsCount_7Numeric() {
		Barcode barcode = new Barcode(BarcodeType.Code128, 2, 65, "1116433");
		int dataCharacters = barcode.getDataCharsCount();
		System.out.println("DC7: " + dataCharacters);
	}
	
	@Test
	public void testGetDataCharsCount_6Numeric() {
		Barcode barcode = new Barcode(BarcodeType.Code128, 2, 65, "111643");
		int dataCharacters = barcode.getDataCharsCount();
		System.out.println("DC6: " + dataCharacters);
	}
	
	@Test
	public void testGetDataCharsCount_3Numeric() {
		Barcode barcode = new Barcode(BarcodeType.Code128, 2, 65, "111");
		int dataCharacters = barcode.getDataCharsCount();
		System.out.println("DC3: " + dataCharacters);
	}
	
	@Test
	public void testGetDataCharsCount_EmptyString() {
		Barcode barcode = new Barcode(BarcodeType.Code128, 2, 65, "");
		int dataCharacters = barcode.getDataCharsCount();
		System.out.println("DC0: " + dataCharacters);
	}

}
