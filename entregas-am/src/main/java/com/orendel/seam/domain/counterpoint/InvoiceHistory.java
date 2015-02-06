package com.orendel.seam.domain.counterpoint;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "PS_TKT_HIST")
public class InvoiceHistory extends Invoice {

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "factura", fetch = FetchType.EAGER)
	private List<InvoiceHistoryLine> invoiceHistoryLines;


	public List<InvoiceLine> getLines() {
		List<InvoiceLine> lines = new ArrayList<InvoiceLine>();
		lines.addAll(invoiceHistoryLines);
		return lines;
	}

	public void setLines(List<InvoiceLine> lines) {
		invoiceHistoryLines.clear();
		for (InvoiceLine line : lines) {
			invoiceHistoryLines.add((InvoiceHistoryLine) line);
		}
	}

}
