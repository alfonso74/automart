package com.orendel.seam.editors;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hibernate.Session;
import org.mihalis.opal.login.LoginDialogVerifier;

import com.orendel.seam.domain.counterpoint.Invoice;
import com.orendel.seam.services.HibernateUtil;

public class MainEditor {

	private Display display;
	protected Shell shell;
	private Text txtFactura;
	private Table table;
	private Text txtAny;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainEditor window = new MainEditor();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
//		Display display = Display.getDefault();
		createContents();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
		HibernateUtil.destroy();
		testMessage();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		display = new Display();
		shell = new Shell(display);
		shell.setText( "Hibernate CRUD Sample using SWT" );
//		shell.setImage( new Image( display, "Mawar.gif" ) );
		shell.setSize(800, 600);
		shell.setLayout(new GridLayout(3, false));
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmUno = new MenuItem(menu, SWT.NONE);
		mntmUno.setText("Uno");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		Label lblFactura = new Label(shell, SWT.NONE);
		lblFactura.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblFactura.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFactura.setText("Factura:");
		
		txtFactura = new Text(shell, SWT.BORDER);
		txtFactura.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testHibernate();
//				testMessage();
			}
		});
		btnNewButton.setText("Buscar");
		new Label(shell, SWT.NONE);
		
		txtAny = new Text(shell, SWT.BORDER);
		txtAny.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnCant = new TableColumn(table, SWT.NONE);
		tblclmnCant.setWidth(100);
		tblclmnCant.setText("Cant.");
		
		TableColumn tblclmnDescripcin = new TableColumn(table, SWT.NONE);
		tblclmnDescripcin.setWidth(167);
		tblclmnDescripcin.setText("Descripción");
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);
		new Label(shell, SWT.NONE);

	}
	
	
	public void testHibernate() {
		Session ss=HibernateUtil.getEditorSession("JA");  
		ss.beginTransaction();  

		Invoice dh = (Invoice) ss.load(Invoice.class, new Long(201113491293L));
		System.out.println("C: " + dh.getTicket());
		
		ss.getTransaction().commit();  
//		ss.close();
		HibernateUtil.closeEditorSession("JA");
		HibernateUtil.verSesiones();
	}
	
	
	public void testMessage() {
		System.out.println("JA!");
	}
	
	
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
}
