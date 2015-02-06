/*
 * TCAppConfig.java
 *
 * 1.0 06/13/2012
 *
 * Copyright (c) Smartmatic Corp.
 * 1001 Broken Sound Parkway NW, Suite D
 * Boca Raton FL 33487, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and propietary information of
 * Smartmatic Corp. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with Smartmatic Corp.
 */
package com.orendel.seam.config;

import java.io.File;


/**
 * Application configuration constants values.  
 *  
 * @author Ernesto Olivo (ernesto.olivo@smartmatic.com)
 * @version 1.0 (06/13/2012).
 */
public class TCAppConfig {

	public static String LOCALE = "en";
	
	/**
	 * Application common name.
	 */
	public static final String APP_COMMON_NAME = "transaction-controller";
	
	/**
     * Component name used in the ApplicationProperties initialization.
     */
    public static final String PROPERTIES_REFERENCE_NAME = "TC";
	
	/**
	 * 
	 */
	public static final String CONFIG_DIR = System.getProperty("jboss.server.home.dir") + File.separator + "conf"
		    + File.separator + "idms" + File.separator + APP_COMMON_NAME;
	
	/**
	 * 
	 */
	public static final String PROPERTIES_DIR = CONFIG_DIR + File.separator + "props";
	
	/**
	 * 
	 */
	public static final String APP_PROPERTIES_FILE = "idms" + File.separator + APP_COMMON_NAME + 
	        File.separator + "props" + File.separator + "tc.properties";
	
	/**
	 * 
	 */
	public static final String APP_MSG_DIR = CONFIG_DIR + File.separator + "messages";
	
	/**
	 * 
	 */
	public static final String APP_MSG_DEFAULT_BUNDLE = APP_MSG_DIR + File.separator + "event_messages" + File.separator + "application_messages.properties";
	
	/**
     * 
     */
    public static final String APP_CLIENT_RESPONSE_MSG_DIR = APP_MSG_DIR + File.separator + "client_response_messages";
    
    /**
     * 
     */
    public static final String APP_CLIENT_RESPONSE_MSG_DEFAULT_BUNDLE = APP_CLIENT_RESPONSE_MSG_DIR 
    		+ File.separator + "response_messages.properties";
	
	

    public static final String APP_EXCEPTION_MSG_DIR = APP_MSG_DIR + File.separator + "exception_messages";

    public static final String APP_EXCEPTION_MSG_DEFAULT_BUNDLE = APP_MSG_DIR + File.separator + "exception_messages";
    
    
	
/*
    public static final String CORE_MSG_DIR = MSG_DIR + File.separator + "sims-core";

    public static final String CORE_EXCEPTION_MSG_DIR = CORE_MSG_DIR + File.separator + "exception messages";

    public static final String CORE_EXCEPTION_MSG_DEFAULT_BUNDLE = CORE_EXCEPTION_MSG_DIR + File.separator 
    		+ "application_exceptions";
*/
    
    
    public static final String APP_XSD_REQUEST_FILE = "/resources/tc-request-v5.xsd";
    
}
