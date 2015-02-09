package com.orendel.transfer.controllers;

import java.util.List;

import com.orendel.transfer.dao.UserDAO;
import com.orendel.transfer.domain.User;


public class UsersController extends AbstractControllerDelivery<User> {

	public UsersController() {
		super(new UserDAO());
	}

	public UsersController(String editorId) {
		super(editorId, new UserDAO());
	}

	
	public boolean verifyPassword_MD5(String userName, String encodedPassword) {
		UserDAO dao = (UserDAO) getDAO();
		return dao.verifyPasswordMD5(userName, encodedPassword);
	}
	
	
	public User findUserByUsername(String userName) {
		User user = null;
		List<User> userList = getDAO().findByField("userName", userName);
		if (userList != null && userList.size() > 0) {
			user = userList.get(0);
		}
		return user;
	}
}
