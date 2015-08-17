package com.orendel.transfer.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ShellImagesService {
	
	private Image[] shellImages = new Image[3];
	

	public ShellImagesService(Display display) {
		shellImages = new Image[3];
		shellImages[0] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_16);
		shellImages[1] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_24);
		shellImages[2] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_32);
	}
	
	public Image[] getShellImages() {
		return shellImages;
	}

}
