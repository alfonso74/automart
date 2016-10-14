package com.orendel.delivery.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.orendel.counterpoint.domain.Comments;


@Entity
@Table(name = "AM_TC_LINES")
public class TransferControlLine {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "XFER_NO", nullable = false)
	private String transferNo;
	
	@Column(name = "XFER_IN_LIN_SEQ_NO", nullable = false)
	private Integer lineId;
	
	@Column(name = "XFER_LIN_SEQ_NO", nullable = false)
	private Integer positionId;
	
	@Column(name = "SELECTD", columnDefinition="varchar", nullable = false)
	private String selected;
	
	@Embedded
	private Comments comments;
	
	/** Expected quantity to be received */
	@Column(name = "PREV_QTY_EXPECTD", columnDefinition="decimal")
	private BigDecimal qtyPrevExpected;
	
	/** Received quantity */
	@Column(name = "QTY_RECVD", columnDefinition="decimal")
	private BigDecimal qtyReceived;
	
	/** Pending quantity to be received */
	@Column(name = "NEW_QTY_EXPECTD", columnDefinition="decimal")
	private BigDecimal qtyNewExpected;
	
	@Column(name = "EST_UNIT_COST", columnDefinition="decimal")
	private BigDecimal estUnitCost;
	
	@Column(name = "EST_EXT_COST", columnDefinition="decimal")
	private BigDecimal estExtCost;
	
	@Column(name = "ITEM_NO")
	private String itemNumber;
	
	@Column(name = "ITEM_DESCR")
	private String itemDescription;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TC_ID", updatable = false, nullable = false)
	private TransferControl transfer;

	
	// *************************** Helper methods ********************************
	
	/**
	 * Adjust the line's quantities:
	 * <li>Initial expected items - property qtyPrevExpected</li>
	 * <li>Received items - property qtyReceived</li>
	 * <li>Pending items - property qtyNewExpected</li>
	 * <br>
	 * Also updates the line's total cost, equals to the quantity received multiplied by the
	 * unit cost.
	 * @param receivedQuantity the item quantity that is being received
	 */
	public void adjustQuantities(int receivedQuantity) {
		int currentQty = this.getQtyReceived().intValue();
		int newQty = currentQty + receivedQuantity;
		this.setQtyReceived(new BigDecimal(newQty));
		this.setQtyNewExpected(this.getQtyPrevExpected().subtract(this.getQtyReceived()));
		this.setEstExtCost(this.getQtyReceived().multiply(this.getEstUnitCost()));
	}

	
	
	// ***************************** Getters and Setters ************************************
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTransferNo() {
		return transferNo;
	}

	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}
	
	public String getSelected() {
		return selected;
	}
	
	public void setSelected(String selected) {
		this.selected = selected;
	}

	public Comments getComments() {
		if (comments == null) {
			comments = new Comments();
		}
		return comments;
	}
	
	public void setComments(Comments comments) {
		this.comments = comments;
	}
	
	/** Gets the item <b>expected</b> quantity to be received */
	public BigDecimal getQtyPrevExpected() {
		return qtyPrevExpected;
	}
	
	/** The item <b>expected</b> quantity to be received */
	public void setQtyPrevExpected(BigDecimal qtyPrevExpected) {
		this.qtyPrevExpected = qtyPrevExpected;
	}

	/** Gets the item <b>received</b> quantity */
	public BigDecimal getQtyReceived() {
		return qtyReceived;
	}

	/** The item <b>received</b> quantity */
	public void setQtyReceived(BigDecimal qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	/** Gets the item <b>pending</b> quantity to be received */
	public BigDecimal getQtyNewExpected() {
		return qtyNewExpected;
	}

	/** The item <b>pending</b> quantity to be received */
	public void setQtyNewExpected(BigDecimal qtyNewExpected) {
		this.qtyNewExpected = qtyNewExpected;
	}

	public BigDecimal getEstUnitCost() {
		return estUnitCost;
	}

	public void setEstUnitCost(BigDecimal estUnitCost) {
		this.estUnitCost = estUnitCost;
	}

	public BigDecimal getEstExtCost() {
		return estExtCost;
	}

	public void setEstExtCost(BigDecimal estExtCost) {
		this.estExtCost = estExtCost;
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

	public TransferControl getTransfer() {
		return transfer;
	}

	public void setTransfer(TransferControl transfer) {
		this.transfer = transfer;
	}
	
}
