package com.orendel.transfer.config;

import org.hibernate.Session;

import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.services.HibernateUtilDelivery;

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
