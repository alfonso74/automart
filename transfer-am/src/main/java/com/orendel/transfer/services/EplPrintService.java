package com.orendel.transfer.services;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

import com.orendel.epl.model.Barcode;
import com.orendel.epl.model.BarcodeNarrowBarWidth;
import com.orendel.epl.model.BarcodeType;
import com.orendel.epl.model.FontSize;
import com.orendel.epl.model.Label;
import com.orendel.epl.model.LabelElement;
import com.orendel.epl.model.OverflowMode;
import com.orendel.epl.model.TextLine;
import com.orendel.transfer.config.AppConfig;
import com.orendel.transfer.exceptions.ApplicationRuntimeException;


public class EplPrintService {	
	
	public void printAutomartLabel(int width, String barCode, boolean reduceBarcodeBarsWidth, String description, String additionalText, int labelsToPrint) {
		String printerIpAddress = AppConfig.INSTANCE.getValue("label.printer.ipaddress");
		String printerPort = AppConfig.INSTANCE.getValue("label.printer.port");
		
		try {
			Socket clientSocket=new Socket(printerIpAddress, Integer.parseInt(printerPort));
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

			
			BarcodeNarrowBarWidth nbWidth = reduceBarcodeBarsWidth ? BarcodeNarrowBarWidth.ONE : BarcodeNarrowBarWidth.TWO;
			String eplCommands = getDynamicEpl(width, barCode,  nbWidth, description, additionalText, labelsToPrint);
			outToServer.write(eplCommands.getBytes(Charset.forName("437")));  // la Eltron utiliza el codepage 437

			outToServer.close();	    
			clientSocket.close();
		} catch (IOException e) {
			throw new ApplicationRuntimeException("Error al intentar imprimir la etiqueta: '" + e.getLocalizedMessage() + "'.", e);
		}
	}
	
	
	/**
	 * Creates an EPL command sequence using the epl-generator objects (and using some parameters).
	 * @param width the label width (in points per inches)
	 * @param barCode the barcode to be printed
	 * @param nbWidth
	 * @param description the barcode description
	 * @param additionalText some additional text
	 * @param labelsToPrint number of label sets to print
	 * @return the EPL command sequence for raw printing
	 */
	public String getDynamicEpl(int width, String barCode, BarcodeNarrowBarWidth nbWidth, String description, String additionalText, int labelsToPrint) {
		Label label = new Label(width, 250);
		LabelElement line01 = TextLine.create(FontSize.FIVE, barCode);
		LabelElement barcode = new Barcode(BarcodeType.Code128, nbWidth, 65, barCode);
		LabelElement line03 = TextLine.create(FontSize.THREE, description);
		LabelElement line04 = TextLine.create(FontSize.THREE, additionalText);
		label.addElementCentered(line01, 30, OverflowMode.RESIZE);
		label.addElementCentered(barcode, 95);
		label.addElementCentered(line03, 170);
		label.addElementCentered(line04, 215);
		String epl = label.getEpl(labelsToPrint);
		return epl;
	}

}
