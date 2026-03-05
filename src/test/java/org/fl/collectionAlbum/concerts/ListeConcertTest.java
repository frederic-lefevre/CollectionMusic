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

package org.fl.collectionAlbum.concerts;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class ListeConcertTest {

	private static final ObjectMapper mapper = new ObjectMapper();
	private static final LieuxDesConcerts lieuxDesConcerts = new LieuxDesConcerts();
	
	@BeforeAll
	static void initControl() {
		
		// Just to configure Logger so that INFO message are not displayed
		Control.getMusicRunningContext();
	}
	
	@Test
	void testEmptyListe() {
		
		emptyListAsserts(ListeConcert.Builder.getBuilder().build());
	}
	
	@Test
	void testEmptyListe2() {
		
		emptyListAsserts(ListeConcert.Builder.getBuilderFrom(new ArrayList<>()).build());
	}
	
	@Test
	void testEmptyListe3() {
		
		emptyListAsserts(ListeConcert.Builder.getBuilderFrom(new ArrayList<>()).withConcertSatisfying(_ -> true).build());
	}
	
	@Test
	void testBuildFromNullListe() {
		
		assertThatNullPointerException().isThrownBy(() -> ListeConcert.Builder.getBuilderFrom(null).build());
	}
	
	private static final String concertStr1 = """
			{ 
			  "auteurCompositeurs": [{ 
			     "nom": "Bridgewater",
			     "prenom": "Dee Dee",
			     "naissance": "1950-05-27"
			   }],
			   "date": "1990-07-01",
			   "lieu": "Juan-les-Pins, Alpes-Maritimes",
			   "imageTicket": ["/Annees1990/1990/07_Juillet/RayCharles01.jpg"] 
			 } 
		""" ;
	
	@Test
	void testEmptyListe4() throws DatabindException, JacksonException {
		
		emptyListAsserts(ListeConcert.Builder.getBuilderFrom(buildConcertBuild(concertStr1)).withConcertSatisfying(c -> c.hasNotes()).build());
	}
	
	@Test
	void testListe1() throws DatabindException, JacksonException {
		
		ListeConcert listeConcert = ListeConcert.Builder.getBuilderFrom(buildConcertBuild(concertStr1)).build();
		
		assertThat(listeConcert).isNotNull();
		assertThat(listeConcert.getNombreConcerts())
			.isEqualTo(listeConcert.getConcerts().size())
			.isEqualTo(1);

		assertThat(listeConcert.getConcerts()).singleElement()
		.satisfies(concert -> {
			assertThat(concert.getAuteurs()).isNotNull().singleElement()
				.satisfies(artiste -> {
					assertThat(artiste.getNom()).isEqualTo("Bridgewater");
				});
		});
	}
	
	private static final String concertStr2 = """
			{ 
			  "auteurCompositeurs": [{ 
			     "nom": "Bridgewater",
			     "prenom": "Dee Dee",
			     "naissance": "1950-05-27"
			   }],
			   "date": "1990-07-01",
			   "lieu": "Nice, Alpes-Maritimes",
			   "imageTicket": ["/Annees1990/1990/07_Juillet/RayCharles01.jpg"] 
			 } 
		""" ;
	
	@Test
	void testListe4() throws DatabindException, JacksonException {
		
		ListeConcert listeConcert = ListeConcert.Builder.getBuilderFrom(buildConcertBuild(concertStr1, concertStr2)).build();
		
		assertThat(listeConcert).isNotNull();
		assertThat(listeConcert.getNombreConcerts())
			.isEqualTo(listeConcert.getConcerts().size())
			.isEqualTo(2);

		assertThat(listeConcert.getConcerts())
		.allSatisfy(concert -> {
			assertThat(concert.getAuteurs()).isNotNull().singleElement()
				.satisfies(artiste -> {
					assertThat(artiste.getNom()).isEqualTo("Bridgewater");
				});
		});
	}
	
	@Test
	void testListe3() throws DatabindException, JacksonException {
		
		ListeConcert listeConcert = ListeConcert.Builder.getBuilderFrom(buildConcertBuild(concertStr1, concertStr2))
				.withConcertSatisfying(concert -> concert.getLieuConcert().getLieu().contains("Juan-les-Pins")).build();
		
		assertThat(listeConcert).isNotNull();
		assertThat(listeConcert.getNombreConcerts())
			.isEqualTo(listeConcert.getConcerts().size())
			.isEqualTo(1);

		assertThat(listeConcert.getConcerts()).singleElement()
		.satisfies(concert -> {
			assertThat(concert.getAuteurs()).isNotNull().singleElement()
				.satisfies(artiste -> {
					assertThat(artiste.getNom()).isEqualTo("Bridgewater");
				});
		});
	}
	
	@Test
	void testListe5() {
		
		ListeConcert listeConcert = ListeConcert.Builder.getBuilderFrom(buildConcertBuild(concertStr1, concertStr2))
				.withConcertSatisfying(List.of(concert -> concert.getLieuConcert().getLieu().contains("Juan-les-Pins"),
						concert -> concert.getAuteurs().get(0).getNom().contains("Bridge"))).build();
		
		assertThat(listeConcert).isNotNull();
		assertThat(listeConcert.getNombreConcerts())
			.isEqualTo(listeConcert.getConcerts().size())
			.isEqualTo(1);

		assertThat(listeConcert.getConcerts()).singleElement()
		.satisfies(concert -> {
			assertThat(concert.getAuteurs()).isNotNull().singleElement()
				.satisfies(artiste -> {
					assertThat(artiste.getNom()).isEqualTo("Bridgewater");
				});
		});
	}
	
	private List<Concert> buildConcertBuild(String ...concertJsons) {
		
		return Stream.of(concertJsons)
				.map(concertJson -> {
					try {
						return (ObjectNode)mapper.readTree(concertJson);
					} catch (JacksonException e) {
						fail("Exception when parsing album json");
						return null;
					}
				})
				.map(jConcert -> {
					ListeArtiste la = new ListeArtiste();
					List<ListeArtiste> lla = new ArrayList<ListeArtiste>();
					lla.add(la);
					return new Concert(jConcert, lla, lieuxDesConcerts, Path.of("dummyPath"));
				}).toList();
	}
	
	private void emptyListAsserts(ListeConcert emptyList) {
		
		assertThat(emptyList).isNotNull();
		assertThat(emptyList.getConcerts()).isEmpty();
		assertThat(emptyList.getNombreConcerts()).isZero();
	}
}
