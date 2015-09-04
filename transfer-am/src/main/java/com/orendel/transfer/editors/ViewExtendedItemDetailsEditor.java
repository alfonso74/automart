package com.orendel.transfer.editors;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.Inventory;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.dialogs.EditBarcodeDialog;
import com.orendel.transfer.dialogs.ItemDetailsDialog;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.util.DateUtil;
import com.orendel.transfer.util.MessagesUtil;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class ViewExtendedItemDetailsEditor extends Composite {
	private static final Logger logger = Logger.getLogger(ViewExtendedItemDetailsEditor.class);
	
	private static final String EMPTY_STRING = "";
	
	private static Color lightCyan = null;
	
	private CounterpointController controller;
	private Text txtBarcode;
	private Table tableItemDetails;
	private Text txtItemDescription;
	private Table tableBarcodes;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewExtendedItemDetailsEditor(Composite parent, int style) {
		super(parent, style);
		
		lightCyan = new Color(getDisplay(), 226, 244, 255);
		controller = new CounterpointController("ViewItemDetailsEditor" + new Date().getTime());
		
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
		txtItemDescription.setEnabled(false);
		txtItemDescription.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtItemDescription.setEditable(false);
		txtItemDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Group groupInventory = new Group(this, SWT.NONE);
		groupInventory.setText("Detalles de inventario");
		GridData gd_groupInventory = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_groupInventory.verticalIndent = 10;
		groupInventory.setLayoutData(gd_groupInventory);
		groupInventory.setLayout(new GridLayout(1, false));
		
		tableItemDetails = new Table(groupInventory, SWT.BORDER | SWT.FULL_SELECTION);
		tableItemDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableItemDetails.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		tableItemDetails.setHeaderVisible(true);
		tableItemDetails.setLinesVisible(true);
		
		TableColumn tblclmnLocation = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnLocation.setWidth(125);
		tblclmnLocation.setText("Bodega");
		
		TableColumn tblclmnOnhand = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnOnhand.setWidth(100);
		tblclmnOnhand.setText("On Hand");
		
		TableColumn tblclmnAvailable = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnAvailable.setWidth(100);
		tblclmnAvailable.setText("Disponible");
		
		TableColumn tblclmnCommited = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnCommited.setWidth(100);
		tblclmnCommited.setText("Commited");
		
		TableColumn tblclmnXferout = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnXferout.setWidth(100);
		tblclmnXferout.setText("Xfer Out");
		
		TableColumn tblclmnXferin = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnXferin.setWidth(100);
		tblclmnXferin.setText("Xfer In");
		
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
		
		Group groupBarcodes = new Group(this, SWT.NONE);
		groupBarcodes.setLayout(new GridLayout(2, false));
		GridData gd_groupBarcodes = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_groupBarcodes.verticalIndent = 10;
		groupBarcodes.setLayoutData(gd_groupBarcodes);
		groupBarcodes.setText("Códigos de barra asociados");
		
		tableBarcodes = new Table(groupBarcodes, SWT.BORDER | SWT.FULL_SELECTION);
		tableBarcodes.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		tableBarcodes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableBarcodes.setHeaderVisible(true);
		tableBarcodes.setLinesVisible(true);
		
		TableColumn tblclmnBarcode = new TableColumn(tableBarcodes, SWT.NONE);
		tblclmnBarcode.setWidth(125);
		tblclmnBarcode.setText("Cód. de Barra");
		
		TableColumn tblclmnId = new TableColumn(tableBarcodes, SWT.NONE);
		tblclmnId.setWidth(100);
		tblclmnId.setText("Tipo");
		
		TableColumn tblclmnDescription = new TableColumn(tableBarcodes, SWT.NONE);
		tblclmnDescription.setWidth(300);
		tblclmnDescription.setText("Descripción");
		
		TableColumn tblclmnUpdated = new TableColumn(tableBarcodes, SWT.CENTER);
		tblclmnUpdated.setWidth(180);
		tblclmnUpdated.setText("Actualizado");
		
		Button btnEdit = new Button(groupBarcodes, SWT.NONE);
		btnEdit.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnEdit.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		btnEdit.setText("Editar...");
		
		txtBarcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					showItemDetails(txtBarcode.getText());
					txtBarcode.selectAll();
				}
			}
		});
		
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showItemDetails(txtBarcode.getText());
				txtBarcode.setFocus();
				txtBarcode.selectAll();
			}
		});
		
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editBarcode();
			}
		});
		
		addDoubleClickListener();		
		addDisposeListener();
		
		txtBarcode.setFocus();
	}
	
	private void addDoubleClickListener() {
		tableItemDetails.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				logger.info("Event: " + e);
				Table t = (Table) e.getSource();
				TableItem item = t.getItem(t.getSelectionIndex());
				Inventory inventory = (Inventory) item.getData();
				logger.info("Delivery: " + item.getText(1) + ", " + inventory);
				ItemDetailsDialog dialog = new ItemDetailsDialog(getShell(), SWT.APPLICATION_MODAL, inventory.getItemNo(), inventory.getLocationId());
				dialog.open();
			}
		});
	}
	
	
	private void editBarcode() {
		BarCode barcode = getSelectedBarcodeLine();
		if (barcode != null) {
			EditBarcodeDialog dialog = new EditBarcodeDialog(getShell(), SWT.APPLICATION_MODAL, controller, barcode.getCode());
			dialog.open();
//			showItemDetails(barcode.getItemNo());
//			Item item = controller.findItemByItemCode(barcode.getItemNo());
			refreshBarcodeDetails(barcode.getItem());
		} else {
			MessagesUtil.showError("Editar código de barra", "No se ha seleccionado ningún código de barra para ser editado.");
		}
	}
	
	private BarCode getSelectedBarcodeLine() {
		BarCode barcode = null;
		int index = tableBarcodes.getSelectionIndex();
		if (index != -1) {
			TableItem item = tableBarcodes.getItem(index);
			barcode = (BarCode) item.getData();
			logger.info("Barcode: " + item.getText(1) + ", " + barcode);
		}
		return barcode;
	}
	
	private void showItemDetails(String code) {
		resetFields();
		if (code == null || code.isEmpty()) {
			return;
		}
		try {
			Item item = controller.findItem(code);
			if (item != null) {
				logger.info("Artículo encontrado en DB: " + item.getDescription());
				refreshItemLocationDetails(item);
				refreshBarcodeDetails(item);
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
		
		TableItem itemLine;
		
		tableItemDetails.removeAll();
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
			itemLine.setText(column++, checkNull(v.getBin01()));
			itemLine.setText(column++, checkNull(v.getBin02()));
			itemLine.setText(column++, checkNull(v.getBin03()));
			itemLine.setText(column++, checkNull(v.getBin04()));
			if (tableItemDetails.getItemCount() % 2 == 0) {
				itemLine.setBackground(lightCyan);
			}
		}		
	}
	
	
	private void refreshBarcodeDetails(Item item) {
		TableItem itemLine;
		
		tableBarcodes.removeAll();
		for (BarCode v : item.getBarcodeList()) {
			itemLine = new TableItem(tableBarcodes, SWT.NONE);
			int column = 0;
			itemLine.setData(v);
			itemLine.setText(column++, " " + v.getCode());
			itemLine.setText(column++, " " + v.getType().getBarCodeId());
			itemLine.setText(column++, " " + v.getType().getDescription());
			itemLine.setText(column++, " " + DateUtil.toString(v.getUpdated(), DateUtil.formatoFechaHora));
			if (tableBarcodes.getItemCount() % 2 == 0) {
				itemLine.setBackground(lightCyan);
			}
		}
	}
	
	
	private void resetFields() {
		txtItemDescription.setText(EMPTY_STRING);
		tableItemDetails.removeAll();
		tableBarcodes.removeAll();
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
	
	private void addDisposeListener() {
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				controller.finalizarSesion();
			}
		});
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
