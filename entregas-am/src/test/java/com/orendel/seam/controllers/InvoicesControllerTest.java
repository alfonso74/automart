package com.orendel.seam.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.orendel.seam.config.AppConfig;
import com.orendel.seam.domain.counterpoint.Invoice;
import com.orendel.seam.domain.counterpoint.InvoiceActive;
import com.orendel.seam.domain.counterpoint.InvoiceActiveLine;
import com.orendel.seam.domain.counterpoint.InvoiceLine;
import com.orendel.seam.domain.counterpoint.Item;


public class InvoicesControllerTest {
	
	private InvoicesController controller = new InvoicesController();
	private Invoice invoice;
	
	
	@BeforeClass
	public static void init() {
		AppConfig.INSTANCE.initializeProperties("am.properties");
	}
	

	@Before
	public void setup() {
		List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
		invoiceLines.add(createActiveLine("777", 1, 0));
		invoiceLines.add(createActiveLine("888", 2, 0));
		invoiceLines.add(createActiveLine("777", 3, 0));
		
		invoice = new InvoiceActive();
		invoice.setLines(invoiceLines);
	}
	
	@Test
	public void testFindItemByBarCode_Happy() {
//		InvoicesController controller = new InvoicesController();
		controller.findItemByBarCode("4");
	}
	
	@Test
	public void testLocateInvoiceLineForItemDelivery_FirstItem777() {
		// setup
		Item item = new Item();
		item.setItemNo("777");
		item.setDescription("Item 777");
		// execute
		InvoiceLine line = controller.locateInvoiceLineForItemDelivery(invoice, item);
		for (InvoiceLine v : invoice.getLines()) {
			System.out.println("Line " + v.getItem().getItemNo() + ": " + v.getQtySold() + ", " + v.getQtyDelivered());
		}
		// verify
		assertEquals(1L, line.getQtySold().longValue());
	}
	
	@Test
	public void testLocateInvoiceLineForItemDelivery_FirstItem888() {
		// setup
		Item item = new Item();
		item.setItemNo("888");
		item.setDescription("Item 888");
		// execute
		InvoiceLine line = controller.locateInvoiceLineForItemDelivery(invoice, item);
		for (InvoiceLine v : invoice.getLines()) {
			System.out.println("Line " + v.getItem().getItemNo() + ": " + v.getQtySold() + ", " + v.getQtyDelivered());
		}
		// verify
		assertEquals(2L, line.getQtySold().longValue());
	}
	
	@Test
	public void testLocateInvoiceLineForItemDelivery_SecondItem777() {
		// setup
		Item item = new Item();
		item.setItemNo("777");
		item.setDescription("Item 777");
		// execute
		InvoiceLine line = controller.locateInvoiceLineForItemDelivery(invoice, item);
		line.setQtyDelivered(new BigDecimal(1));
		line = controller.locateInvoiceLineForItemDelivery(invoice, item);
		for (InvoiceLine v : invoice.getLines()) {
			System.out.println("Line " + v.getItem().getItemNo() + ": " + v.getQtySold() + ", " + v.getQtyDelivered());
		}
		// verify
		assertEquals(3L, line.getQtySold().longValue());
	}
	
	

//	private InvoiceLine createLine(String itemCode, int qtySold, int qtyDelivered) {
//		InvoiceLine line = new InvoiceLine();
//		line.setQtySold(new BigDecimal(qtySold));
//		line.setQtyDelivered(new BigDecimal(qtyDelivered));
//		line.setItem(createItem(itemCode));
//		return line;
//	}
	
	private InvoiceLine createActiveLine(String itemCode, int qtySold, int qtyDelivered) {
		InvoiceLine line = new InvoiceActiveLine();
		line.setQtySold(new BigDecimal(qtySold));
		line.setQtyDelivered(new BigDecimal(qtyDelivered));
		line.setItem(createItem(itemCode));
		return line;
	}
	
	private Item createItem(String itemCode) {
		Item item = new Item();
		item.setItemNo(itemCode);
		item.setDescription("Item " + itemCode);
		return item;
	}
	
}
