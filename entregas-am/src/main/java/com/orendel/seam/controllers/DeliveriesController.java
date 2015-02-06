package com.orendel.seam.controllers;

import java.util.Date;
import java.util.List;

import com.orendel.seam.dao.DeliveryDAO;
import com.orendel.seam.domain.Status;
import com.orendel.seam.domain.delivery.Delivery;

public class DeliveriesController extends AbstractControllerDelivery<Delivery> {

	public DeliveriesController() {
		super(new DeliveryDAO());
	}

	public DeliveriesController(String editorId) {
		super(editorId, new DeliveryDAO());
	}
	
	public List<Delivery> findAllDeliveries() {
		DeliveryDAO dao = new DeliveryDAO();
		return dao.findAllDeliveries();
	}
	
	/**
	 * Busca una entrega en base al número de factura asociado.
	 * @param invoiceNumber número de factura a buscar
	 * @return Entrega asociada al número de factura indicado, o null si no existe ninguna entrega
	 * con el número de factura.
	 */
	public Delivery findDeliveryByInvoiceNumber(String invoiceNumber) {
		Delivery delivery = null;
		if (invoiceNumber != null && !invoiceNumber.isEmpty()) {
			DeliveryDAO dao = new DeliveryDAO();
			List<Delivery> list = dao.findByField("ticketNumber", invoiceNumber);
			if (list != null && !list.isEmpty()) {
				delivery = list.get(0);
			}
		}
		return delivery;
	}
	
	
	public List<Delivery> findDeliveriesByInvoiceNumber(String invoiceNumber) {
		List<Delivery> deliveryList = null;
		if (invoiceNumber != null && !invoiceNumber.isEmpty()) {
			DeliveryDAO dao = new DeliveryDAO();
			deliveryList = dao.findByField("ticketNumber", invoiceNumber);
		}
		return deliveryList;
	}
	
	
	/**
	 * Busca una entrega en base al número indicado.
	 * @param deliveryNumber número de entrega a buscar
	 * @return Entrega asociada al número indicado, o null si no existe ninguna entrega
	 * con el número suministrado.
	 */
	public Delivery findDeliveryByDeliveryNumber(String deliveryNumber) {
		Delivery delivery = null;
		if (deliveryNumber != null && !deliveryNumber.isEmpty()) {
			DeliveryDAO dao = new DeliveryDAO();
			List<Delivery> list = dao.findByField("id", Long.parseLong(deliveryNumber));
			if (list != null && !list.isEmpty()) {
				delivery = list.get(0);
			}
		}
		return delivery;
	}
	
	
	/**
	 * Busca las entregas que estén dentro del rango de fechas especificado.
	 * @param initialDate fecha inicial del rango
	 * @param endDate fecha final del rango
	 * @return Listado de entregas que cumplan con el rango indicado, o un listado vacío
	 * si no se encuentra nada o los parámetros son inválidos.
	 */
	public List<Delivery> findDeliveryByDateRange(Date initialDate, Date endDate) {
		List<Delivery> deliveryList = null;
		if (initialDate != null && endDate != null) {
			DeliveryDAO dao = new DeliveryDAO();
			deliveryList = dao.findByDateRange(initialDate, endDate);
		}
		return deliveryList;
	}
	
	
	public List<Delivery> findPartialDeliveriesByDateRange(Date initialDate, Date endDate) {
		List<Delivery> deliveryList = null;
		if (initialDate != null && endDate != null) {
			DeliveryDAO dao = new DeliveryDAO();
			deliveryList = dao.findByDateRangeAndStatus(initialDate, endDate, Status.PARTIAL);
		}
		return deliveryList;
	}

}
