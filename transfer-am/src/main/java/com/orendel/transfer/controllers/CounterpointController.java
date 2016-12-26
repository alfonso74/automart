package com.orendel.transfer.controllers;

import java.util.List;

import org.hibernate.HibernateException;

import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.BarCodeType;
import com.orendel.counterpoint.domain.Item;
import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.transfer.dao.BarCodeDAO;
import com.orendel.transfer.dao.BarcodeTypeDAO;
import com.orendel.transfer.dao.GenericDAOImpl;
import com.orendel.transfer.dao.ItemDAO;
import com.orendel.transfer.dao.TransferInDAO;
import com.orendel.transfer.services.HibernateUtil;


public class CounterpointController extends AbstractController<TransferIn> {

	public CounterpointController() {
		super(new TransferInDAO());
	}
	
	public CounterpointController(GenericDAOImpl<TransferIn, Long> dao, boolean isTest) {
		super(dao, true);
	}

	public CounterpointController(String editorId) {
		super(editorId, new TransferInDAO());
	}
	
	public TransferIn findTransferInByNumber(String transferInNumber) {
		TransferIn transferIn = null;
		List<TransferIn> result = getDAO().findByField("id", transferInNumber);
		if (result != null && !result.isEmpty()) {
			transferIn = result.get(0);
		}
		return transferIn;
	}
	
	/**
	 * Busca un item en base a un código de barra o al código del item (en ese orden).
	 * @param code código de barra o del item que (hopefully) existe en base de datos
	 * @return un Item identificado por el código suministrado, o null si no se encontró ningún
	 * Item con el mismo.
	 */
	public Item findItem(String code) {
		Item item = findItemByBarCode(code);
		if (item == null) {
			item = findItemByItemCode(code);
		}
		return item;
	}
	
	/**
	 * Busca un item en base a un código de barra. 
	 * @param barCode código de barra de un item (hopefully) existente en base de datos
	 * @return un Item con el código de barra suministrado, o null si no se encontró ningún
	 * Item con el mismo.
	 */
	public Item findItemByBarCode(String barCode) {
		Item item = null;
		BarCodeDAO dao = new BarCodeDAO();
		dao.setSession(this.getSession());
		List<BarCode> itemBarcodeList = dao.findByField("code", barCode);
		if (itemBarcodeList != null && !itemBarcodeList.isEmpty()) {
			if (itemBarcodeList.size() > 1) {
				// TODO lanzar exception por barcode utilizado por más de un item? (posible error) o ignorarlo...
			}
			item = itemBarcodeList.get(0).getItem();
		}
		return item;
	}
	
	/**
	 * Busca un Item en base a un código de artículo. 
	 * @param itemCode el código de artículo que se desea buscar
	 * @return un Item con el código suministrado, o null si ningún Item tiene el código
	 * indicado.
	 */
	public Item findItemByItemCode(String itemCode) {
		Item item = null;
		ItemDAO dao = new ItemDAO();
		dao.setSession(this.getSession());
		List<Item> itemList = dao.findByField("itemNo", itemCode);
		if (itemList != null && !itemList.isEmpty()) {
			item = itemList.get(0);
		}
		return item;
	}
	

	public String doUpdateCounterPointTransferIn(TransferIn transferIn) {
		String msg = null;
		TransferInDAO dao = null;
		try {
			dao = (TransferInDAO) getDAO();
			dao.updateCounterpointTransferIn(transferIn);
		} catch (HibernateException he) {
			if (getSession().isOpen()) {
				HibernateUtil.rollback(getSession().getTransaction());
				HibernateUtil.closeEditorSession(getEditorId());
			}
			HibernateUtil.procesarError(he);
			HibernateUtil.verSesiones();
			msg = he.toString();
		}
		return msg;
	}
	
	public void persistBarcode(BarCode barcode) {
		BarCodeDAO dao = new BarCodeDAO();
		dao.setSession(this.getSession());
		dao.doSave(barcode);
	}
	
	/**
	 * Method to verify if the indicated {@link BarCodeType} has been already associated
	 * to an {@link Item}.
	 * @param code the {@link BarCodeType} to verify
	 * @param itemCode the item code (item number) for the {@link Item} we want to verify
	 * @return <code>true</code> if the indicated {@link BarCodeType} is already associated
	 * to the item.
	 */
	public boolean barcodeTypeExistsForItem(BarCodeType code, String itemCode) {
		boolean result = false;
		Item item = findItemByItemCode(itemCode);
		for (BarCode barcode : item.getBarcodeList()) {
			if (barcode.getType().getBarCodeId().equals(code.getBarCodeId())) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public List<BarCodeType> getBarcodeTypes() {
		BarcodeTypeDAO dao = new BarcodeTypeDAO();
		List<BarCodeType> barcodeTypes = dao.findAll();
		return barcodeTypes;
	}
}
