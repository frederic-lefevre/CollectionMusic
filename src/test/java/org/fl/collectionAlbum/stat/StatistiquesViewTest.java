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

import java.util.Optional;
import java.util.function.Function;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.stat.StatistiquesView.Granularite;
import org.fl.collectionAlbum.utils.TemporalUtils;

class StatistiquesViewTest {
	
	private static final Function<Double, String> statToStringFunction = (d) ->  Optional.ofNullable(d).map(poids -> Format.poidsToString(poids)).orElse("0");
	
	@Test
	void testEmptyStatChrono() {
		
		StatistiquesView statistiquesView = new StatistiquesView(new StatChrono(), Granularite.PAR_AN, statToStringFunction);
		
		assertThat(statistiquesView).isNotNull();
		assertThat(statistiquesView.getPas()).isEqualTo(1);
		assertThat(statistiquesView.getStatisquesMap()).isEmpty();
		assertThat(statistiquesView.getStatFor(2001)).isEqualTo("0");
		assertThat(statistiquesView.getAccumulationStatFor(2001)).isEqualTo("0");
		assertThat(statistiquesView.getSubdivisionName()).isEqualTo("Décennie");
		
		assertThatIllegalArgumentException().isThrownBy(() -> statistiquesView.getYearForLine(0));
	}
	
	@Test
	void testStatChronoAn() {
		
		StatChrono sc1 = new StatChrono();

		sc1.addToStatistic(TemporalUtils.parseDate("1969-09-03"), 1.5);
		sc1.addToStatistic(TemporalUtils.parseDate("1960-01-01"), 1);
		sc1.addToStatistic(TemporalUtils.parseDate("1969-12-31"), 2);
		sc1.addToStatistic(TemporalUtils.parseDate("1970-01-01"), 5);
		
		StatistiquesView statistiquesView = new StatistiquesView(sc1, Granularite.PAR_AN, statToStringFunction);
		assertThat(statistiquesView.getPas()).isEqualTo(1);
		assertThat(statistiquesView.getStatisquesMap()).isNotEmpty().hasSize(2)
			.containsEntry(1960, 4.5)
			.containsEntry(1970, 5.0);
		assertThat(statistiquesView.getStatFor(1969)).isEqualTo("3.5");
		assertThat(statistiquesView.getStatFor(1960)).isEqualTo("1");
		assertThat(statistiquesView.getStatFor(1970)).isEqualTo("5");
		assertThat(statistiquesView.getStatFor(2001)).isEqualTo("0");
		assertThat(statistiquesView.getLineNumber()).isEqualTo(2);
		assertThat(statistiquesView.getYearForLine(0)).isEqualTo(1960);
		assertThat(statistiquesView.getYearForLine(1)).isEqualTo(1970);
		assertThatIllegalArgumentException().isThrownBy(() -> statistiquesView.getYearForLine(2));
		assertThatIllegalArgumentException().isThrownBy(() -> statistiquesView.getYearForLine(-1));
		assertThat(statistiquesView.getAccumulationStatFor(2001)).isEqualTo("0");
		assertThat(statistiquesView.getAccumulationStatFor(1969)).isEqualTo("4.5");
		assertThat(statistiquesView.getAccumulationStatFor(1960)).isEqualTo("4.5");
		assertThat(statistiquesView.getAccumulationStatFor(1965)).isEqualTo("4.5");
		assertThat(statistiquesView.getAccumulationStatFor(1970)).isEqualTo("5");
		assertThat(statistiquesView.getSubdivisionName()).isEqualTo("Décennie");
	}
	
	@Test
	void testStatChronoAn2() {
		
		StatChrono sc1 = new StatChrono();

		sc1.addToStatistic(TemporalUtils.parseDate("1969-09-03"), 1.5);
		sc1.addToStatistic(TemporalUtils.parseDate("1970-01-01"), 5);
		
		StatistiquesView statistiquesView = new StatistiquesView(sc1, Granularite.PAR_AN, statToStringFunction);
		assertThat(statistiquesView.getPas()).isEqualTo(1);
		assertThat(statistiquesView.getStatisquesMap()).isNotEmpty().hasSize(2)
			.containsEntry(1960, 1.5)
			.containsEntry(1970, 5.0);
		assertThat(statistiquesView.getStatFor(1969)).isEqualTo("1.5");
		assertThat(statistiquesView.getStatFor(1970)).isEqualTo("5");
		assertThat(statistiquesView.getStatFor(2001)).isEqualTo("0");
		assertThat(statistiquesView.getLineNumber()).isEqualTo(2);
		assertThat(statistiquesView.getYearForLine(0)).isEqualTo(1960);
		assertThat(statistiquesView.getYearForLine(1)).isEqualTo(1970);
		assertThat(statistiquesView.getAccumulationStatFor(1960)).isEqualTo("1.5");
		assertThat(statistiquesView.getAccumulationStatFor(1965)).isEqualTo("1.5");
		assertThat(statistiquesView.getAccumulationStatFor(1970)).isEqualTo("5");
		assertThat(statistiquesView.getSubdivisionName()).isEqualTo("Décennie");
	}
	
	@Test
	void testStatChronoAn3() {
		
		StatChrono sc1 = new StatChrono();

		sc1.addToStatistic(TemporalUtils.parseDate("1979-09-03"), 1.5);
		sc1.addToStatistic(TemporalUtils.parseDate("1970-01-01"), 5);
		
		StatistiquesView statistiquesView = new StatistiquesView(sc1, Granularite.PAR_AN, statToStringFunction);
		assertThat(statistiquesView.getPas()).isEqualTo(1);
		assertThat(statistiquesView.getStatisquesMap()).isNotEmpty().hasSize(1)
			.containsEntry(1970, 6.5);
		assertThat(statistiquesView.getStatFor(1979)).isEqualTo("1.5");
		assertThat(statistiquesView.getStatFor(1970)).isEqualTo("5");
		assertThat(statistiquesView.getStatFor(2001)).isEqualTo("0");
		assertThat(statistiquesView.getLineNumber()).isEqualTo(1);
		assertThat(statistiquesView.getYearForLine(0)).isEqualTo(1970);
		assertThat(statistiquesView.getSubdivisionName()).isEqualTo("Décennie");
	}
	
	@Test
	void testStatChronoDecennie() {
		
		StatChrono sc1 = new StatChrono();

		sc1.addToStatistic(TemporalUtils.parseDate("1769-09-03"), 1.5);
		sc1.addToStatistic(TemporalUtils.parseDate("1560-01-01"), 1);
		sc1.addToStatistic(TemporalUtils.parseDate("1955-12-31"), 1);
		sc1.addToStatistic(TemporalUtils.parseDate("1969-12-31"), 2);
		sc1.addToStatistic(TemporalUtils.parseDate("1968-01-01"), 5);
		
		StatistiquesView statistiquesView = new StatistiquesView(sc1, Granularite.PAR_DECENNIE, statToStringFunction);
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
		assertThat(statistiquesView.getLineNumber()).isEqualTo(5);
		assertThat(statistiquesView.getYearForLine(0)).isEqualTo(1500);
		assertThat(statistiquesView.getYearForLine(1)).isEqualTo(1600);
		assertThat(statistiquesView.getYearForLine(2)).isEqualTo(1700);
		assertThat(statistiquesView.getYearForLine(3)).isEqualTo(1800);
		assertThat(statistiquesView.getYearForLine(4)).isEqualTo(1900);
		assertThatIllegalArgumentException().isThrownBy(() -> statistiquesView.getYearForLine(5));
		assertThatIllegalArgumentException().isThrownBy(() -> statistiquesView.getYearForLine(-1));
		assertThat(statistiquesView.getAccumulationStatFor(1500)).isEqualTo("1");
		assertThat(statistiquesView.getAccumulationStatFor(1710)).isEqualTo("1.5");
		assertThat(statistiquesView.getAccumulationStatFor(1970)).isEqualTo("8");
		assertThat(statistiquesView.getSubdivisionName()).isEqualTo("Siecle");
	}
	
	@Test
	void testStatChronoDecennie2() {
		
		StatChrono sc1 = new StatChrono();

		sc1.addToStatistic(TemporalUtils.parseDate("1969-09-03"), 1.5);
		sc1.addToStatistic(TemporalUtils.parseDate("1960-01-01"), 1);
		sc1.addToStatistic(TemporalUtils.parseDate("1955-12-31"), 1);
		sc1.addToStatistic(TemporalUtils.parseDate("1969-12-31"), 2);
		sc1.addToStatistic(TemporalUtils.parseDate("1968-01-01"), 5);
		
		StatistiquesView statistiquesView = new StatistiquesView(sc1, Granularite.PAR_DECENNIE, statToStringFunction);
		assertThat(statistiquesView.getPas()).isEqualTo(10);
		assertThat(statistiquesView.getStatisquesMap()).isNotEmpty().hasSize(1)
			.containsEntry(1900, 10.5);
		assertThat(statistiquesView.getStatFor(1950)).isEqualTo("1");
		assertThat(statistiquesView.getStatFor(1960)).isEqualTo("9.5");
		assertThat(statistiquesView.getStatFor(1970)).isEqualTo("0");
		assertThat(statistiquesView.getLineNumber()).isEqualTo(1);
		assertThat(statistiquesView.getYearForLine(0)).isEqualTo(1900);
		assertThat(statistiquesView.getSubdivisionName()).isEqualTo("Siecle");
	}
}
