package com.orendel.seam.editors;

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

import com.orendel.seam.controllers.DeliveriesController;
import com.orendel.seam.controllers.InvoicesController;
import com.orendel.seam.dao.DeliveryDAO;
import com.orendel.seam.domain.InvoiceDeliveryMapper;
import com.orendel.seam.domain.Status;
import com.orendel.seam.domain.counterpoint.Invoice;
import com.orendel.seam.domain.counterpoint.Item;
import com.orendel.seam.domain.delivery.Delivery;
import com.orendel.seam.domain.delivery.DeliveryLine;
import com.orendel.seam.ui.login.LoggedUserService;
import com.orendel.seam.util.MessagesUtil;


public class CreateDeliveryEditor extends Composite {
	private static final Logger logger = Logger.getLogger(CreateDeliveryEditor.class);
	
	private InvoicesController controller;
	private DeliveriesController deliveriesController;
//	private Invoice invoice;
	private Delivery delivery;
	
	private boolean saveFlag = false;
	
	private Text txtInvoiceNo;
	private Table tableInvoiceLines;
	
	private Text txtBarcode;
	private Text txtQty;
	
	private Button btnParcial;
	private Button btnGuardar;
	private Button btnLimpiar;
	
	private Listener listenerF04;
	private Listener listenerF09;
	private Listener listenerF12;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CreateDeliveryEditor(Composite parent, int style) {
		super(parent, style);
		
		controller = new InvoicesController("InvoicesCtrl");
		deliveriesController = new DeliveriesController("DeliveryCtrl");
//		blue = parent.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group groupFind = new Group(this, SWT.NONE);
		groupFind.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_groupFind = new GridLayout(4, false);
		gl_groupFind.marginBottom = 5;
		gl_groupFind.marginHeight = 0;
		groupFind.setLayout(gl_groupFind);
		
		Label lblNoFactura = new Label(groupFind, SWT.NONE);
		lblNoFactura.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNoFactura.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNoFactura.setText("No. Factura:");
		
		txtInvoiceNo = new Text(groupFind, SWT.BORDER);
		GridData gd_txtNoFactura = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_txtNoFactura.widthHint = 100;
		txtInvoiceNo.setLayoutData(gd_txtNoFactura);
		txtInvoiceNo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtInvoiceNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				if (!txtInvoiceNo.getText().isEmpty() && e.keyCode == 13) {
				if (e.keyCode == 13) {
					if (buscarDetallesFactura()) {
						txtQty.setFocus();
						txtQty.selectAll();
					} else {
						txtInvoiceNo.setFocus();
						txtInvoiceNo.selectAll();
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
					if (buscarDetallesFactura()) {
						txtQty.setFocus();
						txtQty.selectAll();
					} else {
						txtInvoiceNo.setFocus();
						txtInvoiceNo.selectAll();
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
		
		tableInvoiceLines = new Table(this, SWT.BORDER | SWT.HIDE_SELECTION);
		tableInvoiceLines.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_tableInvoiceLines = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_tableInvoiceLines.verticalIndent = 10;
		tableInvoiceLines.setLayoutData(gd_tableInvoiceLines);
		tableInvoiceLines.setHeaderVisible(true);
		tableInvoiceLines.setLinesVisible(true);
		
		TableColumn tblclmnCant = new TableColumn(tableInvoiceLines, SWT.CENTER);
		tblclmnCant.setWidth(50);
		tblclmnCant.setText("Cant.");
		
		TableColumn tblclmnOk = new TableColumn(tableInvoiceLines, SWT.CENTER);
		tblclmnOk.setWidth(50);
		tblclmnOk.setText("Ent.");
		
		TableColumn tblclmnItemNo = new TableColumn(tableInvoiceLines, SWT.LEFT);
		tblclmnItemNo.setWidth(100);
		tblclmnItemNo.setText("Item No.");
		
		TableColumn tblclmnDescripcin = new TableColumn(tableInvoiceLines, SWT.LEFT);
		tblclmnDescripcin.setWidth(300);
		tblclmnDescripcin.setText("Descripción");
		
		Composite compositeActions = new Composite(this, SWT.NONE);
		GridLayout gl_compositeActions = new GridLayout(3, false);
		gl_compositeActions.marginWidth = 0;
		compositeActions.setLayout(gl_compositeActions);
		
		btnParcial = new Button(compositeActions, SWT.NONE);
		btnParcial.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnParcial.setText("Entrega parcial (F4)");
		btnParcial.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Partial delivery button pressed!");
				createPartialDelivery();
			}
		});
		
		btnLimpiar = new Button(compositeActions, SWT.NONE);
		btnLimpiar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnLimpiar.setText("Limpiar (F9)");
		btnLimpiar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Reset button pressed!");
				resetDeliveryData();
			}
		});
		
		btnGuardar = new Button(compositeActions, SWT.NONE);
		btnGuardar.setEnabled(false);
		btnGuardar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnGuardar.setText("Entrega realizada (F12)");
		btnGuardar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save delivery button pressed!");
				createDelivery();
			}
		});
		
		addGlobalListeners();
		
		txtInvoiceNo.setFocus();
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
					System.out.println("F12 (save delivery) pressed!");
					if (btnGuardar.getEnabled()) {
						createDelivery();
					}
				}
			}
		};
		
		listenerF09 = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.F9) {
					System.out.println("F9 (reset delivery form) pressed!");
					resetDeliveryData();
				}
			}
		};
		
		listenerF04 = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.F4) {
					System.out.println("F4 (partial delivery form) pressed!");
					createPartialDelivery();
				}
			}
		};
		
		display.addFilter(SWT.KeyDown, listenerF04);
		display.addFilter(SWT.KeyDown, listenerF12);		
		display.addFilter(SWT.KeyDown, listenerF09);		
	}
	
	private void createDelivery() {
		Delivery delivery = saveDelivery();
		logger.info("Orden de entrega generada: " + delivery.getId());
		MessagesUtil.showInformation("Guardar orden de entrega", "<size=+6>Se ha guardado exitosamente la orden de entrega (número " + delivery.getId() + ").</size>");
		resetFields();
		txtInvoiceNo.setFocus();
	}
	
	private void createPartialDelivery() {
		Delivery delivery = savePartialDelivery();
		logger.info("Orden de entrega PARCIAL generada: " + delivery.getId());
		MessagesUtil.showInformation("Guardar orden de entrega parcial", "<size=+6>Se ha guardado exitosamente la entrega parcial (número " + delivery.getId() + ").</size>");
		resetFields();
		txtInvoiceNo.setFocus();
	}

	
	private boolean buscarDetallesFactura() {
		boolean result = false;
		if (!txtInvoiceNo.getText().isEmpty()) {
			Invoice invoice = controller.findInvoiceByNumber(txtInvoiceNo.getText());
			if (invoice != null) {
				delivery = deliveriesController.findDeliveryByInvoiceNumber(txtInvoiceNo.getText());
				if (delivery == null) {
					logger.info("Factura encontrada!, documento: " + invoice.getTicket() + ", líneas: " + invoice.getLines());
					delivery = InvoiceDeliveryMapper.from(invoice);
					refreshFormDetails();
					result = true;
				} else {
					if (delivery.getStatus().equals(Status.CLOSED.getCode())) {
						MessagesUtil.showError("Buscar factura", "La factura número " + txtInvoiceNo.getText() + " ya tiene una entrega realizada.");
					} else {
						int action = MessagesUtil.showConfirmation("Buscar factura", "<size=+2>La factura número " + txtInvoiceNo.getText() + " ya tiene una entrega"
								+ "parcial, desea completarla?</size>");
						logger.info("Botón presionado: " + action);
						if (action == 0) {
							logger.info("Abrir entrega parcial");
							// editar delivery en current editor
							refreshFormDetails();
							result = true;
						}
					}					
				}
			} else {
				tableInvoiceLines.removeAll();
				MessagesUtil.showWarning("Buscar factura", "No se encontró la factura número " + txtInvoiceNo.getText() + ".");
			}
		}
		return result;
	}
	
	
	private void accountForItemWithBarCodeOrItemCode(String barcode) {
		Item item = controller.findItemByBarCode(barcode);
		if (item == null) {
			item = controller.findItemByItemCode(barcode);
		}
		if (item != null) {
			logger.info("Artículo encontrado en DB: " + item.getDescription());
			
//			InvoiceLine line = controller.locateInvoiceLineForItemDelivery(null, item);
//			if (line != null) {
//				int qty = Integer.parseInt(txtQty.getText());
//				line.adjustQuantity(qty);  //TODO retornar qty aplicada o restante por aplicar (considerar negativos)
//			} else {
//				logger.info("Artículo NO encontrado en factura: " + item.getDescription());
//				MessagesUtil.showWarning("Búsqueda por código", "No se encontró ninguna línea con el código suministrado: " + barcode + ".");
//			}
			
			int qty = Integer.parseInt(txtQty.getText());
			DeliveryLine line = delivery.adjustDeliveredQuantityForItem(item.getItemNo(), qty);
			if (line == null) {
				logger.info("Artículo NO encontrado en factura: " + item.getDescription());
				MessagesUtil.showWarning("Búsqueda por código", "No se encontró ninguna línea con el código suministrado: " + barcode + ".");
			}			
			
		} else {
			MessagesUtil.showError("Búsqueda por código", "No se encontró ningún artículo con el código de barra suministrado: " + barcode + ".");
		}
	}
	
	
	private void refreshFormDetails() {
		if (delivery == null) {
			logger.warn("Invoice object is null!");
			return;
		}
		
		tableInvoiceLines.removeAll();
		TableItem item;
		
		saveFlag = true;
		for (DeliveryLine v : delivery.getDeliveryLines()) {
			Color deliveryOK = new Color(getDisplay(), 200, 255, 190);
			
			item = new TableItem(tableInvoiceLines, SWT.NONE);
			int column = 0;
			item.setText(column++, " " + v.getQtySold().setScale(0).toString());
			item.setText(column++, v.getQtyDelivered().setScale(0).toString());
			item.setText(column++, v.getItemNumber());
			item.setText(column++, v.getItemDescription());
			logger.info("SOLD: " + v.getQtySold() + ", DELIVERED: " + v.getQtyDelivered());
			if (v.getQtySold().intValue() == v.getQtyDelivered().intValue()) {
				// marcar verde
				Color green = new Color(getDisplay(), 200, 255, 190);
				item.setBackground(green);
			} else if (v.getQtyDelivered().intValue() == 0) {
				// sin color
				item.setBackground(null);
			} else if (v.getQtyDelivered().intValue() > v.getQtySold().intValue()) {
				// marcar rojo
				Color red = new Color(getDisplay(), 255, 60, 60);
				item.setBackground(red);
			} else if (v.getQtyDelivered().intValue() > 0) {
				// marcar amarillo
				Color yellow = new Color(getDisplay(), 255, 255, 190);
				item.setBackground(yellow);
			}
//			item.setBackground(1, blue);
			if (!item.getBackground().equals(deliveryOK)) {
				saveFlag = false;
			}
		}
		btnGuardar.setEnabled(saveFlag);
	}
	
	/**
	 * Save and close a delivery.
	 * @return
	 */
	private Delivery saveDelivery() {
		delivery.setUserName(LoggedUserService.INSTANCE.getUser().getUserName());
		delivery.close();
		logger.info("Líneas de la entrega: " + delivery.getDeliveryLines().size());
		DeliveryDAO dao = new DeliveryDAO();
		dao.doSave(delivery);
		return delivery;
	}
	
	/**
	 * Saves a partial delivery.
	 * @return
	 */
	private Delivery savePartialDelivery() {
		delivery.setUserName(LoggedUserService.INSTANCE.getUser().getUserName());
		logger.info("Líneas de la entrega: " + delivery.getDeliveryLines().size());
		DeliveryDAO dao = new DeliveryDAO();
		dao.doSave(delivery);
		return delivery;
	}
	
	/**
	 * Clears all the form data, including invoice number, invoice details, and
	 * any delivery information.
	 */
	private void resetFields() {
		txtInvoiceNo.setText("");
		txtBarcode.setText("");
		txtQty.setSelection(1);
		tableInvoiceLines.clearAll();
		delivery = null;
		btnGuardar.setEnabled(false);
	}
	
	/**
	 * Resets the delivery information, keeping the invoice number and the invoice
	 * details (but without any delivery information).
	 */
	private void resetDeliveryData() {
		controller.finalizarSesion();
		deliveriesController.finalizarSesion();
		controller = new InvoicesController("InvoicesCtrl");
		deliveriesController = new DeliveriesController("DeliveryCtrl");
		tableInvoiceLines.clearAll();
		delivery = null;
		btnGuardar.setEnabled(false);
		if (buscarDetallesFactura()) {
			txtQty.setFocus();
			txtQty.selectAll();
		} else {
			txtInvoiceNo.setFocus();
			txtInvoiceNo.selectAll();
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
		controller.finalizarSesion();
		deliveriesController.finalizarSesion();
		super.dispose();		
	}
}

