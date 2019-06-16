package org.fl.collectionAlbum.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

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

}
