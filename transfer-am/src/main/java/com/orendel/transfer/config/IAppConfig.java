package com.orendel.transfer.config;

import java.util.Map;
import java.util.Properties;


public interface IAppConfig {

	public abstract void initializeProperties(String filePath);

	/**
	 * Busca y retorna la propiedad indicada en el archivo de propiedades de la aplicación.
	 * @param propertyName nombre de la propiedad a buscar
	 * @return la propiedad solicidada, con el tipo indicado en el llamado, o un
	 * ApplicationPropertiesException si no se encuentra la propiedad indicada.
	 */
	public abstract <T> T getValue(String propertyName);

	public abstract Properties getProperties();
	
	public abstract void setProperties(Map<String, String> properties);

	public abstract void updateProperties(Map<String, String> properties);

	public abstract void store();

}