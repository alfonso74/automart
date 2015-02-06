/*******************************************************************************
 * Copyright (c) 2011 Laurent CARON
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Laurent CARON (laurent.caron at gmail dot com) - initial API and implementation
 *******************************************************************************/
package com.orendel.seam.ui;

import java.util.Locale;

import org.mihalis.opal.login.LoginDialog;
import org.mihalis.opal.login.LoginDialogVerifier;


/**
 * This snippet demonstrates the Login Dialog widget
 *
 */
public class LoginDialogBasic {

        /**
         * @param args
         */
        public static void main(final String[] args) {

                Locale.setDefault(Locale.ENGLISH);

                final LoginDialogVerifier verifier = new LoginDialogVerifier() {

                        @Override
                        public void authenticate(final String login, final String password) throws Exception {
                                if ("".equals(login)) {
                                        throw new Exception("Por favor, introduzca un usuario.");
                                }

                                if ("".equals(password)) {
                                        throw new Exception("Por favor, introduzca una contraseña.");
                                }

                                if (!login.equalsIgnoreCase("default")) {
                                        throw new Exception("Usuario desconocido.");
                                }

                                if (!password.equalsIgnoreCase("default")) {
                                        throw new Exception("Usuario / contraseña inválido, por favor verifique.");
                                }
                        }
                                                
                };
                
                LoginDialog dialog = new LoginDialog();
                dialog.setDisplayRememberPassword(false);
                dialog.setVerifier(verifier);

                boolean result = dialog.open();
                if (result) {
                        System.out.println("Login confirmado: " + dialog.getLogin());
                } else {
                        System.out.println("Cancelado!");
                }
        }

}
