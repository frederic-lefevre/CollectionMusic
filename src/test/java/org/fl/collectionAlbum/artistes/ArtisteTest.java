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

package org.fl.collectionAlbum.artistes;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.utils.TemporalUtils;
import org.fl.util.FilterCounter;
import org.fl.util.FilterCounter.LogRecordCounter;
import org.junit.jupiter.api.Test;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

class ArtisteTest {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
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
		String expectedDates = " (" + TemporalUtils.formatDate(artiste.getNaissance()) + " - " + TemporalUtils.formatDate(artiste.getMort()) + ")";
		assertThat(artiste.getDates()).isEqualTo(expectedDates);
		
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
		
		artiste.updateArtistRoleAndDates(jArt2, ArtistRole.AUTEUR);
		
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
		
		artiste.updateArtistRoleAndDates(jArt2, ArtistRole.INTERPRETE);
		
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
		
		artiste.updateArtistRoleAndDates(jArt2, ArtistRole.AUTEUR);
		
		assertThat(logCounter.getLogRecordCount()).isEqualTo(2);
		assertThat(logCounter.getLogRecordCount(Level.WARNING)).isEqualTo(2);
		
		logCounter.stopLogCountAndFilter();
	}
	
	@Test
	void dateDecesOnly() {

		ObjectNode jArt = JsonNodeFactory.instance.objectNode();
		jArt.put("nom", "Evans");
		jArt.put("prenom", "Bill");
		jArt.put("mort", "1980-09-15");

		LogRecordCounter logCounter = 
				FilterCounter.getLogRecordCounter(Logger.getLogger(Artiste.class.getName()));
		
		Artiste artiste = new Artiste(jArt, ArtistRole.AUTEUR);

		assertThat(artiste.getNom()).isEqualTo("Evans");
		assertThat(artiste.getPrenoms()).isEqualTo("Bill");

		assertThat(artiste.getDateNaissance()).isEmpty();
		assertThat(artiste.getDateMort()).isEqualTo(TemporalUtils.formatDate(artiste.getMort()));
		
		assertThat(artiste.getDates()).isEqualTo("");
		
		assertThat(logCounter.getLogRecordCount()).isEqualTo(1);
		assertThat(logCounter.getLogRecordCount(Level.WARNING)).isEqualTo(1);
		
		logCounter.stopLogCountAndFilter();
	}
	
	private static final String albumStr1 = """
			{
  "titre" : "Concerto pour violon - Le temps chanté",
  "format" : {
    "cd" : 1,
    "audioFiles" : [ {
      "bitDepth" : 16,
      "samplingRate" : 44.1,
      "source" : "CD",
      "type" : "FLAC",
      "location" : [ "r/Wolfgang Rihm/Le Temps Enhanté, Anne-Sophie Mutter/", "b/Alban Berg/Concerto Pour Violon, Anne-Sophie Mutter/" ]
    } ]
  },
  "interpretes" : [ {
    "nom" : "Mutter",
    "prenom" : "Anne-Sophie",
    "naissance" : "1963-06-29"
  } ],
  "chefOrchestres" : [ {
    "nom" : "Levine",
    "prenom" : "James"
  } ],
  "auteurCompositeurs" : [ {
    "nom" : "Berg",
    "prenom" : "Alban",
    "naissance" : "1885-02-09",
    "mort" : "1935-12-24"
  }, {
    "nom" : "Rihm",
    "prenom" : "Wolfgang",
    "naissance" : "1952-03-13"
  } ],
  "ensembles" : [ {
    "nom" : "Orchestre Symphonique de Chicago"
  } ],
  "enregistrement" : [ "1992-06-01", "1992-06-01" ],
  "composition" : [ "1935-01-01", "1992-12-31" ],
  "jsonVersion" : 4,
  "discogs" : "20029600"
}
			""";
	
	@Test
	void test4() throws DatabindException, JacksonException {
		
		
		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr1);
		
		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		
		assertThat(album.getChefsOrchestre()).isNotNull().isNotEmpty().singleElement()
			.satisfies(artiste -> {
				assertThat(artiste.getArtistRoles()).singleElement().isEqualTo(ArtistRole.CHEF_ORCHESTRE);
				assertThat(artiste.getNomCompletEtDates()).isEqualTo("James Levine");
			});
		assertThat(album.getAuteurs()).isNotNull().isNotEmpty().hasSize(2);
		assertThat(album.getInterpretes()).isNotNull().isNotEmpty().singleElement()
		.satisfies(artiste -> {
			assertThat(artiste.getArtistRoles()).singleElement().isEqualTo(ArtistRole.INTERPRETE);
			assertThat(artiste.getNomCompletEtDates()).isEqualTo("Anne-Sophie Mutter (29 juin 1963 - )");
		});
		assertThat(album.getEnsembles()).isNotNull().isNotEmpty().singleElement()
		.satisfies(artiste -> {
			assertThat(artiste.getArtistRoles()).singleElement().isEqualTo(ArtistRole.ENSEMBLE);
			assertThat(artiste.getNomCompletEtDates())
				.isEqualTo("Orchestre Symphonique de Chicago")
				.isEqualTo(artiste.getNom())
				.isEqualTo(artiste.getNomComplet());
			assertThat(artiste.getPrenoms()).isEmpty();
			assertThat(artiste.getNaissance()).isNull();
			assertThat(artiste.getMort()).isNull();
			assertThat(artiste.getDateNaissance()).isEmpty();
			assertThat(artiste.getDateMort()).isEmpty();
			assertThat(artiste.getDates()).isEmpty();
		});
		
		assertThat(album.getAllArtists()).hasSize(5);
		assertThat(album.getAllArtists())
			.containsAll(album.getAuteurs())
			.containsAll(album.getInterpretes())
			.containsAll(album.getChefsOrchestre())
			.containsAll(album.getEnsembles());
	}
	
	private static final String albumStr2 = """
			{
  "titre" : "In-A-Gadda-Da-Vida",
  "format" : {
    "cd" : 1,
    "audioFiles" : [ {
      "bitDepth" : 24,
      "samplingRate" : 176.4,
      "source" : "SACD",
      "type" : "FLAC",
      "note" : "Remaster MFSL 2020",
      "location" : [ "i/Iron Butterfly/In-A-Gadda-Da-Vida [24 176]/" ]
    } ]
  },
  "groupe" : [ {
    "nom" : "Iron Butterfly"
  } ],
  "enregistrement" : [ "1968-01-01", "1968-07-01" ],
  "jsonVersion" : 4,
  "discogs" : "16293234"
}
			""";
	
	@Test
	void testGroupe() throws DatabindException, JacksonException {
		
		ObjectNode jAlbum = (ObjectNode)mapper.readTree(albumStr2);
		
		ListeArtiste la = new ListeArtiste();
		List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
		lla.add(la);

		Album album = new Album(jAlbum, lla, Path.of("dummyPath"));
		
		assertThat(album.getChefsOrchestre()).isEmpty();
		assertThat(album.getEnsembles()).isEmpty();
		assertThat(album.getInterpretes()).isEmpty();
		assertThat(album.getAuteurs()).singleElement().satisfies(groupe -> {
			assertThat(groupe.getArtistRoles()).singleElement().isEqualTo(ArtistRole.GROUPE);
			assertThat(groupe.getNomCompletEtDates())
				.isEqualTo("Iron Butterfly")
				.isEqualTo(groupe.getNom())
				.isEqualTo(groupe.getNomComplet());
			assertThat(groupe.getPrenoms()).isEmpty();
			assertThat(groupe.getNaissance()).isNull();
			assertThat(groupe.getMort()).isNull();
			assertThat(groupe.getDateNaissance()).isEmpty();
			assertThat(groupe.getDateMort()).isEmpty();
			assertThat(groupe.getDates()).isEmpty();
		});
	}		
}
