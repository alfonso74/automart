package com.orendel.seam.domain.delivery;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "AM_DELIVERY_LINE")
public class DeliveryLine {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
//	@Column(name = "DELIVERY_ID", insertable = false, updatable = false, nullable = false)
//	private Long deliveryId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVERY_ID", updatable = false, nullable = false)
	private Delivery delivery;
	
	@Column(name = "QTY_SOLD", columnDefinition="decimal")
	private BigDecimal qtySold;
	
	@Column(name = "QTY_DELIVERED", columnDefinition="decimal")
	private BigDecimal qtyDelivered;
	
	@Column(name = "ITEM_NO")
	private String itemNumber;
	
	@Column(name = "ITEM_DESCR")
	private String itemDescription;
	

	public DeliveryLine() {
	}

	// ****************************** Helper methods ********************************
	
	public void adjustDeliveredQuantity(int quantity) {
		int currentQty = this.getQtyDelivered().intValue();
		int newQty = currentQty + quantity;
		this.setQtyDelivered(new BigDecimal(newQty));
	}
	
	
	// ***************************** Getters and setters ********************************

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


//	public Long getDeliveryId() {
//		return deliveryId;
//	}


	public Delivery getDelivery() {
		return delivery;
	}


	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}


//	public void setDeliveryId(Long deliveryId) {
//		this.deliveryId = deliveryId;
//	}


	public BigDecimal getQtySold() {
		return qtySold;
	}


	public void setQtySold(BigDecimal qtySold) {
		this.qtySold = qtySold;
	}


	public BigDecimal getQtyDelivered() {
		return qtyDelivered;
	}


	public void setQtyDelivered(BigDecimal qtyDelivered) {
		this.qtyDelivered = qtyDelivered;
	}

	
	public String getItemNumber() {
		return itemNumber;
	}


	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	
	public String getItemDescription() {
		return itemDescription;
	}


	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

}
