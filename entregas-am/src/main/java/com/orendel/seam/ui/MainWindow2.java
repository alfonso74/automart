package com.orendel.seam.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.orendel.seam.domain.delivery.User;
import com.orendel.seam.editors.CreateDeliveryEditor;
import com.orendel.seam.editors.ViewDeliveriesEditorMod;
import com.orendel.seam.services.HibernateUtil;
import com.orendel.seam.ui.login.LoggedUserService;
import com.orendel.seam.ui.login.LoginWindow;


public class MainWindow2 {

	protected Shell shell;
	private Text txtUser;
	private Text txtDatetime;

	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		try {
//			Locale.setDefault(new Locale("es", "ES"));
			MainWindow2 window = new MainWindow2();
			HibernateUtil.verSesiones();
//			LoginWindow dialog = new LoginWindow(Display.getDefault());
//			if (dialog.open()) {
//				User user = dialog.getValidatedUser();
//				System.out.println("GO GO GO!!, usuario: " + user.getFullName());
//				LoggedUserService.INSTANCE.setUser(user);
//				window.open();
//			} else {
//				System.out.println("Cancelado!");
//			}
			
			window.open();
			
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
		createContents();
		createDateTimeThread(display);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		display.dispose();
	}

	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText("SWT Application");
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.marginWidth = 0;
		gl_shell.marginHeight = 0;
		shell.setLayout(gl_shell);
		
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
		
		MenuItem mntmUsuarios = new MenuItem(menu, SWT.CASCADE);
		mntmUsuarios.setText("Usuarios");
		
		Menu menuUsuarios = new Menu(mntmUsuarios);
		mntmUsuarios.setMenu(menuUsuarios);
		
		MenuItem mntmAgregarUsuario = new MenuItem(menuUsuarios, SWT.NONE);
		mntmAgregarUsuario.setText("Agregar usuario");
		
		MenuItem mntmVerUsuarios = new MenuItem(menuUsuarios, SWT.NONE);
		mntmVerUsuarios.setText("Ver usuarios");
		
		MenuItem mntmAcciones = new MenuItem(menu, SWT.CASCADE);
		mntmAcciones.setText("Acciones");
		
		Menu menu_2 = new Menu(mntmAcciones);
		mntmAcciones.setMenu(menu_2);
		
		MenuItem mntmRealizarEntrega = new MenuItem(menu_2, SWT.NONE);
		mntmRealizarEntrega.setText("Realizar entrega");
		
		MenuItem mntmConsultarEntregas = new MenuItem(menu_2, SWT.NONE);
		mntmConsultarEntregas.setText("Consultar entregas");
		
		MenuItem mntmEntregas = new MenuItem(menu, SWT.NONE);
		mntmEntregas.setText("Entregas");
		
		MenuItem mntmConsultas = new MenuItem(menu, SWT.NONE);
		mntmConsultas.setText("Consultas");
		
		CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		coolBar.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		coolBar.setLocked(true);
		
		CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setText("Bocadillo");
		
		ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT | SWT.RIGHT);
		coolItem.setControl(toolBar);
		
		ToolItem tltmDropdown = new ToolItem(toolBar, SWT.DROP_DOWN);
		tltmDropdown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		tltmDropdown.setImage(SWTResourceManager.getImage(MainWindow2.class, "/images/arrowGreenRight.png"));
		tltmDropdown.setText("Aplicación");
		
		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.setImage(SWTResourceManager.getImage(MainWindow2.class, "/images/exclamation.png"));
		tltmNewItem.setText("New Item");
		
		ToolItem tltmDropdownItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		tltmDropdownItem.setText("DropDown Item");
		
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

		mntmEntregas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openCreateDeliveryEditor(composite);
				composite.layout();
			}
		});
		
		mntmConsultas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("JA!");
				disposeChildrenComposites(composite);
				openViewDeliveriesEditor(composite);
				composite.layout();
			}
		});
		
		mntmRealizarEntrega.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openCreateDeliveryEditor(composite);
				composite.layout();
			}
		});
		
		mntmConsultarEntregas.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("JA!");
				disposeChildrenComposites(composite);
				openViewDeliveriesEditor(composite);
				composite.layout();
			}
		});
		
		fillFooterInfo();
		
		openCreateDeliveryEditor(composite);
	}
	
	
	private void disposeChildrenComposites(Composite composite) {
		for (Control c : composite.getChildren()) {
			c.dispose();
		}		
	}
	
	
	private void fillFooterInfo() {
		txtUser.setText("Carlos Pérez");
		txtUser.setText(LoggedUserService.INSTANCE.getUser().getFullName());
		txtDatetime.setText(getDateAsString(new Date()));
	}
	
	
	private CreateDeliveryEditor openCreateDeliveryEditor(Composite composite) {
		CreateDeliveryEditor editor = new CreateDeliveryEditor(composite, SWT.NONE);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		return editor;
	}
	
	
	private ViewDeliveriesEditorMod openViewDeliveriesEditor(Composite composite) {
		ViewDeliveriesEditorMod editor = new ViewDeliveriesEditorMod(composite, SWT.NONE);
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
