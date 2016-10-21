package com.orendel.locator.editors;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.Inventory;
import com.orendel.counterpoint.domain.Item;
import com.orendel.locator.controllers.CounterpointController;
import com.orendel.locator.services.HibernateUtil;
import com.orendel.locator.util.MessagesUtil;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.hibernate.HibernateException;


public class ViewExtendedItemDetailsEditor extends Composite {
	private static final Logger logger = Logger.getLogger(ViewExtendedItemDetailsEditor.class);
	
	private static final String EMPTY_STRING = "";
	
	private static Color lightCyan = null;
	
	private CounterpointController controller;
	
	private String lastSearchedCode;
	
	private Text txtBarcode;
	private Table tableItemDetails;
	private Text txtItemDescription;

	
	private Listener listenerESC;
	
	private final int baseFontSize = 9 + 9;
	private final int labelFontSize = 3;
	private final int buttonFontSize = 2;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewExtendedItemDetailsEditor(Composite parent, int style) {
		super(parent, style);
		
		lightCyan = new Color(getDisplay(), 226, 244, 255);
		controller = new CounterpointController("S-" + getClass().getSimpleName() + new Date().getTime());
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group grpConsultarDetallesPor = new Group(this, SWT.NONE);
		grpConsultarDetallesPor.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize, SWT.NORMAL));
		grpConsultarDetallesPor.setText(" Consultar detalles por artículo ");
		GridData gd_grpConsultarDetallesPor = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_grpConsultarDetallesPor.verticalIndent = 20;
		grpConsultarDetallesPor.setLayoutData(gd_grpConsultarDetallesPor);
		GridLayout gl_grpConsultarDetallesPor = new GridLayout(3, false);
		gl_grpConsultarDetallesPor.marginHeight = 15;
		gl_grpConsultarDetallesPor.verticalSpacing = 10;
		grpConsultarDetallesPor.setLayout(gl_grpConsultarDetallesPor);
		
		Label lblItem = new Label(grpConsultarDetallesPor, SWT.NONE);
		lblItem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblItem.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblItem.setText("Artículo:");
		
		txtBarcode = new Text(grpConsultarDetallesPor, SWT.BORDER);
		txtBarcode.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		GridData gd_txtBarcode = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtBarcode.widthHint = 200;
		txtBarcode.setLayoutData(gd_txtBarcode);
		
		Button btnSearch = new Button(grpConsultarDetallesPor, SWT.NONE);
		btnSearch.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + buttonFontSize, SWT.NORMAL));
		btnSearch.setText("Buscar");
		
		Label lblDescription = new Label(grpConsultarDetallesPor, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblDescription.setText("Descripción:");
		
		txtItemDescription = new Text(grpConsultarDetallesPor, SWT.BORDER);
		txtItemDescription.setEnabled(false);
		txtItemDescription.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		txtItemDescription.setEditable(false);
		txtItemDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Group groupInventory = new Group(this, SWT.NONE);
		groupInventory.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize, SWT.NORMAL));
		groupInventory.setText("Detalles de inventario (Bodega MAIN)");
		GridData gd_groupInventory = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_groupInventory.verticalIndent = 25;
		groupInventory.setLayoutData(gd_groupInventory);
		GridLayout gl_groupInventory = new GridLayout(1, false);
		gl_groupInventory.marginHeight = 10;
		groupInventory.setLayout(gl_groupInventory);
		
		tableItemDetails = new Table(groupInventory, SWT.BORDER | SWT.FULL_SELECTION);
		tableItemDetails.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableItemDetails.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		tableItemDetails.setHeaderVisible(true);
		tableItemDetails.setLinesVisible(true);
		
		TableColumn tblclmnAvailable = new TableColumn(tableItemDetails, SWT.RIGHT);
		tblclmnAvailable.setWidth(150);
		tblclmnAvailable.setText("Disponible");
		
		TableColumn tblclmnBin01 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin01.setWidth(250);
		tblclmnBin01.setText("Bin01");
		
		TableColumn tblclmnBin02 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin02.setWidth(250);
		tblclmnBin02.setText("Bin02");
		
		TableColumn tblclmnBin03 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin03.setWidth(250);
		tblclmnBin03.setText("Bin03");
		
		TableColumn tblclmnBin04 = new TableColumn(tableItemDetails, SWT.NONE);
		tblclmnBin04.setWidth(250);
		tblclmnBin04.setText("Bin04");

		txtBarcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				logger.info("Keyx: " + e.keyCode);
				if (!txtBarcode.getText().isEmpty()
						&& !txtBarcode.getText().equalsIgnoreCase(lastSearchedCode) 
						&& (e.keyCode == 13 || e.keyCode == 16777296)) {
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
		
		addGlobalListeners();
		addDisposeListener();
		
		txtBarcode.setFocus();
	}
	
	
	private void showItemDetails(String code) {
		resetFields();
		lastSearchedCode = code;
		if (code == null || code.isEmpty()) {
			return;
		}
		try {
			Item item = controller.findItemInMainWarehouse(code);
			if (item != null) {
				logger.info("Artículo encontrado en DB: " + item.getDescription());
				refreshItemLocationDetails(item);
			} else {
				MessagesUtil.showWarning("Búsqueda por código", "No se encontró ningún artículo con el código de barra o código de item suministrado: '" + code + "'.");
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
			itemLine.setText(column++, " " + v.getQtyAvailable());
			itemLine.setText(column++, checkNull(v.getBin01()));
			itemLine.setText(column++, checkNull(v.getBin02()));
			itemLine.setText(column++, checkNull(v.getBin03()));
			itemLine.setText(column++, checkNull(v.getBin04()));
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
		controller = new CounterpointController("ViewItemDetailsEditor" + new Date().getTime());
	}
	
	
	public String checkNull(String valorCampo) {
		return valorCampo == null ? "" : valorCampo;
	}
	
	/**
	 * Listeners for global shortcuts like F10 (reset form) and F12 (save form). 
	 */
	private void addGlobalListeners() {
		Display display = getShell().getDisplay();
		listenerESC = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.ESC) {
					System.out.println("Escape button pressed!");
					txtBarcode.setText("");
					resetFields();
				}
			}
		};
		display.addFilter(SWT.KeyDown, listenerESC);
	}
	
	private void addDisposeListener() {
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				getShell().getDisplay().removeFilter(SWT.KeyDown, listenerESC);
				controller.finalizarSesion();
			}
		});
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
