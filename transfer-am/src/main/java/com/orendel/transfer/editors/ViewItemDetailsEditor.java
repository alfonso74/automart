package com.orendel.transfer.editors;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.transfer.controllers.CounterpointController;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ViewItemDetailsEditor extends Composite {
	private static final Logger logger = Logger.getLogger(ViewItemDetailsEditor.class);
	
	private CounterpointController controller;
	private Text txtBarcode;
	private Table tableItemDetails;
	private Text txtItemDescription;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewItemDetailsEditor(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group grpConsultarDetallesPor = new Group(this, SWT.NONE);
		grpConsultarDetallesPor.setText(" Consultar detalles por artículo ");
		grpConsultarDetallesPor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpConsultarDetallesPor.setLayout(new GridLayout(3, false));
		
		Label lblItem = new Label(grpConsultarDetallesPor, SWT.NONE);
		lblItem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblItem.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblItem.setText("Artículo:");
		
		txtBarcode = new Text(grpConsultarDetallesPor, SWT.BORDER);
		txtBarcode.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_txtBarcode = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtBarcode.widthHint = 100;
		txtBarcode.setLayoutData(gd_txtBarcode);
		
		Button btnSearch = new Button(grpConsultarDetallesPor, SWT.NONE);
		btnSearch.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnSearch.setText("Buscar");
		
		Label lblDescription = new Label(grpConsultarDetallesPor, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblDescription.setText("Descripción:");
		
		txtItemDescription = new Text(grpConsultarDetallesPor, SWT.BORDER);
		txtItemDescription.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtItemDescription.setEditable(false);
		txtItemDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		tableItemDetails = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		tableItemDetails.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_tableItemDetails = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tableItemDetails.verticalIndent = 10;
		tableItemDetails.setLayoutData(gd_tableItemDetails);
		tableItemDetails.setHeaderVisible(true);
		tableItemDetails.setLinesVisible(true);
		
		TableColumn tblclmnLocation = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnLocation.setWidth(100);
		tblclmnLocation.setText("Ubicación");
		
		TableColumn tblclmnOnhand = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnOnhand.setWidth(100);
		tblclmnOnhand.setText("OnHand");
		
		TableColumn tblclmnAvailable = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnAvailable.setWidth(100);
		tblclmnAvailable.setText("Disponible");
		
		TableColumn tblclmnCommited = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnCommited.setWidth(100);
		tblclmnCommited.setText("Commited");
		
		TableColumn tblclmnXferout = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnXferout.setWidth(100);
		tblclmnXferout.setText("Salida");
		
		TableColumn tblclmnXferin = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnXferin.setWidth(100);
		tblclmnXferin.setText("Entrada");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
