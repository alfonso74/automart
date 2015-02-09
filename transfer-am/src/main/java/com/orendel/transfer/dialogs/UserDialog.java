package com.orendel.transfer.dialogs;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

import com.orendel.transfer.controllers.UsersController;
import com.orendel.transfer.domain.Condicional;
import com.orendel.transfer.domain.Status;
import com.orendel.transfer.domain.User;
import com.orendel.transfer.services.IBaseKeywords;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;
import com.orendel.transfer.util.AuthenticationUtil;
import com.orendel.transfer.util.DialogUtil;
import com.orendel.transfer.util.MessagesUtil;


public class UserDialog extends Dialog {

	private Logger logger = Logger.getLogger(UserDialog.class);
	
	protected Object result;
	protected Shell shell;
	
	private Text txtHeader;
	
	private User registro;
	private Text txtCodigo;
	private Text txtFirstName;
	private Text txtLastName;
	private Text txtUserName;
	private Text txtPassword;
	
	private Combo comboStatus;
	private Combo comboRoles;
	
	private UsersController controller;
	
	private Image image;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UserDialog(Shell parent, int style) {
		super(parent, style);
		setText("Creación de usuario");
		image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.USER_24);
		registro = null;
		controller = new UsersController();
	}
	
	public UserDialog(Shell parent, int style, User user) {
		super(parent, style);
		setText("Edición de usuario");
		image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.USER_24);
		registro = user;
		controller = new UsersController();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		shell.setLocation(DialogUtil.calculateDialogLocation(shell, false));
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
//		if (image != null) {
//			System.out.println("Dispose image called!");
//			image.dispose();
//		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		Composite compositeTop = new Composite(shell, SWT.NONE);
		compositeTop.setEnabled(false);
		GridData gd_compositeTop = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_compositeTop.heightHint = 30;
		gd_compositeTop.minimumHeight = 30;
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
		txtHeader.setText("Detalles del usuario");
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeContent = new Composite(shell, SWT.NONE);
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeContent.setLayout(new GridLayout(1, false));
		
		Group group = new Group(compositeContent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_group = new GridLayout(4, false);
		gl_group.verticalSpacing = 7;
		group.setLayout(gl_group);
		
		Label lblCode = new Label(group, SWT.NONE);
		lblCode.setSize(42, 15);
		lblCode.setText("Código:");
		
		txtCodigo = new Text(group, SWT.BORDER);
		GridData gd_txtCodigo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtCodigo.widthHint = 35;
		txtCodigo.setLayoutData(gd_txtCodigo);
		txtCodigo.setSize(47, 21);
		txtCodigo.setEnabled(false);
		new Label(group, SWT.NONE);
		new Label(group, SWT.NONE);
		
		Label lblFirstName = new Label(group, SWT.NONE);
		lblFirstName.setSize(47, 15);
		lblFirstName.setText("Nombre:");
		
		txtFirstName = new Text(group, SWT.BORDER);
		GridData gd_txtFirstName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFirstName.widthHint = 120;
		txtFirstName.setLayoutData(gd_txtFirstName);
		txtFirstName.setSize(146, 21);
		
		Label lblLastName = new Label(group, SWT.NONE);
		GridData gd_lblLastName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblLastName.horizontalIndent = 15;
		lblLastName.setLayoutData(gd_lblLastName);
		lblLastName.setSize(47, 15);
		lblLastName.setText("Apellido:");
		
		txtLastName = new Text(group, SWT.BORDER);
		GridData gd_txtLastName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtLastName.widthHint = 120;
		txtLastName.setLayoutData(gd_txtLastName);
		txtLastName.setSize(147, 21);
		
		Label lblUsername = new Label(group, SWT.NONE);
		lblUsername.setSize(43, 15);
		lblUsername.setText("Usuario:");
		
		txtUserName = new Text(group, SWT.BORDER);
		GridData gd_txtUserName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtUserName.widthHint = 100;
		txtUserName.setLayoutData(gd_txtUserName);
		txtUserName.setSize(112, 21);
		
		Label lblPassword = new Label(group, SWT.NONE);
		GridData gd_lblPassword = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblPassword.horizontalIndent = 15;
		lblPassword.setLayoutData(gd_lblPassword);
		lblPassword.setSize(53, 15);
		lblPassword.setText("Password:");
		
		txtPassword = new Text(group, SWT.BORDER | SWT.PASSWORD);
		GridData gd_txtPassword = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtPassword.widthHint = 100;
		txtPassword.setLayoutData(gd_txtPassword);
		txtPassword.setSize(112, 21);
		
		Label lblStatus = new Label(group, SWT.NONE);
		lblStatus.setSize(38, 15);
		lblStatus.setText("Estado:");
		
		comboStatus = new Combo(group, SWT.READ_ONLY);
		comboStatus.setSize(90, 23);
		comboStatus.setItems(IBaseKeywords.ESTADO);
		
		Label lblRoles = new Label(group, SWT.NONE);
		GridData gd_lblRoles = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblRoles.horizontalIndent = 15;
		lblRoles.setLayoutData(gd_lblRoles);
		lblRoles.setSize(79, 15);
		lblRoles.setText("Admin:");
		
		comboRoles = new Combo(group, SWT.READ_ONLY);
		comboRoles.setSize(45, 23);
		comboRoles.setItems(IBaseKeywords.CONDICIONAL);
		
		Composite compositeButtons = new Composite(shell, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		compositeButtons.setBounds(0, 0, 64, 64);
		compositeButtons.setLayout(new GridLayout(2, false));
		
		Button btnConfirm = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnConfirm = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnConfirm.widthHint = 70;
		btnConfirm.setLayoutData(gd_btnConfirm);
		btnConfirm.setText("Guardar");
		
		Button btnCancel = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 70;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancelar");

		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (validateFields()) {
					doSave();
					MessagesUtil.showInformation("Guardar registro", "El usuario ha sido guardado exitosamente.");
					image.dispose();
					shell.close();
				};				
			}
		});
		
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				image.dispose();
				shell.close();
			}
		});
		
		shell.setDefaultButton(btnConfirm);
		llenarControles();
	}
	
	
	private void llenarControles() {
		if (registro == null) {
			logger.info("Creado nuevo usuario...");
			registro = new User();
			comboStatus.setText(Status.ACTIVE.getDescription());
			comboRoles.setText(Condicional.NO.getDescripcion());
		} else {
			logger.info("Cargando usuario existente...");
			txtCodigo.setText(registro.getId().toString());
			txtFirstName.setText(registro.getFirstName());
			txtLastName.setText(registro.getLastName());
			txtUserName.setText(registro.getUserName());
			txtPassword.setText(registro.getPassword());
			txtPassword.setEnabled(false);
			comboStatus.setText(Status.fromCode(registro.getStatus()).getDescription());
			comboRoles.setText(registro.isAdmin() ? Condicional.SI.getDescripcion() : Condicional.NO.getDescripcion());
		}
		logger.info("Done!");
	}
	
	
	private boolean validateFields() {
		String pFirstName = txtFirstName.getText();
		String pLastName = txtLastName.getText();
		String pUserName = txtUserName.getText();
		if (pFirstName.equals("")) {
			MessagesUtil.showInformation("Validación de campos",
					"El campo 'Nombre' no puede quedar en blanco.");
			return false;
		}
		if (pFirstName.length() > 25) {
			MessagesUtil.showInformation("Validación de campos", 
					"El nombre del usuario no puede superar los 25 caracteres (actual: " + pFirstName.length() + ").");
			return false;
		}
		if (pLastName.equals("")) {
			MessagesUtil.showInformation("Validación de campos",
					"El campo 'Apellido' no puede quedar en blanco.");
			return false;
		}
		if (pLastName.length() > 25) {
			MessagesUtil.showInformation("Validación de campos", 
					"El apellido del usuario no puede superar los 25 caracteres (actual: " + pLastName.length() + ").");
			return false;
		}
		if (pUserName.equals("")) {
			MessagesUtil.showInformation("Validación de campos",
					"El campo 'Usuario' no puede quedar en blanco.");
			return false;
		}
		if (pUserName.length() > 20) {
			MessagesUtil.showInformation("Validación de campos", 
					"El campo 'Usuario' no puede superar los 20 caracteres (actual: " + pUserName.length() + ").");
			return false;
		}
		if (txtCodigo.getText().isEmpty()) {
			if (txtPassword.getText().isEmpty()) {
				MessagesUtil.showInformation("Validación de campos",
						"Debe suministrar una contraseña para el nuevo usuario.");
				return false;
			}
		}
		return true;
	}
	
	
	private void doSave() {
		String pFirstName = txtFirstName.getText();
		String pLastName = txtLastName.getText();
		String pUserName = txtUserName.getText();
		String pPassword = txtPassword.getText();
		String pStatusCode = Status.fromDescription(comboStatus.getText()).getCode();
		boolean pIsAdmin = comboRoles.getText().equals(Condicional.SI.getDescripcion()) ? true : false;
		
		registro.setFirstName(pFirstName);
		registro.setLastName(pLastName);
		registro.setUserName(pUserName);
		registro.setPassword(AuthenticationUtil.encodePassword(pPassword));
		registro.setStatus(pStatusCode);
		registro.setAdmin(pIsAdmin);

		controller.doSave(registro);
	}
	
}
