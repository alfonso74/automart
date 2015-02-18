package com.orendel.transfer.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.orendel.transfer.dao.TransferControlDAO;
import com.orendel.transfer.domain.TransferControl;
import com.orendel.transfer.domain.TransferControlStatus;


public class TransferControlControllerTest {
	
	TransferControlController controller = null;
	List<TransferControl> tcList = null;
	
	@BeforeClass
	public static void init() {
	}
	
	@Before
	public void setup() {
		tcList = new ArrayList<TransferControl>();
		tcList.add(createTransferControl("T001"));
		tcList.add(createTransferControl("T002"));
	}
	
	@Test
	public void testFindTransfersByDateRange_Happy() {
		TransferControlDAO dao = mock(TransferControlDAO.class);
		when(dao.findByDateRange(any(Date.class), any(Date.class))).thenReturn(tcList);
		controller = new TransferControlController(dao, true);
		
		List<TransferControl> result = controller.findTransfersByDateRange(new Date(), new Date());
		assertTrue(result.size() == 2);
	}
	
	@Test
	public void testFindTransfersByDateRange_NullParameter() {
		TransferControlDAO dao = mock(TransferControlDAO.class);
		when(dao.findByDateRange(any(Date.class), any(Date.class))).thenReturn(tcList);
		controller = new TransferControlController(dao, true);
		
		List<TransferControl> result = controller.findTransfersByDateRange(null, new Date());
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void testFindTransfersByDateRange_EmptyResult() {
		TransferControlDAO dao = mock(TransferControlDAO.class);
		when(dao.findByDateRange(any(Date.class), any(Date.class))).thenReturn(new ArrayList<TransferControl>());
		controller = new TransferControlController(dao, true);
		
		List<TransferControl> result = controller.findTransfersByDateRange(new Date(), new Date());
		assertTrue(result.size() == 0);
	}
	
	
	private TransferControl createTransferControl(String transferNo) {
		TransferControl tc = new TransferControl();
		tc.setId(new Date().getTime());
		tc.setTransferNo(transferNo);
		tc.setCreated(new Date());
		tc.setUserName("ADMIN");
		tc.setStatus(TransferControlStatus.PARTIAL.getCode());
		return tc;
	}

}
