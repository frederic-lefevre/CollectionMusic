package org.fl.collectionAlbum.rapportHtml;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AlphaBaliseTest {

	@Test
	void test() {
		
		AlphaBalises alphaBalises = new AlphaBalises() ;
		
		StringBuilder fragment = new StringBuilder() ;
		
		alphaBalises.addCheckBalise(fragment, "Animals") ;
		
		int fLength1 = fragment.length() ;
		assertNotEquals(0 , fragment.length()) ;
		
		alphaBalises.addCheckBalise(fragment, "AC/DC") ;
		int fLength2 = fragment.length() ;
		
		assertEquals(fLength1, fLength2) ;
		
		alphaBalises.addCheckBalise(fragment, "Beck") ;
		int fLength3 = fragment.length() ;
		
		assertNotEquals(fLength3, fLength2) ;
	}

}
