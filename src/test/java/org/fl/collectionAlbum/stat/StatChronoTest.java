package org.fl.collectionAlbum.stat;

import static org.junit.jupiter.api.Assertions.*;

import java.time.temporal.TemporalAccessor;
import java.util.logging.Logger;

import org.fl.collectionAlbum.utils.TemporalUtils;
import org.junit.jupiter.api.Test;

class StatChronoTest {

	private final static Logger logger = Logger.getLogger(StatChronoTest.class.getName()) ;
			
	@Test
	void test() {
		
		StatChrono sc1 = new StatChrono(logger) ;
		
		assertEquals("0", sc1.getStatForYears(0, false)) ;
		assertEquals("0", sc1.getStatForYears(1969, false)) ;
		assertEquals("0", sc1.getStatForYears(1969, true)) ;	

		assertEquals(0, sc1.getStatAnnuelle().size()) ;
		assertEquals(0, sc1.getStatDecennale().size()) ;
		assertEquals(0, sc1.getStatSiecle().size()) ;
		
		String d1 = "1969-09-03" ;
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1) ;
		String d2 = "1960-01-01" ;
		TemporalAccessor ta2 = TemporalUtils.parseDate(d2) ;
		String d3 = "1969-12-31" ;
		TemporalAccessor ta3 = TemporalUtils.parseDate(d3) ;
		String d4 = "1970-01-01" ;
		TemporalAccessor ta4 = TemporalUtils.parseDate(d4) ;

		sc1.AddAlbum(ta1, 1.5) ;
		sc1.AddAlbum(ta2, 1) ;
		sc1.AddAlbum(ta3, 2) ;
		sc1.AddAlbum(ta4, 5) ;
		assertEquals("3.5", sc1.getStatForYears(1969, false)) ;
		assertEquals("1",   sc1.getStatForYears(1960, false)) ;
		assertEquals("4.5", sc1.getStatForYears(1960, true)) ;
		assertEquals("5",   sc1.getStatForYears(1970, false)) ;
		assertEquals("5",   sc1.getStatForYears(1970, true)) ;

	}

}
