/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.collectionAlbum.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemporalUtils {

   	// Pattern pour les dates (parse et format)
   	private static final String datePatternParse  = "uuuu[-MM[-dd]]";  	
   	private static final String datePatternFormat = "[[dd ]MMMM] uuuu";
   	private static final String yearPatternFormat = "uuuu";
   	
   	private static final DateTimeFormatter dateTimeParser    = DateTimeFormatter.ofPattern(datePatternParse,  Locale.FRANCE).withResolverStyle(ResolverStyle.STRICT);
   	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePatternFormat, Locale.FRANCE).withResolverStyle(ResolverStyle.STRICT);
   	private static final DateTimeFormatter yearTimeFormatter = DateTimeFormatter.ofPattern(yearPatternFormat, Locale.FRANCE).withResolverStyle(ResolverStyle.STRICT);

	// Parse a date and try to return a LocalDate first, if not possible a YearMonth, if not possible a Year
    public static TemporalAccessor parseDate(String d) {
    	
    	if ((d == null) || (d.isEmpty())) {
    		return null;
    	} else {
    		return dateTimeParser.parseBest(d, LocalDate::from, YearMonth::from, Year::from);
    	}
    }
    
    public static String formatDate(TemporalAccessor d) {
    	
       	if (d == null) {
    		return "";
       	} else {
    		return dateTimeFormatter.format(d);
    	}	
    }
    
   public static String formatYear(TemporalAccessor d) {
    	
       	if (d == null) {
    		return "";
       	} else {
    		return yearTimeFormatter.format(d);
    	}	
    }
   
	// (Try to) compare 2 TemporalAccessors
	public static int compareTemporal(TemporalAccessor t1, TemporalAccessor t2, Logger log) {
		
		if (t1 == null) {
			if (t2 == null) return 0 ;
			return 1 ;
		} else if (t2 == null) {
			return -1 ;
		} else {
			try {
				
				LocalDateTime d1 = getRoundedLocalDateTime(t1);
				LocalDateTime d2 = getRoundedLocalDateTime(t2);
				
				if ((d1 != null) && (d2 != null)) {
					return d1.compareTo(d2) ;
				} else {
					log.severe("Cannot convert to LocalDate comparing 2 TemporalAccessor ");
					return 0 ;
				}
				
			} catch (Exception e) {
				log.log(Level.SEVERE, "Exception comparing 2 TemporalAccessor ", e);
				return 0;
			}
		}
	}
	
	private static LocalDateTime getRoundedLocalDateTime(TemporalAccessor t) {
		
		if (t instanceof LocalDateTime) {
			return (LocalDateTime)t;
		} else if (t instanceof LocalDate) {
			return ((LocalDate)t).atStartOfDay();
		} else if (t instanceof YearMonth) {
			return ((YearMonth)t).atDay(1).atStartOfDay();
		} else if (t instanceof Year) {
			return ((Year)t).atDay(1).atStartOfDay();
		} else {
			// try to get (some) TemporalField manually 
						
			if (t.isSupported(ChronoField.YEAR)) {
				
				int year =  t.get(ChronoField.YEAR);
				
				int month 		 = getRoundedField(t, ChronoField.MONTH_OF_YEAR);
				int day 		 = getRoundedField(t, ChronoField.DAY_OF_MONTH);
				int hour 		 = getRoundedField(t, ChronoField.HOUR_OF_DAY);
				int minute 		 = getRoundedField(t, ChronoField.MINUTE_OF_HOUR);
				int second 		 = getRoundedField(t, ChronoField.SECOND_OF_MINUTE);
				int nanoOfsecond = getRoundedField(t, ChronoField.NANO_OF_SECOND);
				
				return LocalDateTime.of(year, month, day, hour, minute,second, nanoOfsecond);
				
			} else {
				return null;
			}
		}

	}
   
   private static int getRoundedField(TemporalAccessor t, ChronoField c) {
   	
   	if (t.isSupported(c)) {
   		return t.get(c);
   	} else {
   		return (int)c.range().getMinimum();
   	}
   }
}
