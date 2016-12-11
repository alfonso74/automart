package com.orendel.transfer.ui.login;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.mihalis.opal.login.LoginDialog;

import com.orendel.common.services.ImagesService;
import com.orendel.transfer.services.IImageKeys;


public class LoginWindow extends LoginDialog {

	private LoginVerifier verifier;
	
	public LoginWindow(Display display) {
		super();
		Image image = ImagesService.INSTANCE.getImage(display, IImageKeys.LOGIN);
		verifier = new LoginVerifier();
		super.setVerifier(verifier);
		super.setImage(image);
		super.setDisplayRememberPassword(false);
		super.setDescription("Por favor, introduzca su nombre de usuario y contraseña");
	}

}
