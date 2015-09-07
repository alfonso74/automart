package com.orendel.transfer.domain;

import java.util.Collections;
import java.util.List;

public enum Permission {
	
//	NONE(0),				// no bit is set
	ADMIN("Administrador", 1 << 0),			// 1
	EDIT_BARCODE("Códigos de barra", 1 << 1),	// 2
	;
	
	private String name;
	private int value;
	
	private Permission(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	
	public List<Permission> fromRuleValue(String ruleValue) {
		List<Permission> permissions = Collections.emptyList();
		int ruleAsInt = Integer.parseInt(ruleValue);
		for (Permission v : Permission.values()) {
			if ((ruleAsInt & v.value) == v.value) {
				permissions.add(v);
			}
		}
		return permissions;
	}
	
	public String asRuleValue(List<Permission> permissions) {
		int ruleValue = 0;
		for (Permission v : permissions) {
			ruleValue = ruleValue | v.value;
		}
		return null;
	}
}
