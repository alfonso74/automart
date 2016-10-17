package com.orendel.common.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.orendel.common.exceptions.ApplicationRuntimeException;


public enum ImagesService {
	INSTANCE;
	
	private static final Logger logger = Logger.getLogger(ImagesService.class);
	private final Map<String, Image> imageStream = new HashMap<String, Image>();
	
	private static final String UNKNOWN_IMAGE = "icons/unknown.gif";
	
	private ImagesService() {
	}
	
	public Image getImage(Display display, String imageResourcePath) {
		Image image = null;
		// buscamos la imagen en el caché (HashMap)
		image = imageStream.get(imageResourcePath);
		// si no está en caché, o la encontramos pero en estado "DISPOSED", tratamos de instanciar la imagen
		if (image == null || image.isDisposed()) {
			image = loadImage(display, imageResourcePath);
			// si no logramos instanciar la imagen, se asigna la imagen "no definida", en caso contrario,
			// cargamos la imagen al caché y es retornada por el método
			if (image == null) {
				image = loadImage(display, UNKNOWN_IMAGE);
			} else {
				imageStream.put(imageResourcePath, image);
			}	
		}
		return image;
	}
	
	
	private Image loadImage(Display display, String string) {
		Image image = null;
		InputStream stream = getClass().getClassLoader().getResourceAsStream (string);
		if (stream == null) {
			logger.warn("Image for resource path '" + string + "' was not found!!, using default image (" + UNKNOWN_IMAGE + ").");
			if (UNKNOWN_IMAGE.equalsIgnoreCase(string)) {
				throw new ApplicationRuntimeException("Image for UNKNOWN images (" + UNKNOWN_IMAGE + ") was not found!!");
			}
		} else {
			try {
				image = new Image (display, stream);
			} catch (SWTException ex) {
			} finally {
				try {
					stream.close ();
				} catch (IOException ex) {}
			}
		}
		return image;
	}

	public int getImagesCount() {
		return imageStream.size();
	}

	public void disposeImages() {
		logger.info("Disposing " + getImagesCount() + " images...");
		logger.info(imageStream);
		for (Map.Entry<String, Image> entry : imageStream.entrySet()) {
			Image image = entry.getValue();
			if (image != null) {
				image.dispose();
			}
		}
		imageStream.clear();
	}
}
