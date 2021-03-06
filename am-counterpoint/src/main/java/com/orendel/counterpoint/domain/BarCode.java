package com.orendel.counterpoint.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Clase para el manejo de códigos de barra. 
 * Llave primaria compuesta por el ID del item, y por un secuencial.
 * Asociado a un {@link Item} y a un tipo de código de barra ({@link BarCodeType}).  
 * @author Admin
 *
 */
@Entity
@IdClass(BarCodePK.class)
@Table(name = "IM_BARCOD")
public class BarCode {

	/** Id del item al que está asociado este código de barra (PK) */
	@Id
	@Column(name = "ITEM_NO", insertable = false, updatable = false)
	private String itemNo;

	/** Secuencial del código de barra (PK).  Parte de la llave compuesta. */
	@Id
	@Column(name = "SEQ_NO")
	private Integer sequence;
	
	/** Código de barra */
	@Column(name = "BARCOD")
	private String code;
	
	/** Fecha de última actualización */
	@Column(name = "LST_MAINT_DT")
	private Date updated;
	
	/** Usuario que realizó la última actualización */
	@Column(name = "LST_MAINT_USR_ID")
	private String userId;
	
	/** Item al que está asociado este código de barra */
	@ManyToOne
    @JoinColumn(name = "ITEM_NO")
	private Item item;
	
	/** Tipo del código de barra (default, UPC, alterno1, alterno2, etc)  */
	@ManyToOne
    @JoinColumn(name = "BARCOD_ID")
	private BarCodeType type;

	
	
	
	// ***************************** Getters and setters *****************************
	
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		if (userId.length() > 10) {
			this.userId = userId.substring(0, 10);
		} else {
			this.userId = userId;
		}
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public BarCodeType getType() {
		return type;
	}

	public void setType(BarCodeType type) {
		this.type = type;
	}
	
}
