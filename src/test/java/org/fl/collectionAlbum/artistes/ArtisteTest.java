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

package org.fl.collectionAlbum.artistes;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.utils.TemporalUtils;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

class ArtisteTest {
	
	@Test
	void test() {

		ObjectNode jArt = JsonNodeFactory.instance.objectNode();
		jArt.put("nom", "Evans");
		jArt.put("prenom", "Bill");
		jArt.put("naissance", "1929-08-16");
		jArt.put("mort", "1980-09-15");

		Artiste artiste = new Artiste(jArt, ArtistRole.AUTEUR);

		assertThat(artiste.getNom()).isEqualTo("Evans");
		assertThat(artiste.getPrenoms()).isEqualTo("Bill");

		assertThat(artiste.getDateNaissance()).isEqualTo(TemporalUtils.formatDate(artiste.getNaissance()));
		assertThat(artiste.getDateMort()).isEqualTo(TemporalUtils.formatDate(artiste.getMort()));
		
		assertThat(artiste.getNbAlbum()).isZero();
		assertThat(artiste.getNbConcert()).isZero();

		assertThat(artiste.getAlbums().getNombreAlbums()).isZero();
		assertThat(artiste.getConcerts().getNombreConcerts()).isZero();

		assertThat(artiste.getAlbumsFormat().getPoids()).isZero();
		
		assertThat(artiste.hasRole(ArtistRole.AUTEUR)).isTrue();
		assertThat(artiste.hasAnyRole(ArtistRole.AUTEUR)).isTrue();
		
		assertThat(artiste.getArtistRoles()).hasSameElementsAs(Set.of(ArtistRole.AUTEUR));
	}
	
	
	@Test
	void test2() {
		
		ObjectNode jArt = JsonNodeFactory.instance.objectNode();
		jArt.put("nom", "Evans");
		jArt.put("prenom", "Bill");

		Artiste artiste = new Artiste(jArt, ArtistRole.AUTEUR);

		assertThat(artiste.getNom()).isEqualTo("Evans");
		assertThat(artiste.getPrenoms()).isEqualTo("Bill");

		assertThat(artiste.getDateNaissance()).isEmpty();
		assertThat(artiste.getDateMort()).isEmpty();
		
		ObjectNode jArt2 = JsonNodeFactory.instance.objectNode();
		jArt2.put("nom", "Evans");
		jArt2.put("prenom", "Bill");
		jArt2.put("naissance", "1929-08-16");
		jArt2.put("mort", "1980-09-15");
		
		artiste.update(jArt2, ArtistRole.AUTEUR);
		
		assertThat(artiste.getArtistRoles()).hasSameElementsAs(Set.of(ArtistRole.AUTEUR));
		
		assertThat(artiste.getDateNaissance()).isEqualTo(TemporalUtils.formatDate(artiste.getNaissance()));
		assertThat(artiste.getDateMort()).isEqualTo(TemporalUtils.formatDate(artiste.getMort()));
	}
	
	@Test
	void test3() {
		
		ObjectNode jArt = JsonNodeFactory.instance.objectNode();
		jArt.put("nom", "Evans");
		jArt.put("prenom", "Bill");

		Artiste artiste = new Artiste(jArt, ArtistRole.AUTEUR);

		ObjectNode jArt2 = JsonNodeFactory.instance.objectNode();
		jArt2.put("nom", "Evans");
		jArt2.put("prenom", "Bill");
		
		artiste.update(jArt2, ArtistRole.INTERPRETE);
		
		assertThat(artiste.getArtistRoles()).hasSameElementsAs(Set.of(ArtistRole.AUTEUR, ArtistRole.INTERPRETE));
		
		assertThat(artiste.getDateNaissance()).isEmpty();
		assertThat(artiste.getDateMort()).isEmpty();
	}
	
	@Test
	void updatingAlreadySetDateShouldRaiseWarnings() {

		ObjectNode jArt = JsonNodeFactory.instance.objectNode();
		jArt.put("nom", "Evans");
		jArt.put("prenom", "Bill");
		jArt.put("naissance", "1929-08-16");
		jArt.put("mort", "1980-09-15");

		Artiste artiste = new Artiste(jArt, ArtistRole.AUTEUR);
		
		ObjectNode jArt2 = JsonNodeFactory.instance.objectNode();
		jArt2.put("nom", "Evans");
		jArt2.put("prenom", "Bill");
		jArt2.put("naissance", "1929-08-16");
		jArt2.put("mort", "1980-09-15");
		
		LogRecordCounter logCounter = 
				FilterCounter.getLogRecordCounter(Logger.getLogger(Artiste.class.getName()));
		
		artiste.update(jArt2, ArtistRole.AUTEUR);
		
		assertThat(logCounter.getLogRecordCount()).isEqualTo(2);
		assertThat(logCounter.getLogRecordCount(Level.WARNING)).isEqualTo(2);
	}
}
