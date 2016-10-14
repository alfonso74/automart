package com.orendel.counterpoint.domain;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.orendel.counterpoint.domain.Item;

public class ItemTest {
	
	
	@Test
	public void testGetUpdatedDateLabel_Happy() {
		Calendar calendar = new GregorianCalendar();
		calendar.clear();
		calendar.set(2014, Calendar.MAY, 22);
		Item item = new Item();
		String labelForPrinter = item.getDateFormattedForLabel(calendar.getTime());
		assertTrue(labelForPrinter.equalsIgnoreCase("A14H05T22"));		
	}

}
