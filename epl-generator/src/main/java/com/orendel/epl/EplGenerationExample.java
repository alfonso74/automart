package com.orendel.epl;

import com.orendel.epl.model.Barcode;
import com.orendel.epl.model.BarcodeType;
import com.orendel.epl.model.FontSize;
import com.orendel.epl.model.Label;
import com.orendel.epl.model.LabelElement;
import com.orendel.epl.model.TextLine;


public class EplGenerationExample {
	
	/**
	 * Creates an EPL command sequence from a default parameters set
	 * @return
	 */
	public String getStaticEpl() {
		return getStaticEpl(450, "1116433", "REGULADOR VOLTAJE 22S1 12V", "A15H05T02");
	}
	
	/**
	 * Creates an EPL command sequence from a default parameters set (the EPL command will print two
	 * labels).
	 * @return
	 */
	public String getDynamicEpl() {
		return getDynamicEpl(450, "1116433", "REGULADOR ESTÁTICO 22é1 12V", "A15H05T02", 2);
	}
	
	/**
	 * Creates an EPL command sequence from a "burned-in" template (using some parameters).
	 * @param width the label width (in points per inches)
	 * @param barCode the barcode to be printed
	 * @param description the barcode description
	 * @param additionalText some additional text
	 * @return the EPL command sequence for raw printing
	 */
	public String getStaticEpl(int width, String barCode, String description, String additionalText) {
		StringBuilder sb = new StringBuilder();
	    sb.append("\nN\n");            
	    sb.append("q" + width + "\n");
	    sb.append("A102,35,0,5,1,1,N,\"" + barCode + "\"\n");
	    sb.append("B118,100,0,1,2,4,65,B,\"" + barCode + "\"\n");
	    sb.append("A40,180,0,3,1,1,N,\"" + description + "\"\n");
	    sb.append("A158,210,0,3,1,1,N,\"" + additionalText + "\"\n");
	    sb.append("\nP1,1\n");
	    return sb.toString();
	}
	
	/**
	 * Creates an EPL command sequence using the epl-generator objects (and using some parameters).
	 * @param width the label width (in points per inches)
	 * @param barCode the barcode to be printed
	 * @param description the barcode description
	 * @param additionalText some additional text
	 * @param labelsToPrint number of label sets to print
	 * @return the EPL command sequence for raw printing
	 */
	public String getDynamicEpl(int width, String barCode, String description, String additionalText, int labelsToPrint) {
		Label label = new Label(width, 250);
		LabelElement line01 = TextLine.create(FontSize.FIVE, barCode);
		LabelElement barcode = new Barcode(BarcodeType.Code128, 2, 65, barCode);
		LabelElement line03 = TextLine.create(FontSize.THREE, description);
		LabelElement line04 = TextLine.create(FontSize.THREE, additionalText);
		label.addElementCentered(line01, 35);
		label.addElementCentered(barcode, 100);
		label.addElementCentered(line03, 175);
		label.addElementCentered(line04, 210);
		String epl = label.getEpl(labelsToPrint);
		return epl;
	}

}
