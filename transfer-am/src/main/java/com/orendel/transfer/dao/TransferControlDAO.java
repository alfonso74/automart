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
