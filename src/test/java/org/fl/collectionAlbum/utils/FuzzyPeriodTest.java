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

import static org.assertj.core.api.Assertions.*;

import java.time.temporal.TemporalAccessor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.junit.jupiter.api.Test;

class FuzzyPeriodTest {

	@Test
	void test1() {
		
		String d1 = "2019-06-16";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		FuzzyPeriod fp = new FuzzyPeriod(ta1, ta1);
		
		assertThat(fp.isValid()).isTrue();
		assertThat(fp.getDebut()).isEqualTo(fp.getFin()).isEqualTo(ta1);
	}
	
	@Test
	void test2() {
		
		String d1 = "2019-06-16";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		String d2 = "2019-06-17";
		TemporalAccessor ta2 = TemporalUtils.parseDate(d2);
		
		FuzzyPeriod fp = new FuzzyPeriod(ta1, ta2);
		
		assertThat(fp.isValid()).isTrue();
		assertThat(fp.getDebut()).isEqualTo(ta1);
		assertThat(fp.getFin()).isEqualTo(ta2);
	}
	
	@Test
	void invalidFuzzyPeriod() {
		
		LogRecordCounter fuzzyPeriodFilterCounter = FilterCounter.getLogRecordCounter(Logger.getLogger("org.fl.collectionAlbum.utils.FuzzyPeriod"));		
		
		String d1 = "2019-06-16";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		String d2 = "2019-06-17";
		TemporalAccessor ta2 = TemporalUtils.parseDate(d2);
		
		FuzzyPeriod fp = new FuzzyPeriod(ta2, ta1);
		
		assertThat(fp.isValid()).isFalse();
		assertThat(fp.getDebut()).isEqualTo(ta2);
		assertThat(fp.getFin()).isEqualTo(ta1);
		
		assertThat(fuzzyPeriodFilterCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(fuzzyPeriodFilterCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
	}
	
	@Test
	void test3() {
		
		String d1 = "2019";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		String d2 = "2019-06-17";
		TemporalAccessor ta2 = TemporalUtils.parseDate(d2);
		
		FuzzyPeriod fp = new FuzzyPeriod(ta1, ta2);
		
		assertThat(fp.isValid()).isTrue();
		assertThat(fp.getDebut()).isEqualTo(ta1);
		assertThat(fp.getFin()).isEqualTo(ta2);
	}
	
	@Test
	void test4() {
		
		String d1 = "2019-06";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		String d2 = "2019-06-17";
		TemporalAccessor ta2 = TemporalUtils.parseDate(d2);
		
		FuzzyPeriod fp = new FuzzyPeriod(ta1, ta2);
		
		assertThat(fp.isValid()).isTrue();
		assertThat(fp.getDebut()).isEqualTo(ta1);
		assertThat(fp.getFin()).isEqualTo(ta2);
	}
	
	@Test
	void test5() {
		
		String d1 = "2019";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		
		FuzzyPeriod fp = new FuzzyPeriod(ta1, ta1);
		
		assertThat(fp.isValid()).isTrue();
		assertThat(fp.getDebut()).isEqualTo(fp.getFin()).isEqualTo(ta1);
	}
}
