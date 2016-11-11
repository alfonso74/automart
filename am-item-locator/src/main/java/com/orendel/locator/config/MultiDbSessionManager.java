package com.orendel.locator.config;

import org.hibernate.Session;

import com.orendel.locator.services.HibernateUtil;
import com.orendel.locator.services.HibernateUtilDelivery;


public final class MultiDbSessionManager {
	
	public static Session getSessionForDatasource(Datasource dataSource) {
		switch(dataSource) {
		case SEAM:
			return HibernateUtilDelivery.getSessionFactorySQL().getCurrentSession();
		case COUNTERPOINT:
			return HibernateUtil.getSessionFactorySQL().getCurrentSession();
		default:
			throw new IllegalArgumentException("Datasource not defined: " + dataSource);
		}
	}
}
