package org.fl.collectionAlbum.rapportHtml;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Format;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class BalisesTest {

	private final static Logger logger = Logger.getLogger(BalisesTest.class.getName()) ;
	
	@Test
	void testAlpha() {
		
		Balises alphaBalises = new Balises(Balises.BalisesType.ALPHA) ;
		
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

	@Test
	void testFormat() {
		
		String formatStr1 = "{\"cd\": 3 }" ;
		JsonObject jf1 = new JsonParser().parse(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1, logger) ;
		
		Balises formatBalises = new Balises(Balises.BalisesType.POIDS) ;
		

		StringBuilder fragment = new StringBuilder() ;
		
		formatBalises.addCheckBalisePoids(fragment, format1);
		
		int fLength1 = fragment.length() ;
		assertNotEquals(0 , fragment.length()) ;
		
		String formatStr2 = "{\"33t\": 3 }" ;
		JsonObject jf2 = new JsonParser().parse(formatStr2).getAsJsonObject();
		Format format2 = new Format(jf2, logger) ;
		
		formatBalises.addCheckBalisePoids(fragment, format2);
		
		int fLength2 = fragment.length() ;
		assertEquals(fLength1, fLength2) ;
		
		String formatStr3 = "{\"33t\": 4 }" ;
		JsonObject jf3 = new JsonParser().parse(formatStr3).getAsJsonObject();
		Format format3 = new Format(jf3, logger) ;
		
		formatBalises.addCheckBalisePoids(fragment, format3);
		
		int fLength3 = fragment.length() ;
		assertNotEquals(fLength2, fLength3) ;
	}
	
	private final static String datePatternParse  = "uuuu[-MM[-dd]]" ;
   	private final static DateTimeFormatter dateTimeParser    = DateTimeFormatter.ofPattern(datePatternParse,  Locale.FRANCE).withResolverStyle(ResolverStyle.STRICT) ;

   	private static TemporalAccessor getTemporal(String date) {
   		return dateTimeParser.parseBest(date, LocalDate::from, YearMonth::from, Year::from) ;
   	}
	@Test
	void testTemporalAccessor() {
		
		Balises temporalBalises = new Balises(Balises.BalisesType.TEMPORAL) ;
		
		StringBuilder fragment = new StringBuilder() ;
		
		TemporalAccessor tempAcc1 = getTemporal("1966-02-15") ;
		
		temporalBalises.addCheckBaliseTemporal(fragment, tempAcc1);
		
		int fLength1 = fragment.length() ;
		assertNotEquals(0 , fLength1) ;
		
		TemporalAccessor tempAcc2 = getTemporal("1966-02-15") ;
		
		temporalBalises.addCheckBaliseTemporal(fragment, tempAcc2);
		
		int fLength2 = fragment.length() ;
		assertEquals(fLength1 , fLength2) ;
		
		TemporalAccessor tempAcc3 = getTemporal("1967-02-15") ;
		
		temporalBalises.addCheckBaliseTemporal(fragment, tempAcc3);
		
		int fLength3 = fragment.length() ;
		assertNotEquals(fLength2 , fLength3) ;
	}
}
