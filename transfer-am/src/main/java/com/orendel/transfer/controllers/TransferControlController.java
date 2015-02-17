package com.orendel.transfer.controllers;

import com.orendel.transfer.dao.TransferControlDAO;
import com.orendel.transfer.domain.TransferControl;


public class TransferControlController extends AbstractControllerDelivery<TransferControl> {

	public TransferControlController() {
		super(new TransferControlDAO());
	}

	public TransferControlController(String editorId) {
		super(editorId, new TransferControlDAO());
	}

}
