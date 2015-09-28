package com.orendel.transfer.composites;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.EplPrintService;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;
import com.orendel.transfer.util.MessagesUtil;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

public class PrintLabelComposite extends Composite {
	private static final Logger logger = Logger.getLogger(EditBarcodeComposite.class);
	
	private CounterpointController controller;
	
	private Item item;
	
	private String barcode;
	private Text txtHeader;
	
	private Image image;	
	private Text txtItemDescription;
	private Text txtBarcode;
	private Text txtPrintQty;
	
	private final Cursor waitCursor = new Cursor(getDisplay(), SWT.CURSOR_WAIT);
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PrintLabelComposite(Composite parent, int style, CounterpointController controller, String barcode) {
		super(parent, style);
		
		this.controller = controller;
		this.barcode = barcode;
		this.image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.ITEM_24);
		setLayout(new GridLayout(1, false));
		
		Composite compositeTop = new Composite(this, SWT.NONE);
		compositeTop.setEnabled(false);
		GridData gd_compositeTop = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_compositeTop.minimumHeight = 35;
		gd_compositeTop.heightHint = 35;
		compositeTop.setLayoutData(gd_compositeTop);
		compositeTop.setBounds(0, 0, 64, 64);
		compositeTop.setLayout(new GridLayout(2, false));
		
		Label lblHeader = new Label(compositeTop, SWT.NONE);
		lblHeader.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
		lblHeader.setImage(image);
		
		txtHeader = new Text(compositeTop, SWT.READ_ONLY | SWT.WRAP);
		txtHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		txtHeader.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtHeader.setEditable(false);
		txtHeader.setText("Información para impresión de etiquetas");
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		
		Group groupLabelInfo = new Group(this, SWT.NONE);
		groupLabelInfo.setLayout(new GridLayout(2, false));
		groupLabelInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblBarcode = new Label(groupLabelInfo, SWT.NONE);
		lblBarcode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcode.setText("Código de barra:");
		
		txtBarcode = new Text(groupLabelInfo, SWT.BORDER);
		txtBarcode.setEnabled(false);
		txtBarcode.setEditable(false);
		txtBarcode.setText("");
		GridData gd_txtBarcode = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtBarcode.widthHint = 120;
		txtBarcode.setLayoutData(gd_txtBarcode);
		
		Label lblItem = new Label(groupLabelInfo, SWT.NONE);
		lblItem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblItem.setText("Artículo:");
		
		txtItemDescription = new Text(groupLabelInfo, SWT.BORDER);
		txtItemDescription.setEnabled(false);
		txtItemDescription.setEditable(false);
		txtItemDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPrintQty = new Label(groupLabelInfo, SWT.NONE);
		lblPrintQty.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPrintQty.setText("Cantidad:");
		
		txtPrintQty = new Text(groupLabelInfo, SWT.BORDER);
		GridData gd_txtPrintQty = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtPrintQty.widthHint = 45;
		txtPrintQty.setLayoutData(gd_txtPrintQty);
		
		Composite compositeButtons = new Composite(this, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		compositeButtons.setLayout(new GridLayout(2, false));
		
		Button btnPrint = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnPrint = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnPrint.widthHint = 70;
		btnPrint.setLayoutData(gd_btnPrint);
		btnPrint.setText("Imprimir");
		
		Button btnCancel = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 70;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancelar");
		
		btnPrint.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (validateFields()) {
					doPrint();
					MessagesUtil.showInformation("Imprimir etiqueta", "El proceso de impresión ha sido realizado exitosamente.");
					getShell().close();
				};				
			}
		});
		
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getShell().close();
			}
		});
		
		addDisposeListener();
		findItemDetails();
		getShell().setDefaultButton(btnPrint);
	}
	
	
	private boolean validateFields() {
		String pCode = txtPrintQty.getText();
		if (pCode.isEmpty()) {
			MessagesUtil.showInformation("Validación de campos",
					"El campo 'Cantidad' no puede quedar en blanco.");
			txtPrintQty.setFocus();
			return false;
		}
		if (pCode.length() > 3) {
			MessagesUtil.showInformation("Validación de campos", 
					"La cantidad de etiquetas a imprimir no puede ser mayor a 999.");
			txtPrintQty.setFocus();
			return false;
		}
		return true;
	}
	
	
	private void doPrint() {
		EplPrintService printService = new EplPrintService();
		
		int labelWidth = 450;
		String barcode = txtBarcode.getText();
		String description = txtItemDescription.getText();
		int cantidad = Integer.parseInt(txtPrintQty.getText());
		
		// set the "waiting" cursor
		getShell().setCursor(waitCursor);
		try {
			printService.printAutomartLabel(labelWidth, barcode, description, "Línea adicional", cantidad);
		} catch (Exception e) {
			getShell().setCursor(null);   // making sure to show the default cursor
			throw e;
		}
		// reset icon to the default one
		getShell().setCursor(null);
	}
	
	
	/**
	 * Busca un {@link Item} en base de datos para mostrar sus datos en la pantalla.
	 */
	private void findItemDetails() {
		item = controller.findItemByBarCode(barcode);
		logger.info("Barcode " + barcode + ", item: " + item);
		if (item != null) {
			refreshFieldsContents();
		}
	}
	
	/**
	 * Actualiza los campos del UI con la información contenida en el {@link Item}
	 */
	private void refreshFieldsContents() {
		txtItemDescription.setText(item.getDescription());
		
		BarCode barcodeItem = locateBarcode(item, barcode);
		txtBarcode.setText(barcodeItem.getCode());
		txtPrintQty.setText("1");
		txtPrintQty.selectAll();
	}
	
	private BarCode locateBarcode(Item item, String barcode) {
		BarCode result = null;
		for (BarCode v : item.getBarcodeList()) {
			if (v.getCode().equalsIgnoreCase(barcode)) {
				result = v;
				break;
			}
		}
		return result;
	}
	
	private void addDisposeListener() {
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				logger.info("Dispose listener called!");
				waitCursor.dispose();
			}
		});
	}
				
				
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
