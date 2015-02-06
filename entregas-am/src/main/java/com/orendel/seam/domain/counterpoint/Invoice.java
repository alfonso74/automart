package com.orendel.seam.domain.counterpoint;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class Invoice {
	
	@Id
	@GeneratedValue
	@Column(name = "DOC_ID")
	private Long id;

	@Column(name = "TKT_NO")
	private String ticket;
	


	// **************************** Métodos de dominio ***************************
	
	/**
	 * Busca la(s) línea(s) de la factura que tenga(n) el item indicado.
	 * @param item item que será buscado en la factura
	 * @return un listado de objetos InvoiceLine que tienen el item indicado, o una lista vacía si no se encontró 
	 * ninguna línea con el código de item indicado.
	 */
	public List<InvoiceLine> locateLinesWithItem(Item item) {
		List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
		if (this.getLines() == null || this.getLines().isEmpty()) {
			return invoiceLines;
		}
		for (InvoiceLine l : this.getLines()) {
			//TODO el equals de un Item debe ser por equivalencia de código Y secuencia
			if (l.getItem().getItemNo().equals(item.getItemNo())) {
				invoiceLines.add(l);
			}
		}
		return invoiceLines;
	}
	
	/**
	 * Busca la(s) línea(s) de la factura que tenga(n) el código de item indicado.
	 * NOTA:  un mismo código puede ser utilizado por varios items (por tener varios códigos de barra) o incluso
	 * por items diferentes.
	 * @param itemCode código de item que será buscado en las líneas de la factura.
	 * @return un listado de objetos InvoiceLine que tienen el item indicado, o una lista vacía si no se encontró 
	 * ninguna línea con el código de item indicado.
	 */
	public List<InvoiceLine> locateLinesWithItemCode(String itemCode) {
		List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
		if (this.getLines() == null || this.getLines().isEmpty()) {
			return invoiceLines;
		}
		for (InvoiceLine l : this.getLines()) {
			if (l.getItem().getItemNo().equals(itemCode)) {
				invoiceLines.add(l);
			}
		}
		return invoiceLines;
	}
		
	
	// ***************************** Getters and setters *****************************
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	
	abstract public List<InvoiceLine> getLines();
	
	abstract public void setLines(List<InvoiceLine> lines);
	
}
