package com.orendel.seam.ui.components;
/*
 *  $RCSfile$
 *  $Revision$  $Date$ 
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class Spinner2 extends Composite {
	static final int BUTTON_WIDTH = 16;

	protected Text text;
	protected Button up, down;
	protected NumberTextVerifier verifier = new NumberTextVerifier();
	protected int minimum, maximum;
	protected int fValue = 0;
	private boolean commandInProcess = false;       // Flag saying we are processing a change, during which we won't allow further changes.
	private boolean settingText = false;    // Text is being set through the text field itself. In that case, don't set the text back on itself.
	private boolean selectAll = true;       // When setting text should the entire text be set or not.

	public Spinner2(Composite parent, int style, int initialValue) {

		super(parent, style);

		text = new Text(this, style | SWT.SINGLE | SWT.BORDER);
		up = new Button(this, style | SWT.ARROW | SWT.UP);
		down = new Button(this, style | SWT.ARROW | SWT.DOWN);
		text.addVerifyListener(verifier);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				settingText = true;
				try {
					setValue(verifier.getNewValue());
				} finally {
					settingText = false;
				}
			}
		});
		text.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				traverse(e);
			}
		});
		text.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				text.selectAll();
			}
		});
		text.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				text.selectAll();
			}
		});
		up.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				forceTextFocus();
				up();
			}
		});
		down.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				forceTextFocus();
				down();
			}
		});
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				resize();
			}
		});
		if (initialValue < 0)
			setMinimum(initialValue);
		else
			setMinimum(0);
		setMaximum(999);
		text.setFont(getFont());
		fValue = initialValue;
		setTextField();
	}

	/*
	 * Force the text focus, if it already doesn't have it. This is used when someone clicks on
	 * the up/down button so that from that point on the arrows can be used
	 * or someone could type something in.
	 */
	void forceTextFocus() {
		if (!text.isFocusControl())
			text.forceFocus();
	}

	void traverse(Event e) {
		switch (e.detail) {
		case SWT.TRAVERSE_ARROW_PREVIOUS :
			if (e.keyCode == SWT.ARROW_UP) {
				e.doit = true;
				e.detail = SWT.NULL;
				selectAll = false;      // Don't want to select the text when using arrow keys to go up/down
				try {
					up();
				} finally {
					selectAll = true;
				}
			}
			break;
		case SWT.TRAVERSE_ARROW_NEXT :
			if (e.keyCode == SWT.ARROW_DOWN) {
				e.doit = true;
				e.detail = SWT.NULL;
				selectAll = false;      // Don't want to select the text when using arrow keys to go up/down
				try {
					down();
				} finally {
					selectAll = true;
				}

			}
			break;
		}
	}

	void up() {
		setValue(getValue() + 1);
	}

	void down() {
		setValue(getValue() - 1);
	}

	public void setFont(Font font) {
		super.setFont(font);
		text.setFont(font);
	}
	public void setValue(int value) {
		// Don't process anything if we are still in the middle of processing previous commands 
		if (commandInProcess) return;
		/*
		 * A real hack for now. Need to handle the case where notification has been
		 * sent out and commands are processing. To avoid a ConcurrentModificationException,
		 * we need to set a flag to prevent accepting any more input from the user.
		 */             
		commandInProcess = true;
		try {
			Integer.parseInt(String.valueOf(value));
			if (value < minimum || value > maximum) {
				setTextField();
				return;
			} else if (fValue == value){
				return;
			}
			fValue = value; 
			setTextField();
			notifyListeners(SWT.Modify, new Event());
		} catch (NumberFormatException ex) {
			setTextField();
		} finally {
			commandInProcess = false;
		}
	}
	public int getValue() {
		return fValue;
	}
	protected void setTextField() {
		// Added this  check to prevent Linux GTK stack overflow problem
		if (!settingText) {
			settingText = true;
			String valueString = String.valueOf(fValue);
			text.setText(valueString);
			if (selectAll)
				text.selectAll();
			else
				text.setSelection(valueString.length()+1);      // So that cursor at right of text. Looks better.
				settingText = false;
		}
	}
	public void setMaximum(int maximum) {
		checkWidget();
		this.maximum = maximum;
		verifier.setMax(maximum);
		resize();
	}
	public int getMaximum() {
		return maximum;
	}
	public void setMinimum(int minimum) {
		this.minimum = minimum;
		verifier.setMin(minimum);
	}
	public int getMinimum() {
		return minimum;
	}
	void resize() {
		Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int textWidth = pt.x - BUTTON_WIDTH;
		int buttonHeight = pt.y / 2;
		text.setBounds(0, 0, textWidth, pt.y);
		up.setBounds(textWidth, 0, BUTTON_WIDTH, buttonHeight);
		down.setBounds(textWidth, pt.y - buttonHeight, BUTTON_WIDTH, buttonHeight);
	}
	public Point computeSize(int wHint, int hHint, boolean changed) {
		GC gc = new GC(text);
		Point textExtent = gc.textExtent(String.valueOf(maximum));
		gc.dispose();
		Point pt = text.computeSize(textExtent.x, textExtent.y);
		int width = pt.x + BUTTON_WIDTH;
		int height = pt.y;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		return new Point(width, height);
	}
	public void addModifyListener(Listener listener) {
		if (listener != null)
			addListener(SWT.Modify, listener);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */
	 public void setEnabled(boolean enabled) {
		if (text != null && text.getEnabled() != enabled) 
			text.setEnabled(enabled);
		if (up != null && up.getEnabled() != enabled)
			up.setEnabled(enabled);
		if (down != null && down.getEnabled()!= enabled) down.setEnabled(enabled);              
	}
}


