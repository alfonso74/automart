package com.orendel.counterpoint.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "IM_XFER_IN_HDR")
public class TransferIn {
	
	@Id
	@Column(name = "XFER_NO", insertable = false, updatable = false)
	private String id;

	@Column(name = "FROM_LOC_ID")
	private String locationFrom;
	
	@Column(name = "TO_LOC_ID")
	private String locationTo;

	@Column(name = "TOT_QTY_RECVD", columnDefinition="t_qty")
	private BigDecimal qtyReceived;
	
	@Column(name = "EST_RECVD_SUB_TOT", columnDefinition="t_money")
	private BigDecimal subTotal;
	
	@Column(name = "EST_RECVD_TOT", columnDefinition="t_money")
	private BigDecimal total;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "transfer", fetch = FetchType.EAGER)
	private List<TransferInLine> lines = new ArrayList<TransferInLine>();

	// ******************************** Special methods ***********************************
	
	public void addTransferInLine(TransferInLine line) {
		getLines().add(line);
		line.setTransfer(this);
	}
	
	// ******************************** Getters y setters ***********************************
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocationFrom() {
		return locationFrom;
	}

	public void setLocationFrom(String locationFrom) {
		this.locationFrom = locationFrom;
	}

	public String getLocationTo() {
		return locationTo;
	}

	public void setLocationTo(String locationTo) {
		this.locationTo = locationTo;
	}

	public BigDecimal getQtyReceived() {
		return qtyReceived;
	}

	public void setQtyReceived(BigDecimal qtyReceived) {
		this.qtyReceived = qtyReceived;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<TransferInLine> getLines() {
		return lines;
	}

	public void setLines(List<TransferInLine> lines) {
		this.lines = lines;
	}

}
