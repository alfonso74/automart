package com.orendel.counterpoint.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
	
	/** Fecha de última actualización */
	@Column(name = "LST_MAINT_DT", updatable=false)
	private Date updated;

	/** Listado de códigos de barra asociados a este item */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item", fetch = FetchType.LAZY)
	private List<BarCode> barcodeList;
	
	/** Inventario asociado a este item */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "item", fetch = FetchType.LAZY)
	private List<Inventory> inventory;
	
	
	// ******************************* Helper methods ********************************
	
	public String getUpdatedDateLabel() {
		String label = "";
		if (getUpdated() != null) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(getUpdated());
			String yy = Integer.toString(calendar.get(Calendar.YEAR));
			String mm = '0' + Integer.toString(calendar.get(Calendar.MONTH) + 1);
			String dd = '0' + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
			label = "A" + yy.substring(yy.length() - 2) + "H" + mm.substring(mm.length() - 2) + "T" + dd.substring(dd.length() - 2);
		}
		return label;
	}
	
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
	
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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

	public List<Inventory> getInventory() {
		if (inventory == null) {
			inventory = new ArrayList<Inventory>();
		}
		return inventory;
	}

	public void setInventory(List<Inventory> inventory) {
		this.inventory = inventory;
	}
	
}
