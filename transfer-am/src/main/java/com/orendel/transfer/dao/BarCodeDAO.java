package com.orendel.transfer.dao;

import java.util.List;

import org.hibernate.SQLQuery;

import com.orendel.counterpoint.domain.BarCode;


public class BarCodeDAO extends GenericDAOImpl<BarCode, Long> {
	
	@Override
	public void doSave(BarCode entity) {
		if (entity.getItem() == null) {
			throw new IllegalArgumentException("Para persistir un código de barra, el mismo debe estar asociado a un artículo.");
		}
		if (entity.getType() == null) {
			throw new IllegalArgumentException("Para persistir un código de barra, el mismo debe asociarse a un tipo.");
		}
				
		getSession().beginTransaction();
		if (entity.getSequence() == null) {
			// creating a new barcode entity
			entity.setSequence(getNextSequence());
			String insertBarcod = "insert into IM_BARCOD (ITEM_NO, SEQ_NO, BARCOD, BARCOD_ID, LST_MAINT_DT, LST_MAINT_USR_ID) "
					+ "values (:itemNo, :sequence, :code, :barcodId, :updated, :userId)";
			SQLQuery query = getSession().createSQLQuery(insertBarcod);
			query.setParameter("itemNo", entity.getItem().getItemNo());
			query.setParameter("sequence", entity.getSequence());
			query.setParameter("code", entity.getCode());
			query.setParameter("barcodId", entity.getType().getBarCodeId());
			query.setParameter("updated", entity.getUpdated());
			query.setParameter("userId", entity.getUserId());
			query.executeUpdate();
		} else {
			// updating a barcode entity
			getSession().saveOrUpdate(entity);
		}
		
		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	private Integer getNextSequence() {
		Integer nextSequence = null;
		String nextSequenceQuery = "select MAX(SEQ_NO) from IM_BARCOD";
		SQLQuery query = getSession().createSQLQuery(nextSequenceQuery);
		List<Integer> results = query.list();
		if (results != null && !results.isEmpty()) {
			nextSequence = results.get(0) + 1;
			System.out.println("SEQUENCE: " + nextSequence);
		}
		return nextSequence;
	}
	
}
