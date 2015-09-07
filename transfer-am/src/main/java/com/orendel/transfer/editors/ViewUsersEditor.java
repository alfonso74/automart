package com.orendel.transfer.editors;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

import com.orendel.transfer.controllers.UsersController;
import com.orendel.transfer.dialogs.UserDialog;
import com.orendel.transfer.domain.Status;
import com.orendel.transfer.domain.User;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;


public class ViewUsersEditor extends Composite {
	
	private Logger logger = Logger.getLogger(ViewUsersEditor.class);
	private UsersController controller;
	
	private Table table;

	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ViewUsersEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		controller = new UsersController();
		Image image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.USER_24);
		
		Group group = new Group(this, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		GridData gd_group = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_group.heightHint = 50;
		gd_group.minimumHeight = 50;
		group.setLayoutData(gd_group);
		
		Label lblHeaderImage = new Label(group, SWT.NONE);
		lblHeaderImage.setImage(image);
		lblHeaderImage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		Label labelHeaderText = new Label(group, SWT.NONE);
		labelHeaderText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		labelHeaderText.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		labelHeaderText.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		labelHeaderText.setText("Listado de usuarios");
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnFirstname = new TableColumn(table, SWT.NONE);
		tblclmnFirstname.setWidth(150);
		tblclmnFirstname.setText("Nombre");
		
		TableColumn tblclmnLastname = new TableColumn(table, SWT.NONE);
		tblclmnLastname.setWidth(150);
		tblclmnLastname.setText("Apellido");
		
		TableColumn tblclmnUser = new TableColumn(table, SWT.NONE);
		tblclmnUser.setWidth(150);
		tblclmnUser.setText("Usuario");
		
		TableColumn tblclmnAdmin = new TableColumn(table, SWT.CENTER);
		tblclmnAdmin.setWidth(75);
		tblclmnAdmin.setText("Admin");
		
		TableColumn tblclmnBarcode = new TableColumn(table, SWT.CENTER);
		tblclmnBarcode.setWidth(75);
		tblclmnBarcode.setText("Barcode");
		
		TableColumn tblclmnStatus = new TableColumn(table, SWT.NONE);
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Estado");

		addDoubleClickListener(table);
		
		getUsers();
	}
	
	
	private void addDoubleClickListener(Table table) {
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				logger.info("ARG0: " + arg0);
				Table t = (Table) arg0.getSource();
				TableItem item = t.getItem(t.getSelectionIndex());
				User user = (User) item.getData();
				logger.info("User1: " + user.getFullName());
				logger.info("User2: " + item.getText(1) + ", " + user);

				logger.info("View Shell: " + getShell());
				UserDialog dialog = new UserDialog(getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL, user);
				dialog.open();
				getUsers();
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
			}

			@Override
			public void mouseUp(MouseEvent arg0) {
			}
		});
	}
	
	
	private void getUsers() {
		List<User> usersList = controller.getListado();
		if (usersList != null && !usersList.isEmpty()) {
			refreshTableDetails(usersList);
		}
	}
	
	
	private void refreshTableDetails(List<User> usersList) {
		table.removeAll();
		TableItem item;
		for (User user : usersList) {
			item = new TableItem(table, SWT.NONE);
			int column = 0;
			item.setData(user);
			item.setText(column++, user.getFirstName());
			item.setText(column++, user.getLastName());
			item.setText(column++, user.getUserName());
			item.setText(column++, user.isAdmin() ? "Si" : "No");
			item.setText(column++, user.canEditBarcodes() ? "Si" : "No");
			item.setText(column++, Status.fromCode(user.getStatus()).getDescription());
		}
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
