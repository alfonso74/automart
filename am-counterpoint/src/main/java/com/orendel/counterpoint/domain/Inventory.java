package com.orendel.counterpoint.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@IdClass(InventoryPK.class)
@Table(name = "IM_INV")
public class Inventory {
	
	/** Id del item al que está asociado este inventario (PK) */
	@Id
	@Column(name = "ITEM_NO", insertable = false, updatable = false)
	private String itemNo;
	
	/** Id de la ubicación del item para esta línea de inventario */
	@Column(name = "LOC_ID", insertable = false, updatable = false)
	private String locationId;
	
	@Column(name = "QTY_ON_HND", columnDefinition="t_qty")
	private BigDecimal qtyOnHand;
	
	@Column(name = "QTY_COMMIT", columnDefinition="t_qty")
	private BigDecimal qtyCommited;
	
	@Column(name = "QTY_ON_XFER_IN", columnDefinition="t_qty")
	private BigDecimal qtyXferIn;
	
	@Column(name = "QTY_ON_XFER_OUT", columnDefinition="t_qty")
	private BigDecimal qtyXferOut;
	
	@Column(name = "QTY_AVAIL", columnDefinition="decimal")
	private BigDecimal qtyAvailable;
	
	/** Item al que está asociado este inventario */
	@ManyToOne
    @JoinColumn(name = "ITEM_NO")
	private Item item;

	
	// ***************************** Getters and setters *****************************
	
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public BigDecimal getQtyOnHand() {
		return qtyOnHand;
	}

	public void setQtyOnHand(BigDecimal qtyOnHand) {
		this.qtyOnHand = qtyOnHand;
	}

	public BigDecimal getQtyCommited() {
		return qtyCommited;
	}

	public void setQtyCommited(BigDecimal qtyCommited) {
		this.qtyCommited = qtyCommited;
	}

	public BigDecimal getQtyXferIn() {
		return qtyXferIn;
	}

	public void setQtyXferIn(BigDecimal qtyXferIn) {
		this.qtyXferIn = qtyXferIn;
	}

	public BigDecimal getQtyXferOut() {
		return qtyXferOut;
	}

	public void setQtyXferOut(BigDecimal qtyXferOut) {
		this.qtyXferOut = qtyXferOut;
	}

	public BigDecimal getQtyAvailable() {
		return qtyAvailable;
	}

	public void setQtyAvailable(BigDecimal qtyAvailable) {
		this.qtyAvailable = qtyAvailable;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
		
}
