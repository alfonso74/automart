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
	 * Obtiene el siguiente código que puede ser asignado a una transferencia, en base al prefijo 
	 * indicado y con un orden ascendente.
	 * @param prefix el prefijo usado para generar el siguiente código
	 * @return el siguiente código que puede ser utilizado con el prefijo indicado.
	 */
	public String getNextTransferControlNumber(String prefix) {
		String result = null;
		TransferControlDAO dao = (TransferControlDAO) getDAO();
		result = dao.getNextCode(prefix);
		return result;
	}
	
	/**
	 * Busca una transferencia (PARCIAL) en base al número de transferencia indicado.
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
	 * @return {@link TransferControl} asociado al número indicado, o una lista vacía si no se
	 * encuentra ninguna transferencia con los parámetros suministrados.
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
	
	/**
	 * Busca una transferencia (con cualquier estado) en base al número de transferencia indicado.
	 * @param transferNo número de transferencia a buscar
	 * @return {@link TransferControl} asociado al número indicado, o <code>null</code> si no se encuentra
	 */
	public TransferControl findTransferControlByNumber(String transferNo) {
		TransferControl result = null;
		
		TransferControlDAO dao = (TransferControlDAO) getDAO();
		List<TransferControl> tcList = dao.findByField("transferNo", transferNo);
		if (tcList != null && !tcList.isEmpty()) {
			result = tcList.get(0);
		}
		
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
