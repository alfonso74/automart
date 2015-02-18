package com.orendel.transfer.dao;

import java.util.List;

import org.hibernate.Query;

import com.orendel.transfer.config.Datasource;
import com.orendel.transfer.domain.User;


public class UserDAO extends GenericDAOImpl<User, Long> {
	
	public UserDAO() {
		super();
		dataSource = Datasource.SEAM;
	}
	
	public boolean verifyPasswordMD5(String userName, String encodedPassword) {
		boolean result = false;
		String queryHQL = "from User u "
				+ "where u.userName = :userName and u.password = :password";
		getSession().beginTransaction();
		Query query = getSession().createQuery(queryHQL);
		query.setParameter("userName", userName);
		query.setParameter("password", encodedPassword);
		List<User> userList = query.list();
		getSession().getTransaction().commit();
		if (userList != null && !userList.isEmpty()) {
			result = true;
		}
		return result;
	}

}
