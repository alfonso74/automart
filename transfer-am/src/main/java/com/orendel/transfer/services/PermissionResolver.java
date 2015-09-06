package com.orendel.transfer.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.orendel.transfer.domain.Permission;


public class PermissionResolver {
	
	/**
	 * Transforms a "rule" string value into a {@link Permission} list. 
	 * @param ruleValue the value to be transformed
	 * @return a {@link List} with the permissions representing the indicated "rule".  If the "rule" value
	 * is <code>null</code>, an empty {@link String}, or a "0" string, returns a {@link Collections#emptyList()}.
	 */
	public List<Permission> fromRuleValue(String ruleValue) {
		List<Permission> permissions = new ArrayList<Permission>();
		if (ruleValue == null || ruleValue.isEmpty()) {
			return Collections.emptyList();
		}
		int ruleAsInt = Integer.parseInt(ruleValue);
		if (ruleAsInt == 0) {
			return Collections.emptyList();
		} else {
			for (Permission v : Permission.values()) {
				if ((ruleAsInt & v.getValue()) == v.getValue()) {
					permissions.add(v);
				}
			}
		}
		return permissions;
	}
	
	/**
	 * Transforms a group of "permissions" into a rule {@link String} value. 
	 * @param permissions the {@link Permission} group to be transformed
	 * @return a rule {@link String} value representing the selected permissions.
	 */
	public String toRuleValue(Permission... permissions) {
		int ruleValue = 0;
		for (Permission v : permissions) {
			ruleValue = ruleValue | v.getValue();
		}
		return String.valueOf(ruleValue);
	}
	
	/**
	 * Validates that the indicated rule value can be represented by the {@link Permission} enumeration.
	 * @param ruleValue a "rule" value to be tested
	 * @return a {@link String} with the maximum rule value that can be used in the system.
	 */
	public String validateRuleValue(String ruleValue) {
		boolean isValid = true;
		int maxAllowedValue = 0;
		if (ruleValue != null && !ruleValue.isEmpty()) {
			int ruleAsInt = Integer.parseInt(ruleValue);
			for (Permission v : Permission.values()) {
				maxAllowedValue = maxAllowedValue | v.getValue();
			}
			isValid = maxAllowedValue >= ruleAsInt;
		}
		if (!isValid) {
			throw new IllegalArgumentException("The indicated rule value cannot be represented by the Permission "
					+ "enumeration (value: " + ruleValue + ").");
		}
		return String.valueOf(maxAllowedValue);
	}

}
