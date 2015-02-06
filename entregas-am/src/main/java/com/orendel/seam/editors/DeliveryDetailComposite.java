package com.orendel.seam.editors;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.orendel.seam.controllers.DeliveriesController;
import com.orendel.seam.domain.delivery.Delivery;
import com.orendel.seam.domain.delivery.DeliveryLine;
import com.orendel.seam.services.DateUtil;


public class DeliveryDetailComposite extends Composite {
	private Logger logger = Logger.getLogger(DeliveryDetailComposite.class);
	
	private DeliveriesController controller;
	private long deliveryNumber;

	private Delivery delivery;
	private Text txtInvoiceNo;
	private Text txtDeliveryDate;
	private Text txtUsername;
	
	private Table table;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DeliveryDetailComposite(Composite parent, int style, long deliveryNumber) {
		super(parent, style);
		
		this.deliveryNumber = deliveryNumber;
		controller = new DeliveriesController();
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 1;
		gridLayout.verticalSpacing = 10;
		setLayout(gridLayout);
		
		Group groupDelivery = new Group(this, SWT.NONE);
		groupDelivery.setEnabled(false);
		groupDelivery.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		groupDelivery.setText(" Detalles de la entrega ");
		GridData gd_groupDelivery = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_groupDelivery.verticalIndent = 10;
		groupDelivery.setLayoutData(gd_groupDelivery);
		GridLayout gl_groupDelivery = new GridLayout(8, false);
		gl_groupDelivery.marginHeight = 10;
		groupDelivery.setLayout(gl_groupDelivery);
		
		Label lblInvoiceNo = new Label(groupDelivery, SWT.NONE);
		lblInvoiceNo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInvoiceNo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblInvoiceNo.setText("Factura:");
		
		txtInvoiceNo = new Text(groupDelivery, SWT.READ_ONLY);
		GridData gd_txtInvoiceNo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtInvoiceNo.widthHint = 110;
		txtInvoiceNo.setLayoutData(gd_txtInvoiceNo);
		txtInvoiceNo.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		new Label(groupDelivery, SWT.NONE);
		
		Label lblEntregado = new Label(groupDelivery, SWT.NONE);
		lblEntregado.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEntregado.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblEntregado.setText("Creada:");
		
		txtDeliveryDate = new Text(groupDelivery, SWT.READ_ONLY);
		GridData gd_txtDeliveryDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtDeliveryDate.widthHint = 90;
		txtDeliveryDate.setLayoutData(gd_txtDeliveryDate);
		txtDeliveryDate.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		new Label(groupDelivery, SWT.NONE);
		
		Label lblUsername = new Label(groupDelivery, SWT.NONE);
		lblUsername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblUsername.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblUsername.setText("Usuario:");
		
		txtUsername = new Text(groupDelivery, SWT.READ_ONLY);
		GridData gd_txtUsername = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtUsername.widthHint = 100;
		txtUsername.setLayoutData(gd_txtUsername);
		txtUsername.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnCant = new TableColumn(table, SWT.NONE);
		tblclmnCant.setWidth(50);
		tblclmnCant.setText("Cant");
		
		TableColumn tblclmnEnt = new TableColumn(table, SWT.CENTER);
		tblclmnEnt.setWidth(50);
		tblclmnEnt.setText("Ent");
		
		TableColumn tblclmnDescripcion = new TableColumn(table, SWT.NONE);
		tblclmnDescripcion.setWidth(300);
		tblclmnDescripcion.setText("Descripción");
		
		TableColumn tblclmnItemNo = new TableColumn(table, SWT.NONE);
		tblclmnItemNo.setWidth(100);
		tblclmnItemNo.setText("Item No.");

		buscarDetallesDelivery();
	}
	
	
	private void buscarDetallesDelivery() {
//		Long noFactura = Long.parseLong(txtNoDelivery.getText());
		delivery = controller.getRegistroById(deliveryNumber);
		
		if (delivery != null) {
			txtInvoiceNo.setText(delivery.getTicketNumber());
			txtDeliveryDate.setText(DateUtil.toString(delivery.getCreated(), DateUtil.formatoFecha));
			txtUsername.setText(delivery.getUserName() == null ? "" : delivery.getUserName());

			logger.info("Entrega ID: " + delivery.getId() + ", factura ID: " + delivery.getInvoiceId());
			if (delivery.getDeliveryLines() != null) {
				System.out.println("JJ: " + delivery.getDeliveryLines().size());
			}
			refreshInvoiceDetails();
		}
	}
	
	
	private void refreshInvoiceDetails() {
		table.removeAll();
		TableItem item;
		
		for (DeliveryLine v : delivery.getDeliveryLines()) {
			item = new TableItem(table, SWT.NONE);
			int column = 0;
			item.setData(v);
			item.setText(column++, " " + v.getQtySold().setScale(0).toString());
			item.setText(column++, v.getQtyDelivered().setScale(0).toString());
			item.setText(column++, v.getItemDescription());
			item.setText(column++, v.getItemNumber());
		}
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
