package com.orendel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.orendel.seam.domain.Country;
import com.orendel.seam.domain.counterpoint.Invoice;


public class HelloWorldSWT {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Hello world!");
		hibernate();
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}


	private static void hibernate() {
		try{
			Configuration configuration=new Configuration();  
			configuration.configure();  
//			ServiceRegistry sr= new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			StandardServiceRegistryImpl sr = (StandardServiceRegistryImpl) new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			SessionFactory sf=configuration.buildSessionFactory(sr);  
			
			sf.close();
			sf = null;
			configuration = null;
			sr.destroy();
			sr = null;

//			Session ss=sf.openSession();  
//			ss.beginTransaction();  
//
//			DocHeader dh = (DocHeader) ss.load(DocHeader.class, new Long(201113491293L));
//			System.out.println("C: " + dh.getTicket());
//			
//			ss.getTransaction().commit();  
//			ss.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
