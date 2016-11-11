package com.orendel.locator.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hibernate.HibernateException;

import com.orendel.common.config.AppConfig;
import com.orendel.common.services.ImagesService;
import com.orendel.locator.editors.ViewItemLocationEditor;
import com.orendel.locator.services.HibernateUtil;
import com.orendel.locator.services.HibernateUtilDelivery;
import com.orendel.locator.services.ShellImagesService;
import com.orendel.locator.util.MessagesUtil;


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
			
			window.open();
			
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
		shell.setMaximized(true);
		shell.setText("AutoMart - Localizador - v1.0.9");
		shell.setLayout(new GridLayout(1, false));
		
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
		
		fillFooterInfo();
		
		openItemLocatorEditor(composite);
	}
	
	
	private void fillFooterInfo() {
		txtUser.setText("No user");
		txtDatetime.setText(getDateAsString(new Date()));
	}
	
	private ViewItemLocationEditor openItemLocatorEditor(Composite composite) {
		ViewItemLocationEditor editor = new ViewItemLocationEditor(composite, SWT.NONE);
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
