package com.orendel.transfer.composites;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.common.services.ImagesService;
import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.LoggedUserService;
import com.orendel.transfer.util.DateUtil;
import com.orendel.transfer.util.MessagesUtil;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;

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
	private Text txtBarcodeUpdatedBy;

	private Image image;	
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EditBarcodeComposite(Composite parent, int style, CounterpointController controller, String barcode) {
		super(parent, style);
		
		this.controller = controller;
		this.barcode = barcode;
		this.image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.BARCODE_EDIT_24);
		
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
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		
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
		GridData gd_groupBarcode = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_groupBarcode.verticalIndent = 10;
		groupBarcode.setLayoutData(gd_groupBarcode);
		groupBarcode.setBounds(0, 0, 70, 82);
		GridLayout gl_groupBarcode = new GridLayout(2, false);
		groupBarcode.setLayout(gl_groupBarcode);
		
		Label lblBarcode = new Label(groupBarcode, SWT.NONE);
		lblBarcode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcode.setText("Código:");
		
		txtBarcode = new Text(groupBarcode, SWT.BORDER);
		GridData gd_txtBarcode = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtBarcode.widthHint = 120;
		txtBarcode.setLayoutData(gd_txtBarcode);
		
		Label lblBarcodeType = new Label(groupBarcode, SWT.NONE);
		lblBarcodeType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcodeType.setText("Tipo:");
		
		txtBarcodeType = new Text(groupBarcode, SWT.BORDER);
		txtBarcodeType.setEnabled(false);
		txtBarcodeType.setEditable(false);
		GridData gd_txtBarcodeType = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtBarcodeType.widthHint = 80;
		txtBarcodeType.setLayoutData(gd_txtBarcodeType);
		
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
		
		Composite composite = new Composite(groupBarcode, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		txtBarcodeUpdated = new Text(composite, SWT.BORDER);
		GridData gd_txtBarcodeUpdated = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtBarcodeUpdated.widthHint = 130;
		txtBarcodeUpdated.setLayoutData(gd_txtBarcodeUpdated);
		txtBarcodeUpdated.setEditable(false);
		txtBarcodeUpdated.setEnabled(false);
		
		Label lblUpdatedBy = new Label(composite, SWT.NONE);
		GridData gd_lblUpdatedBy = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblUpdatedBy.horizontalIndent = 30;
		lblUpdatedBy.setLayoutData(gd_lblUpdatedBy);
		lblUpdatedBy.setText("Usuario:");
		
		txtBarcodeUpdatedBy = new Text(composite, SWT.BORDER);
		txtBarcodeUpdatedBy.setEnabled(false);
		txtBarcodeUpdatedBy.setEditable(false);
		GridData gd_txtUpdatedBy = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtUpdatedBy.widthHint = 100;
		txtBarcodeUpdatedBy.setLayoutData(gd_txtUpdatedBy);
		
		Composite compositeButtons = new Composite(this, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		compositeButtons.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSave.widthHint = 70;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setText("Guardar");
		
		Button btnCancel = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 70;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancelar");
		
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (validateFields()) {
					doSave();
					MessagesUtil.showInformation("Guardar código de barra", "El código de barra ha sido guardado exitosamente.");
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
		
		findItemDetails();
		
		getShell().setDefaultButton(btnSave);
	}
	
	private boolean validateFields() {
		String pCode = txtBarcode.getText();
		if (pCode.isEmpty()) {
			MessagesUtil.showInformation("Validación de campos",
					"El campo 'Código' no puede quedar en blanco.");
			txtBarcode.setFocus();
			return false;
		}
		if (pCode.length() > 25) {
			MessagesUtil.showInformation("Validación de campos", 
					"El código de barra no puede superar los 25 caracteres (actual: " + pCode.length() + ").");
			txtBarcode.setFocus();
			return false;
		}
		return true;
	}
	
	private void doSave() {
		String pCode = txtBarcode.getText();
		BarCode registro = locateBarcode(item, barcode);
		registro.setCode(pCode);
		registro.setUserId(LoggedUserService.INSTANCE.getUser().getUserName());
		registro.setUpdated(new Date());
		controller.persistBarcode(registro);
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
		txtItemCode.setText(item.getItemNo());
		txtDescription.setText(item.getDescription());
		
		BarCode barcodeItem = locateBarcode(item, barcode);
		txtBarcode.setText(barcodeItem.getCode());
		txtBarcodeType.setText(barcodeItem.getType().getBarCodeId());
		txtBarcodeDescription.setText(barcodeItem.getType().getDescription());
		txtBarcodeUpdated.setText(DateUtil.toString(new Date(), DateUtil.formatoFechaHora));
		txtBarcodeUpdatedBy.setText(barcodeItem.getUserId());
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
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
