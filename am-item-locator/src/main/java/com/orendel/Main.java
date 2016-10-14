package com.orendel;

import com.orendel.am.db.services.HibernateUtilDelivery;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("JA!");
		
		AppConfig.INSTANCE.initializeProperties("am.properties");
		
		HibernateUtilDelivery.INSTANCE.verSesiones();
		
		System.out.println("JA2!");

	}

}
