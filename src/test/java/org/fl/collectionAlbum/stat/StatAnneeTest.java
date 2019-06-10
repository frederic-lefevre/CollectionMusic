package org.fl.collectionAlbum.stat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StatAnneeTest {

	@Test
	void test() {
		
		StatAnnee statAn1 = new StatAnnee(2019, 0) ;
		
		assertEquals(2019, statAn1.getAn()) ;
		assertEquals("", statAn1.getNombre()) ;
		
		statAn1.incrementNombre(1);
		assertEquals(2019, statAn1.getAn()) ;
		assertEquals("1", statAn1.getNombre()) ;

		statAn1.incrementNombre(1.5);
		assertEquals(2019, statAn1.getAn()) ;
		assertEquals("2.5", statAn1.getNombre()) ;

		statAn1.incrementNombre(0.5);
		assertEquals(2019, statAn1.getAn()) ;
		assertEquals("3", statAn1.getNombre()) ;

	}

}
