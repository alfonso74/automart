package com.orendel.transfer.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

import com.orendel.transfer.composites.AddBarcodeComposite;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.util.DialogUtil;


public class AddBarcodeDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private CounterpointController controller;
	private String itemNo;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddBarcodeDialog(Shell parent, int style, CounterpointController controller, String itemNo) {
		super(parent, style);
		setText("Agregar código de barra");
		this.controller = controller;
		this.itemNo = itemNo;
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(500, 360);
		shell.setText(getText());
		shell.setLocation(DialogUtil.calculateDialogLocation(shell, false));
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		AddBarcodeComposite composite = new AddBarcodeComposite(shell, SWT.None, controller, itemNo);
		composite.layout();
	}

}
