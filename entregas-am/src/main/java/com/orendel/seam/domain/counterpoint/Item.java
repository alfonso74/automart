package com.orendel.seam.domain.counterpoint;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "IM_ITEM")
public class Item {

	/** Número del item (PK) */
	@Id
	@Column(name = "ITEM_NO")
	private String itemNo;
	
	/** Nombre o descripción del item */
	@Column(name = "DESCR")
	private String description;

	/** Listado de códigos de barra asociados a este item */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item", fetch = FetchType.LAZY)
	private List<BarCode> barcodeList;
	
	
	
	
	
	// ***************************** Getters and setters *****************************
	
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<BarCode> getBarcodeList() {
		if (barcodeList == null) {
			barcodeList = new ArrayList<BarCode>();
		}
		return barcodeList;
	}

	public void setBarcodeList(List<BarCode> barcodeList) {
		this.barcodeList = barcodeList;
	}

}
