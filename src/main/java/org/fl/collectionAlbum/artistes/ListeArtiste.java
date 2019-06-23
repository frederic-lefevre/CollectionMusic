package org.fl.collectionAlbum.artistes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.PoidsComparator;
import org.fl.collectionAlbum.concerts.ConcertPoidsComparator;

import com.google.gson.JsonObject;

public class ListeArtiste {
	
	private Logger listeArtisteLog;
	
	private static String styles[] = {"main","format","rapport","chrono"} ;
	
	private List<Artiste> artistes;

	public ListeArtiste(Logger laLog) {
		super();
		listeArtisteLog = laLog;
		artistes  		= new ArrayList<Artiste>() ;
	}

	public String[] getCssStyles() {
		return styles ;
	}
	
	public void addArtiste(Artiste a) {
		
		if (! artistes.contains(a)) {
			artistes.add(a) ;
		}
		if (listeArtisteLog.isLoggable(Level.FINEST)) {
			listeArtisteLog.finest("  Nom: " + a.getNom() + "  Prenoms: " + a.getPrenoms()) ;
		}
	}
	
	public void addAllArtistes(List<Artiste> artistes) {
		artistes.stream().forEach(a -> addArtiste(a));
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
		AuteurDateComparator compChrono = new AuteurDateComparator(listeArtisteLog);
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
		
		ListeArtiste artistesRes = new ListeArtiste(listeArtisteLog) ;
		for (Artiste a : artistes) {
			artistesRes.addArtiste(a) ;
		}
		return artistesRes ;
	}

	public List<Artiste> getArtistes() {
		return artistes;
	}	
}
