package org.fl.collectionAlbum.stat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

class StatChronoTest {

	private final static Logger logger = Logger.getLogger(StatChronoTest.class.getName()) ;
			
	@Test
	void test() {
		
		StatChrono sc1 = new StatChrono(logger) ;
		
		assertEquals("0", sc1.getStatForYears(0, false)) ;
		assertEquals("0", sc1.getStatForYears(1969, false)) ;
		assertEquals("0", sc1.getStatForYears(1969, true)) ;	

	}

}
