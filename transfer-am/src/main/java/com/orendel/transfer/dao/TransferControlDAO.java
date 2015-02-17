package com.orendel.transfer.dao;

import com.orendel.transfer.config.Datasource;
import com.orendel.transfer.domain.TransferControl;


public class TransferControlDAO extends GenericDAOImpl<TransferControl, Long> {

	public TransferControlDAO() {
		super();
		dataSource = Datasource.DELIVERY;
	}

}
