package com.orendel.transfer.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.orendel.transfer.exceptions.ApplicationRuntimeException;

public enum ImagesService {
	INSTANCE;
	
	private static final Logger logger = Logger.getLogger(ImagesService.class);
	private final Map<String, Image> imageStream = new HashMap<String, Image>();
	
	private ImagesService() {
	}
	
	public Image getImage(Display display, String imageResourcePath) {
		Image image = null;
		// buscamos la imagen en el caché (HashMap)
		image = imageStream.get(imageResourcePath);
		// si no está en caché, tratamos de instanciar la imagen
		if (image == null) {
			image = loadImage(display, imageResourcePath);
			// si no logramos instanciar la imagen, se asigna la imagen "no definida", en caso contrario,
			// cargamos la imagen al caché y es retornada por el método
			if (image == null) {
				image = loadImage(display, IImageKeys.UNKNOWN);
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
			logger.warn("Image for resource path '" + string + "' was not found!!, using default image (" + IImageKeys.UNKNOWN + ").");
			if (IImageKeys.UNKNOWN.equalsIgnoreCase(string)) {
				throw new ApplicationRuntimeException("Image for UNKNOWN images (" + IImageKeys.UNKNOWN + ") was not found!!");
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
