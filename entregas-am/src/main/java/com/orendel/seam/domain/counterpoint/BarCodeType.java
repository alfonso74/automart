package com.orendel.seam.domain.counterpoint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "IM_BARCOD_ID")
public class BarCodeType {
	
	@Id
	@Column(name = "BARCOD_ID")
	private String barCodeId;
	
	@Column(name = "DESCR")
	private String description;

	
	
	
	public String getBarCodeId() {
		return barCodeId;
	}

	public void setBarCodeId(String barCodeId) {
		this.barCodeId = barCodeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
