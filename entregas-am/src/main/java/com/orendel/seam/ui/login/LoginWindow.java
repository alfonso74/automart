package com.orendel.seam.ui.login;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.mihalis.opal.login.LoginDialog;

import com.orendel.seam.domain.delivery.User;

public class LoginWindow extends LoginDialog {

	private LoginVerifier verifier;
	
	public LoginWindow(Display display) {
		super();
		Image image = new Image(display, LoginWindow.class.getClassLoader().getResourceAsStream("automartLogin.gif"));
		verifier = new LoginVerifier();
		super.setVerifier(verifier);
		super.setImage(image);
		super.setDisplayRememberPassword(false);
	}
	
//	public User getValidatedUser() {
//		return verifier.getUser();
//	}

}
