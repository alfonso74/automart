package com.orendel.locator;

import com.orendel.common.config.AppConfig;
import com.orendel.locator.services.HibernateUtilDelivery;



public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("JA!");
		
		AppConfig.INSTANCE.initializeProperties("am.properties");
		
//		HibernateUtilDelivery.INSTANCE.verSesiones();
		
		HibernateUtilDelivery a = new HibernateUtilDelivery();
		a.verSesiones();
		
		System.out.println("JA2!");

	}

}
