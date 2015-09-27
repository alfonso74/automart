package com.orendel.transfer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import com.orendel.transfer.exceptions.ApplicationPropertiesException;


public enum AppConfig implements IAppConfig {
	INSTANCE;
	private final Properties properties = new Properties();
	private String filePath = "";
	
	
	//private Configuration() {};
	
	/* (non-Javadoc)
	 * @see com.smartmatic.sims.tc.config.IConfiguration#initializeProps(java.lang.String)
	 */
	@Override
	public void initializeProperties(String filePath) {
		try {
			this.filePath = filePath;
			properties.load(new FileInputStream(filePath));
		} catch (IOException e) {
			throw new ApplicationPropertiesException("Error al inicializar el archivo de propiedades: " + filePath, e);
		}
	}
	
	@SuppressWarnings("unchecked")
    private static <T> T transform(String value, Class<T> classType) {
		T result = null;
		if (value != null) {
			try {
				if (classType == Boolean.class) {
					result = ((Class<T>) Boolean.class).cast(Boolean.valueOf(value));
				} else if (classType == Byte.class) {
					result = ((Class<T>) Byte.class).cast(Byte.valueOf(value));
				} else if (classType == Character.class) {
					result = ((Class<T>) Character.class).cast(Character.valueOf(value.charAt(0)));
				} else if (classType == Double.class) {
					result = ((Class<T>) Double.class).cast(Double.valueOf(value));
				} else if (classType == Float.class) {
					result = ((Class<T>) Float.class).cast(Float.valueOf(value));
				} else if (classType == Integer.class) {
					result = ((Class<T>) Integer.class).cast(Integer.valueOf(value));
				} else if (classType == Long.class) {
					result = ((Class<T>) Long.class).cast(Long.valueOf(value));
				} else if (classType == Short.class) {
					result = ((Class<T>) Short.class).cast(Short.valueOf(value));
				} else { // hard cast:
					result = (T) classType.cast(value);
				}
			} catch (Exception ex) {
				throw new ApplicationPropertiesException("Error en transformación de tipo de datos para: " + value);
			}
		}
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see com.smartmatic.sims.tc.config.IConfiguration#getValue(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
    public <T> T getValue(String propertyName) {
		String result = properties.getProperty(propertyName);
		if (result == null) {
			throw new ApplicationPropertiesException("Propiedad no encontrada: '" + propertyName + "'");
		}
		return (T) result;
	}
	
	
	/* (non-Javadoc)
	 * @see com.smartmatic.sims.tc.config.IConfiguration#getProperties()
	 */
	@Override
	public Properties getProperties() {
		return properties;
	}
	
	
	/* (non-Javadoc)
	 * @see com.smartmatic.sims.tc.config.IConfiguration#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<String, String> properties) {
		this.properties.clear();
		this.properties.putAll(properties);
	}
	
	
	/* (non-Javadoc)
	 * @see com.smartmatic.sims.tc.config.IConfiguration#updateProperties(java.util.Map)
	 */
	@Override
	public void updateProperties(Map<String, String> properties) {
		this.properties.putAll(properties);
	}
	
	
	/* (non-Javadoc)
	 * @see com.smartmatic.sims.tc.config.IConfiguration#store()
	 */
	@Override
	public void store() {
		File f = new File(filePath);
		OutputStream out;
		try {
			out = new FileOutputStream( f );
			properties.store(out, "TransactionController Properties");
			out.close();
		} catch (FileNotFoundException e) {
			throw new ApplicationPropertiesException("Error al leer el archivo de configuración: " + filePath, e);
		} catch (IOException e) {
			throw new ApplicationPropertiesException("Error en archivo de propiedades: " + filePath, e);
		}
	}
	

}
