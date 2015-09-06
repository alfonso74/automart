package com.orendel.transfer.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.orendel.transfer.domain.Permission;


public class PermissionTest {
	
	private static final Logger logger = Logger.getLogger(PermissionTest.class);
	
	
	@BeforeClass
	public static void init() {
		logger.info("INIT PermissionTest!");
	}
	
	@Before
	public void setup() {

	}
	
	@Test
	public void testFromRuleValue_OnePermissionRecognized() {
		PermissionResolver resolver = new PermissionResolver();
		List<Permission> permissions = resolver.fromRuleValue("2");
		assertThat(permissions, hasItem(Permission.EDIT_BARCODE));
	}
	
	@Test
	public void testFromRuleValue_TwoPermissionsRecognized() {
		PermissionResolver resolver = new PermissionResolver();
		List<Permission> permissions = resolver.fromRuleValue("3");
		assertThat(permissions.size(), is(2));
		assertThat(permissions, hasItems(Permission.ADMIN, Permission.EDIT_BARCODE));
	}
	
	@Test
	public void testFromRuleValue_Zero() {
		PermissionResolver resolver = new PermissionResolver();
		List<Permission> permissions = resolver.fromRuleValue("0");
		assertThat(permissions.isEmpty(), is(true));
	}
	
	@Test
	public void testFromRuleValue_Empty() {
		PermissionResolver resolver = new PermissionResolver();
		List<Permission> permissions = resolver.fromRuleValue("");
		assertThat(permissions.isEmpty(), is(true));
	}
	
	@Test
	public void testFromRuleValue_NullValue() {
		PermissionResolver resolver = new PermissionResolver();
		List<Permission> permissions = resolver.fromRuleValue(null);
		assertThat(permissions.isEmpty(), is(true));
	}
	
	@Test
	public void testAsRuleValue_OnePermissionAssigned() {
		PermissionResolver resolver = new PermissionResolver();
		String ruleValue = resolver.toRuleValue(Permission.EDIT_BARCODE);
		assertThat(ruleValue, is("2"));
	}
	
	@Test
	public void testAsRuleValue_TwoPermissionsAssigned() {
		PermissionResolver resolver = new PermissionResolver();
		String ruleValue = resolver.toRuleValue(Permission.EDIT_BARCODE, Permission.ADMIN);
		assertThat(ruleValue, is("3"));
	}
	
	@Test
	public void testAsRuleValue_NoPermissionsAssigned() {
		PermissionResolver resolver = new PermissionResolver();
		String ruleValue = resolver.toRuleValue();
		assertThat(ruleValue, is("0"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testValidateRuleValue_IllegalRuleValueArgument() {
		PermissionResolver resolver = new PermissionResolver();
		resolver.validateRuleValue("99");
	}
	
	@Test
	public void testValidateRuleValue_MaxCurrentValue() {
		PermissionResolver resolver = new PermissionResolver();
		String maxRuleValue = resolver.validateRuleValue("0");
		assertThat(maxRuleValue, is("3"));
	}
}
