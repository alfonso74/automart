package com.orendel.seam.util;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;


public final class DialogUtil {
		
	public static Point calculateDialogLocation(Shell dialogShell, boolean centerOnDialog) {
		Point parentLocation = dialogShell.getParent().getLocation();
		Point parentSize = dialogShell.getParent().getSize();
		Point dialogSize = dialogShell.getSize();
		
		int xOffset = 100;
		int yOffset = 100;
		
		if (centerOnDialog) {
			xOffset = (parentSize.x - dialogSize.x) / 2;
			yOffset = (parentSize.y - dialogSize.y) / 2; 
		}
		
		Point dialogLocation = new Point(parentLocation.x + xOffset, parentLocation.y + yOffset);
		return dialogLocation;
	}

}
