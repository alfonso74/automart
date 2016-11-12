package com.orendel.transfer.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.orendel.common.services.ImagesService;

public class ShellImagesService {
	
	private Image[] shellImages = new Image[4];
	

	public ShellImagesService(Display display) {
		shellImages = new Image[4];
		shellImages[0] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_16);
		shellImages[1] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_24);
		shellImages[2] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_32);
		shellImages[3] = ImagesService.INSTANCE.getImage(display, IImageKeys.SHELL_48);
	}
	
	public Image[] getShellImages() {
		return shellImages;
	}

}
