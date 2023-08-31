package org.fl.collectionAlbum.stat;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StatAnneeTest {

	@Test
	void test() {
		
		StatAnnee statAn1 = new StatAnnee(2019, 0);
		
		assertThat(statAn1.getAn()).isEqualTo(2019);
		assertThat(statAn1.getNombre()).isEqualTo("");
		
		statAn1.incrementNombre(1);
		assertThat(statAn1.getAn()).isEqualTo(2019);
		assertThat(statAn1.getNombre()).isEqualTo("1");

		statAn1.incrementNombre(1.5);
		assertThat(statAn1.getAn()).isEqualTo(2019);
		assertThat(statAn1.getNombre()).isEqualTo("2.5");

		statAn1.incrementNombre(0.5);
		assertThat(statAn1.getAn()).isEqualTo(2019);
		assertThat(statAn1.getNombre()).isEqualTo("3");

	}

}
