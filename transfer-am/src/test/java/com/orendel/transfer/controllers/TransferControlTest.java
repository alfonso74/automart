package com.orendel.transfer.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.orendel.transfer.domain.TransferControl;
import com.orendel.transfer.domain.TransferControlLine;
import com.orendel.transfer.domain.TransferControlStatus;


public class TransferControlTest {
	
	private static final Logger logger = Logger.getLogger(TransferControlTest.class);
	
	
	@BeforeClass
	public static void init() {
		logger.info("INIT!");
	}
	
	@Before
	public void setup() {

	}
	
	@Test
	public void testLocateLinesWithItem_Happy() {
		TransferControl delivery = createTransferControl();
		List<TransferControlLine> lines = delivery.locateLinesWithItem("100");
		assertNotNull(lines);
		assertTrue(lines.size() == 1);
	}

	@Test
	public void testLocateLinesWithItem_NoTransferControlLines_Fail() {
		TransferControl delivery = new TransferControl();
		List<TransferControlLine> lines = delivery.locateLinesWithItem("100");
		assertNotNull(lines);
		assertTrue(lines.isEmpty());
	}

	@Test
	public void testLocateLinesWithItem_NonExistingItem_Fail() {
		TransferControl delivery = createTransferControl();
		List<TransferControlLine> lines = delivery.locateLinesWithItem("101");
		assertNotNull(lines);
		assertTrue(lines.isEmpty());
	}

	@Test
	public void testLocateLinesWithItem_NullItemNumber_Fail() {
		TransferControl delivery = createTransferControl();
		List<TransferControlLine> lines = delivery.locateLinesWithItem(null);
		assertNotNull(lines);
		assertTrue(lines.isEmpty());
	}
	
	@Test
	public void testAdjustDeliveredQuantityForItem_Happy() {
		logger.info("------- Scenario: Adjust received quantity (Happy) -----------");
		TransferControl transfer = createTransferControl();

		TransferControlLine line = transfer.adjustReceivedQuantityForItem("00N", 1);
		logger.info("Description: " + line.getItemDescription() + ", Expected: " + line.getQtyPrevExpected() + 
				", received: " + line.getQtyReceived() + ", pending: " + line.getQtyNewExpected());
		assertTrue(line.getQtyReceived().intValue() == 1);
	}

//	@Test
	public void testAdjustDeliveredQuantityForItem_QtyDeliveredOverflow() {
		logger.info("------- Scenario: Adjust received quantity (should distribute among two lines) -----------");
		TransferControl transfer = createTransferControl();

		TransferControlLine line = transfer.adjustReceivedQuantityForItem("00N", 3);
		logger.info("Description: " + line.getItemDescription() + ", Expected: " + line.getQtyPrevExpected() + 
				", received: " + line.getQtyReceived() + ", pending: " + line.getQtyNewExpected());
		line = transfer.adjustReceivedQuantityForItem("00N", 1);
		logger.info("Description: " + line.getItemDescription() + ", Expected: " + line.getQtyPrevExpected() + 
				", received: " + line.getQtyReceived() + ", pending: " + line.getQtyNewExpected());
		assertTrue(line.getItemDescription().equals("N lines3"));
		assertTrue(line.getItemNumber().equals("2"));
	}

	@Test
	public void testAdjustDeliveredQuantityForItem_NoLinePendingTransferControl() {
		logger.info("------- Scenario: Adjust received quantity (No line with pending delivery), should select first found -----------");
		TransferControl transfer = createTransferControl();
		transfer.adjustReceivedQuantityForItem("00N", 2);
		transfer.adjustReceivedQuantityForItem("00N", 3);

		TransferControlLine line = transfer.adjustReceivedQuantityForItem("00N", 1);
		logger.info("Description: " + line.getItemDescription() + ", Expected: " + line.getQtyPrevExpected() + 
				", received: " + line.getQtyReceived() + ", pending: " + line.getQtyNewExpected());
		assertTrue(line.getItemDescription().equals("N lines2"));
	}
	
	@Test
	public void testGetDeliveredItemsIndicator_Happy() {
		logger.info("------- Scenario: Get indicator for received vs expected items -----------");
		TransferControl transfer = createTransferControl();
		transfer.adjustReceivedQuantityForItem("00N", 1);
		transfer.adjustReceivedQuantityForItem("00N", 3);
		transfer.adjustReceivedQuantityForItem("100", 50);
		
		String indicator = transfer.getReceivedItemsIndicator();
		assertTrue("Wrong indicator value: " + indicator, "54 de 108".equals(indicator));
	}
	
	@Test
	public void testClose_Happy() {
		logger.info("------- Scenario: Close delivery (should have a closure date, and status 'CLOSED' -----------");
		TransferControl transfer = createTransferControl();
		
		transfer.close();
		assertNotNull(transfer.getClosed());
		assertTrue("Unexpected status: " + transfer.getStatus(), transfer.getStatus().equalsIgnoreCase(TransferControlStatus.CLOSED.getCode()));
	}
	
	@Test
	public void testGetTotalSelectedLines_Happy() {
		logger.info("------- Scenario: Get the total number of selected lines -----------");
		TransferControl transfer = createTransferControl();
		Integer selected = transfer.getTotalSelectedLines();
		Integer expected = 5;
		assertTrue("Wrong number of total selected lines: " + selected + " expected: " + expected, selected.equals(expected));
	}
	
	@Test
	public void testGetTotalSelectedLines_NullLines() {
		logger.info("------- Scenario: Get the total number of selected lines (TransferControl with lines set to null) -----------");
		TransferControl transfer = new TransferControl();
		transfer.setLines(null);
		Integer selected = transfer.getTotalSelectedLines();
		Integer expected = 0;
		assertTrue("Wrong number of total selected lines: " + selected + " expected: " + expected, selected.equals(expected));
	}
	
	
	private TransferControl createTransferControl() {
		TransferControl transfer = new TransferControl();		
		transfer.addTransferControlLine(createLine("001", "Uno", 1, 0));
		transfer.addTransferControlLine(createLine("002", "Dos", 2, 0));
		transfer.addTransferControlLine(createLine("00N", "N lines2", 2, 0));
		transfer.addTransferControlLine(createLine("100", "Cien", 100, 0));
		transfer.addTransferControlLine(createLine("00N", "N lines3", 3, 0));		
		return transfer;
	}

	private TransferControlLine createLine(String itemNo, String description, int qtyExpected, int qtyReceived) {
		TransferControlLine line = new TransferControlLine();
		line.setItemNumber(itemNo);
		line.setItemDescription(description);
		line.setQtyPrevExpected(new BigDecimal(qtyExpected).setScale(0));
		line.setQtyReceived(new BigDecimal(qtyReceived).setScale(0));
		line.setQtyNewExpected(new BigDecimal(qtyExpected - qtyReceived).setScale(0));
		line.setEstUnitCost(new BigDecimal(1));
		line.setEstExtCost(line.getEstUnitCost().multiply(line.getQtyReceived()));
		line.setSelected("Y");
		return line;
	}
}
