package com.orendel.seam.editors;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Group;

import com.orendel.seam.controllers.DeliveriesController;
import com.orendel.seam.controllers.InvoicesController;
import com.orendel.seam.dao.DeliveryDAO;
import com.orendel.seam.domain.InvoiceDeliveryMapper;
import com.orendel.seam.domain.counterpoint.Invoice;
import com.orendel.seam.domain.counterpoint.InvoiceLine;
import com.orendel.seam.domain.counterpoint.Item;
import com.orendel.seam.domain.delivery.Delivery;
import com.orendel.seam.ui.login.LoggedUserService;
import com.orendel.seam.util.MessagesUtil;


public class CreateDeliveryEditorSpinner extends Composite {
	private Logger logger = Logger.getLogger(CreateDeliveryEditorSpinner.class);
	
	private InvoicesController controller;
	private DeliveriesController deliveriesController;
	private Invoice invoice;
	
	private boolean saveFlag = false;
	
	private Text txtInvoiceNo;
	private Table table;
	
	private Text txtBarcode;
	private Spinner txtQty;
	
	Button btnGuardar;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CreateDeliveryEditorSpinner(Composite parent, int style) {
		super(parent, style);
		
		controller = new InvoicesController("PUF");
		deliveriesController = new DeliveriesController("PUF2");
//		blue = parent.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group group = new Group(this, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_group = new GridLayout(3, false);
		gl_group.marginBottom = 5;
		gl_group.marginHeight = 0;
		group.setLayout(gl_group);
		
		Label lblNoFactura = new Label(group, SWT.NONE);
		lblNoFactura.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNoFactura.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNoFactura.setText("No. Factura:");
		
		txtInvoiceNo = new Text(group, SWT.BORDER);
		GridData gd_txtNoFactura = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtNoFactura.widthHint = 100;
		txtInvoiceNo.setLayoutData(gd_txtNoFactura);
		txtInvoiceNo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtInvoiceNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					if (buscarDetallesFactura()) {
						txtBarcode.setFocus();
					} else {
						txtInvoiceNo.selectAll();
					}
					
				}
			}
		});
		
//		txtNoFactura.setText("201166573491");
		
		Button btnFactura = new Button(group, SWT.NONE);
		btnFactura.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnFactura.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					buscarDetallesFactura();
					txtBarcode.setFocus();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnFactura.setText("Buscar");
		
		Label lblItem = new Label(group, SWT.NONE);
		lblItem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblItem.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblItem.setText("Cód. Barra:");
		
		txtBarcode = new Text(group, SWT.BORDER);
		txtBarcode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		txtBarcode.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtBarcode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				logger.info("Key: " + arg0.keyCode);
				if (arg0.keyCode == 13) {
					if (!txtBarcode.getText().isEmpty()) {
						accountForItemWithBarCodeOrItemCode(txtBarcode.getText());
						if (invoice != null) {
							refreshInvoiceDetails();
						}
						txtBarcode.setText("");
						txtQty.setSelection(1);
					} else {
						logger.info("Código de barra en blanco");
					}
				}
			}
		});
		
		txtQty = new Spinner(group, SWT.BORDER);
		GridData gd_txtQty = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtQty.widthHint = 25;
		txtQty.setLayoutData(gd_txtQty);
		txtQty.setMinimum(-100);
		txtQty.setSelection(1);
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
				txtBarcode.setFocus();
			}
		});
		
		
		table = new Table(this, SWT.BORDER | SWT.HIDE_SELECTION);
		table.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.verticalIndent = 10;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnCant = new TableColumn(table, SWT.CENTER);
		tblclmnCant.setWidth(50);
		tblclmnCant.setText("Cant.");
		
		TableColumn tblclmnOk = new TableColumn(table, SWT.CENTER);
		tblclmnOk.setWidth(50);
		tblclmnOk.setText("Ent.");
		
		TableColumn tblclmnItemNo = new TableColumn(table, SWT.LEFT);
		tblclmnItemNo.setWidth(100);
		tblclmnItemNo.setText("Item No.");
		
		TableColumn tblclmnDescripcin = new TableColumn(table, SWT.LEFT);
		tblclmnDescripcin.setWidth(300);
		tblclmnDescripcin.setText("Descripción");
		
		btnGuardar = new Button(this, SWT.NONE);
		btnGuardar.setEnabled(false);
		btnGuardar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnGuardar.setText("Entrega realizada");
		btnGuardar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Delivery delivery = saveDelivery();
				logger.info("Orden de entrega generada: " + delivery.getId());
				MessagesUtil.showInformation("Guardar orden de entrega", "<size=+2>Se ha guardado exitosamente la orden de entrega (número " + delivery.getId() + ").</size>");
				resetFields();
				txtInvoiceNo.setFocus();
			}
		});
	}

	
	private boolean buscarDetallesFactura() {
		boolean result = false;
		invoice = controller.findInvoiceByNumber(txtInvoiceNo.getText());
		if (invoice != null) {
			Delivery delivery = deliveriesController.findDeliveryByInvoiceNumber(txtInvoiceNo.getText());
			if (delivery == null) {
				logger.info("Factura encontrada!, documento: " + invoice.getTicket() + ", líneas: " + invoice.getLines());
				refreshInvoiceDetails();
				result = true;
			} else {
				MessagesUtil.showError("Buscar factura", "La factura número " + txtInvoiceNo.getText() + " ya tiene una entrega realizada.");
			}
		} else {
			table.removeAll();
			MessagesUtil.showWarning("Buscar factura", "No se encontró la factura número " + txtInvoiceNo.getText() + ".");
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
//			InvoiceLine line = locateInvoiceLineWithIem(item);
			InvoiceLine line = controller.locateInvoiceLineForItemDelivery(invoice, item);
			if (line != null) {
				int qty = txtQty.getSelection();
				line.adjustQuantity(qty);  //TODO retornar qty aplicada o restante por aplicar (considerar negativos)
			} else {
				logger.info("Artículo NO encontrado en factura: " + item.getDescription());
				MessagesUtil.showWarning("Búsqueda por código", "No se encontró ninguna línea con el código suministrado: " + barcode + ".");
			}
		} else {
			MessagesUtil.showError("Búsqueda por código", "No se encontró ningún artículo con el código de barra suministrado: " + barcode + ".");
		}
	}
	
	// movido a clase InvoicesController
	@Deprecated
	private InvoiceLine locateInvoiceLineWithIem(Item item) {
		InvoiceLine invoiceLine = null;
		List<InvoiceLine> invoiceLines = invoice.locateLinesWithItem(item);
		
		// buscamos si hay alguna línea con entregas pendientes
		for (InvoiceLine line : invoiceLines) {
			if (line.getQtySold().longValue() > line.getQtyDelivered().longValue()) {
				invoiceLine = line;
			}
		}
		
		// si no hay ninguna línea con entregas pendientes...
		if (invoiceLine == null) {
			// ...se selecciona la primera que aparezca en la factura
			if (!invoiceLines.isEmpty()) {
				invoiceLine = invoiceLines.get(0);
			}
		}
		
		if (invoiceLine != null) {
			logger.debug("Línea con artículo: " + invoiceLine.getDescripcion() + ", item desc: " + item.getDescription() + ", barcodes: " + item.getBarcodeList());
		}
		return invoiceLine;
	}
	
	
	private void refreshInvoiceDetails() {
		if (invoice == null) {
			logger.warn("Invoice object is null!");
			return;
		}
		
		table.removeAll();
		TableItem item;
		
		saveFlag = true;
		for (InvoiceLine v : invoice.getLines()) {
			Color deliveryOK = new Color(getDisplay(), 200, 255, 190);
			
			item = new TableItem(table, SWT.NONE);
			int column = 0;
			item.setText(column++, " " + v.getQtySold().toString());
			item.setText(column++, v.getQtyDelivered().toString());
			item.setText(column++, v.getItem().getItemNo());
			item.setText(column++, v.getDescripcion());
			if (v.getQtySold().equals(v.getQtyDelivered())) {
				// marcar verde
				Color color = new Color(getDisplay(), 200, 255, 190);
				item.setBackground(color);
			} else if (v.getQtyDelivered().equals(0)) {
				// sin color
				item.setBackground(null);
			} else if (v.getQtyDelivered().intValue() > v.getQtySold().intValue()) {
				// marcar rojo
				Color color = new Color(getDisplay(), 255, 60, 60);
				item.setBackground(color);
			} else if (v.getQtyDelivered().intValue() > 0) {
				// marcar amarillo
				Color color = new Color(getDisplay(), 255, 255, 190);
				item.setBackground(color);
			}
//			item.setBackground(1, blue);
			if (!item.getBackground().equals(deliveryOK)) {
				saveFlag = false;
			}
		}
		btnGuardar.setEnabled(saveFlag);
	}
	
	
	private Delivery saveDelivery() {
		Delivery delivery = InvoiceDeliveryMapper.from(invoice);
		delivery.setUserName(LoggedUserService.INSTANCE.getUser().getUserName());
		logger.info("Líneas de la entrega: " + delivery.getDeliveryLines().size());
		DeliveryDAO dao = new DeliveryDAO();
		dao.doSave(delivery);
		return delivery;
	}
	
	
	private void resetFields() {
		txtInvoiceNo.setText("");
		txtBarcode.setText("");
		txtQty.setSelection(1);
		table.clearAll();
		invoice = null;
		btnGuardar.setEnabled(false);
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


	@Override
	public void dispose() {
		super.dispose();
		controller.finalizarSesion();
		deliveriesController.finalizarSesion();
	}

}
