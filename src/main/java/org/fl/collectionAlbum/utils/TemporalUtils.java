package org.fl.collectionAlbum.utils;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class TemporalUtils {

   	// Pattern pour les dates (parse et format)
   	private final static String datePatternParse  = "uuuu[-MM[-dd]]" ;  	
   	private final static String datePatternFormat = "[[dd ]MMMM] uuuu" ;
   	private final static String yearPatternFormat = "uuuu" ;
   	
   	private final static DateTimeFormatter dateTimeParser    = DateTimeFormatter.ofPattern(datePatternParse,  Locale.FRANCE).withResolverStyle(ResolverStyle.STRICT) ;
   	private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePatternFormat, Locale.FRANCE).withResolverStyle(ResolverStyle.STRICT) ;
   	private final static DateTimeFormatter yearTimeFormatter = DateTimeFormatter.ofPattern(yearPatternFormat, Locale.FRANCE).withResolverStyle(ResolverStyle.STRICT) ;

	// Parse a date and try to return a LocalDate first, if not possible a YearMonth, if not possible a Year
    public static TemporalAccessor parseDate(String d) {
    	
    	if ((d == null) || (d.isEmpty())) {
    		return null ;
    	} else {
    		return dateTimeParser.parseBest(d, LocalDate::from, YearMonth::from, Year::from) ;
    	}
    }
    
    public static String formatDate(TemporalAccessor d) {
    	
       	if (d == null) {
    		return "" ;
       	} else {
    		return dateTimeFormatter.format(d) ;
    	}	
    }
    
   public static String formatYear(TemporalAccessor d) {
    	
       	if (d == null) {
    		return "" ;
       	} else {
    		return yearTimeFormatter.format(d) ;
    	}	
    }  
}
