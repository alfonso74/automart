package com.orendel.transfer.util;

import java.util.Date;

import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.counterpoint.domain.TransferInLine;
import com.orendel.transfer.domain.TransferControl;
import com.orendel.transfer.domain.TransferControlLine;
import com.orendel.transfer.domain.TransferControlStatus;

public final class TransferMapper {
	
	public static TransferIn from(TransferControl source) {
		TransferIn result = new TransferIn();
		return result;
	}
	
	public static TransferControl from(TransferIn transferIn) {
		TransferControl control = new TransferControl();
		
		control.setTransferNo(transferIn.getId());
		control.setReference(transferIn.getReference());
		control.setComments(transferIn.getComments());
		control.setCreated(new Date());
		control.setStatus(TransferControlStatus.PARTIAL.getCode());
		for (TransferInLine line : transferIn.getLines()) {
			TransferControlLine tcLine = from(line);
			tcLine.setTransferNo(control.getTransferNo());
			control.addTransferControlLine(tcLine);
		}
		
		return control;
	}

	
	private static TransferControlLine from(TransferInLine transferInLine) {
		TransferControlLine tcLine = new TransferControlLine();
		
		tcLine.setLineId(transferInLine.getLineId());
		tcLine.setPositionId(transferInLine.getPositionId());
		tcLine.setComments(transferInLine.getComments());
		tcLine.setQtyPrevExpected(transferInLine.getQtyPrevExpected());
		tcLine.setQtyReceived(transferInLine.getQtyReceived());
		tcLine.setQtyNewExpected(transferInLine.getQtyNewExpected());
		tcLine.setEstUnitCost(transferInLine.getEstUnitCost());
		tcLine.setEstExtCost(transferInLine.getEstExtCost());
		
		tcLine.setItemNumber(transferInLine.getItem().getItemNo());
		tcLine.setItemDescription(transferInLine.getItem().getDescription());
		
		return tcLine;
	}
}
