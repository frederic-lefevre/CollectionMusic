package org.fl.collectionAlbum;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.stat.StatChrono;

import com.google.gson.JsonObject;

public class CollectionAlbumContainer {

	// Liste d'artistes pour les albums
	private ListeArtiste collectionArtistes;
	// Liste d'artistes pour les concerts
	private ListeArtiste concertsArtistes ;
	
	private List<ListeArtiste> allArtistes ;
	
	// Liste de tous les albums
	private ListeAlbum collectionAlbumsMusiques;
	
	// Listes des albums par rangement
	private EnumMap<Format.RangementSupportPhysique, ListeAlbum> rangementsAlbums ;	
	
	private ListeConcert   concerts ;	
	private ChronoArtistes calendrierArtistes ;
	
	private StatChrono statChronoEnregistrement ;
	private StatChrono statChronoComposition ;
	
	private LieuxDesConcerts lieuxDesConcerts ;
	
	private Logger albumLog ;
	
	private static CollectionAlbumContainer collectionAlbumContainer ;
	
	public static CollectionAlbumContainer getEmptyInstance(Logger aLog) {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer(aLog) ;
		}
		collectionAlbumContainer.reset() ;
		return collectionAlbumContainer ;
	}
	
	public static CollectionAlbumContainer getInstance(Logger aLog) {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer(aLog) ;
		}
		return collectionAlbumContainer ;
	}
	
	private CollectionAlbumContainer(Logger aLog) {		
		albumLog = aLog;	
	}

	public void addAlbum(JsonObject arteFactJson) {
		
		Album album = new Album(arteFactJson, allArtistes, albumLog) ;
		
		album.addMusicArtfactArtistesToList(collectionArtistes);
		
		collectionAlbumsMusiques.addAlbum(album) ;
				
		Format.RangementSupportPhysique rangement = album.getFormatAlbum().getRangement() ;
		if (rangement != null) {
			rangementsAlbums.get(rangement).addAlbum(album) ;
		} else {
			albumLog.warning("Album impossible à ranger: " + album.getTitre()) ;
		}
			
		statChronoEnregistrement.AddAlbum(album.getDebutEnregistrement(), album.getFormatAlbum().getPoids());
	    statChronoComposition.AddAlbum(album.getDebutComposition(), album.getFormatAlbum().getPoids());
	}
	
	public void addConcert(JsonObject arteFactJson) { 
		
		Concert concert = new Concert(arteFactJson, allArtistes, lieuxDesConcerts, albumLog) ;
		
		concert.getLieuConcert().addConcert(concert) ;
		concert.addMusicArtfactArtistesToList(concertsArtistes);
		
		concerts.addConcert(concert) ; 	
	}
	
	public ListeAlbum getRangementAlbums(Format.RangementSupportPhysique sPhys) { return rangementsAlbums.get(sPhys) ; }
	
	public ListeArtiste     getCollectionArtistes() 	  { return collectionArtistes		; }
	public ListeAlbum 	  	getCollectionAlbumsMusiques() { return collectionAlbumsMusiques ; }
	public ListeArtiste   	getConcertsArtistes() 		  { return concertsArtistes		 	; }
	public ListeConcert   	getConcerts() 				  { return concerts				 	; }
	public ChronoArtistes 	getCalendrierArtistes() 	  { return calendrierArtistes		; }
	public StatChrono 	  	getStatChronoComposition() 	  { return statChronoComposition	; }
	public StatChrono 	  	getStatChronoEnregistrement() { return statChronoEnregistrement ; }
	public LieuxDesConcerts getLieuxDesConcerts() 		  { return lieuxDesConcerts			; }

	private void reset() {
		
   		collectionAlbumsMusiques = new ListeAlbum(albumLog) ;
		collectionArtistes 		 = new ListeArtiste(albumLog) ;
   		concertsArtistes 		 = new ListeArtiste(albumLog) ;   		
   		concerts 				 = new ListeConcert(albumLog) ; 		
   		statChronoEnregistrement = new StatChrono(albumLog) ;
   		statChronoComposition 	 = new StatChrono(albumLog) ;   		
   		calendrierArtistes 		 = new ChronoArtistes() ;
   		lieuxDesConcerts		 = new LieuxDesConcerts() ;
   		allArtistes				 = new ArrayList<ListeArtiste>() ;
   		rangementsAlbums 		 = new EnumMap<Format.RangementSupportPhysique, ListeAlbum>(Format.RangementSupportPhysique.class) ;
   		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
   			rangementsAlbums.put(rangement, new ListeAlbum(albumLog)) ;
   		}
   		allArtistes.add(collectionArtistes) ;
   		allArtistes.add(concertsArtistes) ;
	}
	
	public Artiste getArtisteKnown(String nom, String prenom) {
		
		Artiste a = collectionArtistes.getArtisteKnown(nom, prenom) ;
		if (a == null) {
			a = concertsArtistes.getArtisteKnown(nom, prenom) ;
		}
		return a ;
		
	}
	
}
