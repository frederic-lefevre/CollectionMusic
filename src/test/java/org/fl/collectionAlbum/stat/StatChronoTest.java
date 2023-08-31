package org.fl.collectionAlbum.stat;

import static org.assertj.core.api.Assertions.assertThat;

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
