package com.orendel.transfer.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orendel.transfer.dao.GenericDAOImpl;
import com.orendel.transfer.dao.TransferControlDAO;
import com.orendel.transfer.domain.TransferControl;


public class TransferControlController extends AbstractControllerDelivery<TransferControl> {

	public TransferControlController() {
		super(new TransferControlDAO());
	}
	
	public TransferControlController(GenericDAOImpl<TransferControl, Long> dao, boolean isTest) {
		super(dao, isTest);
	}

	public TransferControlController(String editorId) {
		super(editorId, new TransferControlDAO());
	}
	
	/**
	 * Busca las transferencias que estén dentro del rango de fechas especificado.
	 * @param initialDate fecha inicial del rango
	 * @param endDate fecha final del rango
	 * @return Listado de transferencias que cumplan con el rango indicado, o un listado vacío
	 * si no se encuentra nada o los parámetros son inválidos.
	 */
	public List<TransferControl> findTransfersByDateRange(Date initialDate, Date endDate) {
		List<TransferControl> tcList = new ArrayList<TransferControl>();
		if (initialDate != null && endDate != null) {
			TransferControlDAO dao = (TransferControlDAO) getDAO();
			tcList = dao.findByDateRange(initialDate, endDate);
		}
		return tcList;
	}

}
