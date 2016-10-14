package com.orendel.transfer.ui.login;

import org.mihalis.opal.login.LoginDialogVerifier;

import com.orendel.delivery.domain.User;
import com.orendel.transfer.services.LoggedUserService;
import com.orendel.transfer.util.AuthenticationUtil;


public class LoginVerifier implements LoginDialogVerifier {

	
	@Override
	public void authenticate(String userName, String password) throws Exception {
		
		if ("".equals(userName)) {
			throw new Exception("Por favor, introduzca un usuario.");
		}

		if ("".equals(password)) {
			throw new Exception("Por favor, introduzca una contraseña.");
		}

		if (AuthenticationUtil.verifyUser(userName, password)) {
			User user = LoggedUserService.INSTANCE.getUser();
			System.out.println("User: " + user);
			System.out.println("Usuario: " + user.getFullName());
		} else {
			throw new Exception("Usuario / contraseña inválido, por favor verifique.");
		}
		
	}
	
}
