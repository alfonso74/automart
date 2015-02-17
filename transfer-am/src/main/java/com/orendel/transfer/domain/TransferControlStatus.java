package com.orendel.transfer.domain;


public enum TransferControlStatus {
	
	/** Active delivery/user */
	ACTIVE("A", "Activo"),
	/** Closed delivery */
	CLOSED("C", "Cerrado"),
	/** Partial/Pending delivery */
	PARTIAL("P", "Parcial"),
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
