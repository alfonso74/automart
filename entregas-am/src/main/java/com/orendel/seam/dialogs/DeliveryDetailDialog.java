package com.orendel.seam.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

import com.orendel.seam.editors.DeliveryDetailComposite;
import com.orendel.seam.util.DialogUtil;


public class DeliveryDetailDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private long deliveryNumber;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DeliveryDetailDialog(Shell parent, int style, long deliveryNumber) {
		super(parent, style);
		setText("Consultar entrega");
		this.deliveryNumber = deliveryNumber;
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
		
//		ConsultasEditor editor = new ConsultasEditor(shell, SWT.None);
		DeliveryDetailComposite composite = new DeliveryDetailComposite(shell, SWT.None, deliveryNumber);
		composite.layout();
	}

}
