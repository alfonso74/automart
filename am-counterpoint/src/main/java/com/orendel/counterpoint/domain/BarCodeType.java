package com.orendel.counterpoint.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Define los diferentes TIPOS de código de barra que son utilizados en el sistema.  Estos tipos son
 * reutilizados en la definición de todos los códigos de barra ({@link BarCode}) asociados a un
 * {@link Item} en particular.
 * @author Admin
 *
 */
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
