package com.orendel.transfer.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.orendel.common.config.AppConfig;
import com.orendel.delivery.domain.TransferControl;
import com.orendel.delivery.domain.TransferControlLine;
import com.orendel.transfer.export.csv.ExcelCSVPrinter;


public class CsvExportService {
	
	private String csvExportDir;
	
	public CsvExportService() {
		this.csvExportDir = AppConfig.INSTANCE.getValue("csv.export.directory");;
	}
	
	public CsvExportService(String csvExportDir) {
		this.csvExportDir = csvExportDir;
	}
	
	public String createCsvFile(TransferControl tcControl, String csvFileName) throws IOException {
		String csvExportPath = getExportPath(csvFileName);
		
		OutputStream out = new FileOutputStream(csvExportPath);
		ExcelCSVPrinter csv = new ExcelCSVPrinter(out);

		String[] linea = new String[2];
		for (TransferControlLine line : tcControl.getLines()) {
			linea[0] = line.getItemNumber();
			linea[1] = line.getQtyReceived().setScale(0).toString();
			csv.writeln(linea);
		}
		csv.close();
		out.close();
		
		return csvExportPath;
	}
	
	public String getExportPath(String csvFileName) {
		String csvExportPath = "error.csv";
		if (csvExportDir == null || csvExportDir.isEmpty()) {
			csvExportPath = csvFileName;
		} else {
			System.out.println("Last '" + File.separator + "' for current export dir: " + csvExportDir.
					lastIndexOf(File.separator) + ", size: " + csvExportDir.length());
			if (csvExportDir.lastIndexOf(File.separator) == (csvExportDir.length() - 1)) {
				csvExportDir = csvExportDir.substring(0, csvExportDir.length() - 1);
			}
			csvExportPath = csvExportDir + File.separator + csvFileName;
		}
		
		return csvExportPath;
	}

}
