package com.orendel.transfer.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

import com.orendel.transfer.composites.EditBarcodeComposite;
import com.orendel.transfer.util.DialogUtil;


public class EditBarcodeDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private String itemNo;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EditBarcodeDialog(Shell parent, int style, String itemNo) {
		super(parent, style);
		setText("Editar código de barra");
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
		shell.setSize(600, 450);
		shell.setText(getText());
		shell.setLocation(DialogUtil.calculateDialogLocation(shell, false));
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		EditBarcodeComposite composite = new EditBarcodeComposite(shell, SWT.None, itemNo);
		composite.layout();
	}

}
