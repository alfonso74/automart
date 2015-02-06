package com.orendel.seam.domain.delivery;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.log4j.Logger;
import org.junit.*;

import com.orendel.seam.domain.Status;


public class DeliveryTest {

	private static final Logger logger = Logger.getLogger(DeliveryTest.class);

	@BeforeClass
	public static void init() {
		logger.info("INIT!");
	}


	@Before
	public void setup() {

	}

	@Test
	public void testLocateLinesWithItem_Happy() {
		Delivery delivery = createDelivery();
		List<DeliveryLine> lines = delivery.locateLinesWithItem("100");
		assertNotNull(lines);
		assertTrue(lines.size() == 1);
	}

	@Test
	public void testLocateLinesWithItem_NoDeliveryLines_Fail() {
		Delivery delivery = new Delivery();
		List<DeliveryLine> lines = delivery.locateLinesWithItem("100");
		assertNotNull(lines);
		assertTrue(lines.isEmpty());
	}

	@Test
	public void testLocateLinesWithItem_NonExistingItem_Fail() {
		Delivery delivery = createDelivery();
		List<DeliveryLine> lines = delivery.locateLinesWithItem("101");
		assertNotNull(lines);
		assertTrue(lines.isEmpty());
	}

	@Test
	public void testLocateLinesWithItem_NullItemNumber_Fail() {
		Delivery delivery = createDelivery();
		List<DeliveryLine> lines = delivery.locateLinesWithItem(null);
		assertNotNull(lines);
		assertTrue(lines.isEmpty());
	}

	@Test
	public void testAdjustDeliveredQuantityForItem_Happy() {
		logger.info("------- Scenario: Adjust delivered quantity (Happy) -----------");
		Delivery delivery = createDelivery();

		DeliveryLine line = delivery.adjustDeliveredQuantityForItem("00N", 1);
		logger.info("Description: " + line.getItemDescription() + ", sold: " + line.getQtySold() + ", delivered: " + line.getQtyDelivered());
		assertTrue(line.getQtyDelivered().intValue() == 1);
	}

//	@Test
	public void testAdjustDeliveredQuantityForItem_QtyDeliveredOverflow() {
		logger.info("------- Scenario: Adjust delivered quantity (should distribute among two lines) -----------");
		Delivery delivery = createDelivery();

		DeliveryLine line = delivery.adjustDeliveredQuantityForItem("00N", 3);
		logger.info("Description: " + line.getItemDescription() + ", sold: " + line.getQtySold() + ", delivered: " + line.getQtyDelivered());
		line = delivery.adjustDeliveredQuantityForItem("00N", 1);
		logger.info("Description: " + line.getItemDescription() + ", sold: " + line.getQtySold() + ", delivered: " + line.getQtyDelivered());
		assertTrue(line.getItemDescription().equals("N lines3"));
		assertTrue(line.getItemNumber().equals("2"));
	}

	@Test
	public void testAdjustDeliveredQuantityForItem_NoLinePendingDelivery() {
		logger.info("------- Scenario: Adjust delivered quantity (No line with pending delivery), should select first found -----------");
		Delivery delivery = createDelivery();
		delivery.adjustDeliveredQuantityForItem("00N", 2);
		delivery.adjustDeliveredQuantityForItem("00N", 3);

		DeliveryLine line = delivery.adjustDeliveredQuantityForItem("00N", 1);
		logger.info("Description: " + line.getItemDescription() + ", sold: " + line.getQtySold() + ", delivered: " + line.getQtyDelivered());
		assertTrue(line.getItemDescription().equals("N lines2"));
	}

	@Test
	public void testGetDeliveredItemsTotalQty_Happy() {
		logger.info("------- Scenario: Get total quantity of delivered items -----------");
		Delivery delivery = createDelivery();
		delivery.adjustDeliveredQuantityForItem("00N", 1);
		delivery.adjustDeliveredQuantityForItem("00N", 3);
		delivery.adjustDeliveredQuantityForItem("100", 50);

		int totalItems = delivery.getDeliveredItemsTotalQty();
		assertTrue("Wrong total for items: " + totalItems, totalItems == 54);
	}
	
	@Test
	public void testGetDeliveredItemsIndicator_Happy() {
		logger.info("------- Scenario: Get total quantity of delivered items -----------");
		Delivery delivery = createDelivery();
		delivery.adjustDeliveredQuantityForItem("00N", 1);
		delivery.adjustDeliveredQuantityForItem("00N", 3);
		delivery.adjustDeliveredQuantityForItem("100", 50);

		String indicator = delivery.getDeliveredItemsIndicator();
		assertTrue("Wrong indicator value: " + indicator, "54/108".equals(indicator));
	}
	
	@Test
	public void testClose_Happy() {
		logger.info("------- Scenario: Close delivery (should have a closure date, and status 'CLOSED' -----------");
		Delivery delivery = createDelivery();
		
		delivery.close();
		assertNotNull(delivery.getClosed());
		assertTrue("Unexpected status: " + delivery.getStatus(), delivery.getStatus().equalsIgnoreCase(Status.CLOSED.getCode()));
	}


	private Delivery createDelivery() {
		Delivery delivery = new Delivery();		
		delivery.addDeliveryLine(createLine("001", "Uno", 1));
		delivery.addDeliveryLine(createLine("002", "Dos", 2));
		delivery.addDeliveryLine(createLine("00N", "N lines2", 2));
		delivery.addDeliveryLine(createLine("100", "Cien", 100));
		delivery.addDeliveryLine(createLine("00N", "N lines3", 3));		
		return delivery;
	}

	private DeliveryLine createLine(String itemNo, String description, int qtySold) {
		DeliveryLine line = new DeliveryLine();
		line.setItemNumber(itemNo);
		line.setItemDescription(description);
		line.setQtySold(new BigDecimal(qtySold).setScale(0));
		line.setQtyDelivered(new BigDecimal(0).setScale(0));
		return line;
	}

}
