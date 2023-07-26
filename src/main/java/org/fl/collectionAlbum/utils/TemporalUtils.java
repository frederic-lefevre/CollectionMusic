/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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
