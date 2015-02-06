package com.orendel.seam.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "COUNTRY_ISO3166")
public class Country {
	
	@Id
	@GeneratedValue
	@Column(name = "COU_ID")
	private Long id;
	
	@Column(name = "COU_NAME")
	private String name;
	
	@Column(name = "COU_A2_CODE")
	private String codeA2;
	
	@Column(name = "COU_A3_CODE")
	private String codeA3;
	
	@Column(name = "COU_NUMBER_CODE")
	private Long codeNumber;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodeA2() {
		return codeA2;
	}

	public void setCodeA2(String codeA2) {
		this.codeA2 = codeA2;
	}

	public String getCodeA3() {
		return codeA3;
	}

	public void setCodeA3(String codeA3) {
		this.codeA3 = codeA3;
	}

	public Long getCodeNumber() {
		return codeNumber;
	}

	public void setCodeNumber(Long codeNumber) {
		this.codeNumber = codeNumber;
	}

}
