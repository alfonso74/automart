package com.orendel.transfer.composites;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.common.services.ImagesService;
import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.BarCodeType;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.LoggedUserService;
import com.orendel.transfer.util.ComboData;
import com.orendel.transfer.util.MessagesUtil;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Combo;


public class CommonAddBarcodeComposite extends Composite {
	private static final Logger logger = Logger.getLogger(CommonAddBarcodeComposite.class);
	
	private static final String EMPTY_STRING = "";
	
	private CounterpointController controller;
	
	private String lastSearchedCode;
	
	private Item currentItem;
	
	private Text txtHeader;
	private Text txtItemCode;
	private Text txtDescription;
	private Text txtBarcode;
	private Combo comboBarcodeType;
	private Text txtBarcodeDescription;

	private Image image;
	
	private ComboData<BarCodeType> cdBarcodeTypes;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CommonAddBarcodeComposite(Composite parent, int style, CounterpointController controller) {
		super(parent, style);
		
		this.controller = controller;
		this.image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.BARCODE_EDIT_24);
		this.initBarcodeTypeComboData();
		
		GridLayout gridLayout = new GridLayout(1, false);
		setLayout(gridLayout);
		
		Composite compositeTop = new Composite(this, SWT.NONE);
		compositeTop.setEnabled(false);
		GridData gd_compositeTop = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_compositeTop.heightHint = 35;
		gd_compositeTop.minimumHeight = 35;
		compositeTop.setLayoutData(gd_compositeTop);
		compositeTop.setBounds(0, 0, 64, 64);
		GridLayout gl_compositeTop = new GridLayout(2, false);
		compositeTop.setLayout(gl_compositeTop);
		
		Label lblHeader = new Label(compositeTop, SWT.NONE);
		lblHeader.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
		lblHeader.setImage(image);
		
		txtHeader = new Text(compositeTop, SWT.READ_ONLY | SWT.WRAP);
		txtHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		txtHeader.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtHeader.setEditable(false);
		txtHeader.setText("Agregar código de barra");
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		Group groupItem = new Group(this, SWT.NONE);
		groupItem.setText(" Selección del artículo ");
		GridData gd_groupItem = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_groupItem.verticalIndent = 5;
		groupItem.setLayoutData(gd_groupItem);
		groupItem.setBounds(0, 0, 70, 82);
		GridLayout gl_groupItem = new GridLayout(3, false);
		groupItem.setLayout(gl_groupItem);
		
		Label lblCode = new Label(groupItem, SWT.NONE);
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Código:");
		
		txtItemCode = new Text(groupItem, SWT.BORDER);
		GridData gd_txtItemCode = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txtItemCode.widthHint = 120;
		txtItemCode.setLayoutData(gd_txtItemCode);
		
		Button btnSearch = new Button(groupItem, SWT.NONE);
		btnSearch.setToolTipText("Busca un artículo para agregarle un nuevo código de barra");
		btnSearch.setText("Buscar");
		
		Label lblDescription = new Label(groupItem, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Descripción:");
		
		txtDescription = new Text(groupItem, SWT.BORDER);
		txtDescription.setEditable(false);
		txtDescription.setEnabled(false);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		
		Group groupBarcode = new Group(this, SWT.NONE);
		groupBarcode.setText("Nuevo código de barra");
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
		
		comboBarcodeType = new Combo(groupBarcode, SWT.READ_ONLY);
		comboBarcodeType.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboBarcodeType.setItems(cdBarcodeTypes.getEntriesAsStringArray());
		
		Label lblBarcodeDescription = new Label(groupBarcode, SWT.NONE);
		lblBarcodeDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcodeDescription.setText("Descripción:");
		
		txtBarcodeDescription = new Text(groupBarcode, SWT.BORDER);
		txtBarcodeDescription.setEnabled(false);
		txtBarcodeDescription.setEditable(false);
		txtBarcodeDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Composite compositeButtons = new Composite(this, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		compositeButtons.setLayout(new GridLayout(2, false));
		
		Button btnSave = new Button(compositeButtons, SWT.NONE);
		btnSave.setToolTipText("Agrega el código de barra al artículo seleccionado");
		GridData gd_btnSave = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSave.widthHint = 70;
		btnSave.setLayoutData(gd_btnSave);
		btnSave.setText("Agregar");
		
		Button btnCancel = new Button(compositeButtons, SWT.NONE);
		btnCancel.setToolTipText("Cancela la acción actual sin realizar modificaciones");
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 70;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancelar");
		
		txtItemCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				logger.info("Keyx: " + e.keyCode);
				if (!txtItemCode.getText().isEmpty()	&& (e.keyCode == 13 || e.keyCode == 16777296)) {
					if (!txtItemCode.getText().equalsIgnoreCase(lastSearchedCode)) {
						showItemDetails(txtItemCode.getText());
					}
					txtItemCode.selectAll();
				}
			}
		});
		
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showItemDetails(txtItemCode.getText());
				txtItemCode.setFocus();
				txtItemCode.selectAll();
			}
		});
		
		comboBarcodeType.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (comboBarcodeType.getText() != null && !comboBarcodeType.getText().isEmpty()) {
					BarCodeType v = cdBarcodeTypes.getEntry(comboBarcodeType.getText());
					txtBarcodeDescription.setText(v.getDescription());
				}
			}
		});
		
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
		
//		getShell().setDefaultButton(btnSave);
	}
	
	
	
	private boolean validateFields() {
		String pCode = txtBarcode.getText();
		int pBarcodeTypeSelectedEntry = comboBarcodeType.getSelectionIndex();
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
		if (pBarcodeTypeSelectedEntry == -1) {
			MessagesUtil.showInformation("Validación de campos", 
					"Debe indicar el tipo de código de barra que se está creando.");
			comboBarcodeType.setFocus();
			return false;
		}
		return true;
	}
	
	private void doSave() {
		String pCode = txtBarcode.getText();
		BarCode registro = new BarCode();
		registro.setCode(pCode);
		registro.setUserId(LoggedUserService.INSTANCE.getUser().getUserName());
		registro.setUpdated(new Date());
		registro.setItem(currentItem);
		registro.setType(cdBarcodeTypes.getEntry(comboBarcodeType.getText()));
		controller.persistBarcode(registro);
	}
	
	/**
	 * Busca un {@link Item} en base de datos para mostrar sus datos en la pantalla.
	 */
	private void showItemDetails(String code) {
		resetFields();
		
		lastSearchedCode = code;
		if (code == null || code.isEmpty()) {
			return;
		}
		
		Item item = controller.findItem(code);
		if (item != null) {
			logger.info("Artículo encontrado en DB: " + item.getDescription());
			this.currentItem = item;
			refreshFieldsContents();
		} else {
			MessagesUtil.showWarning("Búsqueda por código", "No se encontró ningún artículo con el código de barra o código de item suministrado: '" + code + "'.");
		}	
	}
	
	private void resetFields() {
		currentItem = null;
		txtDescription.setText(EMPTY_STRING);
		txtBarcode.setText(EMPTY_STRING);
		comboBarcodeType.deselectAll();
		txtBarcodeDescription.setText(EMPTY_STRING);
	}
	
	/**
	 * Actualiza los campos del UI con la información contenida en el {@link Item}
	 */
	private void refreshFieldsContents() {
		txtDescription.setText(currentItem.getDescription());
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void initBarcodeTypeComboData() {
		List<BarCodeType> barcodeTypes = controller.getBarcodeTypes();
		Map<String, BarCodeType> result = new HashMap<String, BarCodeType>();
		for (BarCodeType barcodeType : barcodeTypes) {
			result.put(barcodeType.getBarCodeId(), barcodeType);
		}
		cdBarcodeTypes = new ComboData<BarCodeType>(result);
	}
}
