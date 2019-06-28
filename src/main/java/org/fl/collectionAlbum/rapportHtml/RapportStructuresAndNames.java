package org.fl.collectionAlbum.rapportHtml;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

import com.ibm.lge.fl.util.AdvancedProperties;

public class RapportStructuresAndNames {

	private final static String albumDir 		   = "albums" ;
	private final static String concertDir 		   = "concerts" ;
	private final static String artisteDir 		   = "artistes" ;
   	private final static String homeCollectionFile = "index.html" ;
   	private final static String homeConcertFile    = "indexConcert.html" ;

	private static Path collectionDirectoryName ;
	private static Path concertDirectoryName ;

	private static Path rapportPath ;
	private static Path oldRapportPath ;
	private static HtmlLinkList accueils ;
	private static String concertTicketImgPath ;
	private static Charset charset ;

	private static HashMap<Album,   Path> 		  albumRapportPaths ;
	private static HashMap<Concert, Path> 		  concertRapportPaths ;
	private static HashMap<Artiste, ArtistePaths> artisteRapportPaths ;
	
	private static class ArtistePaths {
		protected Path albums ;
		protected Path concerts ;
		protected ArtistePaths(Path a, Path c) {
			albums   = a ;
			concerts = c ;
		}
	}
	private static int idAlbum   = 0 ;
	private static int idConcert = 0 ;
	private static int idArtiste = 0 ;
	
	private static boolean isInitialized = false ;
	
	private static Logger rapportLog ;
	
	public static boolean init() {
		
		rapportLog = Control.getAlbumLog() ;
		AdvancedProperties collectionProperties = Control.getCollectionProperties() ;
		
		rapportPath 		 = collectionProperties.getPathFromURI("album.rapportDirectory.name") ;
		oldRapportPath		 = collectionProperties.getPathFromURI("album.oldRapportDirectory.name") ;
		
		// Get the root directory for the album collection and concert
		collectionDirectoryName = collectionProperties.getPathFromURI("album.rootDir.name") ;
		concertDirectoryName 	= collectionProperties.getPathFromURI("concert.rootDir.name") ;

		accueils = new HtmlLinkList() ;
		accueils.addLink("Accueil Collection", homeCollectionFile) ;
		accueils.addLink("Accueil Concert",    homeConcertFile) ;

		// get the concert ticket image path
		concertTicketImgPath = collectionProperties.getProperty("concert.ticketImgDir.name") ;	

		// Get charset to write rapport
		charset = Charset.forName(collectionProperties.getProperty("rapport.charset", "UTF-8")) ;
		RapportHtml.withCharset(charset.name(), rapportLog);
			
		albumRapportPaths   = new HashMap<>() ;
		concertRapportPaths = new HashMap<>() ;
		artisteRapportPaths = new HashMap<>() ;
		
		isInitialized = true ;
		return isInitialized ;
	}

	public static Path getRapportPath() {
		return rapportPath;
	}
	
	public static Path getOldRapportPath() {
		return oldRapportPath;
	}

	public static Charset getCharset() 				{return charset;				}
	public static String  getConcertTicketImgPath() 	{return concertTicketImgPath;	}	
	public static Path 	  getAbsoluteAlbumDir() {		return rapportPath.resolve(albumDir) ;	}	
	public static Path getAbsoluteConcertDir() {		return rapportPath.resolve(concertDir) ;	}	
	public static Path getAbsoluteArtisteDir() {		return rapportPath.resolve(artisteDir) ;	}	
	public static Path getAbsoluteHomeCollectionFile() {		return rapportPath.resolve(homeCollectionFile) ;	}	
	public static Path getAbsoluteHomeConcertFile() {		return rapportPath.resolve(homeConcertFile) ;	}
	public static Path getCollectionDirectoryName() {		return collectionDirectoryName;	}

	public static Path getConcertDirectoryName() {		return concertDirectoryName;	}

	public static HtmlLinkList getAccueils() {		return accueils;	}
	
	private static final String styles[] = {RapportHtml.albumStyle} ;
	
	public static URI getAlbumRapportPath(Album album) {
		if (album.additionnalInfo()) {
			Path aPath = albumRapportPaths.get(album) ;
			if (aPath == null) {
				aPath = RapportStructuresAndNames.getAbsoluteAlbumDir().resolve("i" + idAlbum + ".html") ;
				idAlbum++ ;
				albumRapportPaths.put(album, aPath) ;				
				if (! Files.exists(aPath)) {
					RapportAlbum rapportAlbum = new RapportAlbum(album, "../", rapportLog) ;
					rapportAlbum.printReport( aPath, styles) ;
				}
			}
			return aPath.toUri() ;
		} else {
			return null ;
		}
	}
	
	public static URI getConcertRapportPath(Concert concert) {
		if (concert.additionnalInfo()) {
			Path aPath = concertRapportPaths.get(concert) ;
			if (aPath == null) {
				aPath = RapportStructuresAndNames.getAbsoluteConcertDir().resolve("i" + idConcert + ".html") ;
				idConcert++ ;
				concertRapportPaths.put(concert, aPath) ;     	
	        	if (! Files.exists(aPath)) {
	        		RapportConcert rapportConcert = new RapportConcert(concert,  "../", rapportLog) ;
	        		rapportConcert.printReport(aPath, styles) ;
	        	}
			}
			return aPath.toUri() ;
		} else {
			return null ;
		}
	}

	private final static String stylesArtiste[] = {"main","format","rapport", "artiste"} ;

	public static Path getArtisteAlbumRapportRelativePath(Artiste artiste) {

			ArtistePaths aPath = artisteRapportPaths.get(artiste) ;
			if (aPath == null) {
				aPath = genererLesRapportsDunArtiste(artiste) ;	
			}
			if (aPath.albums != null) {
				return rapportPath.relativize(aPath.albums) ;	
			} else {
				return null ;
			}
	}
	
	public static Path getArtisteConcertRapportRelativePath(Artiste artiste) {

		ArtistePaths aPath = artisteRapportPaths.get(artiste) ;
		if (aPath == null) {
			aPath = genererLesRapportsDunArtiste(artiste) ;	
		}
		if (aPath.concerts != null) {
			return rapportPath.relativize(aPath.concerts) ;
		} else {
			return null ;
		}
	}
	
	private static ArtistePaths genererLesRapportsDunArtiste(Artiste artiste) {
		String subdirectory = "a" + idArtiste/100 ;
		Path subdirectoryPath = RapportStructuresAndNames.getAbsoluteArtisteDir().resolve(subdirectory) ;
		try {
			if (! Files.exists(subdirectoryPath)) {
				Files.createDirectories(subdirectoryPath) ;
			}
			Path albumPath   = null ;
			Path concertPath = null ;
			if (artiste.getNbAlbum() > 0) {
				String htmlAlbum 	= "a" + idArtiste + ".html" ;
				albumPath   = 	subdirectoryPath.resolve(htmlAlbum) ;
			}
			if (artiste.getNbConcert() > 0) {
				String htmlConcert 	= "c" + idArtiste + ".html" ;			
				concertPath = subdirectoryPath.resolve(htmlConcert) ;
			}
			
			idArtiste++ ;
			ArtistePaths aPath = new ArtistePaths(albumPath, concertPath) ;
			artisteRapportPaths.put(artiste, aPath) ;	
			
			if ((aPath.albums != null) && (! Files.exists(aPath.albums))) {
				RapportAlbumsDunArtiste rapportDeSesAlbums = new RapportAlbumsDunArtiste(artiste, "../../", rapportLog) ;
				rapportDeSesAlbums.printReport(albumPath, stylesArtiste) ;
			}
			if ((aPath.concerts != null) && (! Files.exists(aPath.concerts))) {
				 RapportConcertsDunArtiste rapportDeSesConcerts = new RapportConcertsDunArtiste(artiste, "../../", rapportLog) ;
				 rapportDeSesConcerts.printReport(concertPath, stylesArtiste) ;
			}
			return aPath ;
		} catch (Exception e) {
			rapportLog.log(Level.SEVERE, "Excetion generating artiste rapports (concerts and albums); path=" + subdirectoryPath, e);
			return null ;
		}
	}
	
}