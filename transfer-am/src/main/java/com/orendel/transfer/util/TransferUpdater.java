package com.orendel.transfer.util;

import java.math.BigDecimal;

import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.counterpoint.domain.TransferInLine;
import com.orendel.transfer.domain.TransferControl;
import com.orendel.transfer.domain.TransferControlLine;

public class TransferUpdater {
	
	public void updateTransferInFromTransferControl(TransferIn in, TransferControl tc) {
		//TODO Verificar existencia de todas las líneas del tc en in, y en caso contrario reventar olímpicamente
		in.setReference(tc.getReference());
		in.setComments(tc.getComments());
		in.setQtyReceived(new BigDecimal(tc.getTotalReceivedItems()));
		in.setSubTotal(tc.getTotalReceivedItemsValue());
		in.setTotal(tc.getTotalReceivedItemsValue());
		for (TransferControlLine tcLine : tc.getLines()) {
			TransferInLine inLine = in.findLineById(tcLine.getLineId());
			updateLine(inLine, tcLine);
		}
	}
	
	private void updateLine(TransferInLine inLine, TransferControlLine tcLine) {
		inLine.setComments(tcLine.getComments());
		inLine.setQtyPrevExpected(tcLine.getQtyPrevExpected());
		inLine.setQtyReceived(tcLine.getQtyReceived());
		inLine.setQtyNewExpected(tcLine.getQtyNewExpected());
		inLine.setEstExtCost(tcLine.getEstExtCost());
	}

}
