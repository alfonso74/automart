package com.orendel.locator.dao;

import org.hibernate.Query;

import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.counterpoint.domain.TransferInLine;


public class TransferInDAO extends GenericDAOImpl<TransferIn, Long> {
	
	public void updateCounterpointTransferIn(TransferIn transferIn) {
		getSession().beginTransaction();
		updateCounterPointTxInLine(transferIn);
		getSession().saveOrUpdate(transferIn);
		getSession().flush();
		getSession().getTransaction().commit();
	}
	
	
	private void updateCounterPointTxInLine(TransferIn transferIn) {
		for (TransferInLine line : transferIn.getLines()) {
			String updateTable_IM_INV = "update IM_INV set qty_commit = qty_commit - :qtyReceived, "
					+ "qty_on_xfer_in = "
					+ "(case "
					+ "   when qty_on_xfer_in > 0 then qty_on_xfer_in - :qtyReceived "
					+ "   else qty_on_xfer_in "
					+ "end) "
					+ "where item_no = :itemNo and loc_id = :toLocation";
			Query query = getSession().createSQLQuery(updateTable_IM_INV);
			query.setParameter("qtyReceived", line.getQtyReceived());
			query.setParameter("itemNo", line.getItem().getItemNo());
			query.setParameter("toLocation", transferIn.getLocationTo());
			query.executeUpdate();
		}
	}

}
