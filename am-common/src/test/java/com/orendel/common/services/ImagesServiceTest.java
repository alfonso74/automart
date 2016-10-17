package com.orendel.common.services;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ImagesServiceTest {
	
	private static final Logger logger = Logger.getLogger(ImagesServiceTest.class);
	
	private static final String UNKNOWN = "icons/unknown.gif";
	private static final String BAR_CHART_16 = "icons/bar_chart_16.png";
	
	
	@BeforeClass
	public static void init() {
		logger.info("INIT!");
	}

	@Before
	public void setup() {

	}
	
	@Test
	public void testGetImage_Happy() {
		logger.info("--------------  Running: testGetImage_Happy()  -----------------");
		ImagesService.INSTANCE.getImage(null, BAR_CHART_16);
		ImagesService.INSTANCE.getImage(null, UNKNOWN);
		ImagesService.INSTANCE.getImage(null, BAR_CHART_16);
		ImagesService.INSTANCE.getImage(null, BAR_CHART_16);
		Assert.assertEquals(2, ImagesService.INSTANCE.getImagesCount());
	}
	
	@Test
	public void testDisposeImages_OneLoadedImage_Happy() {
		logger.info("--------------  Running: testDisposeImages_OneLoadedImage_Happy()  -----------------");
		ImagesService.INSTANCE.getImage(null, UNKNOWN);
		ImagesService.INSTANCE.getImage(null, BAR_CHART_16);
		ImagesService.INSTANCE.disposeImages();
		Assert.assertEquals(0, ImagesService.INSTANCE.getImagesCount());
	}	
	
	@Test
	public void testDisposeImages_NoLoadedImages() {
		logger.info("--------------  Running: testDisposeImages_NoLoadedImages()  -----------------");
		ImagesService.INSTANCE.disposeImages();
		Assert.assertEquals(0, ImagesService.INSTANCE.getImagesCount());
	}
}
