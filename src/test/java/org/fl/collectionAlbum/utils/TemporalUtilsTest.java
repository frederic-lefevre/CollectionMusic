package org.fl.collectionAlbum.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
