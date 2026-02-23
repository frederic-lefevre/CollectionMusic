/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import org.fl.collectionAlbum.utils.TemporalUtils;

class StatistiquesViewTest {

	@Test
	void testEmptyStatChrono() {
		
		StatistiquesView statistiquesView = new StatistiquesView(new StatChrono(), 100);
		
		assertThat(statistiquesView).isNotNull();
		assertThat(statistiquesView.getPas()).isEqualTo(1);
		assertThat(statistiquesView.getStatisquesMap()).isEmpty();
		assertThat(statistiquesView.getStatFor(2001)).isEqualTo("0");
	}
	
	@Test
	void testStatChronoAn() {
		
		StatChrono sc1 = new StatChrono();

		sc1.addAlbum(TemporalUtils.parseDate("1969-09-03"), 1.5);
		sc1.addAlbum(TemporalUtils.parseDate("1960-01-01"), 1);
		sc1.addAlbum(TemporalUtils.parseDate("1969-12-31"), 2);
		sc1.addAlbum(TemporalUtils.parseDate("1970-01-01"), 5);
		
		StatistiquesView statistiquesView = new StatistiquesView(sc1, 100);
		assertThat(statistiquesView.getPas()).isEqualTo(1);
		assertThat(statistiquesView.getStatisquesMap()).isNotEmpty().hasSize(2)
			.containsEntry(1960, 4.5)
			.containsEntry(1970, 5.0);
		assertThat(statistiquesView.getStatFor(1969)).isEqualTo("3.5");
		assertThat(statistiquesView.getStatFor(1960)).isEqualTo("1");
		assertThat(statistiquesView.getStatFor(1970)).isEqualTo("5");
		assertThat(statistiquesView.getStatFor(2001)).isEqualTo("0");
	}
	
	@Test
	void testStatChronoDecennie() {
		
		StatChrono sc1 = new StatChrono();

		sc1.addAlbum(TemporalUtils.parseDate("1769-09-03"), 1.5);
		sc1.addAlbum(TemporalUtils.parseDate("1560-01-01"), 1);
		sc1.addAlbum(TemporalUtils.parseDate("1955-12-31"), 1);
		sc1.addAlbum(TemporalUtils.parseDate("1969-12-31"), 2);
		sc1.addAlbum(TemporalUtils.parseDate("1968-01-01"), 5);
		
		StatistiquesView statistiquesView = new StatistiquesView(sc1, 100);
		assertThat(statistiquesView.getPas()).isEqualTo(10);
		assertThat(statistiquesView.getStatisquesMap()).isNotEmpty().hasSize(3)
			.containsEntry(1500, 1.0)
			.containsEntry(1700, 1.5)
			.containsEntry(1900, 8.0);
		assertThat(statistiquesView.getStatFor(1760)).isEqualTo("1.5");
		assertThat(statistiquesView.getStatFor(1765)).isEqualTo("1.5");
		assertThat(statistiquesView.getStatFor(1560)).isEqualTo("1");
		assertThat(statistiquesView.getStatFor(1950)).isEqualTo("1");
		assertThat(statistiquesView.getStatFor(1960)).isEqualTo("7");
		assertThat(statistiquesView.getStatFor(1970)).isEqualTo("0");
		assertThat(statistiquesView.getStatFor(2001)).isEqualTo("0");
	}
}
