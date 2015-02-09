package com.orendel.transfer.ui.login;

import com.orendel.transfer.domain.User;

public enum LoggedUserService {
	
	INSTANCE;
	
	private User user = null;
	
	
	private LoggedUserService() {
	}

	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
