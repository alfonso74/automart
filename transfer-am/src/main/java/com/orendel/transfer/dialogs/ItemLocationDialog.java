package com.orendel.transfer.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.composites.ItemLocationComposite;
import com.orendel.transfer.util.DialogUtil;

public class ItemLocationDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	
	private Item item;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ItemLocationDialog(Shell parent, int style, Item item) {
		super(parent, style);
		setText("Ver inventario de artículos");
		
		this.item = item;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(700, 300);
		shell.setText(getText());

		shell.setLocation(DialogUtil.calculateDialogLocation(shell, false));
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ItemLocationComposite composite = new ItemLocationComposite(shell, SWT.None, item);
		composite.layout();
	}

}
