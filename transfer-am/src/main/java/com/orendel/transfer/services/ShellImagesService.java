package com.orendel.transfer.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ShellImagesService {
	
//	private Display display;
	private Image[] shellImages = new Image[3];
	

	public ShellImagesService(Display display) {
//		this.display = display;
		shellImages = new Image[3];
		shellImages[0] = ImagesService.INSTANCE.getImage(display, IImageKeys.PDF_16);
		shellImages[1] = ImagesService.INSTANCE.getImage(display, IImageKeys.PDF_24);
		shellImages[2] = ImagesService.INSTANCE.getImage(display, IImageKeys.PDF_32);
	}
	
	public Image[] getShellImages() {
		return shellImages;
	}
	
//	public Image[] getShellImages() {
//		Image[] shellImages = new Image[3];
//		shellImages[0] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/pdf_16.png"));
//		shellImages[1] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/pdf_24.png"));
//		shellImages[2] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/pdf_32.png"));
//		return shellImages;
//	}
	
	
//	public Image[] getShellImagesBMP() {
//		Image[] shellImages = new Image[3];
//		shellImages[0] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/assetsTrust_16_32.bmp"));
//		shellImages[1] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/assetsTrust_32_32.bmp"));
//		shellImages[2] = new Image(display, getClass().getClassLoader().getResourceAsStream("icons/assetsTrust_48_32.bmp"));
//		return shellImages;
//	}

}
