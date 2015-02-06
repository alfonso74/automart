package com.orendel.seam.domain;

public enum Status {
	/** Active delivery/user */
	ACTIVE("A", "Activo"),
	/** Closed delivery */
	CLOSED("C", "Cerrado"),
	/** Inactive user */
	INACTIVE("I", "Inactivo"),
	/** Partial/Pending delivery */
	PARTIAL("P", "Parcial"),
	;

	private String code;
	private String description;
	
	
	private Status(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static Status fromDescription(String description) {
		for (Status v : values()) {
			if (v.getDescription().equals(description)) {
				return v;
			}
		}
		throw new IllegalArgumentException("No se encontró el estado con descripción: " + description + ".");
	}
	
	public static Status fromCode(String code) {
		for (Status v : values()) {
			if (v.getCode().equals(code)) {
				return v;
			}
		}
		throw new IllegalArgumentException("No se encontró el estado con código: " + code + ".");
	}
}
