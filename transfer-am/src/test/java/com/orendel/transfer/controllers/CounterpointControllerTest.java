package com.orendel.transfer.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.orendel.counterpoint.domain.Item;
import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.counterpoint.domain.TransferInLine;
import com.orendel.transfer.dao.TransferInDAO;


public class CounterpointControllerTest {
	
	private CounterpointController controller = null;
	
	
	@BeforeClass
	public static void init() {
	}
	
	@Before
	public void setup() {
		TransferIn transferIn = new TransferIn();
		transferIn.addTransferInLine(createTransferInLine("777", 7, 0, 7));
		transferIn.addTransferInLine(createTransferInLine("888", 8, 0, 8));
		transferIn.addTransferInLine(createTransferInLine("777", 2, 0, 2));
		
		List<TransferIn> transferInList = new ArrayList<TransferIn>();
		transferInList.add(transferIn);
		
		TransferInDAO dao = mock(TransferInDAO.class);
		when(dao.findByField(anyString(), eq("T001"))).thenReturn(transferInList);
		controller = new CounterpointController(dao, true);
	}
	
	@Test
	public void testFindTransferInByNumber_Happy() {
		TransferIn transferIn = controller.findTransferInByNumber("T001");
		assertNotNull(transferIn);
	}
	
	
	private TransferInLine createTransferInLine(String itemCode, int qtyExpected, int qtyReceived, int qtyPending) {
		TransferInLine line = new TransferInLine();
		line.setQtyPrevExpected(new BigDecimal(qtyExpected));
		line.setQtyReceived(new BigDecimal(qtyReceived));
		line.setQtyNewExpected(new BigDecimal(qtyPending));
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
