package com.orendel.seam.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.orendel.seam.config.Datasource;
import com.orendel.seam.domain.Status;
import com.orendel.seam.domain.delivery.Delivery;


public class DeliveryDAO extends GenericDAOImpl<Delivery, Long> {
	
	
	public DeliveryDAO() {
		super();
		dataSource = Datasource.DELIVERY;
	}


	public List<Delivery> findAllDeliveries() {
		List<Delivery> deliveryList = new ArrayList<Delivery>();
		String queryHQL = "from Delivery d "
				+ "inner join fetch d.deliveryLines l "
				+ "order by d.created";
		
		getSession().beginTransaction();
		Query query = getSession().createQuery(queryHQL);
		deliveryList = query.list();
		getSession().getTransaction().commit();
		
		return deliveryList;
	}
	
	
	public List<Delivery> findByDateRange(Date initialDate, Date endDate) {
		List<Delivery> deliveryList = new ArrayList<Delivery>();
		String queryHQL = "from Delivery d "
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
	
	
	public List<Delivery> findByDateRangeAndStatus(Date initialDate, Date endDate, Status status) {
		List<Delivery> deliveryList = new ArrayList<Delivery>();
		String queryHQL = "from Delivery d "
				+ "where d.created >= :initialDate and d.created <= :endDate "
				+ "and d.status = :status "
				+ "order by d.created";
		getSession().beginTransaction();
		Query query = getSession().createQuery(queryHQL);
		query.setParameter("initialDate", initialDate);
		query.setParameter("endDate", endDate);
		query.setParameter("status", status.getCode());
		deliveryList = query.list();
		getSession().getTransaction().commit();
		
		return deliveryList;
	}
	
}
