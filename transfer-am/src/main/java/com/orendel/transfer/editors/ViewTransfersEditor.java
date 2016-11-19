package com.orendel.transfer.editors;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.orendel.delivery.domain.TransferControl;
import com.orendel.delivery.domain.TransferControlStatus;
import com.orendel.transfer.controllers.TransferControlController;
import com.orendel.transfer.util.DateUtil;
import com.orendel.transfer.util.MessagesUtil;


public class ViewTransfersEditor extends Composite {
	private static final Logger logger = Logger.getLogger(ViewTransfersEditor.class);
	
	private TransferControlController controller; 
	
	private Table table;
	
	private Composite compositeParams;
	
	private Composite parent;

	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewTransfersEditor(Composite parent, int style) {
		super(parent, style);

		this.parent = parent;
		controller = new TransferControlController();
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Group groupBuscar = new Group(this, SWT.NONE);
		groupBuscar.setText(" Búsqueda de Entradas Realizadas");
		GridLayout gl_groupBuscar = new GridLayout(1, false);
		gl_groupBuscar.marginBottom = 5;
		gl_groupBuscar.marginHeight = 0;
		groupBuscar.setLayout(gl_groupBuscar);
		groupBuscar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		compositeParams = new Composite(groupBuscar, SWT.NONE);
		compositeParams.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeParams = new GridLayout(6, false);
		gl_compositeParams.marginTop = 10;
		gl_compositeParams.marginBottom = 5;
		gl_compositeParams.marginHeight = 0;
		compositeParams.setLayout(gl_compositeParams);
		
		createCompositeParams_ByDate(compositeParams);

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.verticalIndent = 10;
		gd_table.widthHint = 440;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnEntrada = new TableColumn(table, SWT.NONE);
		tblclmnEntrada.setWidth(75);
		tblclmnEntrada.setText("Entrada");
		
		TableColumn tblclmnTransferencia = new TableColumn(table, SWT.NONE);
		tblclmnTransferencia.setWidth(110);
		tblclmnTransferencia.setText("Transferencia");
		
		TableColumn tblclmnLineas = new TableColumn(table, SWT.RIGHT);
		tblclmnLineas.setWidth(70);
		tblclmnLineas.setText("Líneas");
		
		TableColumn tblclmnItems = new TableColumn(table, SWT.RIGHT);
		tblclmnItems.setWidth(80);
		tblclmnItems.setText("Items");
		
		TableColumn tblclmnRecibidos = new TableColumn(table, SWT.RIGHT);
		tblclmnRecibidos.setWidth(80);
		tblclmnRecibidos.setText("Recibidos");
		
		TableColumn tblclmnUsuario = new TableColumn(table, SWT.LEFT);
		tblclmnUsuario.setWidth(200);
		tblclmnUsuario.setText("Usuario");
		
		TableColumn tblclmnStatus = new TableColumn(table, SWT.NONE);
		tblclmnStatus.setWidth(90);
		tblclmnStatus.setText("Status");
		
		TableColumn tblclmnCreacion = new TableColumn(table, SWT.CENTER);
		tblclmnCreacion.setWidth(175);
		tblclmnCreacion.setText("Fecha creación");
		
		TableColumn tblclmnCierre = new TableColumn(table, SWT.CENTER);
		tblclmnCierre.setWidth(175);
		tblclmnCierre.setText("Fecha de cierre");
		
		TableColumn tblclmnLog = new TableColumn(table, SWT.LEFT);
		tblclmnLog.setWidth(200);

		addDoubleClickListener(table);
	}
	
	
	private void executeSearchByDateRange(Date initialDate, Date endDate) {
		List<TransferControl> tcList = controller.findTransfersByDateRange(initialDate, endDate);
		if (tcList != null && !tcList.isEmpty()) {
			refreshTableDetails(tcList);
		} else {
			table.removeAll();
			MessagesUtil.showWarning("Buscar entradas por fecha", 
					"No se encontró ninguna entrada en el rango de fechas indicado.");
		}
	}
	
	
	private void refreshTableDetails(List<TransferControl> tcList) {
		table.removeAll();
		TableItem item;
		
		for (TransferControl v : tcList) {
			item = new TableItem(table, SWT.NONE);
			int column = 0;
			item.setData(v);
			item.setText(column++, " " + v.getId());
			item.setText(column++, " " + v.getTransferNo());
			item.setText(column++, v.getLines().size() + " ");
			item.setText(column++, v.getTotalExpectedItems() + " ");
			item.setText(column++, v.getTotalReceivedItems() + " ");
			item.setText(column++, v.getUserName() == null ? "" : " " + v.getUserName());
			item.setText(column++, v.getStatus() == null ? "?" : TransferControlStatus.fromCode(v.getStatus()).getDescription());
			item.setText(column++, DateUtil.toString(v.getCreated(), DateUtil.formatoFechaHora));
			item.setText(column++, DateUtil.toString(v.getClosed(), DateUtil.formatoFechaHora));
			item.setText(column++, checkNull(v.getLog()));
		}
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
				TransferControl control = (TransferControl) item.getData();
				logger.info("TransferControl selected: " + item.getText(1) + ", " + control);
				
				disposeChildrenComposites(parent);
				CreateTransferInCsvEditor editor = new CreateTransferInCsvEditor(parent, SWT.NONE, control.getTransferNo());
				editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				parent.layout();
			}
		});
	}
	
	private void disposeChildrenComposites(Composite composite) {
		for (Control c : composite.getChildren()) {
			c.dispose();
		}		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public String checkNull(String valorCampo) {
		return valorCampo == null ? "" : valorCampo;
	}
}
