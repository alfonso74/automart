package com.orendel.delivery.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.orendel.delivery.domain.helper.PermissionResolver;

@Entity
@Table(name = "AM_USERS")
public class User {

	
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "FIRSTNAME")
	private String firstName;
	
	@Column(name = "LASTNAME")
	private String lastName;
	
	@Column(name = "USERNAME")
	private String userName;

	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "STATUS")
	private String status;
	
//	@Column(name = "ADMIN", columnDefinition="varchar")
//	private Boolean isAdmin;

	@Column(name = "ADMIN", columnDefinition="varchar")
	private String permissionsRule;
	
	public User() {
	}

	
	// ****************************** Helper methods ********************************
	
	@Override
	public String toString() {
		return "User (id-name): " + getId() + "-" + getFullName();
	}
	
	public String getFullName() {
		String firstName = getFirstName() == null ? "" : getFirstName();
		String lastName = getLastName() == null ? "" : getLastName();
		return firstName + " " + lastName;
	}
	
	public List<Permission> getPermissions() {
		PermissionResolver resolver = new PermissionResolver();
		return resolver.fromRuleValue(permissionsRule);
	}
	
	public void setPermissions(List<Permission> permissions) {
		PermissionResolver resolver = new PermissionResolver();
		this.permissionsRule = resolver.toRuleValue(permissions);
	}
	
	public Boolean isAdmin() {
		return getPermissions().contains(Permission.ADMIN);
	}
	
	public Boolean canEditBarcodes() {
		return getPermissions().contains(Permission.EDIT_BARCODE);
	}
	
	// ***************************** Getters and setters ********************************
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getPermissionsRule() {
		return permissionsRule;
	}


	public void setPermissionsRule(String permissionsRule) {
		PermissionResolver resolver = new PermissionResolver();
		resolver.validateRuleValue(permissionsRule);
		this.permissionsRule = permissionsRule;
	}
	
	
//	public Boolean isAdmin() {
//		return isAdmin;
//	}
//
//
//	public void setAdmin(Boolean isAdmin) {
//		this.isAdmin = isAdmin == null ? Boolean.FALSE : isAdmin.booleanValue();
//	}
	
}
