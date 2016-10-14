package com.orendel.delivery.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
	
	@Column(name = "REF")
	private String reference;
	
	@Embedded
	private Comments comments;
	
	@Column(name = "CREATED_DATE", nullable = false)
	private Date created;
	
	/** Transfer closed date */
	@Column(name = "CLOSED_DATE")
	private Date closed;
	
	@Column(name = "STATUS", nullable = false)
	private String status;
	
	@Column(name = "USERNAME", nullable = false)
	private String userName;
	
	@Column(name = "SYS_LOG")
	private String log;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "transfer", fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<TransferControlLine> lines = new ArrayList<TransferControlLine>();

	
	// *************************** Helper methods ********************************
	
	public Integer getTotalSelectedLines() {
		Integer selected = 0;
		if (getLines() == null || getLines().isEmpty()) {
			return selected;
		}
		for (TransferControlLine line : getLines()) {
			if (line.getSelected().equalsIgnoreCase("y")) {
				selected++;
			}
		}
		return selected;
	}
	
	
	public TransferControlLine adjustReceivedQuantityForItem(String itemNo, int qtyReceived) {
		TransferControlLine tcLine = null;
		List<TransferControlLine> tcLines = this.locateLinesWithItem(itemNo);
		
		// buscamos si alguna línea tiene artículos pendientes de entrega
		for (TransferControlLine line : tcLines) {
			if (line.getQtyPrevExpected().longValue() > line.getQtyReceived().longValue()) {
				tcLine = tcLine == null ? line : tcLine;
			}
		}
		
		// si no hay ninguna línea con cantidades pendientes...
		if (tcLine == null) {
			// ...se selecciona la primera línea (con el item indicado) que aparezca listada en la transferencia
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
	
	/**
	 * Calcula el total de artículos recibidos vs los esperados.
	 * @return un {@link String} con formato "N1 de N2" (N1: recibidos, N2: esperados) 
	 */
	public String getReceivedItemsIndicator() {
		int totalExpected = 0;
		int totalReceived = 0;
		for (TransferControlLine line : getLines()) {
			totalReceived += line.getQtyReceived().intValue();
			totalExpected += line.getQtyPrevExpected().intValue();
		}
		return totalReceived + " de " + totalExpected;
	}
	
	public int getTotalExpectedItems() {
		int totalExpected = 0;
		for (TransferControlLine line : getLines()) {
			totalExpected += line.getQtyPrevExpected().intValue();
		}
		return totalExpected;
	}
	
	public int getTotalReceivedItems() {
		int totalReceived = 0;
		for (TransferControlLine line : getLines()) {
			totalReceived += line.getQtyReceived().intValue();
		}
		return totalReceived;
	}
	
	/**
	 * Calcula el valor de todos los items recibidos en la transferencia.  Para esto se suma el valor total 
	 * de cada línea (campo estExtCost del objeto {@link TransferControlLine}).
	 * @return
	 */
	public BigDecimal getTotalReceivedItemsValue() {
		BigDecimal totalReceivedValue = new BigDecimal(0);
		for (TransferControlLine line : getLines()) {
			totalReceivedValue = totalReceivedValue.add(line.getEstExtCost());
		}
		return totalReceivedValue;
	}
	
	public void addTransferControlLine(TransferControlLine line) {
		getLines().add(line);
		line.setTransfer(this);
	}
		
	public void close(TransferControlStatus finalStatus) {
		this.setClosed(new Date());
		this.setStatus(finalStatus.getCode());
	}
	
	public void setComments(String comment1, String comment2, String comment3) {
		Comments comments = new Comments();
		comments.setComment1(comment1);
		comments.setComment2(comment2);
		comments.setComment3(comment3);
		setComments(comments);
	}
	
	/**
	 * Indicates if a user can edit the Transfer Control document.
	 * @param aspiringUser the username trying to edit the Transfer Control.
	 * @return <code>true</code> if the transfer control doesn't have an assigned user, or if the same user is 
	 * trying to edit, <code>false</code> otherwise.
	 */
	public boolean isEditableByUser(String aspiringUser) {
		// if the transfer control doesn't have an assigned user, or the same user is trying to edit
		// then return true.
		if (this.userName == null || this.userName.equalsIgnoreCase(aspiringUser)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Adds the indicated text to the Transfer Control document log.  The log has a maximum capacity of 255
	 * characters. 
	 * @param entry the text being added
	 * @return <code>true</code> if the entry is successfully inserted in the log, <code>false</code> otherwise.
	 */
	public boolean addLogEntry(String entry) {
		boolean result = false;
		if (entry == null || entry.isEmpty()) {
			return result;
		}
		if (log == null) {
			log = new String();
		}
		if ((log.length() + entry.length()) < 255) {
			log = log += entry + "\n";
			result = true;
		}
		return result;
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
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public Comments getComments() {
		return comments;
	}
	
	public void setComments(Comments comments) {
		this.comments = comments;
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
	
	public String getLog() {
		return log;
	}
	
	public void setLog(String log) {
		this.log = log;
	}
	
	public List<TransferControlLine> getLines() {
		return lines;
	}

	public void setLines(List<TransferControlLine> lines) {
		this.lines = lines;
	}

}
