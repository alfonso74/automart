package com.orendel.transfer.composites;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.orendel.common.services.ImagesService;
import com.orendel.counterpoint.domain.BarCode;
import com.orendel.counterpoint.domain.BarCodeType;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.EplPrintService;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.util.ComboData;
import com.orendel.transfer.util.MessagesUtil;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;


public class CommonPrintLabelComposite extends Composite {
	private static final Logger logger = Logger.getLogger(CommonPrintLabelComposite.class);
	
	private static final String EMPTY_STRING = "";
	private static final String USE_ITEM_CODE = "Imprimir el código de artículo";
	
	private CounterpointController controller;
	
	private String lastSearchedCode;
	
	private Item currentItem;
	
	private Text txtHeader;
	private Text txtItemCode;
	private Text txtItemDescription;
	
	private Image image;	
	private Text txtPrintcode;
	private Combo comboBarcodeType;
	private Text txtPrintQty;
	private Button btnCompressBarcode;
	
	private ComboData<BarCodeType> cdBarcodeTypes;
	
	private final Cursor waitCursor = new Cursor(getDisplay(), SWT.CURSOR_WAIT);
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CommonPrintLabelComposite(Composite parent, int style, CounterpointController controller) {
		super(parent, style);
		
		this.controller = controller;
		this.image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.PRINT_24);
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
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		Group groupItem = new Group(this, SWT.NONE);
		groupItem.setText(" Selección del artículo ");
		GridData gd_groupItem = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_groupItem.verticalIndent = 5;
		groupItem.setLayoutData(gd_groupItem);
		groupItem.setBounds(0, 0, 70, 82);
		GridLayout gl_groupItem = new GridLayout(3, false);
		groupItem.setLayout(gl_groupItem);
		
		Label lblItemCode = new Label(groupItem, SWT.NONE);
		lblItemCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblItemCode.setText("Código:");
		
		txtItemCode = new Text(groupItem, SWT.BORDER);
		GridData gd_txtItemCode = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txtItemCode.widthHint = 100;
		txtItemCode.setLayoutData(gd_txtItemCode);
		
		Button btnSearch = new Button(groupItem, SWT.NONE);
		btnSearch.setToolTipText("Busca un artículo para agregarle un nuevo código de barra");
		btnSearch.setText("Buscar");
		
		Label lblDescription = new Label(groupItem, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Descripción:");
		
		txtItemDescription = new Text(groupItem, SWT.BORDER);
		txtItemDescription.setEditable(false);
		txtItemDescription.setEnabled(false);
		txtItemDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		
		Group groupLabelInfo = new Group(this, SWT.NONE);
		groupLabelInfo.setText("Código a imprimir");
		groupLabelInfo.setLayout(new GridLayout(2, false));
		GridData gd_groupLabelInfo = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_groupLabelInfo.verticalIndent = 10;
		groupLabelInfo.setLayoutData(gd_groupLabelInfo);
				
		Label lblBarcodeType = new Label(groupLabelInfo, SWT.NONE);
		lblBarcodeType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcodeType.setText("Tipo:");
		
		comboBarcodeType = new Combo(groupLabelInfo, SWT.READ_ONLY);
		comboBarcodeType.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1));
		comboBarcodeType.add(USE_ITEM_CODE);
		
		Label lblBarcode = new Label(groupLabelInfo, SWT.NONE);
		lblBarcode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBarcode.setText("Código:");
		
		txtPrintcode = new Text(groupLabelInfo, SWT.BORDER);
		txtPrintcode.setEditable(false);
		GridData gd_txtBarcode = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtBarcode.widthHint = 100;
		txtPrintcode.setLayoutData(gd_txtBarcode);
		
		Label lblPrintQty = new Label(groupLabelInfo, SWT.NONE);
		GridData gd_lblPrintQty = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPrintQty.horizontalIndent = 14;
		lblPrintQty.setLayoutData(gd_lblPrintQty);
		lblPrintQty.setText("Cantidad:");
		
		txtPrintQty = new Text(groupLabelInfo, SWT.BORDER);
		GridData gd_txtPrintQty = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_txtPrintQty.widthHint = 45;
		txtPrintQty.setLayoutData(gd_txtPrintQty);
		
		btnCompressBarcode = new Button(groupLabelInfo, SWT.CHECK);
		GridData gd_btnCompressBarcode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_btnCompressBarcode.verticalIndent = 10;
		btnCompressBarcode.setLayoutData(gd_btnCompressBarcode);
		btnCompressBarcode.setText("Reducir ancho del código de barra");
		
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
		
		txtItemCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				logger.info("Keyx: " + e.keyCode);
				if (!txtItemCode.getText().isEmpty() && (e.keyCode == 13 || e.keyCode == 16777296)) {
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
				if (comboBarcodeType.getText() != null && !comboBarcodeType.getText().isEmpty() &&
						currentItem != null) {
					BarCodeType barcodeType = cdBarcodeTypes.getEntry(comboBarcodeType.getText());
					if (barcodeType == null) {  // implica que se seleccionó la opción 0 (usar código del item)
						txtPrintcode.setText(currentItem.getItemNo());
					} else {
						for (BarCode v : currentItem.getBarcodeList()) {
							if (v.getType().equals(barcodeType)) {
								txtPrintcode.setText(v.getCode());
								break;
							}
						}
					}
				}
			}
		});
		
		btnPrint.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (validateFields()) {
					doPrint();
					MessagesUtil.showInformation("Imprimir etiqueta", "Etiqueta enviada correctamente a la impresora.");
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
//		getShell().setDefaultButton(btnPrint);
	}
	
	
	private boolean validateFields() {
		String pCode = txtItemCode.getText();
		int pBarcodeTypeSelectedEntry = comboBarcodeType.getSelectionIndex();
		String pQuantity = txtPrintQty.getText();
		
		if (pCode.isEmpty()) {
			MessagesUtil.showInformation("Validación de campos",
					"El campo 'Código' no puede quedar en blanco.");
			txtItemCode.setFocus();
			return false;
		}
		if (pBarcodeTypeSelectedEntry == -1) {
			MessagesUtil.showInformation("Validación de campos", 
					"Debe indicar el tipo de código de barra que se está imprimiendo.");
			comboBarcodeType.setFocus();
			return false;
		}
		if (pQuantity.isEmpty()) {
			MessagesUtil.showInformation("Validación de campos",
					"El campo 'Cantidad' no puede quedar en blanco.");
			txtPrintQty.setFocus();
			return false;
		}
		if (pQuantity.length() > 3) {
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
		String barcode = txtPrintcode.getText();
		boolean reduceBarcodeBarsWidth = btnCompressBarcode.getSelection() ? true : false;
		String description = txtItemDescription.getText();
		String timeStampLabel = currentItem.getDateFormattedForLabel(currentItem.getLastReceived());
		int cantidad = Integer.parseInt(txtPrintQty.getText());
		
		// set the "waiting" cursor
		getShell().setCursor(waitCursor);
		try {
			printService.printAutomartLabel(labelWidth, barcode, reduceBarcodeBarsWidth, description, timeStampLabel, cantidad);
		} catch (RuntimeException e) {
			getShell().setCursor(null);   // making sure to show the default cursor
			throw e;
		}
		// reset icon to the default one
		getShell().setCursor(null);
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
		txtItemDescription.setText(EMPTY_STRING);
		comboBarcodeType.deselectAll();
		txtPrintcode.setText(EMPTY_STRING);
		txtPrintQty.setText("1");
		btnCompressBarcode.setSelection(false);
	}
	
	/**
	 * Actualiza los campos del UI con la información contenida en el {@link Item}
	 */
	private void refreshFieldsContents() {
		txtItemDescription.setText(currentItem.getDescription());
		
		cdBarcodeTypes = getComboDataBarcodeTypesForItem(currentItem);
		comboBarcodeType.setItems(cdBarcodeTypes.getEntriesAsStringArray());
		comboBarcodeType.add(USE_ITEM_CODE, 0);
		comboBarcodeType.select(0);
		
		txtPrintcode.setText(currentItem.getItemNo());
		txtPrintQty.setText("1");
		txtPrintQty.selectAll();
	}
	
	private ComboData<BarCodeType> getComboDataBarcodeTypesForItem(Item item) {
		Map<String, BarCodeType> result = new HashMap<String, BarCodeType>();
		for (BarCode barcode : item.getBarcodeList()) {
			BarCodeType barcodeType = barcode.getType();
			result.put(barcodeType.getBarCodeId(), barcodeType);
		}
		cdBarcodeTypes = new ComboData<BarCodeType>(result);
		return cdBarcodeTypes;
	}
	
	
	private void addDisposeListener() {
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				logger.debug("Dispose listener called!");
				waitCursor.dispose();
			}
		});
	}
				
				
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
