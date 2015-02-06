package com.orendel.seam.domain.delivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.orendel.seam.domain.Status;


@Entity
@Table(name = "AM_DELIVERY")
public class Delivery {
	
	/** Delivery database id */
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	/** Invoice database id */
	@Column(name = "DOC_ID")
	private Long invoiceId;
	
	/** Invoice number (CounterPoint document number) */
	@Column(name = "TKT_NO")
	private String ticketNumber;
	
	/** Delivery created date */
	@Column(name = "CREATED")
	private Date created;
	
	/** Delivery closed date */
	@Column(name = "CLOSED")
	private Date closed;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "USERNAME")
	private String userName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "delivery", fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
    private List<DeliveryLine> deliveryLines;
	

	public Delivery() {
	}

	
	// ****************************** Helper methods ********************************
	
	
	public DeliveryLine adjustDeliveredQuantityForItem(String itemNo, int qty) {
		DeliveryLine deliveryLine = null;
		List<DeliveryLine> deliveryLines = this.locateLinesWithItem(itemNo);
		
		// buscamos si alguna línea tiene artículos para entregar
		for (DeliveryLine line : deliveryLines) {
			if (line.getQtySold().longValue() > line.getQtyDelivered().longValue()) {
				deliveryLine = deliveryLine == null ? line : deliveryLine;
			}
		}
		
		// si no hay ninguna línea con cantidades pendientes...
		if (deliveryLine == null) {
			// ...se selecciona la primera línea (con el item indicado) que aparezca listada en la factura
			if (!deliveryLines.isEmpty()) {
				deliveryLine = deliveryLines.get(0);
			}
		}
		
		if (deliveryLine != null) {
			deliveryLine.adjustDeliveredQuantity(qty);
		}

		return deliveryLine;
	}
	
	
	/**
	 * Busca la(s) línea(s) de la factura que tenga(n) el item indicado.
	 * @param item item que será buscado en la factura
	 * @return un listado de objetos InvoiceLine que tienen el item indicado, o una lista vacía si no se encontró 
	 * ninguna línea con el código de item indicado.
	 */
	public List<DeliveryLine> locateLinesWithItem(String itemNo) {
		List<DeliveryLine> deliveryLines = new ArrayList<DeliveryLine>();
		if (this.getDeliveryLines() == null || this.getDeliveryLines().isEmpty()) {
			return deliveryLines;
		}
		for (DeliveryLine l : this.getDeliveryLines()) {
			//TODO el equals de un Item debe ser por equivalencia de código Y secuencia
			if (l.getItemNumber().equals(itemNo)) {
				deliveryLines.add(l);
			}
		}
		return deliveryLines;
	}
	
	
	public void addDeliveryLine(DeliveryLine line) {
		line.setDelivery(this);
		getDeliveryLines().add(line);
	}
	
	
	public int getDeliveredItemsTotalQty() {
		int total = 0;
		for (DeliveryLine line : getDeliveryLines()) {
			total += line.getQtyDelivered().intValue();
		}
		return total;
	}
	
	
	public String getDeliveredItemsIndicator() {
		int delivered = 0;
		int total = 0;
		for (DeliveryLine line : getDeliveryLines()) {
			delivered += line.getQtyDelivered().intValue();
			total += line.getQtySold().intValue();
		}
		return delivered + "/" + total;
	}
	
	
	public void close() {
		this.setClosed(new Date());
		this.setStatus(Status.CLOSED.getCode());
	}
	
	
	// ***************************** Getters and setters ********************************
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getInvoiceId() {
		return invoiceId;
	}


	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}


	public String getTicketNumber() {
		return ticketNumber;
	}


	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}

	
	public Date getClosed() {
		return closed;
	}


	public void setClosed(Date closed) {
		this.closed = closed;
	}
	

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public List<DeliveryLine> getDeliveryLines() {
		if (deliveryLines == null) {
			this.deliveryLines = new ArrayList<DeliveryLine>();
		}
		return deliveryLines;
	}


	public void setDeliveryLines(List<DeliveryLine> deliveryLines) {
		this.deliveryLines = deliveryLines;
	}
	
}
