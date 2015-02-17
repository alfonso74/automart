package com.orendel.counterpoint.domain;

import java.io.Serializable;

public class TransferInLinePK implements Serializable {
	
	private static final long serialVersionUID = -7827543570595926360L;
	
	protected String transferId;
	protected Integer lineId;

	
	public TransferInLinePK() {
	}
	
	public TransferInLinePK(String transferId, Integer lineId) {
		this.transferId = transferId;
		this.lineId = lineId;
	}
	

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((transferId == null) ? 0 : transferId.hashCode());
		result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
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
		TransferInLinePK other = (TransferInLinePK) obj;
		if (transferId == null) {
			if (other.transferId != null)
				return false;
		} else if (!transferId.equals(other.transferId))
			return false;
		if (lineId == null) {
			if (other.lineId != null)
				return false;
		} else if (!lineId.equals(other.lineId))
			return false;
		return true;
	}
	
	
	

}
