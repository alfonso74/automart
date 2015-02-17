package com.orendel.transfer.domain;

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


@Entity
@Table(name = "AM_TC")
public class TransferControl {
	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "XFER_NO", updatable = false, nullable = false)
	private String transferNo;
	
	@Column(name = "CREATED_DATE", nullable = false)
	private Date created;
	
	/** Delivery closed date */
	@Column(name = "CLOSED_DATE")
	private Date closed;
	
	@Column(name = "STATUS", nullable = false)
	private String status;
	
	@Column(name = "USERNAME", nullable = false)
	private String userName;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "transfer", fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<TransferControlLine> lines = new ArrayList<>();

	
	// *************************** Helper methods ********************************
	
	public TransferControlLine adjustReceivedQuantityForItem(String itemNo, int qtyReceived) {
		TransferControlLine tcLine = null;
		List<TransferControlLine> tcLines = this.locateLinesWithItem(itemNo);
		
		// buscamos si alguna línea tiene artículos para entregar
		for (TransferControlLine line : tcLines) {
			if (line.getQtyPrevExpected().longValue() > line.getQtyReceived().longValue()) {
				tcLine = tcLine == null ? line : tcLine;
			}
		}
		
		// si no hay ninguna línea con cantidades pendientes...
		if (tcLine == null) {
			// ...se selecciona la primera línea (con el item indicado) que aparezca listada en la factura
			if (!tcLines.isEmpty()) {
				tcLine = tcLines.get(0);
			}
		}
		
		if (tcLine != null) {
			tcLine.adjustQuantities(qtyReceived);
		}

		return tcLine;
	}
	
	
	/**
	 * Busca la(s) línea(s) de la factura que tenga(n) el item indicado.
	 * @param item item que será buscado en la factura
	 * @return un listado de objetos InvoiceLine que tienen el item indicado, o una lista vacía si no se encontró 
	 * ninguna línea con el código de item indicado.
	 */
	public List<TransferControlLine> locateLinesWithItem(String itemNo) {
		List<TransferControlLine> tcLines = new ArrayList<TransferControlLine>();
		if (this.getLines() == null || this.getLines().isEmpty()) {
			return tcLines;
		}
		for (TransferControlLine l : this.getLines()) {
			//TODO el equals de un Item debe ser por equivalencia de código Y secuencia
			if (l.getItemNumber().equals(itemNo)) {
				tcLines.add(l);
			}
		}
		return tcLines;
	}
	
	
	public void addTransferControlLine(TransferControlLine line) {
		getLines().add(line);
		line.setTransfer(this);
	}
	
	
	public void close() {
		this.setClosed(new Date());
		this.setStatus(Status.CLOSED.getCode());
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

	public List<TransferControlLine> getLines() {
		return lines;
	}

	public void setLines(List<TransferControlLine> lines) {
		this.lines = lines;
	}

}
