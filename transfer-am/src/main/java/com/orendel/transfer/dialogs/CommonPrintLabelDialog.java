package com.orendel.transfer.dialogs;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.hibernate.HibernateException;

import com.orendel.transfer.composites.CommonPrintLabelComposite;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.util.DialogUtil;


public class CommonPrintLabelDialog extends Dialog {
	
	private static final Logger logger = Logger.getLogger(CommonPrintLabelDialog.class);

	protected Object result;
	protected Shell shell;
	private CounterpointController controller;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CommonPrintLabelDialog(Shell parent, int style) {
		super(parent, style);
		setText("Imprimir etiqueta");
		this.controller = new CounterpointController("S-" + getClass().getSimpleName() + new Date().getTime());
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
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (HibernateException ex) {
				resetHibernateConnection(ex);
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(400, 350);
		shell.setText(getText());
		shell.setLocation(DialogUtil.calculateDialogLocation(shell, false));
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CommonPrintLabelComposite composite = new CommonPrintLabelComposite(shell, SWT.None, controller);
		composite.layout();
	}
	
	private void resetHibernateConnection(HibernateException ex) {
		logger.error(ex.getMessage(), ex);
		logger.info("Resetting sessions after HibernateException...");
		controller.finalizarSesion();
		HibernateUtil.verSesiones();
		controller = new CounterpointController();
	}

}
