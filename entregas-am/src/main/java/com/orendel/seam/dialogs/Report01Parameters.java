package com.orendel.seam.dialogs;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

import com.orendel.seam.config.AppConfig;
import com.orendel.seam.util.DialogUtil;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;


public class Report01Parameters extends Dialog {
	
	private Logger logger = Logger.getLogger(Report01Parameters.class);

	protected Map<String, Serializable> result = new HashMap<String, Serializable>();
	protected Shell shell;

	private Text txtHeader;
	
	private DateTime dtInitialDate;
	private DateTime dtEndDate;
	
	private Image image;
	
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Report01Parameters(Shell parent, int style) {
		super(parent, style);
		setText("Selección de parámetros");
		image = new Image(parent.getDisplay(), getClass().getClassLoader().getResourceAsStream("bar_chart_16.png"));
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Map<String, Serializable> open() {
		createContents();
		shell.open();
		shell.layout();
		shell.setLocation(DialogUtil.calculateDialogLocation(shell, false));
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (image != null) {
			System.out.println("Dispose image called!");
			image.dispose();
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Composite compositeTop = new Composite(shell, SWT.NONE);
		compositeTop.setEnabled(false);
		compositeTop.setLayout(new GridLayout(2, false));
		compositeTop.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeTop.setBounds(0, 0, 64, 64);
		
		Label lblHeader = new Label(compositeTop, SWT.NONE);
		lblHeader.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblHeader.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
		lblHeader.setImage(image);
		
		txtHeader = new Text(compositeTop, SWT.READ_ONLY | SWT.WRAP);
		txtHeader.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		txtHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		txtHeader.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtHeader.setText("Por favor, seleccione los parámetros del reporte");
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeContent = new Composite(shell, SWT.NONE);
		compositeContent.setLayout(new GridLayout(1, false));
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContent.setBounds(0, 0, 64, 64);
		
		Group groupParameters = new Group(compositeContent, SWT.NONE);
		groupParameters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		groupParameters.setLayout(new GridLayout(2, false));
		
		Label lblFechaInicial = new Label(groupParameters, SWT.NONE);
		lblFechaInicial.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFechaInicial.setText("Fecha inicial:");
		
		dtInitialDate = new DateTime(groupParameters, SWT.BORDER | SWT.DROP_DOWN);
		
		Label lblFechaFinal = new Label(groupParameters, SWT.NONE);
		lblFechaFinal.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFechaFinal.setText("Fecha final:");
		
		dtEndDate = new DateTime(groupParameters, SWT.BORDER | SWT.DROP_DOWN);
		
		Composite compositeButtons = new Composite(shell, SWT.NONE);
		compositeButtons.setLayout(new GridLayout(2, false));
		compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		compositeButtons.setBounds(0, 0, 64, 64);
		
		Button btnConfirm = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnConfirm = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnConfirm.widthHint = 70;
		btnConfirm.setLayoutData(gd_btnConfirm);
		btnConfirm.setText("OK");
		
		Button btnCancel = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 70;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancelar");

		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (validateFields()) {
					doSave();
					image.dispose();
					shell.close();
				};				
			}
		});
		
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				image.dispose();
				shell.close();
			}
		});
		
		shell.setDefaultButton(btnConfirm);
		llenarControles();
	}

	
	private void llenarControles() {
		dtInitialDate.setDay(1);
	}

	
	private boolean validateFields() {
		return true;
	}
	
	
	private void doSave() {
		result.put("initialDate", parseDate(dtInitialDate));
		result.put("endDate", parseDate(dtEndDate));
		result.put("dbURL", (String) AppConfig.INSTANCE.getValue("delivery.database.url"));
	}
	
	
	private Calendar parseDate(DateTime dtParameter) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(dtParameter.getYear(), dtParameter.getMonth(), dtParameter.getDay());
		return cal;
	}
	
}
