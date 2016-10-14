package com.orendel.delivery.domain;


public enum TransferControlStatus {
	
	/** Active */
	ACTIVE("A", "Activo"),
	/** Closed */
	CLOSED("C", "Cerrado"),
	/** Partial/Pending */
	PARTIAL("P", "Parcial"),
	/** Canceled */
	CANCELED("X", "Cancelada")
	;

	private String code;
	private String description;
	
	
	private TransferControlStatus(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static TransferControlStatus fromDescription(String description) {
		for (TransferControlStatus v : values()) {
			if (v.getDescription().equals(description)) {
				return v;
			}
		}
		throw new IllegalArgumentException("No se encontró el estado con descripción: " + description + ".");
	}
	
	public static TransferControlStatus fromCode(String code) {
		for (TransferControlStatus v : values()) {
			if (v.getCode().equals(code)) {
				return v;
			}
		}
		throw new IllegalArgumentException("No se encontró el estado con código: " + code + ".");
	}

}
