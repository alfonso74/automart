package com.orendel.seam.domain.counterpoint;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "PS_DOC_LIN")
public class InvoiceActiveLine extends InvoiceLine {

	@ManyToOne
    @JoinColumn(name = "DOC_ID")
	private InvoiceActive factura;
	
}
