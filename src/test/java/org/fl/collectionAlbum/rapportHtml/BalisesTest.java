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

package org.fl.collectionAlbum.rapportHtml;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.fl.collectionAlbum.format.Format;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class BalisesTest {
	
	@Test
	void testAlpha() {

		Balises alphaBalises = new Balises(Balises.BalisesType.ALPHA);

		StringBuilder fragment = new StringBuilder();

		alphaBalises.addCheckBaliseString(fragment, "Animals");

		int fLength1 = fragment.length();
		assertThat(fragment).isNotEmpty();

		alphaBalises.addCheckBaliseString(fragment, "AC/DC");
		int fLength2 = fragment.length();

		assertThat(fLength1).isEqualTo(fLength2);

		alphaBalises.addCheckBaliseString(fragment, "Beck");
		int fLength3 = fragment.length();

		assertThat(fLength3).isNotEqualTo(fLength2);
	}

	@Test
	void testFormat() {

		String formatStr1 = "{\"cd\": 3 }";
		JsonObject jf1 = JsonParser.parseString(formatStr1).getAsJsonObject();
		Format format1 = new Format(jf1);

		Balises formatBalises = new Balises(Balises.BalisesType.POIDS);

		StringBuilder fragment = new StringBuilder();

		formatBalises.addCheckBalisePoids(fragment, format1);

		int fLength1 = fragment.length();
		assertThat(fragment).isNotEmpty();

		String formatStr2 = "{\"33t\": 3 }";
		JsonObject jf2 = JsonParser.parseString(formatStr2).getAsJsonObject();
		Format format2 = new Format(jf2);

		formatBalises.addCheckBalisePoids(fragment, format2);

		int fLength2 = fragment.length();
		assertThat(fLength1).isEqualTo(fLength2);

		String formatStr3 = "{\"33t\": 4 }";
		JsonObject jf3 = JsonParser.parseString(formatStr3).getAsJsonObject();
		Format format3 = new Format(jf3);

		formatBalises.addCheckBalisePoids(fragment, format3);

		int fLength3 = fragment.length();
		assertThat(fLength3).isNotEqualTo(fLength2);
	}
	
	private final static String datePatternParse  = "uuuu[-MM[-dd]]";
   	private final static DateTimeFormatter dateTimeParser = 
   			DateTimeFormatter
   				.ofPattern(datePatternParse,  Locale.FRANCE)
   				.withResolverStyle(ResolverStyle.STRICT);

   	private static TemporalAccessor getTemporal(String date) {
   		return dateTimeParser.parseBest(date, LocalDate::from, YearMonth::from, Year::from);
   	}
   	
	@Test
	void testTemporalAccessor() {

		Balises temporalBalises = new Balises(Balises.BalisesType.TEMPORAL);

		StringBuilder fragment = new StringBuilder();

		TemporalAccessor tempAcc1 = getTemporal("1966-02-15");

		temporalBalises.addCheckBaliseTemporal(fragment, tempAcc1);

		int fLength1 = fragment.length();
		assertThat(fragment).isNotEmpty();

		TemporalAccessor tempAcc2 = getTemporal("1966-02-15");

		temporalBalises.addCheckBaliseTemporal(fragment, tempAcc2);

		int fLength2 = fragment.length();
		assertThat(fLength1).isEqualTo(fLength2);

		TemporalAccessor tempAcc3 = getTemporal("1967-02-15");

		temporalBalises.addCheckBaliseTemporal(fragment, tempAcc3);

		int fLength3 = fragment.length();
		assertThat(fLength3).isNotEqualTo(fLength2);
	}
}
