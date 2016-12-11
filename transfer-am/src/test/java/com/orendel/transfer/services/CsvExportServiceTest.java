package com.orendel.transfer.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CsvExportServiceTest {
	
	@BeforeClass
	public static void init() {
	}
	
	@Before
	public void setup() {
	}
	
	@Test
	public void testGetExportPath_Happy() {
		String csvExportDir = ".";
		CsvExportService service = new CsvExportService(csvExportDir);
		String exportPath = service.getExportPath("testFile.csv");
		
		assertThat(exportPath, CoreMatchers.is("." + File.separator + "testFile.csv"));
	}
	
	@Test
	public void testGetExportPath_NullExportDir() {
		String csvExportDir = null;
		CsvExportService service = new CsvExportService(csvExportDir);
		String exportPath = service.getExportPath("testFile.csv");
		
		assertThat(exportPath, CoreMatchers.is("testFile.csv"));
	}
	
	@Test
	public void testGetExportPath_EmptyExportDir() {
		String csvExportDir = "";
		CsvExportService service = new CsvExportService(csvExportDir);
		String exportPath = service.getExportPath("testFile.csv");
		
		assertThat(exportPath, CoreMatchers.is("testFile.csv"));
	}
	
	@Test
	public void testGetExportPath_ExportDirIsJustTheFileSeparator() {
		String csvExportDir = File.separator;
		CsvExportService service = new CsvExportService(csvExportDir);
		String exportPath = service.getExportPath("testFile.csv");
		
		assertThat(exportPath, CoreMatchers.is(File.separator + "testFile.csv"));
	}
	
	@Test
	public void testGetExportPath_MultiLevelExportDir() {
		String csvExportDir = "level1" + File.separator + "level2";
		CsvExportService service = new CsvExportService(csvExportDir);
		String exportPath = service.getExportPath("testFile.csv");
		
		assertThat(exportPath, CoreMatchers.is(csvExportDir + File.separator + "testFile.csv"));
	}
	
	@Test
	public void testGetExportPath_MultiLevelExportDirIncludingEndSeparator() {
		String csvExportDir = "level1" + File.separator + "level2" + File.separator;
		CsvExportService service = new CsvExportService(csvExportDir);
		String exportPath = service.getExportPath("testFile.csv");
		
		assertThat(exportPath, CoreMatchers.is(csvExportDir + "testFile.csv"));
	}

}
