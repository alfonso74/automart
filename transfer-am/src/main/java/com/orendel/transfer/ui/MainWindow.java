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
import org.eclipse.wb.swt.SWTResourceManager;
import org.hibernate.HibernateException;

import com.orendel.common.config.AppConfig;
import com.orendel.common.services.ImagesService;
import com.orendel.delivery.domain.User;
import com.orendel.transfer.dialogs.UpdatePasswordDialog;
import com.orendel.transfer.dialogs.UserDialog;
import com.orendel.transfer.editors.CreateTransferInEditor;
import com.orendel.transfer.editors.ViewExtendedItemDetailsEditor;
import com.orendel.transfer.editors.ViewTransfersEditor;
import com.orendel.transfer.editors.ViewUsersEditor;
import com.orendel.transfer.services.HibernateUtil;
import com.orendel.transfer.services.HibernateUtilDelivery;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.LoggedUserService;
import com.orendel.transfer.services.ShellImagesService;
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
	public static void main(String[] args) {
		try {
			Locale.setDefault(new Locale("es", "ES"));
			logger.info("Current path: " + System.getProperty("user.dir"));
			AppConfig.INSTANCE.initializeProperties("am.properties");
			MainWindow window = new MainWindow();
			HibernateUtil.verSesiones();
			HibernateUtilDelivery.verSesiones();
			LoginWindow dialog = new LoginWindow(Display.getDefault());
			if (dialog.open()) {
				User user = LoggedUserService.INSTANCE.getUser();
				System.out.println("GO GO GO!!, usuario: " + user.getFullName());
				window.open();
			} else {
				System.out.println("Cancelado!");
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				HibernateUtil.verSesiones();
				HibernateUtilDelivery.verSesiones();
				HibernateUtil.destroy();
				HibernateUtilDelivery.destroy();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
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
		try {
			while (!shell.isDisposed()) {
				try {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				} catch (HibernateException e) {
					// una HibernateException cierra el cliente, ya que no sabemos en qué estado están las sesiones/txs
					MessagesUtil.showError("Error de aplicación / base de datos", 
							"Error grave durante interacción con la base de datos.  Debe reiniciar la aplicación.");
					throw e;
				} catch (Exception e) {
					// otras excepciones muestran su mensaje, pero permiten seguir utilizando el sistema
					logger.error(e.getMessage(), e);
					MessagesUtil.showError("Error de aplicación", 
							(e.getMessage() == null ? e.toString() + '\n' + e.getStackTrace()[0] : e.getMessage()));
				}
			}
		} finally {
			SWTResourceManager.dispose();
			ImagesService.INSTANCE.disposeImages();
			display.dispose();
		}
	}

	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
//		shell.setSize(800, 600);
		shell.setMaximized(true);
		shell.setText("AutoMart - Control de Entrada (Transferencias) - v1.0.8");
		shell.setLayout(new GridLayout(1, false));
		
		Menu menuMainBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuMainBar);
		
		MenuItem itemApplication = new MenuItem(menuMainBar, SWT.CASCADE);
		itemApplication.setText("Aplicación");
		
		Menu menuApplication = new Menu(itemApplication);
		itemApplication.setMenu(menuApplication);
		
		MenuItem itemPassword = new MenuItem(menuApplication, SWT.NONE);
		itemPassword.setText("Contraseña");
		
		MenuItem itemExit = new MenuItem(menuApplication, SWT.NONE);
		itemExit.setText("Salir");
		
		MenuItem mntmAgregarUsuario = null;
		MenuItem mntmVerUsuarios = null;
		if (LoggedUserService.INSTANCE.getUser().isAdmin()) {

			MenuItem mntmUsuarios = new MenuItem(menuMainBar, SWT.CASCADE);
			mntmUsuarios.setText("Usuarios");

			Menu menuUsuarios = new Menu(mntmUsuarios);
			mntmUsuarios.setMenu(menuUsuarios);

			mntmAgregarUsuario = new MenuItem(menuUsuarios, SWT.NONE);
			mntmAgregarUsuario.setText("Agregar usuario");

			mntmVerUsuarios = new MenuItem(menuUsuarios, SWT.NONE);
			mntmVerUsuarios.setText("Ver usuarios");

		}
		
		MenuItem itemAcciones = new MenuItem(menuMainBar, SWT.CASCADE);
		itemAcciones.setText("Acciones");
		
		Menu menuAcciones = new Menu(itemAcciones);
		itemAcciones.setMenu(menuAcciones);
		
		MenuItem itemEntradaTx = new MenuItem(menuAcciones, SWT.NONE);
		itemEntradaTx.setText("Entrada de transferencia");
		
		final MenuItem itemCancelarEntrega = new MenuItem(menuAcciones, SWT.NONE);
		itemCancelarEntrega.setText("Cancelar entrada");
		Image image = ImagesService.INSTANCE.getImage(Display.getDefault(), IImageKeys.CANCEL_16);
		itemCancelarEntrega.setImage(image);
		
		new MenuItem(menuAcciones, SWT.SEPARATOR);
		
		MenuItem itemConsultarTransferencias = new MenuItem(menuAcciones, SWT.NONE);
		itemConsultarTransferencias.setText("Consultar entradas");
		
		new MenuItem(menuAcciones, SWT.SEPARATOR);
		
		MenuItem itemConsultarArticulosExtended = new MenuItem(menuAcciones, SWT.NONE);
		itemConsultarArticulosExtended.setText("Consultar artículos");

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

		itemExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		itemPassword.addSelectionListener(new SelectionAdapter() {
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
		
		
		itemEntradaTx.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openCreateTransferInEditor(composite);
				composite.layout();
			}
		});
		
		itemCancelarEntrega.addSelectionListener(new SelectionAdapter() {
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
		
		itemAcciones.addSelectionListener(new SelectionAdapter() {
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
				itemCancelarEntrega.setEnabled(enableCancelarEntrega);
			}
		});
		
		itemConsultarTransferencias.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openViewTransfersEditor(composite);
				composite.layout();
			}
		});
		
		itemConsultarArticulosExtended.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openViewExtendedItemDetailsEditor(composite);
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
	
	
	private ViewTransfersEditor openViewTransfersEditor(Composite composite) {
		ViewTransfersEditor editor = new ViewTransfersEditor(composite, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return editor;
	}
	
	
	private ViewExtendedItemDetailsEditor openViewExtendedItemDetailsEditor(Composite composite) {
		ViewExtendedItemDetailsEditor editor = new ViewExtendedItemDetailsEditor(composite, SWT.NONE);
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
