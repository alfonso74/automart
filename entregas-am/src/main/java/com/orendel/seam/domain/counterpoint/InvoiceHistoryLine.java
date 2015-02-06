package com.orendel.seam.domain.counterpoint;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "PS_TKT_HIST_LIN")
public class InvoiceHistoryLine extends InvoiceLine {

	@ManyToOne
    @JoinColumn(name = "DOC_ID")
	private InvoiceHistory factura;
	
}
