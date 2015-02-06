package com.orendel.seam.editors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.orendel.seam.controllers.DeliveriesController;
import com.orendel.seam.dialogs.DeliveryDetailDialog;
import com.orendel.seam.domain.Status;
import com.orendel.seam.domain.delivery.Delivery;
import com.orendel.seam.services.DateUtil;
import com.orendel.seam.util.MessagesUtil;


public class ViewDeliveriesEditorMod extends Composite {
	private static final Logger logger = Logger.getLogger(ViewDeliveriesEditorMod.class);
	
	private DeliveriesController controller;
	private final String[] searchModeItems = new String[] {"Número de factura", "Número de entrega", "Rango de fecha"}; 
	
	private Table table;
	private Text txtFactura;
	private Text txtEntrega;
	
	private Combo comboBuscar;
	
	private Shell shell;
	
	private Composite compositeMode;
	private Composite compositeParams;

	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewDeliveriesEditorMod(Composite parent, int style) {
		super(parent, style);

		shell = parent.getShell();
		controller = new DeliveriesController();
//		logger.info("BBBB: " + controller.getListado().size());
//		logger.info("BBBB: " + controller.findAllDeliveries().size());
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group groupBuscar = new Group(this, SWT.NONE);
		GridLayout gl_groupBuscar = new GridLayout(1, false);
		gl_groupBuscar.marginBottom = 5;
		gl_groupBuscar.marginHeight = 0;
		groupBuscar.setLayout(gl_groupBuscar);
		groupBuscar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		compositeMode = new Composite(groupBuscar, SWT.NONE);
		compositeMode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeMode = new GridLayout(3, false);
		gl_compositeMode.marginHeight = 0;
		compositeMode.setLayout(gl_compositeMode);
		
		compositeParams = new Composite(groupBuscar, SWT.NONE);
		compositeParams.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeParams = new GridLayout(6, false);
		gl_compositeParams.marginHeight = 0;
		compositeParams.setLayout(gl_compositeParams);
		
		createCompositeModeContent(compositeMode);
		createCompositeParams_ByInvoice(compositeParams);
		new Label(compositeParams, SWT.NONE);
		new Label(compositeParams, SWT.NONE);
		new Label(compositeParams, SWT.NONE);

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.verticalIndent = 10;
		gd_table.widthHint = 440;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnFactura = new TableColumn(table, SWT.NONE);
		tblclmnFactura.setWidth(100);
		tblclmnFactura.setText("Factura");
		
		TableColumn tblclmnEntrega = new TableColumn(table, SWT.CENTER);
		tblclmnEntrega.setWidth(100);
		tblclmnEntrega.setText("Entrega");
		
		TableColumn tblclmnLneas = new TableColumn(table, SWT.CENTER);
		tblclmnLneas.setWidth(60);
		tblclmnLneas.setText("Líneas");
		
		TableColumn tblclmnItems = new TableColumn(table, SWT.CENTER);
		tblclmnItems.setWidth(60);
		tblclmnItems.setText("Items");
		
		TableColumn tblclmnUsuario = new TableColumn(table, SWT.NONE);
		tblclmnUsuario.setWidth(200);
		tblclmnUsuario.setText("Usuario");
		
		TableColumn tblclmnStatus = new TableColumn(table, SWT.CENTER);
		tblclmnStatus.setWidth(80);
		tblclmnStatus.setText("Status");
		
		TableColumn tblclmnCreated = new TableColumn(table, SWT.NONE);
		tblclmnCreated.setWidth(175);
		tblclmnCreated.setText("Fecha creación");
		
		TableColumn tblclmnClosed = new TableColumn(table, SWT.NONE);
		tblclmnClosed.setWidth(175);
		tblclmnClosed.setText("Fecha de cierre");
		
		addDoubleClickListener(table);
	}
	
	
	private void executeSearchByInvoiceNumber(String invoiceNumber) {
		if (invoiceNumber.isEmpty()) {
			return;
		}
		List<Delivery> deliveryList = controller.findDeliveriesByInvoiceNumber(invoiceNumber);
		if (deliveryList != null && !deliveryList.isEmpty()) {
			refreshTableDetails(deliveryList);
		} else {
			table.removeAll();
			MessagesUtil.showWarning("Buscar entrega", 
					"No se encontró ninguna entrega asociada al número de factura '" + invoiceNumber + "'.");
		}
	}
	
	
	private void executeSearchByDeliveryNumber(String deliveryNumber) {
		if (deliveryNumber.isEmpty()) {
			return;
		}
		List<Delivery> deliveryList = new ArrayList<Delivery>();
		Delivery delivery = controller.findDeliveryByDeliveryNumber(deliveryNumber);
		if (delivery != null) {
			deliveryList.add(delivery);
			refreshTableDetails(deliveryList);
		} else {
			table.removeAll();
			MessagesUtil.showWarning("Buscar entrega", 
					"No se encontró ninguna entrega con el número '" + deliveryNumber + "'.");
		}
	}
	
	
	private void executeSearchByDateRange(Date initialDate, Date endDate) {
		List<Delivery> deliveryList = controller.findDeliveryByDateRange(initialDate, endDate);
		if (deliveryList != null && !deliveryList.isEmpty()) {
			refreshTableDetails(deliveryList);
		} else {
			table.removeAll();
			MessagesUtil.showWarning("Buscar entrega", 
					"No se encontró ninguna entrega en el rango de fechas indicado.");
		}
	}
	
	
	private void refreshTableDetails(List<Delivery> deliveryList) {
		table.removeAll();
		TableItem item;
		
		for (Delivery v : deliveryList) {
			item = new TableItem(table, SWT.NONE);
			int column = 0;
			item.setData(v);
			item.setText(column++, " " + v.getTicketNumber());
			item.setText(column++, v.getId().toString());
			item.setText(column++, Integer.toString(v.getDeliveryLines().size()));
			item.setText(column++, v.getDeliveredItemsIndicator());
			item.setText(column++, v.getUserName() == null ? "" : v.getUserName());
			item.setText(column++, v.getStatus() == null ? "?" : Status.fromCode(v.getStatus()).getDescription());
			item.setText(column++, DateUtil.toString(v.getCreated(), DateUtil.formatoFechaHora));
			item.setText(column++, DateUtil.toString(v.getClosed(), DateUtil.formatoFechaHora));			
		}
	}
	
	
	private void toggleCompositeSearchParametersGroup() {
		for (Control c : compositeParams.getChildren()) {
			c.dispose();
		}
		int searchMode = comboBuscar.getSelectionIndex();
		if (searchMode == 0) {
			createCompositeParams_ByInvoice(compositeParams);
		} else if (searchMode == 1) {
			createCompositeParams_ByDelivery(compositeParams);
		} else if (searchMode == 2) {
			createCompositeParams_ByDate(compositeParams);
		}
		compositeParams.layout();
		compositeParams.pack();
	}
	
	
	private void createCompositeModeContent(Composite composite) {
		Label lblBuscarPor = new Label(composite, SWT.NONE);
		GridData gd_lblBuscarPor = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblBuscarPor.widthHint = 85;
		lblBuscarPor.setLayoutData(gd_lblBuscarPor);
		lblBuscarPor.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblBuscarPor.setText("Buscar por:");

		comboBuscar = new Combo(composite, SWT.READ_ONLY);
		comboBuscar.setItems(searchModeItems);
		comboBuscar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));

		comboBuscar.select(0);
		comboBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toggleCompositeSearchParametersGroup();
			}
		});
		new Label(composite, SWT.NONE);
	}
	
	
	private void createCompositeParams_ByInvoice(Composite composite) {
		Label lblNoFactura = new Label(composite, SWT.NONE);
		GridData gd_lblNoFactura = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNoFactura.widthHint = 85;
		lblNoFactura.setLayoutData(gd_lblNoFactura);
		lblNoFactura.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNoFactura.setText("No. Factura:");
		
		txtFactura = new Text(composite, SWT.BORDER);
		GridData gd_txtFactura = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFactura.widthHint = 146;
		txtFactura.setLayoutData(gd_txtFactura);
		txtFactura.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtFactura.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					executeSearchByInvoiceNumber(txtFactura.getText());
				}
			}			
		});
		
		Button btnBuscar = new Button(composite, SWT.NONE);
		GridData gd_btnBuscar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBuscar.horizontalIndent = 10;
		btnBuscar.setLayoutData(gd_btnBuscar);
		btnBuscar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnBuscar.setText("Buscar");
		btnBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeSearchByInvoiceNumber(txtFactura.getText());
			}
		});
		
		txtFactura.setFocus();
	}
	
	
	private void createCompositeParams_ByDelivery(Composite composite) {
		Label lblNoEntrega = new Label(composite, SWT.NONE);
		GridData gd_lblNoEntrega = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNoEntrega.widthHint = 85;
		lblNoEntrega.setLayoutData(gd_lblNoEntrega);
		lblNoEntrega.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNoEntrega.setText("No. Entrega:");
		
		txtEntrega = new Text(composite, SWT.BORDER);
		GridData gd_txtEntrega = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtEntrega.widthHint = 146;
		txtEntrega.setLayoutData(gd_txtEntrega);
		txtEntrega.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		txtEntrega.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					executeSearchByDeliveryNumber(txtEntrega.getText());
				}
			}			
		});
		
		Button btnBuscar = new Button(composite, SWT.NONE);
		GridData gd_btnBuscar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBuscar.horizontalIndent = 10;
		btnBuscar.setLayoutData(gd_btnBuscar);
		btnBuscar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnBuscar.setText("Buscar");
		btnBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeSearchByDeliveryNumber(txtEntrega.getText());
			}
		});
		
		txtEntrega.setFocus();
	}
	
	
	private void createCompositeParams_ByDate(Composite composite) {

		Label lblFechaIni = new Label(composite, SWT.NONE);
		lblFechaIni.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFechaIni.setText("Fecha inicial:");
		
		final DateTime dateTimeIni = new DateTime(composite, SWT.DROP_DOWN);
		dateTimeIni.setDate(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH), 1);
		dateTimeIni.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		new Label(composite, SWT.NONE);
		
		Label lblFechaFin = new Label(composite, SWT.NONE);
		lblFechaFin.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFechaFin.setText("Fecha final:");
		
		final DateTime dateTimeFin = new DateTime(composite, SWT.DROP_DOWN);
		dateTimeFin.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		
		Button btnBuscar = new Button(composite, SWT.NONE);
		GridData gd_btnBuscar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnBuscar.horizontalIndent = 10;
		btnBuscar.setLayoutData(gd_btnBuscar);
		btnBuscar.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		btnBuscar.setText("Buscar");
		btnBuscar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar ini = Calendar.getInstance();
				ini.clear();
				ini.set(Calendar.DAY_OF_MONTH, dateTimeIni.getDay());
				ini.set(Calendar.MONTH, dateTimeIni.getMonth());
				ini.set(Calendar.YEAR, dateTimeIni.getYear());
				Calendar end = Calendar.getInstance();
				end.clear();
				end.set(Calendar.DAY_OF_MONTH, dateTimeFin.getDay());
				end.set(Calendar.MONTH, dateTimeFin.getMonth());
				end.set(Calendar.YEAR, dateTimeFin.getYear());
				end.set(Calendar.HOUR_OF_DAY, 23);
				end.set(Calendar.MINUTE, 59);
				end.set(Calendar.SECOND, 59);
				executeSearchByDateRange(ini.getTime(), end.getTime());
			}
		});
		
		btnBuscar.setFocus();
	}
	
	
	private void addDoubleClickListener(Table table) {
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				logger.info("ARG0: " + arg0);
				Table t = (Table) arg0.getSource();
				TableItem item = t.getItem(t.getSelectionIndex());
				Delivery delivery = (Delivery) item.getData();
				logger.info("Delivery: " + item.getText(1) + ", " + delivery);
				DeliveryDetailDialog dialog = new DeliveryDetailDialog(shell, SWT.APPLICATION_MODAL, delivery.getId());
				dialog.open();
			}
		});
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
