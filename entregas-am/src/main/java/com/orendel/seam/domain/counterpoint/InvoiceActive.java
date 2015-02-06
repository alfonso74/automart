package com.orendel.seam.domain.counterpoint;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "PS_DOC_HDR")
public class InvoiceActive extends Invoice {

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "factura", fetch = FetchType.EAGER)
	private List<InvoiceActiveLine> invoiceActiveLines;


	public List<InvoiceLine> getLines() {
		List<InvoiceLine> lines = new ArrayList<InvoiceLine>();
		lines.addAll(invoiceActiveLines);
		return lines;
	}

	public void setLines(List<InvoiceLine> lines) {
		if (invoiceActiveLines == null) {
			invoiceActiveLines = new ArrayList<InvoiceActiveLine>();
		}
		invoiceActiveLines.clear();
		for (InvoiceLine line : lines) {
			invoiceActiveLines.add((InvoiceActiveLine) line);
		}
	}

}
