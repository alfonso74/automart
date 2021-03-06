package com.orendel.transfer.dialogs;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.hibernate.HibernateException;

import com.orendel.transfer.composites.CommonAddBarcodeComposite;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.util.DialogUtil;


public class CommonAddBarcodeDialog extends Dialog {
	
	private static final Logger logger = Logger.getLogger(CommonAddBarcodeDialog.class);

	protected Object result;
	protected Shell shell;
	private CounterpointController controller;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CommonAddBarcodeDialog(Shell parent, int style) {
		super(parent, style);
		setText("Agregar código de barra");
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
		controller.finalizarSesion();
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
		
		CommonAddBarcodeComposite composite = new CommonAddBarcodeComposite(shell, SWT.None, controller);
		composite.layout();
	}
	
	private void resetHibernateConnection(HibernateException ex) {
		logger.error(ex.getMessage(), ex);
		logger.info("Resetting sessions after HibernateException...");
		controller.finalizarSesion();
		HibernateUtil.verSesiones();
		controller = new CounterpointController("S-" + getClass().getSimpleName() + new Date().getTime());
	}
	
	

}
