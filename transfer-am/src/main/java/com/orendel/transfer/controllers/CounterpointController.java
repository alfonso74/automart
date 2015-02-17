package com.orendel.transfer.controllers;

import java.util.List;

import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.Item;
import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.transfer.dao.BarCodeDAO;
import com.orendel.transfer.dao.GenericDAOImpl;
import com.orendel.transfer.dao.ItemDAO;
import com.orendel.transfer.dao.TransferInDAO;


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
	 * Busca un item en base a un código de barra. 
	 * @param barCode código de barra de un item (hopefully) existente en base de datos
	 * @return un Item con el código de barra suministrado, o null si no se encontró ningún
	 * Item con el mismo.
	 */
	public Item findItemByBarCode(String barCode) {
		Item item = null;
		BarCodeDAO dao = new BarCodeDAO();
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
		List<Item> itemList = dao.findByField("itemNo", itemCode);
		if (itemList != null && !itemList.isEmpty()) {
			item = itemList.get(0);
		}
		return item;
	}

}
