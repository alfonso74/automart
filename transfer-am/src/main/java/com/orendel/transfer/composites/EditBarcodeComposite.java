package com.orendel.transfer.composites;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;
import com.orendel.transfer.util.DateUtil;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class EditBarcodeComposite extends Composite {
	private static final Logger logger = Logger.getLogger(EditBarcodeComposite.class);
	
	private CounterpointController controller;
	
	private Item item;
	
	private String barcode;
	private Text txtHeader;
	private Text txtItemCode;
	private Text txtDescription;
	private Text txtBarcode;
	private Text txtBarcodeType;
	private Text txtBarcodeDescription;
	private Text txtBarcodeUpdated;

	private Image image;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EditBarcodeComposite(Composite parent, int style, String barcode) {
		super(parent, style);
		
		this.barcode = barcode;
		this.controller = new CounterpointController("EditBarcode" + new Date().getTime());
		this.image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.ITEM_24);
		
		GridLayout gridLayout = new GridLayout(1, false);
		setLayout(gridLayout);
		
		Composite compositeTop = new Composite(this, SWT.NONE);
		compositeTop.setEnabled(false);
		GridData gd_compositeTop = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_compositeTop.heightHint = 35;
		gd_compositeTop.minimumHeight = 35;
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
		txtHeader.setText("Detalles del artículo / código de barra");
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		Group groupItem = new Group(this, SWT.NONE);
		groupItem.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		groupItem.setBounds(0, 0, 70, 82);
		groupItem.setLayout(new GridLayout(2, false));
		
		Label lblCode = new Label(groupItem, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Código:");
		
		txtItemCode = new Text(groupItem, SWT.BORDER);
		txtItemCode.setEnabled(false);
		txtItemCode.setEditable(false);
		txtItemCode.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		
		Label lblDescription = new Label(groupItem, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Descripción:");
		
		txtDescription = new Text(groupItem, SWT.BORDER);
		txtDescription.setEditable(false);
		txtDescription.setEnabled(false);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Group groupBarcode = new Group(this, SWT.NONE);
		groupBarcode.setText("Código de barra");
		GridData gd_groupBarcode = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_groupBarcode.verticalIndent = 10;
		groupBarcode.setLayoutData(gd_groupBarcode);
		groupBarcode.setBounds(0, 0, 70, 82);
		groupBarcode.setLayout(new GridLayout(2, false));
		
		Label lblBarcode = new Label(groupBarcode, SWT.NONE);
		lblBarcode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcode.setText("Código:");
		
		txtBarcode = new Text(groupBarcode, SWT.BORDER);
		GridData gd_txtBarcode = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtBarcode.widthHint = 100;
		txtBarcode.setLayoutData(gd_txtBarcode);
		
		Label lblBarcodeType = new Label(groupBarcode, SWT.NONE);
		lblBarcodeType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcodeType.setText("Tipo:");
		
		txtBarcodeType = new Text(groupBarcode, SWT.BORDER);
		txtBarcodeType.setEnabled(false);
		txtBarcodeType.setEditable(false);
		txtBarcodeType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblBarcodeDescription = new Label(groupBarcode, SWT.NONE);
		lblBarcodeDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcodeDescription.setText("Descripción:");
		
		txtBarcodeDescription = new Text(groupBarcode, SWT.BORDER);
		txtBarcodeDescription.setEnabled(false);
		txtBarcodeDescription.setEditable(false);
		txtBarcodeDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblUpdated = new Label(groupBarcode, SWT.NONE);
		lblUpdated.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUpdated.setText("Actualizado:");
		
		txtBarcodeUpdated = new Text(groupBarcode, SWT.BORDER);
		txtBarcodeUpdated.setEditable(false);
		txtBarcodeUpdated.setEnabled(false);
		txtBarcodeUpdated.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		addDisposeListener();
		
		findItemDetails();
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
	 * Actualiza los campos con la información contenida en el {@link Item}
	 */
	private void refreshFieldsContents() {
		txtItemCode.setText(item.getItemNo());
		txtDescription.setText(item.getDescription());
		
		BarCode barcodeItem = locateBarcode(item, barcode);
		txtBarcode.setText(barcodeItem.getCode());
		txtBarcodeType.setText(barcodeItem.getType().getBarCodeId());
		txtBarcodeDescription.setText(barcodeItem.getType().getDescription());
		txtBarcodeUpdated.setText(DateUtil.toString(new Date(), DateUtil.formatoFechaHora));
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
				controller.finalizarSesion();
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
