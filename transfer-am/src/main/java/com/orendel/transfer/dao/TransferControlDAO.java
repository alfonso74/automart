package com.orendel.transfer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.orendel.delivery.domain.TransferControl;
import com.orendel.transfer.config.Datasource;


public class TransferControlDAO extends GenericDAOImpl<TransferControl, Long> {

	public TransferControlDAO() {
		super();
		dataSource = Datasource.SEAM;
	}
	
	public String getNextCode(String prefix) {
		String nativeQuery = "select COALESCE(REPLACE(MAX(XFER_NO), :prefix01, ''), 0) + 1 as next_code "
				+ "from AM_TC "
				+ "where XFER_NO like :prefix02";
		getSession().beginTransaction();
		Query query = getSession().createSQLQuery(nativeQuery);
		query.setParameter("prefix01", prefix);
		query.setParameter("prefix02", prefix + "%");
		Integer next_code = (Integer) query.uniqueResult();
		getSession().getTransaction().commit();
		
		return next_code.toString();
	}
	
	public List<TransferControl> findByDateRange(Date initialDate, Date endDate) {
		List<TransferControl> deliveryList = new ArrayList<TransferControl>();
		String queryHQL = "from TransferControl d "
				+ "where d.created >= :initialDate and d.created <= :endDate "
				+ "order by d.created";
		getSession().beginTransaction();
		Query query = getSession().createQuery(queryHQL);
		query.setParameter("initialDate", initialDate);
		query.setParameter("endDate", endDate);
		deliveryList = query.list();
		getSession().getTransaction().commit();
		
		return deliveryList;
	}

}
