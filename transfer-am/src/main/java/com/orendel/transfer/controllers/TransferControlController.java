package com.orendel.transfer.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orendel.transfer.dao.GenericDAOImpl;
import com.orendel.transfer.dao.TransferControlDAO;
import com.orendel.transfer.domain.TransferControl;
import com.orendel.transfer.domain.TransferControlStatus;


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
	 * Busca una transferencia en base al número de transferencia indicado.
	 * @param transferNo número de transferencia a buscar
	 * @return {@link TransferControl} asociado al número indicado, o <code>null</code> si no se encuentra
	 */
	public TransferControl findPartialTransferControlByNumber(String transferNo) {
		TransferControl tc = null;
		TransferControlDAO dao = (TransferControlDAO) getDAO();
		List<TransferControl> tcList = dao.findByField("transferNo", transferNo);
		if (tcList != null && !tcList.isEmpty()) {
			for(TransferControl v : tcList) {
				if (v.getStatus().equalsIgnoreCase(TransferControlStatus.PARTIAL.getCode())) {
					tc = v;
					break;
				}
			}
		}
		return tc;
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
