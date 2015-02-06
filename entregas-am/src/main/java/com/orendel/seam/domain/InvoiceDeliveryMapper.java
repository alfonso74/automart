package com.orendel.seam.domain;

import java.util.Date;

import com.orendel.seam.domain.counterpoint.Invoice;
import com.orendel.seam.domain.counterpoint.InvoiceLine;
import com.orendel.seam.domain.delivery.Delivery;
import com.orendel.seam.domain.delivery.DeliveryLine;

/**
 * Used to create {@link Delivery} and {@link DeliveryLine} objects from invoices/invoices lines. 
 * @author Admin
 */
public final class InvoiceDeliveryMapper {

	
	public static Delivery from(Invoice invoice) {
		Delivery delivery = new Delivery();		
		delivery.setInvoiceId(invoice.getId());
		delivery.setTicketNumber(invoice.getTicket());
		delivery.setCreated(new Date());
		delivery.setStatus(Status.PARTIAL.getCode());
		for (InvoiceLine invoiceLine : invoice.getLines()) {
			DeliveryLine line = from(invoiceLine);
			delivery.addDeliveryLine(line);
		}
		return delivery;
	}
	
	
	public static DeliveryLine from(InvoiceLine invoiceLine) {
		if (invoiceLine == null) {
			return null;
		}
		DeliveryLine deliveryLine = new DeliveryLine();
		deliveryLine.setItemNumber(invoiceLine.getItem().getItemNo());
		deliveryLine.setItemDescription(invoiceLine.getDescripcion());
		deliveryLine.setQtySold(invoiceLine.getQtySold());
		deliveryLine.setQtyDelivered(invoiceLine.getQtyDelivered());
		
		return deliveryLine;
	}

}
