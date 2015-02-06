package com.orendel.seam.domain.counterpoint;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;


@MappedSuperclass
@IdClass(InvoiceLinePK.class)
public class InvoiceLine {

	
	@Id
	@Column(name = "DOC_ID", insertable = false, updatable = false)
	private Long docId;
	
	@Id
	@GeneratedValue
	@Column(name = "LIN_SEQ_NO")
	private Integer lineaId;
	
//	@ManyToOne
//    @JoinColumn(name = "DOC_ID")
//	private Invoice factura;
	
	@Column(name = "TKT_NO")
	private String ticket;
	
	@ManyToOne
    @JoinColumn(name = "ITEM_NO")
	private Item item;
	
	@Column(name = "QTY_SOLD", columnDefinition="t_qty")
	private BigDecimal qtySold;
	
	@Column(name = "DESCR")
	private String descripcion;

	@Transient
	private BigDecimal qtyDelivered;
		
	
	// **************************** Métodos de dominio ***************************
	
	public void adjustQuantity(int quantity) {
		int currentQty = this.getQtyDelivered().intValue();
		int newQty = currentQty + quantity;
		this.setQtyDelivered(new BigDecimal(newQty));
	}
	
	
	
	// ***************************** Getters and setters *****************************
	
	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Integer getLineaId() {
		return lineaId;
	}

	public void setLineaId(Integer lineaId) {
		this.lineaId = lineaId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public BigDecimal getQtySold() {
		return qtySold.setScale(0);
	}

	public void setQtySold(BigDecimal cantidad) {
		this.qtySold = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getQtyDelivered() {
		return qtyDelivered == null ? new BigDecimal(0) : qtyDelivered;
	}

	public void setQtyDelivered(BigDecimal qtyEntregada) {
		this.qtyDelivered = qtyEntregada;
	}
	
}
