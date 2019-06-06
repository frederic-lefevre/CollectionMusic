package org.fl.collectionAlbum;

import java.util.EnumMap;
import java.util.Optional;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.Groupe;
import org.fl.collectionAlbum.artistes.ListeArtiste;

import com.google.gson.JsonObject;

public class CollectionAlbumContainer {

	// Liste d'artistes pour les albums
	private ListeArtiste collectionArtistes;
	// Liste d'artistes pour les concerts
	private ListeArtiste concertsArtistes ;
	
	// Liste de tous les albums
	private ListeAlbum collectionAlbumsMusiques;
	
	// Listes des albums par rangement
	private EnumMap<Format.RangementSupportPhysique, ListeAlbum> rangementsAlbums ;	
	
	private ListeConcert concerts ;	
	private ChronoArtistes calendrierArtistes ;
	
	/**
	 * <code>statChronoEnregistrement</code> : statistics on recordings chronology
	 */
	private StatChrono statChronoEnregistrement ;
	/**
	 * <code>statChronoComposition</code> : statistics on composition chronology
	 */
	private StatChrono statChronoComposition ;
	
	private Logger albumLog ;
	
	public CollectionAlbumContainer(Logger aLog) {
		
		albumLog = aLog;
		reset() ;
	}

	public void addAlbum(Album alb) {
		
		collectionAlbumsMusiques.addAlbum(alb) ;
		
		Format.RangementSupportPhysique rangement = alb.getFormatAlbum().getRangement() ;
		if (rangement != null) {
			rangementsAlbums.get(rangement).addAlbum(alb) ;
		} else {
			albumLog.warning("Album impossible à ranger: " + alb.getTitre()) ;
		}
			
		statChronoEnregistrement.AddAlbum(alb.getDebutEnregistrement(), alb.getFormatAlbum().getPoids());
	    statChronoComposition.AddAlbum(alb.getDebutComposition(), alb.getFormatAlbum().getPoids());
	}
	
	public void addConcert(Concert c) {
		concerts.addConcert(c) ;
	}
	
	public ListeArtiste getCollectionArtistes() {
		return collectionArtistes;
	}

	public ListeAlbum getCollectionAlbumsMusiques() {
		return collectionAlbumsMusiques;
	}

	public ListeAlbum getRangementAlbums(Format.RangementSupportPhysique sPhys) {
		return rangementsAlbums.get(sPhys) ;
	}
	
	public ListeArtiste getConcertsArtistes() {
		return concertsArtistes;
	}

	public ListeConcert getConcerts() {
		return concerts;
	}

	public ChronoArtistes getCalendrierArtistes() {
		return calendrierArtistes;
	}

	public StatChrono getStatChronoComposition() {
		return statChronoComposition;
	}

	public StatChrono getStatChronoEnregistrement() {
		return statChronoEnregistrement;
	}
	
	public void reset() {
		
   		collectionAlbumsMusiques = new ListeAlbum(albumLog) ;
   		rangementsAlbums = new EnumMap<Format.RangementSupportPhysique, ListeAlbum>(Format.RangementSupportPhysique.class) ;
   		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
   			rangementsAlbums.put(rangement, new ListeAlbum(albumLog)) ;
   		}
   		
		collectionArtistes 		 = new ListeArtiste(albumLog) ;
   		concertsArtistes 		 = new ListeArtiste(albumLog) ;
   		
   		concerts 				 = new ListeConcert(albumLog) ;
   		
   		statChronoEnregistrement = new StatChrono(albumLog) ;
   		statChronoComposition 	 = new StatChrono(albumLog) ;
   		
   		calendrierArtistes 		 = new ChronoArtistes(albumLog) ;
	}
	
	public Artiste getArtisteKnown(String nom, String prenom) {
		
		Artiste a = collectionArtistes.getArtisteKnown(nom, prenom) ;
		if (a == null) {
			a = concertsArtistes.getArtisteKnown(nom, prenom) ;
		}
		return a ;
		
	}

	// Get an artiste, if it exists, return the existing one eventually updated
	// if it does not exists, create it
	public Artiste createGetOrUpdateArtiste(Class<? extends Artiste> cls, JsonObject jArtiste) {
		
		Artiste artiste;
		Optional<Artiste> eventualArtiste = collectionArtistes.getArtisteKnown(jArtiste) ;
		if (! eventualArtiste.isPresent()) {
			eventualArtiste = concertsArtistes.getArtisteKnown(jArtiste) ;
		} 
		
		if (! eventualArtiste.isPresent()) {
			if (cls == Groupe.class) {
				artiste = new Groupe(jArtiste, albumLog) ;
			} else {
				artiste = new Artiste(jArtiste, albumLog) ;
			}
		} else {
			artiste = eventualArtiste.get() ;
			artiste.update(jArtiste);
		}
		return artiste ;
	}
	
	public void setHtmlIds() {
		
		int i;
		
		// id for artiste
		i = 0 ;
		for (Artiste a : collectionArtistes.getArtistes()) {
			i = a.setHtmlNames(i) ;
		}
		for (Artiste a : concertsArtistes.getArtistes()) {
			i = a.setHtmlNames(i) ;
		}
		
		// id for album
		i = 0 ;
		for (Album a : collectionAlbumsMusiques.getAlbums()) {
			i = a.setHtmlName(i) ;
		}	
		
		// id for concert
		i = 0 ;
		for (Concert c : concerts.getConcerts()) {
			i = c.setHtmlName(i) ;
		}	
	}
}
