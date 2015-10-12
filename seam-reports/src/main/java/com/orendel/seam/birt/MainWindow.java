package com.orendel.seam.birt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.orendel.seam.composites.BirtReport;
import com.orendel.seam.services.ImagesService;


public class MainWindow {
	
	private Logger logger = Logger.getLogger(MainWindow.class);

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
			System.out.println("UUU: " + System.getProperty("user.dir"));
			MainWindow window = new MainWindow();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
//		int x = Window.WIDTH;
		ImagesService imageService = new ImagesService(display);
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
		shell.setText("AutoMart - Control de Entregas");
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
		
			MenuItem mntmUsuarios = new MenuItem(menu, SWT.CASCADE);
			mntmUsuarios.setText("Usuarios");

			Menu menuUsuarios = new Menu(mntmUsuarios);
			mntmUsuarios.setMenu(menuUsuarios);

			mntmAgregarUsuario = new MenuItem(menuUsuarios, SWT.NONE);
			mntmAgregarUsuario.setText("Agregar usuario");

			mntmVerUsuarios = new MenuItem(menuUsuarios, SWT.NONE);
			mntmVerUsuarios.setText("Ver usuarios");
		
		MenuItem mntmAcciones = new MenuItem(menu, SWT.CASCADE);
		mntmAcciones.setText("Acciones");
		
		Menu menu_2 = new Menu(mntmAcciones);
		mntmAcciones.setMenu(menu_2);
		
		MenuItem mntmRealizarEntrega = new MenuItem(menu_2, SWT.NONE);
		mntmRealizarEntrega.setText("Realizar entrega");
		
		MenuItem mntmConsultarEntregas = new MenuItem(menu_2, SWT.NONE);
		mntmConsultarEntregas.setText("Consultar entregas");
		
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
		
		
		mntmRealizarEntrega.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeChildrenComposites(composite);
				openCreateDeliveryEditor(composite);
				composite.layout();
			}
		});
		
		fillFooterInfo();
		
//		openCreateDeliveryEditorX(composite);
	}
	
	
	private void disposeChildrenComposites(Composite composite) {
		for (Control c : composite.getChildren()) {
			c.dispose();
		}		
	}
	
	
	private void fillFooterInfo() {
		txtUser.setText("YO!");
		txtDatetime.setText(getDateAsString(new Date()));
	}
	
	private BirtReport openCreateDeliveryEditor(Composite composite) {
		BirtReport editor = new BirtReport(composite, SWT.None);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		editor.execute(null);
		return editor;
	}
	
//	private CreateDeliveryEditor openCreateDeliveryEditor(Composite composite) {
//		CreateDeliveryEditor editor = new CreateDeliveryEditor(composite, SWT.NONE);
//		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		return editor;
//	}
	
	
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
