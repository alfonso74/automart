package com.orendel.seam.domain.counterpoint;

import java.io.Serializable;

public class InvoiceLinePK implements Serializable {
	
	private static final long serialVersionUID = -7827543570595926360L;
	
	protected Long docId;
	protected Integer lineaId;

	
	public InvoiceLinePK() {
	}
	
	public InvoiceLinePK(Long docId, Integer lineaId) {
		this.docId = docId;
		this.lineaId = lineaId;
	}
	

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Integer getLineaId() {
		return lineaId;
	}

	public void setLineaId(Integer lineaId) {
		this.lineaId = lineaId;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docId == null) ? 0 : docId.hashCode());
		result = prime * result + ((lineaId == null) ? 0 : lineaId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvoiceLinePK other = (InvoiceLinePK) obj;
		if (docId == null) {
			if (other.docId != null)
				return false;
		} else if (!docId.equals(other.docId))
			return false;
		if (lineaId == null) {
			if (other.lineaId != null)
				return false;
		} else if (!lineaId.equals(other.lineaId))
			return false;
		return true;
	}
	
	
	

}
