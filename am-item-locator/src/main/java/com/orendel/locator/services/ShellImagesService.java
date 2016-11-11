package com.orendel.locator.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.orendel.common.services.ImagesService;

public class ShellImagesService {
	
	private Image[] shellImages = new Image[3];
	

	public ShellImagesService(Display display) {
		shellImages = new Image[5];
		shellImages[0] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_16);
		shellImages[1] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_24);
		shellImages[2] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_32);
		shellImages[3] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_48);
		shellImages[4] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_64);
	}
	
	public Image[] getShellImages() {
		return shellImages;
	}

}
