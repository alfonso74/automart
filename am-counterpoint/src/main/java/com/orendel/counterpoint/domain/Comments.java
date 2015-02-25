package com.orendel.counterpoint.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class Comments {
	
	@Column(name = "COMMNT_1")
	private String comment1;
	
	@Column(name = "COMMNT_2")
	private String comment2;
	
	@Column(name = "COMMNT_3")
	private String comment3;

	
	// ***************************** Getters and Setters ************************************
	
	public String getComment1() {
		return comment1;
	}

	public void setComment1(String comment1) {
		this.comment1 = comment1;
	}

	public String getComment2() {
		return comment2;
	}

	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}

	public String getComment3() {
		return comment3;
	}

	public void setComment3(String comment3) {
		this.comment3 = comment3;
	}
	
}
