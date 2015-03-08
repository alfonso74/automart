package com.orendel.transfer.domain;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
//import static org.mockito.Mockito.*;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class TransferControlLineTest {
	
	private static final Logger logger = Logger.getLogger(TransferControlLineTest.class);
	
	@BeforeClass
	public static void init() {
		logger.info("INIT!");
	}
	
	@Before
	public void setup() {

	}
	
	@Test
	public void testAdjustQuantites_Happy() {
		logger.info("------- Scenario: Adjust line quantities (Happy) -----------");
		TransferControlLine line = createLine("ITEM001", "Item number one", 10, 0);
		line.adjustQuantities(4);
		assertThat(line.getQtyPrevExpected(), is(new BigDecimal(10)));
		assertThat(line.getQtyReceived(), is(new BigDecimal(4)));
		assertThat(line.getQtyNewExpected(), is(new BigDecimal(6)));
		assertThat(line.getEstExtCost(), is(new BigDecimal(4)));
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
