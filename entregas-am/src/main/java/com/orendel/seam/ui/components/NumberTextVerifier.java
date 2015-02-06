
/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.orendel.seam.ui.components;

/*
 *  $RCSfile: NumberTextVerifier.java,v $
 *  $Revision: 1.4 $  $Date: 2005/08/24 23:30:46 $ 
 */

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

public class NumberTextVerifier implements VerifyListener {

	private int newValue;
	private int defaultValue = 0;
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;

	/**
	 * The default value is the value of the verifier when no text is entered,
	 * or if the text is only ('-') when negative values are allowed. The
	 * default is 0.
	 * 
	 * @return Returns the defaultValue.
	 */
	public int getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            The defaultValue to set.
	 */
	public void setDefaultValue(int defaultValue) {
		if (defaultValue < min || defaultValue > max)
			return;
		this.defaultValue = defaultValue;
	}

	/**
	 * Get the latest valid value from the verifier. The value will be an
	 * integer between min and max.
	 * 
	 * @return Returns the newValue.
	 */
	public int getNewValue() {
		return newValue;
	}

	/**
	 * @return Returns the maximum valid integer.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set the maximum valid integer value this verifier should accept.
	 * 
	 * @param max
	 *            The max to set.
	 */
	public void setMax(int max) {
		this.max = max;
		if (defaultValue > max) {
			defaultValue = max;
		}
	}

	/**
	 * @return Returns the minimum valid integer.
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Set the minimum valid integer value this verifier should accept.
	 * 
	 * @param min
	 *            The min to set.
	 */
	public void setMin(int min) {
		this.min = min;
		if (defaultValue < min) {
			defaultValue = min;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.VerifyListener#verifyText(org.eclipse.swt.events
	 * .VerifyEvent)
	 */
	public void verifyText(VerifyEvent e) {
		if (e.widget instanceof Text) {
			StringBuffer textbuf = new StringBuffer(((Text) e.widget).getText());
			textbuf.replace(e.start, e.end, e.text);
			if (textbuf.length() == 0
					|| (min < 0 && textbuf.toString().equals("-"))) { //$NON-NLS-1$
				newValue = defaultValue;
				return;
			}
			try {
				int value = Integer.parseInt(textbuf.toString());
				if (value < min || value > max) {
					e.doit = false;
				}
				newValue = value;
			} catch (NumberFormatException ex) {
				e.doit = false;
			}
		}
	}
}
