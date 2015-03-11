package com.orendel.transfer.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.MenuItem;

import com.orendel.transfer.config.AppConfig;
import com.orendel.transfer.dialogs.UpdatePasswordDialog;
import com.orendel.transfer.dialogs.UserDialog;
import com.orendel.transfer.domain.User;
import com.orendel.transfer.editors.CreateTransferInEditor;
import com.orendel.transfer.editors.CreateTransferInEditorV0;
import com.orendel.transfer.editors.ViewTransfersEditor;
import com.orendel.transfer.editors.ViewUsersEditor;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.services.HibernateUtilDelivery;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;
import com.orendel.transfer.services.ShellImagesService;
import com.orendel.transfer.ui.login.LoggedUserService;
import com.orendel.transfer.ui.login.LoginWindow;
import com.orendel.transfer.util.MessagesUtil;


public class MainWindow {

	private static final Logger logger = Logger.getLogger(MainWindow.class);
	
	protected Shell shell;
	private Text txtUser;
	private Text txtDatetime;

	/**
	 * Launch the application.
	 * @param args
	 */
//	public static void main(String[] args) {
//		try {
//			MainWindow window = new MainWindow();
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public static void main(String[] args) {
		try {
			Locale.setDefault(new Locale("es", "ES"));
			logger.info("Current path: " + System.getProperty("user.dir"));
			AppConfig.INSTANCE.initializeProperties("am.properties");
			MainWindow window = new MainWindow();
			HibernateUtil.verSesiones();
			HibernateUtilDelivery.verSesiones();
			LoginWindow dialog = new LoginWindow(Display.getDefault());
//			LoginDialog dialog = new LoginDialog();
//			LoginVerifier verifier = new LoginVerifier();
//			dialog.setVerifier(verifier);
			if (dialog.open()) {
				User user = LoggedUserService.INSTANCE.getUser();
//				User user = dialog.getValidatedUser();
				System.out.println("GO GO GO!!, usuario: " + user.getFullName());
//				LoggedUserService.INSTANCE.setUser(user);
				window.open();
			} else {
				System.out.println("Cancelado!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				HibernateUtil.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
//		int x = Window.WIDTH;
		ShellImagesService imageService = new ShellImagesService(display);
		createContents();
		createDateTimeThread(display);
		shell.open();
		shell.layout();
		shell.setImages(imageService.getShellImages());
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MessagesUtil.showError("Error de aplicación", 
						(e.getMessage() == null ? e.toString() + '\n' + e.getStackTrace()[0] : e.getMessage()));
			}
		}
		ImagesService.INSTANCE.disposeImages();
		display.dispose();
	}

	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
//		shell.setSize(800, 600);
		shell.setMaximized(true);
		shell.setText("AutoMart - Control de Entrada (Transferencias)");
		shell.setLayout(new GridLayout(1, false));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmAplicacin = new MenuItem(menu, SWT.CASCADE);
		mntmAplicacin.setText("Aplicación");
		
		Menu menu_1 = new Menu(mntmAplicacin);
		mntmAplicacin.setMenu(menu_1);
		
		MenuItem mntmCambiarPassword = new MenuItem(menu_1, SWT.NONE);
		mntmCambiarPassword.setText("Contraseña");
		
		MenuItem mntmSalir = new MenuItem(menu_1, SWT.NONE);
		mntmSalir.setText("Salir");
		
		MenuItem mntmAgregarUsuario = null;
		MenuItem mntmVerUsuarios = null;
		if (LoggedUserService.INSTANCE.getUser().isAdmin()) {

			MenuItem mntmUsuarios = new MenuItem(menu, SWT.CASCADE);
			mntmUsuarios.setText("Usuarios");

			Menu menuUsuarios = new Menu(mntmUsuarios);
			mntmUsuarios.setMenu(menuUsuarios);

			mntmAgregarUsuario = new MenuItem(menuUsuarios, SWT.NONE);
			mntmAgregarUsuario.setText("Agregar usuario");

			mntmVerUsuarios = new MenuItem(menuUsuarios, SWT.NONE);
			mntmVerUsuarios.setText("Ver usuarios");

		}
		
		MenuItem mntmAcciones = new MenuItem(menu, SWT.CASCADE);
		mntmAcciones.setText("Acciones");
		
		Menu menu_2 = new Menu(mntmAcciones);
		mntmAcciones.setMenu(menu_2);
		
		MenuItem mntmRealizarEntrega = new MenuItem(menu_2, SWT.NONE);
		mntmRealizarEntrega.setText("Entrada de transferencia");
		
//		MenuItem mntmRealizarEntregaV0 = null;
//		if (LoggedUserService.INSTANCE.getUser().isAdmin()) {
//			mntmRealizarEntregaV0 = new MenuItem(menu_2, SWT.NONE);
//			mntmRealizarEntregaV0.setText("Realizar transferencia V0");
//		}
		
		final MenuItem mntmCancelarEntrega = new MenuItem(menu_2, SWT.NONE);
		mntmCancelarEntrega.setText("Cancelar entrada");
		Image image = ImagesService.INSTANCE.getImage(Display.getDefault(), IImageKeys.CANCEL_16);
		mntmCancelarEntrega.setImage(image);
//		mntmCancelarEntrega.setEnabled(false);
		
		new MenuItem(menu_2, SWT.SEPARATOR);
		
		MenuItem mntmConsultarTransferencias = new MenuItem(menu_2, SWT.NONE);
		mntmConsultarTransferencias.setText("Consultar entradas");

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite.setLayout(new GridLayout(1, false));		
		
		Composite compositeFooter = new Composite(shell, SWT.NONE);
		compositeFooter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		compositeFooter.setLayout(new GridLayout(3, false));
		
		Label label = new Label(compositeFooter, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Label lblUser = new Label(compositeFooter, SWT.NONE);
		lblUser.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUser.setText("Usuario:");
		
		txtUser = new Text(compositeFooter, SWT.READ_ONLY);
		txtUser.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		txtDatetime = new Text(compositeFooter, SWT.READ_ONLY);
		txtDatetime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		mntmSalir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		mntmCambiarPassword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				openUpdatePasswordDialog(composite);
			}
		});
		
		
		if (LoggedUserService.INSTANCE.getUser().isAdmin()) {
			mntmAgregarUsuario.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					openCreateUserDialog(composite);
				}
			});

			mntmVerUsuarios.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					disposeChildrenComposites(composite);
					openViewUsersEditor(composite);
					composite.layout();
				}
			});
		}
		
		
		mntmRealizarEntrega.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openCreateTransferInEditor(composite);
				composite.layout();
			}
		});
		
//		if (LoggedUserService.INSTANCE.getUser().isAdmin()) {
//			mntmRealizarEntregaV0.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					disposeChildrenComposites(composite);
//					openCreateTransferInEditorV0(composite);
//					composite.layout();
//				}
//			});
//		}
		
		mntmCancelarEntrega.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CreateTransferInEditor editor = null;
				for (Control v : composite.getChildren()) {
					if (v instanceof CreateTransferInEditor) {
						logger.info("TransferInEditor found!");
						editor = (CreateTransferInEditor) v;
						editor.cancelTransferControl();
						break;
					}
				}
				if (editor == null) {
					MessagesUtil.showInformation("Cancelar entrada", "<size=+2>Debe estar abierta una entrada de transferencia para ejecutar esta acción.</size>");
				}
			}
		});
		
		mntmAcciones.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.info("mntmAcciones selected!");
				boolean enableCancelarEntrega = false;
				Object txNo = composite.getData("txtTransferNo");
				String currentTxNo = "";
				if (txNo != null) {
					currentTxNo = (String) txNo;
					if (!currentTxNo.isEmpty()) {
						enableCancelarEntrega = true;
					}
				}
				mntmCancelarEntrega.setEnabled(enableCancelarEntrega);
			}
		});
		
		mntmConsultarTransferencias.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openViewTransfersEditor(composite);
				composite.layout();
			}
		});
		
		fillFooterInfo();
		
		openCreateTransferInEditor(composite);
	}
	
	
	private void disposeChildrenComposites(Composite composite) {
		for (Control c : composite.getChildren()) {
			c.dispose();
		}		
	}
	
	
	private void fillFooterInfo() {
		txtUser.setText(LoggedUserService.INSTANCE.getUser().getFullName());
		txtDatetime.setText(getDateAsString(new Date()));
	}
	
	
	private void openUpdatePasswordDialog(Composite composite) {
		// TODO ver opción de shell vs composite en llamado a dialog
		logger.info("Update password called.");
		UpdatePasswordDialog dialog = new UpdatePasswordDialog(composite.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.open();
	}
	
	
	private void openCreateUserDialog(Composite composite) {
		logger.info("Create user called.");
		UserDialog dialog = new UserDialog(composite.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.open();
	}
	
	
	private ViewUsersEditor openViewUsersEditor(Composite composite) {
		logger.info("View users called.");
		ViewUsersEditor editor = new ViewUsersEditor(composite, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return editor;
	}
	
	
	private CreateTransferInEditor openCreateTransferInEditor(Composite composite) {
		CreateTransferInEditor editor = new CreateTransferInEditor(composite, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return editor;
	}
	
	
	private CreateTransferInEditorV0 openCreateTransferInEditorV0(Composite composite) {
		CreateTransferInEditorV0 editor = new CreateTransferInEditorV0(composite, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return editor;
	}
	
	
	private ViewTransfersEditor openViewTransfersEditor(Composite composite) {
		ViewTransfersEditor editor = new ViewTransfersEditor(composite, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return editor;
	}

	
	private void createDateTimeThread(final Display display) {
		Thread timeThread = new Thread() {
	        public void run() {
	            while (true) {
	                display.syncExec(new Runnable() {
	                    @Override
	                    public void run() {
	                    	if (txtDatetime != null && !txtDatetime.isDisposed()) {
	                    		txtDatetime.setText(getDateAsString(new Date()));
	                    	}
	                    }
	                });
	                try {
	                    Thread.sleep(1000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    };
	    timeThread.setDaemon(true);
	    timeThread.start();
	}

	
	private String getDateAsString(Date date) {
		String dateString = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		dateString = format.format(date);
		return dateString;
	}
	
}
