package com.orendel.transfer.editors;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

import com.orendel.counterpoint.domain.Item;
import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.transfer.controllers.TransferControlController;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.domain.TransferControl;
import com.orendel.transfer.domain.TransferControlLine;
import com.orendel.transfer.ui.login.LoggedUserService;
import com.orendel.transfer.util.MessagesUtil;
import com.orendel.transfer.util.TransferMapper;


public class CreateTransferInEditor extends Composite {
	private static final Logger logger = Logger.getLogger(CreateTransferInEditor.class);
	
	private TransferControl tcControl;
	
	private CounterpointController cpController;
	private TransferControlController tcController;
	
	private boolean saveFlag = false;
	
	private Text txtTransferNo;
	private Table tableTransferLines;
	
	private Text txtBarcode;
	private Text txtQty;
	
	private Button btnParcial;
	private Button btnGuardar;
	private Button btnLimpiar;
	
	private Listener listenerF04;
	private Listener listenerF09;
	private Listener listenerF12;
	private Text txtLines;
	private Text txtReceived;
	private Text txtTotal;
	private Text txtPending;
	private Text txtReference;
	private Text txtComment1;
	private Text txtComment2;
	private Text txtComment3;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CreateTransferInEditor(Composite parent, int style) {
		super(parent, style);
		
		cpController = new CounterpointController("TransferIn");
		tcController = new TransferControlController("TransferControl");
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group groupFind = new Group(this, SWT.NONE);
		groupFind.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_groupFind = new GridLayout(4, false);
		gl_groupFind.marginBottom = 5;
		gl_groupFind.marginHeight = 0;
		groupFind.setLayout(gl_groupFind);
		
		Label lblNoTransfer = new Label(groupFind, SWT.NONE);
		lblNoTransfer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNoTransfer.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNoTransfer.setText("Transferencia:");
		
		txtTransferNo = new Text(groupFind, SWT.BORDER);
		GridData gd_txtNoFactura = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtNoFactura.widthHint = 100;
		txtTransferNo.setLayoutData(gd_txtNoFactura);
		txtTransferNo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtTransferNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				if (!txtInvoiceNo.getText().isEmpty() && e.keyCode == 13) {
				if (e.keyCode == 13) {
					if (existeRegistro(txtTransferNo.getText())) {
						txtQty.setFocus();
						txtQty.selectAll();
					} else {
						tableTransferLines.removeAll();
						txtTransferNo.setFocus();
						txtTransferNo.selectAll();
					}
					
				}
			}
		});
		
		Button btnFactura = new Button(groupFind, SWT.NONE);
		btnFactura.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnFactura.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					if (existeRegistro(txtTransferNo.getText())) {
						txtQty.setFocus();
						txtQty.selectAll();
					} else {
						tableTransferLines.removeAll();
						txtTransferNo.setFocus();
						txtTransferNo.selectAll();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnFactura.setText("Buscar");
		
		Label lblItem = new Label(groupFind, SWT.NONE);
		lblItem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblItem.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblItem.setText("Artículo:");
		
		txtQty = new Text(groupFind, SWT.BORDER);
		txtQty.setText("1");
		GridData gd_txtQty = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtQty.widthHint = 25;
		txtQty.setLayoutData(gd_txtQty);
		txtQty.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtQty.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					txtBarcode.setFocus();
				}
			}
		});
		
				txtQty.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						super.widgetSelected(e);
						txtQty.selectAll();
					}
				});
		
		txtBarcode = new Text(groupFind, SWT.BORDER);
		txtBarcode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		txtBarcode.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtBarcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				logger.info("Key: " + arg0.keyCode);
				if (arg0.keyCode == 13) {
					if (!txtBarcode.getText().isEmpty()) {
						accountForItemWithBarCodeOrItemCode(txtBarcode.getText());
						refreshFormDetails();
						txtBarcode.setText("");
						txtQty.setFocus();
						txtQty.setText("1");
						txtQty.selectAll();
					} else {
						logger.info("Código de barra en blanco");
						txtQty.setFocus();
						txtQty.setText("1");
						txtQty.selectAll();
					}
				}
			}
		});
		
		Group groupReference = new Group(this, SWT.NONE);
		GridLayout gl_groupReference = new GridLayout(6, false);
		groupReference.setLayout(gl_groupReference);
		groupReference.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Label lblReference = new Label(groupReference, SWT.NONE);
		lblReference.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_lblReference = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblReference.horizontalIndent = 19;
		lblReference.setLayoutData(gd_lblReference);
		lblReference.setText("Referencia:");
		
		txtReference = new Text(groupReference, SWT.BORDER);
		txtReference.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_txtReference = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtReference.widthHint = 160;
		txtReference.setLayoutData(gd_txtReference);
		
		Label lblComments = new Label(groupReference, SWT.NONE);
		GridData gd_lblComments = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblComments.horizontalIndent = 40;
		lblComments.setLayoutData(gd_lblComments);
		lblComments.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblComments.setText("Comentarios:");
		
		txtComment1 = new Text(groupReference, SWT.BORDER);
		txtComment1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_txtComment1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtComment1.widthHint = 200;
		txtComment1.setLayoutData(gd_txtComment1);
		
		txtComment2 = new Text(groupReference, SWT.BORDER);
		txtComment2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_txtComment2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtComment2.widthHint = 200;
		txtComment2.setLayoutData(gd_txtComment2);
		
		txtComment3 = new Text(groupReference, SWT.BORDER);
		txtComment3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_txtComment3 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtComment3.widthHint = 200;
		txtComment3.setLayoutData(gd_txtComment3);
		
		tableTransferLines = new Table(this, SWT.BORDER | SWT.HIDE_SELECTION);
		tableTransferLines.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_tableInvoiceLines = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tableInvoiceLines.verticalIndent = 10;
		tableTransferLines.setLayoutData(gd_tableInvoiceLines);
		tableTransferLines.setHeaderVisible(true);
		tableTransferLines.setLinesVisible(true);
		
		TableColumn tblclmnCant = new TableColumn(tableTransferLines, SWT.CENTER);
		tblclmnCant.setToolTipText("Cantidad total esperada");
		tblclmnCant.setWidth(50);
		tblclmnCant.setText("Total");
		
		TableColumn tblclmnOk = new TableColumn(tableTransferLines, SWT.CENTER);
		tblclmnOk.setToolTipText("Cantidad recibida");
		tblclmnOk.setWidth(50);
		tblclmnOk.setText("Recib");
		
		TableColumn tblclmnDif = new TableColumn(tableTransferLines, SWT.NONE);
		tblclmnDif.setToolTipText("Diferencia");
		tblclmnDif.setWidth(50);
		tblclmnDif.setText("Difer");
		
		TableColumn tblclmnItemNo = new TableColumn(tableTransferLines, SWT.LEFT);
		tblclmnItemNo.setWidth(100);
		tblclmnItemNo.setText("Item No.");
		
		TableColumn tblclmnDescripcin = new TableColumn(tableTransferLines, SWT.LEFT);
		tblclmnDescripcin.setWidth(300);
		tblclmnDescripcin.setText("Descripción");
		
		TableColumn tblclmnComentarios = new TableColumn(tableTransferLines, SWT.NONE);
		tblclmnComentarios.setWidth(300);
		tblclmnComentarios.setText("Comentario");
		
		Composite grpTotales = new Composite(this, SWT.NONE);
		grpTotales.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		GridLayout gl_grpTotales = new GridLayout(8, false);
		gl_grpTotales.marginHeight = 0;
		gl_grpTotales.marginBottom = 10;
		grpTotales.setLayout(gl_grpTotales);
		grpTotales.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1));
		
		Label lblLineas = new Label(grpTotales, SWT.NONE);
		lblLineas.setText("Total de líneas:");
		
		txtLines = new Text(grpTotales, SWT.BORDER);
		txtLines.setEnabled(false);
		
		Label lblArtRecibidos = new Label(grpTotales, SWT.NONE);
		GridData gd_lblArtRecibidos = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblArtRecibidos.horizontalIndent = 20;
		lblArtRecibidos.setLayoutData(gd_lblArtRecibidos);
		lblArtRecibidos.setText("Art. recibidos:");
		
		txtReceived = new Text(grpTotales, SWT.BORDER);
		txtReceived.setEnabled(false);
		
		Label lblValor = new Label(grpTotales, SWT.NONE);
		GridData gd_lblValor = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblValor.horizontalIndent = 20;
		lblValor.setLayoutData(gd_lblValor);
		lblValor.setText("Valor:");
		
		txtTotal = new Text(grpTotales, SWT.BORDER);
		txtTotal.setEnabled(false);
		
		Label lblArtPendientes = new Label(grpTotales, SWT.NONE);
		GridData gd_lblArtPendientes = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblArtPendientes.horizontalIndent = 20;
		lblArtPendientes.setLayoutData(gd_lblArtPendientes);
		lblArtPendientes.setText("Art. pendientes");
		
		txtPending = new Text(grpTotales, SWT.BORDER);
		txtPending.setEnabled(false);
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_label.verticalIndent = 5;
		label.setLayoutData(gd_label);
		
		Composite compositeActions = new Composite(this, SWT.NONE);
		GridLayout gl_compositeActions = new GridLayout(3, true);
		gl_compositeActions.marginWidth = 0;
		compositeActions.setLayout(gl_compositeActions);
		
		btnParcial = new Button(compositeActions, SWT.NONE);
		btnParcial.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnParcial.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnParcial.setText("Txfer parcial (F4)");
		btnParcial.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Partial transfer button pressed!");
				createPartialTransfer();
			}
		});
		
		btnLimpiar = new Button(compositeActions, SWT.NONE);
		btnLimpiar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnLimpiar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnLimpiar.setText("Limpiar (F9)");
		btnLimpiar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Reset button pressed!");
				resetTransferData();
			}
		});
		
		btnGuardar = new Button(compositeActions, SWT.NONE);
		btnGuardar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnGuardar.setEnabled(false);
		btnGuardar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnGuardar.setText("Txfer realizada (F12)");
		btnGuardar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save transfer button pressed!");
				createTransfer();
			}
		});
		
		addGlobalListeners();
		
		txtTransferNo.setFocus();
	}
	
	
	/**
	 * Listeners for global shortcuts like F10 (reset form) and F12 (save form). 
	 */
	private void addGlobalListeners() {
		Display display = getShell().getDisplay();
		
		listenerF12 = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.F12) {
					System.out.println("F12 (save transfer) pressed!");
					if (btnGuardar.getEnabled()) {
						createTransfer();
					}
				}
			}
		};
		
		listenerF09 = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.F9) {
					System.out.println("F9 (reset transfer form) pressed!");
					resetTransferData();
				}
			}
		};
		
		listenerF04 = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.F4) {
					System.out.println("F4 (partial transfer form) pressed!");
					createPartialTransfer();
				}
			}
		};
		
		display.addFilter(SWT.KeyDown, listenerF04);
		display.addFilter(SWT.KeyDown, listenerF12);		
		display.addFilter(SWT.KeyDown, listenerF09);		
	}
	
	private void createTransfer() {
		saveTransfer();
		logger.info("Control de transferencia generada: " + tcControl.getId());
		MessagesUtil.showInformation("Guardar transferencia", "<size=+6>Se ha guardado exitosamente la transferencia (número " + tcControl.getId() + ").</size>");
		resetFields();
		txtTransferNo.setFocus();
	}
	
	private void createPartialTransfer() {
		savePartialTransfer();
		logger.info("Control de transferencia PARCIAL generada: " + tcControl.getId());
		MessagesUtil.showInformation("Guardar transferencia parcial", "<size=+6>Se ha guardado exitosamente la transferencia parcial (número " + tcControl.getId() + ").</size>");
		resetFields();
		txtTransferNo.setFocus();
	}
	
	private boolean existeRegistro(String transferNo) {
		boolean result = false;
		
		if (transferNo.isEmpty()) {
			return result;
		}
		TransferControl tc = findPartialTransferControl(transferNo);
		if (tc != null) {
			int action = MessagesUtil.showConfirmation("Buscar transferencia", "<size=+2>La transferencia número " + transferNo + " ya tiene un control de "
					+ "entrega parcial, desea completarla?</size>");
			logger.info("Botón presionado: " + action);
			if (action == 0) {
				logger.info("Editar transferencia parcial: " + transferNo + ", id: " + tc.getId());
			} else {
				return false;
			}
		} else {
			tc = tryToGetTransferControlFromCounterPoint(transferNo);
		}
		if (tc != null) {
			tcControl = tc;
			refreshFormDetails();
			result = true;
		} else {
			MessagesUtil.showWarning("Buscar transferencia", "No se encontró la transferencia número " + transferNo + ".");
		}
		return result;
	}
	
	private TransferControl tryToGetTransferControlFromCounterPoint(String transferNo) {
		TransferControl tc = null;
		TransferIn transferIn = cpController.findTransferInByNumber(transferNo);
		if (transferIn != null) {
			logger.info("Transferencia encontrada (CP), número: " + transferIn.getId() + ", líneas: " + transferIn.getLines().size());
			tc = TransferMapper.from(transferIn);
		}
		return tc;
	}
	
	private TransferControl findPartialTransferControl(String transferNo) {
		TransferControl tc = tcController.findPartialTransferControlByNumber(transferNo);
		if (tc != null) {
			logger.info("Transferencia encontrada (Delivery), número: " + tc.getId() + ", líneas: " + tc.getLines().size());
		}
		return tc;
	}
	
	
	private void accountForItemWithBarCodeOrItemCode(String barcode) {
		Item item = cpController.findItemByBarCode(barcode);
		if (item == null) {
			item = cpController.findItemByItemCode(barcode);
		}
		if (item != null) {
			logger.info("Artículo encontrado en DB: " + item.getDescription());

			int qty = Integer.parseInt(txtQty.getText());
			TransferControlLine line = tcControl.adjustReceivedQuantityForItem(item.getItemNo(), qty);
			if (line == null) {
				logger.info("Artículo NO encontrado en la transferencia: " + item.getDescription());
				MessagesUtil.showWarning("Búsqueda por código", "No se encontró ninguna línea con el código suministrado: " + barcode + ".");
			}			

		} else {
			MessagesUtil.showError("Búsqueda por código", "No se encontró ningún artículo con el código de barra suministrado: " + barcode + ".");
		}
	}

	
	private void refreshFormDetails() {
		if (tcControl == null) {
			logger.warn("TransferControl object is null!");
			return;
		}
		
		txtReference.setText(checkNull(tcControl.getReference()));
		if (tcControl.getComments() != null) {
			txtComment1.setText(checkNull(tcControl.getComments().getComment1()));
			txtComment2.setText(checkNull(tcControl.getComments().getComment2()));
			txtComment3.setText(checkNull(tcControl.getComments().getComment3()));
		}
		
		txtLines.setText("" + tcControl.getLines().size());
		txtReceived.setText("" + tcControl.getTotalReceivedItems());
		txtTotal.setText("" + tcControl.getTotalReceivedItemsValue().setScale(2));
		txtPending.setText("" + (tcControl.getTotalExpectedItems() - tcControl.getTotalReceivedItems()));
		
		tableTransferLines.removeAll();
		TableItem item;
		
		saveFlag = true;
		
		System.out.println("TTT: " + tcControl.getLines());
		
		for (TransferControlLine v : tcControl.getLines()) {
			Color transferOK = new Color(getDisplay(), 200, 255, 190);
			
			item = new TableItem(tableTransferLines, SWT.NONE);
			int column = 0;
			item.setText(column++, " " + v.getQtyPrevExpected().setScale(0).toString());
			item.setText(column++, v.getQtyReceived().setScale(0).toString());
			item.setText(column++, v.getQtyNewExpected().setScale(0).toString());
			item.setText(column++, v.getItemNumber());
			item.setText(column++, v.getItemDescription());
			item.setText(column++, v.getComments().getComment1() == null ? "" : v.getComments().getComment1());
			logger.info("EXPECTED: " + v.getQtyPrevExpected() + ", RECEIVED: " + v.getQtyReceived() + ", PENDING: " + v.getQtyNewExpected());
			if (v.getQtyPrevExpected().intValue() == v.getQtyReceived().intValue()) {
				// marcar verde
				Color green = new Color(getDisplay(), 200, 255, 190);
				item.setBackground(green);
			} else if (v.getQtyReceived().intValue() == 0) {
				// sin color
				item.setBackground(null);
			} else if (v.getQtyReceived().intValue() > v.getQtyPrevExpected().intValue()) {
				// marcar rojo
				Color red = new Color(getDisplay(), 255, 60, 60);
				item.setBackground(red);
			} else if (v.getQtyReceived().intValue() > 0) {
				// marcar amarillo
				Color yellow = new Color(getDisplay(), 255, 255, 190);
				item.setBackground(yellow);
			}
//			item.setBackground(1, blue);
			if (!item.getBackground().equals(transferOK)) {
				saveFlag = false;
			}
		}
		
		btnGuardar.setEnabled(saveFlag);
	}
	
	/**
	 * Save and close a transfer.
	 * @return
	 */
	private TransferControl saveTransfer() {
		return this.save(true);
	}
	
	/**
	 * Saves a partial transfer.
	 * @return
	 */
	private TransferControl savePartialTransfer() {
		return this.save(false);
	}
	
	/**
	 * Saves a transfer
	 * @param closeTransfer indicates if the {@link TransferControl} is final and should be closed
	 * or is a partial transfer.
	 */
	private TransferControl save(boolean closeTransfer) {
		tcControl.setUserName(LoggedUserService.INSTANCE.getUser().getUserName());
		tcControl.setReference(txtReference.getText());
		tcControl.setComments(txtComment1.getText(), txtComment2.getText(), txtComment3.getText());		
		if (closeTransfer) {
			tcControl.close();
		}
		logger.info("Líneas de la transferencia: " + tcControl.getLines().size());
		tcController.doSave(tcControl);
		return tcControl;
	}
	
	/**
	 * Clears all the form data, including transfer number, transfer details, and
	 * any transfer information.
	 */
	private void resetFields() {
		txtTransferNo.setText("");
		txtBarcode.setText("");
		txtQty.setSelection(1);
		txtReference.setText("");
		txtComment1.setText("");
		txtComment2.setText("");
		txtComment3.setText("");
		tableTransferLines.clearAll();
		tcControl = null;
		btnGuardar.setEnabled(false);
	}
	
	/**
	 * Resets the transfer information, keeping the transfer number and the transfer
	 * details (but without any transfer information).
	 */
	private void resetTransferData() {
		cpController.finalizarSesion();
		tcController.finalizarSesion();
		cpController = new CounterpointController("TransferIn");
		tcController = new TransferControlController("TransferControl");
		tableTransferLines.clearAll();
		tcControl = null;
		btnGuardar.setEnabled(false);
		if (existeRegistro(txtTransferNo.getText())) {
			txtQty.setFocus();
			txtQty.selectAll();
		} else {
			tableTransferLines.removeAll();
			txtTransferNo.setFocus();
			txtTransferNo.selectAll();
		}
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


	@Override
	public void dispose() {
		getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF04);
		getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF09);
		getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF12);
		cpController.finalizarSesion();
		tcController.finalizarSesion();
		super.dispose();		
	}
	
	public String checkNull(String valorCampo) {
		return valorCampo == null ? "" : valorCampo;
	}
}

