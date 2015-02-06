package com.orendel.seam.controllers;

import java.util.List;

import org.apache.log4j.Logger;

import com.orendel.seam.dao.BarCodeDAO;
import com.orendel.seam.dao.InvoiceActiveDAO;
import com.orendel.seam.dao.InvoiceDAO;
import com.orendel.seam.dao.ItemDAO;
import com.orendel.seam.domain.counterpoint.BarCode;
import com.orendel.seam.domain.counterpoint.Invoice;
import com.orendel.seam.domain.counterpoint.InvoiceActive;
import com.orendel.seam.domain.counterpoint.InvoiceLine;
import com.orendel.seam.domain.counterpoint.Item;


public class InvoicesController extends AbstractController<Invoice> {
	
	private final Logger logger = Logger.getLogger(InvoicesController.class);
	

	public InvoicesController() {
		super(new InvoiceDAO());
	}

	public InvoicesController(String editorId) {
		super(editorId, new InvoiceDAO());
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
	
	
	public Invoice findInvoiceByNumber(String noInvoice) {
		Invoice invoice = null;
		List<Invoice> invoices = getDAO().findByField("ticket", noInvoice);
		if (invoices != null && !invoices.isEmpty()) {
			invoice = invoices.get(0);
		}
		return invoice;
	}
	
	
	public InvoiceActive findInvoiceActiveByNumber(String noInvoice) {
		InvoiceActive invoice = null;
		InvoiceActiveDAO dao = new InvoiceActiveDAO();
		List<InvoiceActive> invoices = dao.findByField("ticket", noInvoice);
		if (invoices != null && !invoices.isEmpty()) {
			invoice = invoices.get(0);
		}
		return invoice;
	}
	
	
	public InvoiceLine locateInvoiceLineForItemDelivery(Invoice invoice, Item item) {
		InvoiceLine invoiceLine = null;
		List<InvoiceLine> invoiceLines = invoice.locateLinesWithItem(item);
		
		// buscamos si alguna línea tiene artículos para entregar
		for (InvoiceLine line : invoiceLines) {
			if (line.getQtySold().longValue() > line.getQtyDelivered().longValue()) {
				invoiceLine = invoiceLine == null ? line : invoiceLine;
			}
		}
		
		// si no hay ninguna línea con entregas pendientes...
		if (invoiceLine == null) {
			// ...se selecciona la primera que aparezca en la factura
			if (!invoiceLines.isEmpty()) {
				invoiceLine = invoiceLines.get(0);
			}
		}
		
		// esto produce un LazyInit Exception
//		if (invoiceLine != null) {
//			logger.debug("Línea con artículo: " + invoiceLine.getDescripcion() + ", item desc: " + item.getDescription() + ", barcodes: " + item.getBarcodeList());
//		}
		return invoiceLine;
	}

}
