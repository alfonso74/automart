package com.orendel.counterpoint.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@IdClass(TransferInLinePK.class)
@Table(name = "IM_XFER_IN_LIN")
public class TransferInLine {

	@Id
	@Column(name = "XFER_NO", insertable = false, updatable = false)
	private String transferId;
	
	@Id
	@Column(name = "XFER_IN_LIN_SEQ_NO", insertable = false, updatable = false)
	private Integer lineId;
	
	@Column(name = "XFER_LIN_SEQ_NO", insertable = false, updatable = false)
	private Integer positionId;
	
	@Column(name = "SELECTD")
	private String selected;
	
	@Embedded
	private Comments comments;
	
	@Column(name = "PREV_QTY_EXPECTD", columnDefinition="t_qty")
	private BigDecimal qtyPrevExpected;
	
	@Column(name = "QTY_RECVD", columnDefinition="t_qty")
	private BigDecimal qtyReceived;
	
	@Column(name = "NEW_QTY_EXPECTD", columnDefinition="t_qty")
	private BigDecimal qtyNewExpected;
	
	@Column(name = "EST_UNIT_COST", columnDefinition="t_cost")
	private BigDecimal estUnitCost;
	
	@Column(name = "EST_EXT_COST", columnDefinition="t_money")
	private BigDecimal estExtCost;
	
	
	@ManyToOne
    @JoinColumn(name = "ITEM_NO")
	private Item item;
	
	@ManyToOne
    @JoinColumn(name = "XFER_NO")
	private TransferIn transfer;

	
	// ********************************* Helper methods *************************************
	
	public void setComments(String comment1, String comment2, String comment3) {
		Comments comments = new Comments();
		comments.setComment1(comment1);
		comments.setComment2(comment2);
		comments.setComment3(comment3);
		setComments(comments);
	}
	
	// ******************************** Getters y setters ***********************************
	
	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public TransferIn getTransfer() {
		return transfer;
	}

	public void setTransfer(TransferIn transfer) {
		this.transfer = transfer;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
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
		return comments;
	}

	public void setComments(Comments comments) {
		this.comments = comments;
	}

	public BigDecimal getQtyPrevExpected() {
		return qtyPrevExpected;
	}

	public void setQtyPrevExpected(BigDecimal qtyPrevExpected) {
		this.qtyPrevExpected = qtyPrevExpected;
	}

	public BigDecimal getQtyReceived() {
		return qtyReceived;
	}

	public void setQtyReceived(BigDecimal qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public BigDecimal getQtyNewExpected() {
		return qtyNewExpected;
	}

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
	
}
