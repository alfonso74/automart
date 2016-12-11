package com.orendel.transfer.services;

import java.math.BigDecimal;
import java.util.List;

import com.orendel.delivery.domain.TransferControl;
import com.orendel.delivery.domain.TransferControlLine;

public class TransferControlHelper {
	
	private TransferControl tc;
	
	public TransferControlHelper(TransferControl tc) {
		this.tc = tc;
	}
	
	public TransferControlLine adjustReceivedQuantityForItem(String itemNo, int qtyReceived) {
		TransferControlLine tcLine = null;
		List<TransferControlLine> tcLines = tc.locateLinesWithItem(itemNo);
		
		if (!tcLines.isEmpty()) {
			tcLine = tcLines.get(0);
			tcLine.setQtyPrevExpected(tcLine.getQtyPrevExpected().
					add(new BigDecimal(qtyReceived))); // mantiene el line.qtyPrevExpected en 0
			tcLine.adjustQuantities(qtyReceived);
		}

		return tcLine;
	}
	
	public void addTransferControlLine(String itemNumber, String itemDescription, int itemQty) {
		int nextLineIndex = tc.getLines().size() + 1;
		
		TransferControlLine line = new TransferControlLine();
		line.setTransferNo(tc.getTransferNo());
		line.setLineId(nextLineIndex);
		line.setPositionId(nextLineIndex);
		line.setSelected("Y");
		line.setQtyPrevExpected(new BigDecimal(itemQty));
		line.setQtyReceived(new BigDecimal(itemQty));
		line.setQtyNewExpected(new BigDecimal(0));
		line.setEstUnitCost(new BigDecimal(0));
		line.setEstExtCost(new BigDecimal(0));
		
		line.setItemDescription(itemDescription);
		line.setItemNumber(itemNumber);
		
		tc.getLines().add(line);
		line.setTransfer(tc);
	}
	
	public TransferControl getTransferControl() {
		return tc;
	}

}
