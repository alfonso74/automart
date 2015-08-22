package com.orendel.transfer.editors;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.Inventory;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.util.MessagesUtil;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.hibernate.HibernateException;

public class ViewItemDetailsEditor extends Composite {
	private static final Logger logger = Logger.getLogger(ViewItemDetailsEditor.class);
	
	private static final String EMPTY_STRING = "";
	
	private static Color lightCyan = null;
	
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
		
		lightCyan = new Color(getDisplay(), 226, 244, 255);
		controller = new CounterpointController("ItemDetails" + new Date().getTime());
		
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
		
		TableColumn tblclmnOnhand = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnOnhand.setWidth(100);
		tblclmnOnhand.setText("OnHand");
		
		TableColumn tblclmnAvailable = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnAvailable.setWidth(100);
		tblclmnAvailable.setText("Disponible");
		
		TableColumn tblclmnCommited = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnCommited.setWidth(100);
		tblclmnCommited.setText("Commited");
		
		TableColumn tblclmnXferout = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnXferout.setWidth(100);
		tblclmnXferout.setText("Saliendo");
		
		TableColumn tblclmnXferin = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnXferin.setWidth(100);
		tblclmnXferin.setText("Entrando");
		
		TableColumn tblclmnBin01 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin01.setWidth(100);
		tblclmnBin01.setText("Bin01");
		
		TableColumn tblclmnBin02 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin02.setWidth(100);
		tblclmnBin02.setText("Bin02");
		
		TableColumn tblclmnBin03 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin03.setWidth(100);
		tblclmnBin03.setText("Bin03");
		
		TableColumn tblclmnBin04 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin04.setWidth(100);
		tblclmnBin04.setText("Bin04");
		
		txtBarcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					showItemDetails(txtBarcode.getText());
				}
			}
		});
		
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showItemDetails(txtBarcode.getText());
			}
		});

		txtBarcode.setFocus();
	}
	
	
	private void showItemDetails(String code) {
		if (code == null || code.isEmpty()) {
			resetFields();
			return;
		}
		resetFields();
		try {
			Item item = controller.findItem(code);
			if (item != null) {
				logger.info("Artículo encontrado en DB: " + item.getDescription());
				refreshItemLocationDetails(item);
			} else {
				MessagesUtil.showWarning("Búsqueda por código", "No se encontró ningún artículo con el código de barra o código de item suministrado: " + code + ".");
			}	
		} catch (HibernateException e) {
			resetHibernateConnection(e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			MessagesUtil.showError("Error de aplicación", 
					(e.getMessage() == null ? e.toString() + '\n' + e.getStackTrace()[0] : e.getMessage()));
		}
	}
	
	
	private void refreshItemLocationDetails(Item item) {
		txtItemDescription.setText(item.getDescription());
		
		tableItemDetails.clearAll();
		TableItem itemLine;
		
		for (Inventory v : item.getInventory()) {
			itemLine = new TableItem(tableItemDetails, SWT.NONE);
			int column = 0;
			itemLine.setData(v);
			itemLine.setText(column++, " " + v.getLocationId());
			itemLine.setText(column++, " " + v.getQtyOnHand());
			itemLine.setText(column++, " " + v.getQtyAvailable());
			itemLine.setText(column++, " " + v.getQtyCommited());
			itemLine.setText(column++, " " + v.getQtyXferOut());
			itemLine.setText(column++, " " + v.getQtyXferIn());
			itemLine.setText(column++, " " + checkNull(v.getBin01()));
			itemLine.setText(column++, " " + checkNull(v.getBin02()));
			itemLine.setText(column++, " " + checkNull(v.getBin03()));
			itemLine.setText(column++, " " + checkNull(v.getBin04()));
			if (tableItemDetails.getItemCount() % 2 == 0) {
				itemLine.setBackground(lightCyan);
			}
		}		
	}
	
	
	private void resetFields() {
		txtItemDescription.setText(EMPTY_STRING);
		tableItemDetails.removeAll();
	}
	
	
	private void resetHibernateConnection(HibernateException ex) {
		logger.error(ex.getMessage(), ex);
		logger.info("Reloading sessions after HibernateException...");
		controller.finalizarSesion();
		HibernateUtil.verSesiones();
		controller = new CounterpointController("ItemDetails" + new Date().getTime());
	}
	
	
	public String checkNull(String valorCampo) {
		return valorCampo == null ? "" : valorCampo;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
