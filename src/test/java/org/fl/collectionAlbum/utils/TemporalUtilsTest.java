/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

import static org.assertj.core.api.Assertions.*;

import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.UnsupportedTemporalTypeException;

import org.junit.jupiter.api.Test;

class TemporalUtilsTest {

	@Test
	void test() {
		
		String d1 = "1962-09-03";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		assertThat(ta1.get(ChronoField.DAY_OF_MONTH)).isEqualTo(3);
		assertThat(ta1.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(9);
		assertThat(ta1.get(ChronoField.YEAR)).isEqualTo(1962);
	}

	@Test
	void test2() {
		
		String d1 = "2019-06-16";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		assertThat(ta1.get(ChronoField.DAY_OF_MONTH)).isEqualTo(16);
		assertThat(ta1.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(6);
		assertThat(ta1.get(ChronoField.YEAR)).isEqualTo(2019);
		
		String dRes1 = TemporalUtils.formatDate(ta1);
		assertThat(dRes1).isEqualTo("16 juin 2019");
	}
	
	@Test
	void test3() {
		
		assertThatExceptionOfType(DateTimeParseException.class)
			.isThrownBy(() -> TemporalUtils.parseDate("2019-00-16"));
	}
	
	@Test
	void test4() {
		
		String d1 = "2016-01";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		assertThatExceptionOfType(UnsupportedTemporalTypeException.class)
			.isThrownBy(() -> ta1.get(ChronoField.DAY_OF_MONTH));

		assertThat(ta1.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(1);
		assertThat(ta1.get(ChronoField.YEAR)).isEqualTo(2016);
		
		String dRes1 = TemporalUtils.formatDate(ta1);
		assertThat(dRes1).isEqualTo("janvier 2016");
	}
}
