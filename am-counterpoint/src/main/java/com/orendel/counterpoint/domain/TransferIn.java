package com.orendel.counterpoint.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
	
	@Column(name = "TOT_SEL_LINS")
	private Integer totalSelectedLines;
	
	@Column(name = "REF")
	private String reference;
	
	@Embedded
	private Comments comments;

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
	
	/**
	 * Busca una línea que tenga la propiedad lineId indicada
	 * @param lineId propiedad "lineId" de la línea a ser buscada (no confundir con el identificador de la línea o campo "id")
	 * @return línea con el id indicado, o <code>null</code> si no se encuentra ninguna línea
	 */
	public TransferInLine findLineById(Integer lineId) {
		TransferInLine line = null;
		if (getLines() != null) {
			for (TransferInLine v : getLines()) {
				if (v.getLineId().intValue() == lineId.intValue()) {
					line = v;
					break;
				}
			}
		}
		return line;
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
	
	public Integer getTotalSelectedLines() {
		return totalSelectedLines;
	}

	public void setTotalSelectedLines(Integer totalSelectedLines) {
		this.totalSelectedLines = totalSelectedLines;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public Comments getComments() {
		comments = comments == null ? new Comments() : comments;
		return comments;
	}

	public void setComments(Comments comments) {
		this.comments = comments;
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
