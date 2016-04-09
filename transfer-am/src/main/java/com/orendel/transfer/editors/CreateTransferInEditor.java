package com.orendel.transfer.editors;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.hibernate.HibernateException;

import com.orendel.counterpoint.domain.Item;
import com.orendel.counterpoint.domain.TransferIn;
import com.orendel.transfer.controllers.TransferControlController;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.dialogs.ItemLocationDialog;
import com.orendel.transfer.domain.TransferControl;
import com.orendel.transfer.domain.TransferControlLine;
import com.orendel.transfer.domain.TransferControlStatus;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.services.HibernateUtilDelivery;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;
import com.orendel.transfer.services.LoggedUserService;
import com.orendel.transfer.util.MessagesUtil;
import com.orendel.transfer.util.TransferMapper;
import com.orendel.transfer.util.TransferUpdater;


public class CreateTransferInEditor extends Composite {
	private static final Logger logger = Logger.getLogger(CreateTransferInEditor.class);
	
	private TransferControl tcControl;
	
	private CounterpointController cpController;
	private TransferControlController tcController;
	
	private boolean allItemsAccountedFor = false;
	
	private Text txtTransferNo;
	private Table tableTransferLines;
	private TableEditor editor;
	
	private Text txtQty;
	private Text txtBarcode;
	// comment fiels
	private Text txtReference;
	private Text txtComment1;
	private Text txtComment2;
	private Text txtComment3;
	// summary fields
	private Text txtLines;
	private Text txtReceived;
	private Text txtPending;
	
	private Button btnInitTransfer;
	private Button btnSaveDraft;
	private Button btnCounterPoint;
	
	private Listener listenerF04;
	private Listener listenerF09;
	private Listener listenerF12;
	
	private final Cursor handCursor = new Cursor(getDisplay(), SWT.CURSOR_HAND);
	
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CreateTransferInEditor(Composite parent, int style) {
		super(parent, style);
		
		cpController = new CounterpointController("TransferIn" + new Date().getTime());
		tcController = new TransferControlController("TransferControl" + new Date().getTime());
		
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
				if (e.keyCode == 13) {
					resetFields();
					toggleEditableFields(false);
					if (existsRegister(txtTransferNo.getText())) {
//						getParent().setData("txtTransferNo", txtTransferNo.getText());
						txtQty.setFocus();
						txtQty.selectAll();
					} else {
						txtTransferNo.setFocus();
						txtTransferNo.selectAll();
					}
					
				}
			}
		});
		
		Button btnBuscar = new Button(groupFind, SWT.NONE);
		btnBuscar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					resetFields();
					toggleEditableFields(false);
					if (existsRegister(txtTransferNo.getText())) {
//						getParent().setData("txtTransferNo", txtTransferNo.getText());
						txtQty.setFocus();
						txtQty.selectAll();
					} else {
						txtTransferNo.setFocus();
						txtTransferNo.selectAll();
					}
				} catch (HibernateException ex) {
					ex.printStackTrace();
					logger.info("Reloading sessions after HibernateException...");
					cpController.finalizarSesion();
					tcController.finalizarSesion();
					HibernateUtil.verSesiones();
					cpController = new CounterpointController("TransferIn" + new Date().getTime());
					tcController = new TransferControlController("TransferControl" + new Date().getTime());
				} catch (Exception ex) {
					ex.printStackTrace();
					MessagesUtil.showError("Error de aplicación", 
							(ex.getMessage() == null ? e.toString() + '\n' + ex.getStackTrace()[0] : ex.getMessage()));
				}
			}
		});
		btnBuscar.setText("Buscar");
		
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
				if (arg0.keyCode == 13 || arg0.keyCode == 16777296) {
					if (!txtBarcode.getText().isEmpty()) {
						Item itemAccounted = accountForItemWithBarCodeOrItemCode(txtBarcode.getText());
						refreshFormDetails(itemAccounted);
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
//		tableTransferLines = new Table(this, SWT.FULL_SELECTION | SWT.BORDER | SWT.HIDE_SELECTION);
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
		
		TableColumn tblclmnInventory = new TableColumn(tableTransferLines, SWT.NONE);
		tblclmnInventory.setWidth(25);
		tblclmnInventory.setText("");
		tblclmnInventory.setToolTipText("Permite ver el inventario de un artículo al hacer clic en el ícono");
		
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
		
		Label lblArtPendientes = new Label(grpTotales, SWT.NONE);
		GridData gd_lblArtPendientes = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblArtPendientes.horizontalIndent = 20;
		lblArtPendientes.setLayoutData(gd_lblArtPendientes);
		lblArtPendientes.setText("Art. pendientes");
		
		txtPending = new Text(grpTotales, SWT.BORDER);
		txtPending.setEnabled(false);
		new Label(grpTotales, SWT.NONE);
		new Label(grpTotales, SWT.NONE);
		
		Label label = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_label.verticalIndent = 5;
		label.setLayoutData(gd_label);
		
		Composite compositeActions = new Composite(this, SWT.NONE);
		GridLayout gl_compositeActions = new GridLayout(3, true);
		gl_compositeActions.marginWidth = 0;
		compositeActions.setLayout(gl_compositeActions);
		
		btnInitTransfer = new Button(compositeActions, SWT.NONE);
		btnInitTransfer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnInitTransfer.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnInitTransfer.setText("Iniciar Entrada (F4)");
		btnInitTransfer.setToolTipText("Inicia el proceso de entrada para una transferencia");
		btnInitTransfer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Init transfer button pressed!");
				initTransfer();
			}
		});
		
		btnSaveDraft = new Button(compositeActions, SWT.NONE);
		btnSaveDraft.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSaveDraft.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnSaveDraft.setText("Guardar Entrada (F9)");
		btnSaveDraft.setToolTipText("Guarda una entrada parcial en la computadora (sin actualizar CounterPoint)");
		btnSaveDraft.setEnabled(false);
		btnSaveDraft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save partial transfer button pressed!");
				createPartialTransfer();
			}
		});
		
		btnCounterPoint = new Button(compositeActions, SWT.NONE);
		btnCounterPoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnCounterPoint.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnCounterPoint.setText("Actualizar CP (F12)");
		btnCounterPoint.setToolTipText("Guarda y actualiza la información de la transferencia en el CounterPoint");
		btnCounterPoint.setEnabled(false);
		btnCounterPoint.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Update CounterPoint transfer button pressed!");
				createTransfer();
			}
		});
		
		addGlobalListeners();
		addDisposeListener();
		
		toggleEditableFields(false);
		
		txtTransferNo.setFocus();
	}
	
	
	private void toggleEditableFields(boolean newStatus) {
		txtQty.setEnabled(newStatus);
		txtBarcode.setEnabled(newStatus);
		txtReference.setEnabled(newStatus);
		txtComment1.setEnabled(newStatus);
		txtComment2.setEnabled(newStatus);
		txtComment3.setEnabled(newStatus);
		
		tableTransferLines.setEnabled(newStatus);
		
		btnInitTransfer.setEnabled(!newStatus);
		btnSaveDraft.setEnabled(newStatus);
		btnCounterPoint.setEnabled(newStatus);
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
					System.out.println("Update CounterPoint transfer button pressed!");
					if (btnCounterPoint.getEnabled()) {
						createTransfer();
					}
				}
			}
		};
		
		listenerF09 = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.F9) {
					System.out.println("Save partial transfer button pressed!");
					if (btnSaveDraft.getEnabled()) {
						createPartialTransfer();
					}
				}
			}
		};
		
		listenerF04 = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.F4) {
					System.out.println("F4 (init transfer) pressed!");
					if (btnInitTransfer.getEnabled()) {
						initTransfer();
					}
				}
			}
		};
		
		display.addFilter(SWT.KeyDown, listenerF04);
		display.addFilter(SWT.KeyDown, listenerF12);		
		display.addFilter(SWT.KeyDown, listenerF09);		
	}
	
	private void createTransfer() {
		if (!allItemsAccountedFor) {
			int action = MessagesUtil.showConfirmation("Actualizar CounterPoint", "<size=+2>La entrada para la transferencia número " + 
					txtTransferNo.getText() + " tiene " + txtPending.getText() + " artículos pendientes, está seguro\n" +
					"de querer actualizar la información en CounterPoint?</size>");
			logger.info("Botón presionado: " + action);
			if (action != 0) {
				MessagesUtil.showInformation("Actualizar CounterPoint", "<size=+2>La acción ha sido cancelada.</size>");
				return;
			}
		}
		txtTransferNo.setFocus();	// necesario para capturar el comentario de una línea (si no se ha perdido el foco)
		saveTransfer();
		String errorMsg = updateCounterpoint();
		if (errorMsg == null) {
			logger.info("Entrada de transferencia cerrada exitosamente: " + tcControl.getId());
			MessagesUtil.showInformation("Actualizar CounterPoint", "<size=+6>Se ha completado exitosamente la entrada número " + tcControl.getId() + " (transferencia " + tcControl.getTransferNo() + ").</size>");
		} else {
			logger.info("Control de transferencia cancelada (error actualizando CP): " + tcControl.getId());
//			tcControl.addLogEntry("Canceled by the system. TransferIn " + tcControl.getTransferNo() + " not found in CP.");
			logger.info("Error: " + errorMsg);
			tcControl.addLogEntry("Canceled by the system:  " + errorMsg);
			tcControl.close(TransferControlStatus.CANCELED);
			tcController.doSave(tcControl);
		}
		createNewEditor();
	}
	
	private void createPartialTransfer() {
		txtTransferNo.setFocus();	// necesario para capturar el comentario de una línea (si no se ha perdido el foco)
		savePartialTransfer();
		logger.info("Control de transferencia PARCIAL generada: " + tcControl.getId());
		MessagesUtil.showInformation("Guardar entrada parcial", "<size=+6>Se ha guardado exitosamente la entrada parcial (número " + tcControl.getId() + ").</size>");
		createNewEditor();
	}
	
	private void initTransfer() {
		if (tcControl == null) {
			logger.info("No se ha cargando ningún control de transferencia... cancelando acción.");
			return;
		}
		if (!validateUser()) {
			return;
		}
		toggleEditableFields(true);
		savePartialTransfer();
		txtQty.setFocus();
		txtQty.selectAll();
	}
	
	private boolean validateUser() {
		String currentUser = LoggedUserService.INSTANCE.getUser().getUserName();
		if (!tcControl.isEditableByUser(currentUser)) {
			MessagesUtil.showWarning("Iniciar entrada de transferencia", "<size=+2>La transferencia número " + 
					tcControl.getTransferNo() + " está asignada al usuario '" + tcControl.getUserName() + "', y debe ser finalizada o cancelada para\n" +
					"poder ser atendida por otro usuario.</size>");
			return false;
		}
		return true;
	}
	
	private boolean existsRegister(String transferNo) {
		boolean result = false;
		
		if (transferNo.isEmpty()) {
			return result;
		}
		TransferControl tc = findPartialTransferControl(transferNo);
		if (tc != null) {
			int action = MessagesUtil.showConfirmation("Buscar transferencia", "<size=+2>La transferencia número " + transferNo + " ya tiene una entrada "
					+ "parcial, desea completarla?</size>");
			logger.info("Botón presionado: " + action);
			if (action == 0) {
				logger.info("Editar transferencia parcial: " + transferNo + ", id: " + tc.getId());
			} else {
				return false;
			}
		} else {
			if (transferHasAnyClosedTransferControl(transferNo)) {
				MessagesUtil.showError("Buscar transferencia", "<size=+2>La transferencia número " + transferNo + " ya tiene una entrada "
						+ "completada y enviada al Counterpoint, no es\nposible realizar modificaciones adicionales.</size>");
				return false;
			}
			tc = tryToGetTransferControlFromCounterPoint(transferNo);
		}
		if (tc != null) {
			tcControl = tc;
			refreshFormDetails(null);
			result = true;
		} else {
			MessagesUtil.showWarning("Buscar transferencia", "No se encontró la transferencia número " + transferNo + ".");
		}
		if (result) {
			validateUser();
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
			logger.info("Entrada de transferencia PARCIAL encontrada, número: " + tc.getId() + ", líneas: " + tc.getLines().size());
		}
		return tc;
	}
	
	private boolean transferHasAnyClosedTransferControl(String transferNo) {
		boolean result = false;
		List<TransferControl> tcList  = tcController.findTransferControlByNumberAndStatus(transferNo, TransferControlStatus.CLOSED);
		if (tcList != null && !tcList.isEmpty()) {
			result = true;
		}
		return result;
	}
	
	
	private Item accountForItemWithBarCodeOrItemCode(String barcode) {
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
		return item;
	}

	
	private void refreshFormDetails(Item itemAccounted) {
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
		txtPending.setText("" + (tcControl.getTotalExpectedItems() - tcControl.getTotalReceivedItems()));
		
		tableTransferLines.removeAll();
		TableItem item;
		
		allItemsAccountedFor = true;
		
		System.out.println("TTT: " + tcControl.getLines());
		
		int updatedItemIndex = 0;
		int lineNumber = 0;
		for (TransferControlLine v : tcControl.getLines()) {
			Color transferOK = new Color(getDisplay(), 200, 255, 190);
			
			item = new TableItem(tableTransferLines, SWT.NONE);
			item.setData("lineNumber", lineNumber++);
			item.setData("itemNo", v.getItemNumber());
			if (itemAccounted != null && v.getItemNumber().equals(itemAccounted.getItemNo())) {
				updatedItemIndex = lineNumber - 1;
			}
			int column = 0;
			item.setText(column++, " " + v.getQtyPrevExpected().setScale(0).toString());
			item.setText(column++, v.getQtyReceived().setScale(0).toString());
			item.setText(column++, v.getQtyNewExpected().setScale(0).toString());
			item.setText(column++, v.getItemNumber());
			item.setText(column++, v.getItemDescription());
			item.setText(column++, v.getComments().getComment1() == null ? "" : v.getComments().getComment1());
			item.setImage(column, ImagesService.INSTANCE.getImage(getDisplay(), IImageKeys.ITEM_24));
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
				allItemsAccountedFor = false;
			}
		}
		
		if (tableTransferLines.getTopIndex() != updatedItemIndex) {
			System.out.println("Top: " + tableTransferLines.getTopIndex() + ", updated: " + updatedItemIndex);
			tableTransferLines.setTopIndex(updatedItemIndex);
			tableTransferLines.select(updatedItemIndex);
		}
		
		addEditorControl2();
		addHandCursorListener();
	}
	
	
	private void addHandCursorListener() {
		final int HAND_CURSOR_COLUMN = 6;
		tableTransferLines.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				boolean showHandCursor = false;
				Rectangle clientArea = tableTransferLines.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = tableTransferLines.getTopIndex();
				while (index < tableTransferLines.getItemCount()) {
					boolean visible = false;
					final TableItem item = tableTransferLines.getItem(index);
					Rectangle rect6 = item.getBounds(HAND_CURSOR_COLUMN);
					if (rect6.contains(pt)) {
						showHandCursor = true;
						logger.debug("MouseMove over item code: " + item.getData("itemNo"));
					}
					if (!visible && rect6.intersects(clientArea)) {
						visible = true;
					}
					if (!visible)
						return;
					index++;
				}
				if (showHandCursor) {
					if (getShell().getCursor() == null || !getShell().getCursor().equals(handCursor)) {
						getShell().setCursor(handCursor);
						logger.debug("Changing to hand cursor!");
					}
				} else {
					if (getShell().getCursor() != null) {
						getShell().setCursor(null);
						logger.debug("Removing hand cursor!");
					}
				}
			}
		});
	}
	
	private void addEditorControl2() {
		editor = new TableEditor(tableTransferLines);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		// editing the fifth column
		final int EDITABLECOLUMN = 5;
		tableTransferLines.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = tableTransferLines.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = tableTransferLines.getTopIndex();
				while (index < tableTransferLines.getItemCount()) {
					boolean visible = false;
					final TableItem item = tableTransferLines.getItem(index);
					Rectangle rect = item.getBounds(EDITABLECOLUMN);
					if (rect.contains(pt)) {
						final Text text = new Text(tableTransferLines, SWT.NONE);
						text.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
						Listener textListener = new Listener() {
							public void handleEvent(final Event e) {
								int lineNumber = (Integer) editor.getItem().getData("lineNumber");
								System.out.println("Setting line number: " + lineNumber + ", event: " + e.type);
								switch (e.type) {
								case SWT.FocusOut:
									System.out.println("FocusOut");
									item.setText(EDITABLECOLUMN, text.getText());
									tcControl.getLines().get(lineNumber).getComments().setComment1(text.getText());
									text.dispose();
									break;
								case SWT.Traverse:
									switch (e.detail) {
									case SWT.TRAVERSE_RETURN:
										System.out.println("TRAVERSE_RETURN");
										item.setText(EDITABLECOLUMN, text.getText());
										tcControl.getLines().get(lineNumber).getComments().setComment1(text.getText());
										// FALL THROUGH
									case SWT.TRAVERSE_ESCAPE:
										System.out.println("TRAVERSE_ESCAPE");
										txtQty.setFocus();
										txtQty.setText("1");
										txtQty.selectAll();
										text.dispose();
										e.doit = false;
									}
									break;
								}
							}
						};
						text.addListener(SWT.FocusOut, textListener);
						text.addListener(SWT.Traverse, textListener);
						editor.setEditor(text, item, EDITABLECOLUMN);
						text.setText(item.getText(EDITABLECOLUMN));
						text.selectAll();
						text.setFocus();
						return;
					}
					Rectangle rect6 = item.getBounds(EDITABLECOLUMN + 1);
					if (rect6.contains(pt)) {
						logger.info("Image click!... showing location details for item number: " + item.getData("itemNo"));
						getShell().setCursor(null);   // making sure to show the default cursor
						openLocationDetailsDialog((String) item.getData("itemNo"));
					}
					if (!visible && rect.intersects(clientArea)) {
						visible = true;
					}
					if (!visible)
						return;
					index++;
				}
			}
		});
	}
	
	private void openLocationDetailsDialog(String itemNo) {
		Item item = cpController.findItemByItemCode(itemNo);
		if (item != null) {
			ItemLocationDialog dialog = new ItemLocationDialog(this.getShell(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL, item);
			dialog.open();
		}
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
			tcControl.close(TransferControlStatus.CLOSED);
		}
		logger.info("Líneas de la transferencia: " + tcControl.getLines().size());
		tcController.doSave(tcControl);
		return tcControl;
	}
	
	private String updateCounterpoint() {
		String errorMsg = null;
		TransferIn transferIn = cpController.findTransferInByNumber(tcControl.getTransferNo());
		if (transferIn == null) {
			MessagesUtil.showError("Actualizar CounterPoint", "<size=+2>No se encontró la transferencia con el código: " + tcControl.getTransferNo() + 
					".  La entrada\nactual (número " + tcControl.getId() + ") será cancelada por el sistema.</size>");
			errorMsg = "TransferIn " + tcControl.getTransferNo() + " not found in CP.";
			return errorMsg;
		}
		TransferUpdater updater = new TransferUpdater();
		updater.updateTransferInFromTransferControl(transferIn, tcControl);
		errorMsg = cpController.doUpdateCounterPointTransferIn(transferIn); 
		return errorMsg;
	}
	
	/**
	 * Clears all the form data, including transfer number, transfer details, and
	 * any transfer information.
	 */
	private void resetFields() {
		txtBarcode.setText("");
		txtQty.setText("1");
		txtLines.setText("");
		txtReceived.setText("");
		txtPending.setText("");
		txtReference.setText("");
		txtComment1.setText("");
		txtComment2.setText("");
		txtComment3.setText("");
		tableTransferLines.removeAll();
		if (editor != null && editor.getEditor() != null) {
			editor.getEditor().dispose();
		}
		tcControl = null;
	}
	
	public void cancelTransferControl() {
		if (tcControl == null) {
			MessagesUtil.showInformation("Cancelar entrada", "<size=+2>Debe estar abierta una entrada de transferencia para ejecutar esta acción.</size>");
			return;
		}
		int action = MessagesUtil.showConfirmation("Cancelar entrada", "<size=+2>Está seguro de querer cancelar la entrada para la transferencia " + 
				tcControl.getTransferNo() + "?</size>");
		logger.info("Botón presionado: " + action);
		if (action != 0) {
			MessagesUtil.showInformation("Cancelar entrada", "<size=+2>La acción ha sido cancelada.</size>");
			return;
		}
		tcControl.addLogEntry("Canceled by the user");
		tcControl.close(TransferControlStatus.CANCELED);
		tcController.doSave(tcControl);
		MessagesUtil.showInformation("Cancelar entrada", "<size=+6>Se ha cancelado exitosamente la entrada número " + tcControl.getId() + 
				" (transferencia " + tcControl.getTransferNo() + ").</size>");
		createNewEditor();
	}
	
	/**
	 * This method:
	 * <li>Creates a new {@link CreateTransferInEditor}</li>
	 * <li>Disposes of the current editor (and associated widgets)</li>
	 * <li>Finally, opens the new {@link CreateTransferInEditor} inside the current composite</li>
	 */
	private void createNewEditor() {
		// get the memory reference to avoid 'Widget disposed' problems (the getParent() 
		//  method needs an existing widget).
		Composite parent = getParent();
		CreateTransferInEditor editor = new CreateTransferInEditor(getParent(), SWT.None);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		for (Control v : getParent().getChildren()) {
			if (v != editor) { 
				v.dispose();
			}
		}
		parent.layout();
	}
	
	private void addDisposeListener() {
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF04);
				getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF09);
				getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF12);
				handCursor.dispose();
				cpController.finalizarSesion();
				tcController.finalizarSesion();
				HibernateUtil.verSesiones();
				HibernateUtilDelivery.verSesiones();
			}
		});
	}
	
	public String checkNull(String valorCampo) {
		return valorCampo == null ? "" : valorCampo;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

