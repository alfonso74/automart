package com.orendel.transfer.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orendel.delivery.domain.TransferControl;
import com.orendel.delivery.domain.TransferControlStatus;
import com.orendel.transfer.dao.GenericDAOImpl;
import com.orendel.transfer.dao.TransferControlDAO;


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
	 * Busca una transferencia en base al número de transferencia indicado.
	 * @param transferNo número de transferencia a buscar
	 * @param tcStatus {@link TransferControlStatus estado} de la transferencia a buscar
	 * @return {@link TransferControl} asociado al número indicado, o <code>null</code> si no se encuentra
	 */
	public List<TransferControl> findTransferControlByNumberAndStatus(String transferNo, TransferControlStatus tcStatus) {
		List<TransferControl> result = new ArrayList<TransferControl>();
		TransferControlDAO dao = (TransferControlDAO) getDAO();
		List<TransferControl> tcList = dao.findByField("transferNo", transferNo);
		if (tcList != null && !tcList.isEmpty()) {
			if (tcStatus != null) {
				for(TransferControl v : tcList) {
					if (v.getStatus().equalsIgnoreCase(tcStatus.getCode())) {
						result.add(v);
						break;
					}
				}
			}			
		}
		return result;
	}
	
	//TODO programar esto!!
	public List<TransferControl> findTransferControlByNumber(String transferNo, TransferControlStatus... tcStatus) {
		List<TransferControl> result = new ArrayList<TransferControl>();
		return result;
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
