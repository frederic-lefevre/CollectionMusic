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

package org.fl.collectionAlbum.albums;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.junit.jupiter.api.Test;

class ListeAlbumTest {

	@Test
	void testEmptyListe() {
		
		ListeAlbum emptyListe =  ListeAlbum.Builder.getBuilder().build();
		
		assertThat(emptyListe).isNotNull();
		
		assertThat(emptyListe.getAlbums()).isNotNull().isEmpty();
		assertThat(emptyListe.getNombreAlbums()).isZero();
		
		Format format = emptyListe.getFormatListeAlbum();
		
		assertThat(format.hasError()).isFalse();
		assertThat(format.getPoids()).isZero();
		
		assertThat(format.getContentNatures()).isEmpty();
		assertThat(format.getSupportsPhysiques()).isNotNull().containsOnly(MediaSupportCategories.values());
		assertThat(format.getSupportsPhysiquesNumbers()).allSatisfy((sp, number) -> assertThat(number).isZero());	
	}
	
	@Test
	void testEmptyListe2() {
		
		ListeAlbum emptyListe =  ListeAlbum.Builder.getBuilderFrom(new ArrayList<>()).build();
		
		assertThat(emptyListe).isNotNull();
		
		assertThat(emptyListe.getAlbums()).isNotNull().isEmpty();
		assertThat(emptyListe.getNombreAlbums()).isZero();
		
		Format format = emptyListe.getFormatListeAlbum();
		
		assertThat(format.hasError()).isFalse();
		assertThat(format.getPoids()).isZero();
		
		assertThat(format.getContentNatures()).isEmpty();
		assertThat(format.getSupportsPhysiques()).isNotNull().containsOnly(MediaSupportCategories.values());
		assertThat(format.getSupportsPhysiquesNumbers()).allSatisfy((sp, number) -> assertThat(number).isZero());	
	}
	
	private static final String albumStr1 = """
			{ 
			  "titre": "Portrait in jazz",
			  "format": {  "cd": 1,
				"audioFiles" : [{
				    "bitDepth": 16 , 
				    "samplingRate" : 44.1, 
				    "source" : "MOFI Fidelity Sound Lab", 
				    "type" : "FLAC" }]
			     }, 
			  "auteurCompositeurs": [ 
			    {  
			      "nom": "Evans",
			      "prenom": "Bill", 
			      "naissance": "1929-08-16",
			      "mort": "1980-09-15"  
			    }                                 
			  ],    								
			  "enregistrement": [ "1959-12-28",  "1959-12-28" ],  
			  "liensUrl":  [ "/Concerts/2006/EricClapton20060505" ] 
			 } 
			""" ;
	
	private static final String albumStr2 = """
{ 
  "titre": "Van Halen",
  "format": {
    "cd": 1,
    "audioFiles": [
      {
        "bitDepth": 24,
        "samplingRate": 192,
        "source": "File",
        "type": "FLAC"
      }
    ]
  },
  "groupe": [
    {
      "nom": "Van Halen"
    }
  ],
  "enregistrement": [
    "1977-09-01",
    "1977-11-01"
  ]
}
			""" ;
	
	private static final String albumStr3 = """
{
  "titre": "Magical mystery tour",
  "format": {
    "cd": 1,
    "audioFiles": [
      {
        "bitDepth": 16,
        "samplingRate": 44.1,
        "source": "CD",
        "type": "FLAC",
        "location": [
          "b/The Beatles/Magical Mystery Tour"
        ]
      }
    ]
  },
  "groupe": [
    {
      "nom": "Beatles",
      "article": "The"
    }
  ],
  "enregistrement": [
    "1966-11-24",
    "1967-11-07"
  ],
  "jsonVersion": 4
}
			""";
	

	@Test
	void testListeBuild1() {
		
		ListeAlbum listeAlbum =  ListeAlbum.Builder.getBuilder().build();
	}
}
