package org.fl.collectionAlbum.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;

import org.junit.jupiter.api.Test;

class TemporalUtilsTest {

	@Test
	void test() {
		
		String d1 = "1962-09-03" ;
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1) ;
		
		assertEquals(3, ta1.get(ChronoField.DAY_OF_MONTH)) ;
		assertEquals(9, ta1.get(ChronoField.MONTH_OF_YEAR)) ;
		assertEquals(1962, ta1.get(ChronoField.YEAR)) ;
	}

	@Test
	void test2() {
		
		String d1 = "2019-06-16" ;
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1) ;
		
		assertEquals(16, ta1.get(ChronoField.DAY_OF_MONTH)) ;
		assertEquals(6, ta1.get(ChronoField.MONTH_OF_YEAR)) ;
		assertEquals(2019, ta1.get(ChronoField.YEAR)) ;
		
		String dRes1 = TemporalUtils.formatDate(ta1) ;
		assertEquals("16 juin 2019", dRes1) ;
	}
	
	@Test
	void test3() {
		
		try {
			TemporalUtils.parseDate("2019-00-16") ;
			assertTrue(false) ;
		} catch (DateTimeParseException e) {
			assertTrue(true) ;
		}
	}
	
	@Test
	void test4() {
		
		String d1 = "2016-01" ;
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1) ;
		
		try {
			assertEquals(1, ta1.get(ChronoField.DAY_OF_MONTH)) ;
			assertTrue(false) ;
		} catch (UnsupportedTemporalTypeException e) {
			assertTrue(true) ;
		}
		assertEquals(1, ta1.get(ChronoField.MONTH_OF_YEAR)) ;
		assertEquals(2016, ta1.get(ChronoField.YEAR)) ;
		
		String dRes1 = TemporalUtils.formatDate(ta1) ;
		assertEquals("janvier 2016", dRes1) ;
	}
}
