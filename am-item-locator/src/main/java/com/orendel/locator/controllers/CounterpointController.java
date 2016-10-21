package com.orendel.locator.controllers;

import java.util.List;

import org.hibernate.HibernateException;

import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.BarCodeType;
import com.orendel.counterpoint.domain.Inventory;
import com.orendel.counterpoint.domain.Item;
import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.locator.dao.BarCodeDAO;
import com.orendel.locator.dao.BarcodeTypeDAO;
import com.orendel.locator.dao.GenericDAOImpl;
import com.orendel.locator.dao.ItemDAO;
import com.orendel.locator.dao.TransferInDAO;
import com.orendel.locator.services.HibernateUtil;


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
	
	public Item findItemInMainWarehouse(String code) {
		Item item = this.findItem(code);
		if (item != null) {
			Inventory mainLocation = null;
			for (Inventory v : item.getInventory()) {
				LOGGER.info("Found item in location: " + v.getLocationId());
				if (v.getLocationId().equals("MAIN")) {
					mainLocation = v;
					break;
				}
			}
			if (mainLocation == null) {
				item = null;
			} else {
				item.getInventory().clear();
				item.getInventory().add(mainLocation);
			}
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
	
	public List<BarCodeType> getBarcodeTypes() {
		BarcodeTypeDAO dao = new BarcodeTypeDAO();
		List<BarCodeType> barcodeTypes = dao.findAll();
		return barcodeTypes;
	}
}
