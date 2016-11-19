package com.orendel.transfer.editors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.hibernate.HibernateException;

import com.orendel.common.config.AppConfig;
import com.orendel.counterpoint.domain.Item;
import com.orendel.delivery.domain.TransferControl;
import com.orendel.delivery.domain.TransferControlLine;
import com.orendel.delivery.domain.TransferControlStatus;
import com.orendel.transfer.controllers.TransferControlController;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.export.csv.ExcelCSVPrinter;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.services.HibernateUtilDelivery;
import com.orendel.transfer.services.LoggedUserService;
import com.orendel.transfer.services.TransferControlHelper;
import com.orendel.transfer.util.MessagesUtil;


public class CreateTransferInCsvEditor extends Composite {
	private static final Logger logger = Logger.getLogger(CreateTransferInCsvEditor.class);
	
	private static Color lightCyan = null;
	
	private TransferControl tcControl;
	private TransferControlHelper tcHelper;
	
	private CounterpointController cpController;
	private TransferControlController tcController;
	
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
	
	private Button btnSaveDraft;
	private Button btnCounterPoint;
	
	private Listener listenerF09;
	private Listener listenerF12;
	
	
	public CreateTransferInCsvEditor(Composite parent, int style) {
		this(parent, style, null);
	}
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CreateTransferInCsvEditor(Composite parent, int style, String transferNo) {
		super(parent, style);
		
		lightCyan = new Color(getDisplay(), 226, 244, 255);
		
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
		
		Label lblNoDoc = new Label(groupFind, SWT.NONE);
		lblNoDoc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNoDoc.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNoDoc.setText("No. Documento:");
		
		txtTransferNo = new Text(groupFind, SWT.BORDER);
		GridData gd_txtNoFactura = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_txtNoFactura.widthHint = 100;
		txtTransferNo.setLayoutData(gd_txtNoFactura);
		txtTransferNo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		
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
				if (e.keyCode == 13 || e.keyCode == 16777296) {
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
		GridData gd_txtBarcode = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_txtBarcode.widthHint = 100;
		txtBarcode.setLayoutData(gd_txtBarcode);
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
		tableTransferLines.setData("lastTopIndex", 0);
		
		TableColumn tblclmnHack = new TableColumn(tableTransferLines, SWT.CENTER);
		tblclmnHack.setToolTipText("Hack por right align bug in first column");
		tblclmnHack.setWidth(0);
		tblclmnHack.setText("Hack");
		
		TableColumn tblclmnCant = new TableColumn(tableTransferLines, SWT.RIGHT);
		tblclmnCant.setToolTipText("Cantidad total esperada");
		tblclmnCant.setWidth(60);
		tblclmnCant.setText("Total  ");
		
		TableColumn tblclmnItemNo = new TableColumn(tableTransferLines, SWT.LEFT);
		tblclmnItemNo.setWidth(120);
		tblclmnItemNo.setText("Item No.");
		
		TableColumn tblclmnDescripcin = new TableColumn(tableTransferLines, SWT.LEFT);
		tblclmnDescripcin.setWidth(350);
		tblclmnDescripcin.setText("Descripción");
		
		TableColumn tblclmnComentarios = new TableColumn(tableTransferLines, SWT.NONE);
		tblclmnComentarios.setWidth(350);
		tblclmnComentarios.setText("Comentario");
		
		Composite grpTotales = new Composite(this, SWT.NONE);
		grpTotales.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		GridLayout gl_grpTotales = new GridLayout(6, false);
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
		
		btnSaveDraft = new Button(compositeActions, SWT.NONE);
		btnSaveDraft.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnSaveDraft.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnSaveDraft.setText("Guardar Entrada Parcial (F9)");
		btnSaveDraft.setToolTipText("Guarda una entrada parcial (para continuarla posteriormente)");
		btnSaveDraft.setEnabled(false);
		btnSaveDraft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save partial transfer button pressed!");
				tryToCreatePartialTransfer();
			}
		});
		
		btnCounterPoint = new Button(compositeActions, SWT.NONE);
		btnCounterPoint.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnCounterPoint.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnCounterPoint.setText("Guardar Entrada y Finalizar (F12)");
		btnCounterPoint.setToolTipText("Guarda la entrada de artículos, la cierra y genera el archivo CSV que actualiza CounterPoint");
		btnCounterPoint.setEnabled(false);
		btnCounterPoint.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save-Close-GenerateCSV button pressed!");
				tryToCreateTransfer();
			}
		});
		
		addGlobalListeners();
		addDisposeListener();
		
		addEditorControl2();
		
		tcControl = new TransferControl();
		
		if (transferNo != null) {
			tcControl = findPartialTransferControl(transferNo);
			tcHelper = new TransferControlHelper(tcControl);
			refreshFormDetails(null);
			if (tcControl.getStatus().equalsIgnoreCase(TransferControlStatus.ACTIVE.getCode()) ||
					tcControl.getStatus().equalsIgnoreCase(TransferControlStatus.PARTIAL.getCode())) {
				initTransfer();
			} else {
				toggleEditableFields(false);
			}
		} else {
			tcHelper = new TransferControlHelper(tcControl);
			tcControl.setCreated(new Date());
			tcControl.setStatus(TransferControlStatus.ACTIVE.getCode());
			toggleEditableFields(true);
			txtQty.setFocus();
			txtQty.selectAll();
		}
		
	}
	
	
	private void createCsvFile(String fileName) throws IOException {
		String csvPath = AppConfig.INSTANCE.getValue("csv.export.path");
		System.out.println("CURRENT PATH last '/': " + csvPath.lastIndexOf(File.separator));
		
		OutputStream out = new FileOutputStream(csvPath + '/' + fileName + ".csv");
		ExcelCSVPrinter csv = new ExcelCSVPrinter(out);

		String[] linea = new String[2];
		for (TransferControlLine line : tcControl.getLines()) {
			linea[0] = line.getItemNumber();
			linea[1] = line.getQtyReceived().setScale(0).toString();
			csv.writeln(linea);
		}
		csv.close();
	}
	
	
	private void toggleEditableFields(boolean newStatus) {
		txtTransferNo.setEnabled(false);
		
		txtQty.setEnabled(newStatus);
		txtBarcode.setEnabled(newStatus);
		txtReference.setEnabled(newStatus);
		txtComment1.setEnabled(newStatus);
		txtComment2.setEnabled(newStatus);
		txtComment3.setEnabled(newStatus);
		
		tableTransferLines.setEnabled(newStatus);
		
		btnSaveDraft.setEnabled(newStatus);
		btnCounterPoint.setEnabled(newStatus);
	}
	
	
	private void tryToCreateTransfer() {
		try {
			int action = MessagesUtil.showConfirmation("Finalizar entrada de transferencia", "<size=+2>Está seguro " +
					"de querer finalizar la entrada de transferencia?</size>");
			logger.info("Botón presionado: " + action);
			if (action != 0) {
				MessagesUtil.showInformation("Guardar entrada de artículos", "<size=+2>La acción ha sido cancelada.</size>");
				return;
			}

			txtTransferNo.setFocus();	// necesario para capturar el comentario de una línea (si no se ha perdido el foco)
			saveTransfer();
			logger.info("Entrada de transferencia cerrada exitosamente: " + tcControl.getId());
			createCsvFile(txtTransferNo.getText());
			logger.info("Archivo CSV generado exitosamente: " + txtTransferNo.getText() + ".csv");
			MessagesUtil.showInformation("Guardar entrada de artículos", "<size=+6>Se ha finalizado exitosamente la entrada número " + 
					tcControl.getTransferNo() + "\ny se generó el archivo " + tcControl.getTransferNo() + ".csv.</size>");		
			createNewEditor();
		} catch (IOException ex) {
			tcControl.setClosed(null);
			tcControl.addLogEntry("Error generando archivo CSV.");
			savePartialTransfer();
			logger.error(ex);
			MessagesUtil.showError("Error guardando la entrada de transferencia", "<size=+2>Se generó un error al intentar guardar el archivo " + tcControl.getTransferNo() + ".csv.\n" +
							"Debe guardar como entrada parcial (tecla F09) y corregir la ruta de generación del archivo.\n" +
							"Error: " + ex.getMessage() + "</size>");
		} catch (HibernateException ex) {
			resetHibernateConnection(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			MessagesUtil.showError("Error guardando la entrada de transferencia", 
					(ex.getMessage() == null ? ex.toString() + '\n' + ex.getStackTrace()[0] : ex.getMessage()));
		}
	}
	
	private void tryToCreatePartialTransfer() {
		try {
			txtTransferNo.setFocus();	// necesario para capturar el comentario de una línea (si no se ha perdido el foco)
			savePartialTransfer();
			logger.info("Control de transferencia PARCIAL generada: " + tcControl.getId());
			MessagesUtil.showInformation("Guardar entrada parcial", "<size=+6>Se ha guardado exitosamente la entrada parcial (número " + tcControl.getTransferNo() + ").</size>");
			createNewEditor();
		} catch (HibernateException ex) {
			resetHibernateConnection(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			MessagesUtil.showError("Error guardando la entrada parcial", 
					(ex.getMessage() == null ? ex.toString() + '\n' + ex.getStackTrace()[0] : ex.getMessage()));
		}
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
	
	private TransferControl findPartialTransferControl(String transferNo) {
		TransferControl tc = tcController.findTransferControlByNumber(transferNo);
		if (tc != null) {
			logger.info("Entrada de transferencia encontrada, número: " + tc.getId() + ", líneas: " + tc.getLines().size());
		}
		return tc;
	}
	
	private Item accountForItemWithBarCodeOrItemCode(String barcode) {
		Item item = cpController.findItemByBarCode(barcode);
		if (item == null) {
			item = cpController.findItemByItemCode(barcode);
		}
		if (item != null) {
			logger.info("Artículo encontrado en DB: " + item.getDescription());

			int qty = Integer.parseInt(txtQty.getText());
			TransferControlLine line = tcHelper.adjustReceivedQuantityForItem(item.getItemNo(), qty);
			if (line == null) {
				tcHelper.addTransferControlLine(item.getItemNo(), item.getDescription(), qty);
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
		
		txtTransferNo.setText(checkNull(tcControl.getTransferNo()));
		txtReference.setText(checkNull(tcControl.getReference()));
		if (tcControl.getComments() != null) {
			txtComment1.setText(checkNull(tcControl.getComments().getComment1()));
			txtComment2.setText(checkNull(tcControl.getComments().getComment2()));
			txtComment3.setText(checkNull(tcControl.getComments().getComment3()));
		}
		
		txtLines.setText("" + tcControl.getLines().size());
		txtReceived.setText("" + tcControl.getTotalReceivedItems());
		
		tableTransferLines.removeAll();
		TableItem item = null;
		
		System.out.println("TTT: " + tcControl.getLines());
		
		int updatedItemIndex = 0;
		int lineNumber = 0;
		for (TransferControlLine v : tcControl.getLines()) {
			item = new TableItem(tableTransferLines, SWT.NONE);
			item.setData("lineNumber", lineNumber++);
			item.setData("itemNo", v.getItemNumber());
			if (itemAccounted != null && v.getItemNumber().equals(itemAccounted.getItemNo())) {
				updatedItemIndex = lineNumber - 1;
			}
			int column = 0;
			item.setText(column++, "right");
			item.setText(column++, v.getQtyReceived().setScale(0).toString() + " ");
			item.setText(column++, v.getItemNumber());
			item.setText(column++, v.getItemDescription());
			item.setText(column++, v.getComments().getComment1() == null ? "" : v.getComments().getComment1());
			if (tableTransferLines.getItemCount() % 2 == 0) {
				item.setBackground(lightCyan);
			}
		}
		
		scrollToUpdatedItem(updatedItemIndex);
	}
	
	
	private void scrollToUpdatedItem(int updatedItemIndex) {
		boolean itemIsVisible = isVisibleTheUpdatedItem(updatedItemIndex);
		
		if (!itemIsVisible) {
			tableTransferLines.setTopIndex(updatedItemIndex);
			tableTransferLines.setData("lastTopIndex", updatedItemIndex);
		} else {
			// we need to re-set the top index every time, since the table is always repopulated
			// with data from the controller (every time an item is updated).
			int lastTopIndex = (Integer) tableTransferLines.getData("lastTopIndex");
			tableTransferLines.setTopIndex(lastTopIndex);
		}
	}
	
	
	private boolean isVisibleTheUpdatedItem(int updatedItemIndex) {
		int tableHeight = tableTransferLines.getBounds().height;
		int rowHeight = tableTransferLines.getItemHeight();
		
		int lastTopIndex = (Integer) tableTransferLines.getData("lastTopIndex");
		
		int visibleItems = tableHeight / rowHeight - 1;
		
		int nn = updatedItemIndex - lastTopIndex;
		
		boolean isVisibleSelectedItem = false;
		if (nn > 0 && nn < visibleItems) {
			isVisibleSelectedItem = true;
		}
		
		System.out.println("TH: " + tableHeight + ", RH: " + rowHeight + ", visible items: " + visibleItems + ", last index: "
				+ lastTopIndex + ", current index: " + updatedItemIndex + ", visible: " + isVisibleSelectedItem);
		return isVisibleSelectedItem;
	}
	
	
	private void addEditorControl2() {
		editor = new TableEditor(tableTransferLines);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		// editing the fifth column
		final int EDITABLECOLUMN = 4;
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
		if (tcControl.getTransferNo() == null) {
			assignTransferNumber("CSV");
		}
		tcControl.setUserName(LoggedUserService.INSTANCE.getUser().getUserName());
		tcControl.setReference(txtReference.getText());
		tcControl.setComments(txtComment1.getText(), txtComment2.getText(), txtComment3.getText());		
		if (closeTransfer) {
			tcControl.close(TransferControlStatus.CLOSED);
		} else {
			tcControl.setStatus(TransferControlStatus.PARTIAL.getCode());
		}
		logger.info("Líneas de la transferencia: " + tcControl.getLines().size());
		tcController.doSave(tcControl);
		return tcControl;
	}
	
	/**
	 * Gets the next transfer number to be assigned to the {@link TransferControl} register
	 * and his {@link TransferControlLine} item lines.
	 */
	private void assignTransferNumber(String prefix) {
		String nextCode = "0000" + tcController.getNextTransferControlNumber(prefix);
		nextCode = prefix + nextCode.substring(nextCode.length() - 4);
		txtTransferNo.setText(nextCode);
		tcControl.setTransferNo(nextCode);
		for (TransferControlLine line : tcControl.getLines()) {
			line.setTransferNo(nextCode);
		}
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
	 * <li>Creates a new {@link CreateTransferInCsvEditor}</li>
	 * <li>Disposes of the current editor (and associated widgets)</li>
	 * <li>Finally, opens the new {@link CreateTransferInCsvEditor} inside the current composite</li>
	 */
	private void createNewEditor() {
		// get the memory reference to avoid 'Widget disposed' problems (the getParent() 
		//  method needs an existing widget).
		Composite parent = getParent();
		CreateTransferInCsvEditor editor = new CreateTransferInCsvEditor(getParent(), SWT.None);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		for (Control v : getParent().getChildren()) {
			if (v != editor) { 
				v.dispose();
			}
		}
		parent.layout();
	}
	
	private void resetHibernateConnection(HibernateException ex) {
		logger.error(ex.getMessage(), ex);
		logger.info("Reloading sessions after HibernateException...");
		cpController.finalizarSesion();
		tcController.finalizarSesion();
		HibernateUtil.verSesiones();
		cpController = new CounterpointController("TransferIn" + new Date().getTime());
		tcController = new TransferControlController("TransferControl" + new Date().getTime());
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
						tryToCreateTransfer();
						event.keyCode = 0;
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
						tryToCreatePartialTransfer();
						event.keyCode = 0;
					}
				}
			}
		};
		
		display.addFilter(SWT.KeyDown, listenerF09);
		display.addFilter(SWT.KeyDown, listenerF12);
	}


	private void addDisposeListener() {
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF09);
				getShell().getDisplay().removeFilter(SWT.KeyDown, listenerF12);
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

