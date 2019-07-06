package org.fl.collectionAlbum.rapportHtml;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BalisesTest {

	@Test
	void test() {
		
		Balises alphaBalises = new Balises() ;
		
		StringBuilder fragment = new StringBuilder() ;
		
		alphaBalises.addCheckBaliseString(fragment, "Animals") ;
		
		int fLength1 = fragment.length() ;
		assertNotEquals(0 , fragment.length()) ;
		
		alphaBalises.addCheckBaliseString(fragment, "AC/DC") ;
		int fLength2 = fragment.length() ;
		
		assertEquals(fLength1, fLength2) ;
		
		alphaBalises.addCheckBaliseString(fragment, "Beck") ;
		int fLength3 = fragment.length() ;
		
		assertNotEquals(fLength3, fLength2) ;
	}

}
