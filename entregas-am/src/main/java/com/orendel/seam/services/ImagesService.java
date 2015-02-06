package com.orendel.seam.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImagesService {
	
	private Display display;
	

	public ImagesService(Display display) {
		this.display = display;
	}
	
	
	public Image[] getShellImages() {
		Image[] shellImages = new Image[3];
		shellImages[0] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/pdf_16.png"));
		shellImages[1] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/pdf_24.png"));
		shellImages[2] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/pdf_32.png"));
		return shellImages;
	}
	
	
	public Image[] getShellImagesBMP() {
		Image[] shellImages = new Image[3];
		shellImages[0] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/assetsTrust_16_32.bmp"));
		shellImages[1] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/assetsTrust_32_32.bmp"));
		shellImages[2] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/assetsTrust_48_32.bmp"));
		return shellImages;
	}

}
