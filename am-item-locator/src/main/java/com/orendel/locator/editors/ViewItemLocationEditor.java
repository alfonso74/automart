package com.orendel.locator.editors;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.common.config.AppConfig;
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.hibernate.HibernateException;


public class ViewItemLocationEditor extends Composite {
	private static final Logger logger = Logger.getLogger(ViewItemLocationEditor.class);
	
	private static final String EMPTY_STRING = "";
	
	private CounterpointController controller;
	
	private String lastSearchedCode;
	
	private Text txtBarcode;
	private Text txtItemDescription;

	
	private Listener listenerESC;
	
	private int baseFontSize = 9 + 9;
	private final int labelFontSize = 3;
	private final int buttonFontSize = 2;
	private Text txtCantidad;
	private Text txtBin01;
	private Text txtBin02;
	private Text txtBin03;
	private Text txtBin04;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewItemLocationEditor(Composite parent, int style) {
		super(parent, style);
		
		String fontSizeTxt = AppConfig.INSTANCE.getValue("item.locator.baseFontSize");
		baseFontSize = Integer.parseInt(fontSizeTxt);
		logger.info("FUENTE: " + baseFontSize);
		
		controller = new CounterpointController("S-" + getClass().getSimpleName() + new Date().getTime());
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginBottom = 10;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group grpConsultarDetallesPor = new Group(this, SWT.NONE);
		grpConsultarDetallesPor.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize, SWT.NORMAL));
		grpConsultarDetallesPor.setText(" Consultar detalles por artículo ");
		GridData gd_grpConsultarDetallesPor = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_grpConsultarDetallesPor.verticalIndent = 25;
		grpConsultarDetallesPor.setLayoutData(gd_grpConsultarDetallesPor);
		GridLayout gl_grpConsultarDetallesPor = new GridLayout(3, false);
		gl_grpConsultarDetallesPor.horizontalSpacing = 10;
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
		new Label(grpConsultarDetallesPor, SWT.NONE);
		
		Label lblDescription = new Label(grpConsultarDetallesPor, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblDescription.setText("Descripción:");
		
		txtItemDescription = new Text(grpConsultarDetallesPor, SWT.BORDER);
		txtItemDescription.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		txtItemDescription.setEditable(false);
		txtItemDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Group groupInventory = new Group(this, SWT.NONE);
		groupInventory.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize, SWT.NORMAL));
		groupInventory.setText(" Ubicación del artículo (Bodega MAIN) ");
		GridData gd_groupInventory = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_groupInventory.verticalIndent = 25;
		groupInventory.setLayoutData(gd_groupInventory);
		GridLayout gl_groupInventory = new GridLayout(2, false);
		gl_groupInventory.horizontalSpacing = 10;
		gl_groupInventory.verticalSpacing = 10;
		gl_groupInventory.marginHeight = 15;
		groupInventory.setLayout(gl_groupInventory);
		
		Label lblCantidad = new Label(groupInventory, SWT.NONE);
		lblCantidad.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCantidad.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblCantidad.setText("Cantidad:");
		
		txtCantidad = new Text(groupInventory, SWT.BORDER);
		txtCantidad.setEditable(false);
		txtCantidad.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		GridData gd_txtCantidad = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtCantidad.widthHint = 100;
		txtCantidad.setLayoutData(gd_txtCantidad);
		
		Label lblBin01 = new Label(groupInventory, SWT.NONE);
		lblBin01.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBin01.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblBin01.setText("Bin01:");
		
		txtBin01 = new Text(groupInventory, SWT.BORDER);
		txtBin01.setEditable(false);
		txtBin01.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBin01.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		
		Label lblBin02 = new Label(groupInventory, SWT.NONE);
		lblBin02.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBin02.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblBin02.setText("Bin02:");
		
		txtBin02 = new Text(groupInventory, SWT.BORDER);
		txtBin02.setEditable(false);
		txtBin02.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBin02.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		
		Label lblBin03 = new Label(groupInventory, SWT.NONE);
		lblBin03.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBin03.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblBin03.setText("Bin03:");
		
		txtBin03 = new Text(groupInventory, SWT.BORDER);
		txtBin03.setEditable(false);
		txtBin03.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBin03.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		
		Label lblBin04 = new Label(groupInventory, SWT.NONE);
		lblBin04.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBin04.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		lblBin04.setText("Bin04:");
		
		txtBin04 = new Text(groupInventory, SWT.BORDER);
		txtBin04.setEditable(false);
		txtBin04.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtBin04.setFont(SWTResourceManager.getFont("Segoe UI", baseFontSize + labelFontSize, SWT.NORMAL));
		
		txtBarcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				logger.info("Keyx: " + e.keyCode);
				if (!txtBarcode.getText().isEmpty() && (e.keyCode == 13 || e.keyCode == 16777296)) {
					if (!txtBarcode.getText().equalsIgnoreCase(lastSearchedCode)) {
						showItemDetails(txtBarcode.getText());
					}
					txtBarcode.selectAll();
				}
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
		
		for (Inventory v : item.getInventory()) {
			txtCantidad.setText(" " + v.getQtyAvailable());
			txtBin01.setText(checkNull(v.getBin01()));
			txtBin02.setText(checkNull(v.getBin02()));
			txtBin03.setText(checkNull(v.getBin03()));
			txtBin04.setText(checkNull(v.getBin04()));
		}
		
	}
	
	
	private void resetFields() {
		txtItemDescription.setText(EMPTY_STRING);
		
		txtCantidad.setText(EMPTY_STRING);
		txtBin01.setText(EMPTY_STRING);
		txtBin02.setText(EMPTY_STRING);
		txtBin03.setText(EMPTY_STRING);
		txtBin04.setText(EMPTY_STRING);
		
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
