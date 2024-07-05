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

package org.fl.collectionAlbum.stat;

import static org.assertj.core.api.Assertions.*;

import java.time.temporal.TemporalAccessor;

import org.fl.collectionAlbum.utils.TemporalUtils;
import org.junit.jupiter.api.Test;

class StatChronoTest {
			
	@Test
	void test() {
		
		StatChrono sc1 = new StatChrono();
		
		assertThat(sc1.getStatForYear(0)).isEqualTo("0");
		assertThat(sc1.getStatForYear(1969)).isEqualTo("0");
		assertThat(sc1.getStatForDecennie(1969)).isEqualTo("0");	

		assertThat(sc1.getStatAnnuelle()).isNotNull().isEmpty();
		assertThat(sc1.getStatDecennale()).isNotNull().isEmpty();
		assertThat(sc1.getStatSiecle()).isNotNull().isEmpty();
		
		String d1 = "1969-09-03";
		TemporalAccessor ta1 = TemporalUtils.parseDate(d1);
		String d2 = "1960-01-01";
		TemporalAccessor ta2 = TemporalUtils.parseDate(d2);
		String d3 = "1969-12-31";
		TemporalAccessor ta3 = TemporalUtils.parseDate(d3);
		String d4 = "1970-01-01";
		TemporalAccessor ta4 = TemporalUtils.parseDate(d4);

		sc1.AddAlbum(ta1, 1.5);
		sc1.AddAlbum(ta2, 1);
		sc1.AddAlbum(ta3, 2);
		sc1.AddAlbum(ta4, 5);
		assertThat(sc1.getStatForYear(1969)).isEqualTo("3.5");
		assertThat(sc1.getStatForYear(1960)).isEqualTo("1");
		assertThat(sc1.getStatForDecennie(1960)).isEqualTo("4.5");
		assertThat(sc1.getStatForDecennie(1965)).isEqualTo("4.5");
		assertThat(sc1.getStatForYear(1970)).isEqualTo("5");
		assertThat(sc1.getStatForDecennie(1970)).isEqualTo("5");
	}

}
