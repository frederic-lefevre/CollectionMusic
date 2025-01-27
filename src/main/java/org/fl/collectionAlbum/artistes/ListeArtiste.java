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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.logging.Logger;

import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.PoidsComparator;
import org.fl.collectionAlbum.concerts.ConcertPoidsComparator;
import org.fl.collectionAlbum.utils.ListUtils;

import com.google.gson.JsonObject;

public class ListeArtiste {
	
	private final static Logger albumLog = Logger.getLogger(ListeArtiste.class.getName());
	
	private final List<Artiste> artistes;

	public ListeArtiste() {
		super();
		artistes = new ArrayList<Artiste>();
	}

	public void reset() {
		artistes.clear();
	}
	
	public void addArtiste(Artiste a) {
		
		if (! artistes.contains(a)) {
			artistes.add(a) ;
		}
		albumLog.finest(() -> "  Nom: " + a.getNom() + "  Prenoms: " + a.getPrenoms());
	}
	
	public void addAllArtistes(List<Artiste> artistes, MusicArtefact musicArtefact) {
		artistes.stream().forEach(a -> addArtiste(a, musicArtefact)  );
	}
	
	private void addArtiste(Artiste a,  MusicArtefact musicArtefact) {
		
		a.addArteFact(musicArtefact) ;
		addArtiste(a) ;
	}
	
	public Artiste getArtisteKnown(String nom, String prenom) {
		if (nom    == null) nom    = "" ;
		if (prenom == null) prenom = "" ;
		for (Artiste a : artistes) {
			if (nom.equals(a.getNom()) && (prenom.equals(a.getPrenoms()))) {
				return a ;
			}
		}
		return null ;
	}
	
	public Optional<Artiste> getArtisteKnown(JsonObject jArtiste) {
		return artistes.stream().filter(a -> a.isSameArtiste(jArtiste)).findFirst() ;
	}
		
	public ListeArtiste sortArtistesAlpha() {
		AuteurComparator compAuteur = new AuteurComparator();
		Collections.sort(artistes, compAuteur) ;
		return this;
	}

	public ListeArtiste sortArtistesPoidsAlbums() {
		PoidsComparator compPoids = new PoidsComparator();
		Collections.sort(artistes, compPoids) ;
		return this;
	}

	public ListeArtiste sortArtistesPoidsConcerts() {
		ConcertPoidsComparator compConcertPoids = new ConcertPoidsComparator();
		Collections.sort(artistes, compConcertPoids) ;
		return this;
	}
	
	public ListeArtiste sortArtistesChrono() {
		AuteurDateComparator compChrono = new AuteurDateComparator();
		Collections.sort(artistes, compChrono) ;
		return this;
	}
	
	public int getNombreArtistes() {
		return (artistes.size()) ;
	}
	
	public ListeArtiste getReunion(ListeArtiste la) {
		
		ListeArtiste artistesRes = cloneListe() ;
		for (Artiste a : la.artistes) {
			artistesRes.addArtiste(a) ;
		}
		return artistesRes ;
	}
	
	private ListeArtiste cloneListe() {
		
		ListeArtiste artistesRes = new ListeArtiste() ;
		for (Artiste a : artistes) {
			artistesRes.addArtiste(a) ;
		}
		return artistesRes ;
	}

	public List<Artiste> getArtistes() {
		return artistes;
	}
	
	public List<Artiste> pickRandomArtistes(int nbArtiste) {
		return ListUtils.pickRandomDistinctElements(artistes, nbArtiste);
	}
}
