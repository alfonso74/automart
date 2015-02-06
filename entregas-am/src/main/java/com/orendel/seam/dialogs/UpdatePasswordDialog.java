package com.orendel.seam.dialogs;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Group;

import com.orendel.seam.controllers.UsersController;
import com.orendel.seam.domain.delivery.User;
import com.orendel.seam.ui.login.LoggedUserService;
import com.orendel.seam.util.AuthenticationUtil;
import com.orendel.seam.util.DialogUtil;
import com.orendel.seam.util.MessagesUtil;


public class UpdatePasswordDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text txtCurrentPassword;
	private Text txtNewPassword;
	private Text txtConfirmPassword;
	
	private Image image;
	private Text txtPorFavorIntroduzca;
	
	private UsersController controller;
	

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public UpdatePasswordDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
		image = new Image(parent.getDisplay(), UpdatePasswordDialog.class.getClassLoader().getResourceAsStream("bar_chart_16.png"));
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
		if (image != null) {
			System.out.println("Dispose image called!");
			image.dispose();
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(285, 230);
		shell.setText("Actualizar password");
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 0;
		gl_shell.horizontalSpacing = 0;
		gl_shell.marginWidth = 0;
		gl_shell.marginHeight = 0;
		shell.setLayout(gl_shell);
		
		Composite compositeTop = new Composite(shell, SWT.NONE);
		compositeTop.setEnabled(false);
		GridData gd_compositeTop = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_compositeTop.heightHint = 50;
		gd_compositeTop.minimumHeight = 50;
		compositeTop.setLayoutData(gd_compositeTop);
		compositeTop.setLayout(new GridLayout(2, false));
		
		Label dialogImage = new Label(compositeTop, SWT.NONE);
		dialogImage.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
		dialogImage.setImage(image);
		
		txtPorFavorIntroduzca = new Text(compositeTop, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		txtPorFavorIntroduzca.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtPorFavorIntroduzca.setText("Por favor, introduzca el password actual y el nuevo password.");
		txtPorFavorIntroduzca.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite compositeContent = new Composite(shell, SWT.NONE);
		compositeContent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_compositeContent = new GridLayout(1, false);
		gl_compositeContent.marginHeight = 0;
		compositeContent.setLayout(gl_compositeContent);
		
		Group group = new Group(compositeContent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_group = new GridLayout(2, false);
		group.setLayout(gl_group);
		
		Label lblCurrentPassword = new Label(group, SWT.NONE);
		lblCurrentPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCurrentPassword.setText("Password actual:");
		
		txtCurrentPassword = new Text(group, SWT.BORDER);
		txtCurrentPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewPassword = new Label(group, SWT.NONE);
		lblNewPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewPassword.setText("Password nuevo:");
		
		txtNewPassword = new Text(group, SWT.BORDER | SWT.PASSWORD);
		txtNewPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblConfirmPassword = new Label(group, SWT.NONE);
		lblConfirmPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmPassword.setText("Confirmar password:");
		
		txtConfirmPassword = new Text(group, SWT.BORDER | SWT.PASSWORD);
		txtConfirmPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Composite compositeButtons = new Composite(shell, SWT.NONE);
		compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		GridLayout gl_compositeButtons = new GridLayout(2, false);
		gl_compositeButtons.marginTop = 10;
		compositeButtons.setLayout(gl_compositeButtons);
		
		Button btnConfirm = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnConfirmar = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
		gd_btnConfirmar.widthHint = 70;
		btnConfirm.setLayoutData(gd_btnConfirmar);
		btnConfirm.setText("OK");
		
		Button btnCancel = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnCancelar = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancelar.widthHint = 70;
		btnCancel.setLayoutData(gd_btnCancelar);
		btnCancel.setText("Cancelar");
		
		btnConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (validateFields()) {
					updatePassword();
					MessagesUtil.showInformation("Actualizar password", "El password ha sido actualizado exitosamente.");
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
	}
	
	
	private boolean validateFields() {
		String pCurrentPass = txtCurrentPassword.getText();
		String pNewPass = txtNewPassword.getText();
		String pConfirmPass = txtConfirmPassword.getText();
		
		if (pCurrentPass.equals("")) {
			MessagesUtil.showError("Error de validación", "El campo de \"Password actual\" no puede quedar en blanco.");
			txtCurrentPassword.setFocus();
			return false;
		}
		if (pNewPass.equals("")) {
			MessagesUtil.showError("Error de validación", "El campo de \"Password nuevo\" no puede quedar en blanco.");
			txtNewPassword.setFocus();
			return false;
		}
		if (pConfirmPass.equals("")) {
			MessagesUtil.showError("Error de validación", "El campo de \"Confirmar password\" no puede quedar en blanco.");
			txtConfirmPassword.setFocus();
			return false;
		}
		if (!pConfirmPass.equals(pNewPass)) {
			MessagesUtil.showError("Error de validación", "El nuevo password no ha sido confirmado correctamente.");
			txtConfirmPassword.setFocus();
			return false;
		}
		return true;
	}
	
	
	private void updatePassword() {
		String pNewPassword = txtNewPassword.getText();
		User user = LoggedUserService.INSTANCE.getUser();
		user.setPassword(AuthenticationUtil.encodePassword(pNewPassword));
		controller.doSave(user);
	}
	
}
