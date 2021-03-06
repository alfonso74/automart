package com.orendel.transfer.util;

import java.math.BigDecimal;

import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.counterpoint.domain.TransferInLine;
import com.orendel.delivery.domain.TransferControl;
import com.orendel.delivery.domain.TransferControlLine;

public class TransferUpdater {
	
	public void updateTransferInFromTransferControl(TransferIn in, TransferControl tc) {
		//TODO Verificar existencia de todas las líneas del tc en in, y en caso contrario reventar olímpicamente
		for (TransferControlLine tcLine : tc.getLines()) {
			TransferInLine inLine = in.findLineById(tcLine.getLineId());
			if (inLine == null) {
				// Lanzar exception
			}					
		}
		in.setTotalSelectedLines(tc.getTotalSelectedLines());
		in.setReference(tc.getReference());
		in.setComments(tc.getComments().getComment1(), 
				tc.getComments().getComment2(), 
				tc.getComments().getComment3());
		in.setQtyReceived(new BigDecimal(tc.getTotalReceivedItems()));
		in.setSubTotal(tc.getTotalReceivedItemsValue());
		in.setTotal(tc.getTotalReceivedItemsValue());
		for (TransferControlLine tcLine : tc.getLines()) {
			TransferInLine inLine = in.findLineById(tcLine.getLineId());
			updateLine(inLine, tcLine);
		}
	}
	
	private void updateLine(TransferInLine inLine, TransferControlLine tcLine) {
		inLine.setSelected(tcLine.getSelected());
		inLine.setComments(tcLine.getComments().getComment1(), 
				tcLine.getComments().getComment2(), 
				tcLine.getComments().getComment3());
		inLine.setQtyPrevExpected(tcLine.getQtyPrevExpected());
		inLine.setQtyReceived(tcLine.getQtyReceived());
		inLine.setQtyNewExpected(tcLine.getQtyNewExpected());
		inLine.setEstExtCost(tcLine.getEstExtCost());
	}

}
