package org.fl.collectionAlbum.rapportHtml;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;
import org.fl.collectionAlbum.concerts.ListeConcert;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

import com.ibm.lge.fl.util.AdvancedProperties;

public class RapportStructuresAndNames {

	private final static String albumDir 		   = "albums" ;
	private final static String concertDir 		   = "concerts" ;
	private final static String artisteAlbumsDir   = "artistes/albums" ;
	private final static String artisteConcertsDir = "artistes/concerts" ;
	private final static String lieuDir 		   = "lieux" ;
   	private final static String homeCollectionFile = "index.html" ;
   	private final static String homeConcertFile    = "indexConcert.html" ;

	private static Path collectionDirectoryName ;
	private static Path concertDirectoryName ;

	private static Path 		rapportPath ;
	private static Path 		oldRapportPath ;
	private static HtmlLinkList accueils ;
	private static String 		concertTicketImgUri ;
	private static Charset 		charset ;

	private static RapportMap<Album> 		albumRapportPaths ;
	private static RapportMap<Artiste> 		artisteAlbumRapportPaths ;
	private static RapportMap<Artiste> 		artisteConcertRapportPaths ;
	private static RapportMap<Concert>  	concertRapportPaths ;
	private static RapportMap<LieuConcert> 	lieuRapportPaths ;
	
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
		concertTicketImgUri = collectionProperties.getProperty("concert.ticketImgDir.name") ;	

		// Get charset to write rapport
		charset = Charset.forName(collectionProperties.getProperty("rapport.charset", "UTF-8")) ;
		RapportHtml.withCharset(charset.name(), rapportLog);
			
		albumRapportPaths   	   = new RapportMap<>(rapportPath, getAbsoluteAlbumDir()) ;
		artisteAlbumRapportPaths   = new RapportMap<>(rapportPath, getAbsoluteArtisteAlbumDir()) ;
		artisteConcertRapportPaths = new RapportMap<>(rapportPath, getAbsoluteArtisteConcertDir()) ;
		concertRapportPaths 	   = new RapportMap<>(rapportPath, getAbsoluteConcertDir()) ;
		lieuRapportPaths		   = new RapportMap<>(rapportPath, getAbsoluteLieuDir()) ;
		
		isInitialized = true ;
		return isInitialized ;
	}

	public static void createRapports(ListeArtiste 	listeArtiste, 
							   ListeAlbum 		listeAlbum, 
							   ListeConcert 	listeConcert, 
							   LieuxDesConcerts lieuxDesConcerts) {
		
		if (! isInitialized) {
			init() ;
		}
		
		for (Artiste artiste : listeArtiste.getArtistes()) {	
			if (artiste.getNbAlbum() > 0) {
				URI artisteAlbumUri   = artisteAlbumRapportPaths.getUri(artiste) ;
				Path albumAbsolutePath   = rapportPath.resolve(artisteAlbumUri.getPath()) ;
				if (! Files.exists(albumAbsolutePath)) {
					RapportAlbumsDunArtiste rapportDeSesAlbums = new RapportAlbumsDunArtiste(artiste, "../../", rapportLog) ;
					rapportDeSesAlbums.printReport(albumAbsolutePath, stylesArtiste) ;
				}
			}
			
			if (artiste.getNbConcert() > 0) {
				URI artisteConcertUri = artisteConcertRapportPaths.getUri(artiste) ;
				Path concertAbsolutePath = rapportPath.resolve(artisteConcertUri.getPath()) ;
				if (! Files.exists(concertAbsolutePath)) {
					 RapportConcertsDunArtiste rapportDeSesConcerts = new RapportConcertsDunArtiste(artiste, "../../", rapportLog) ;
					 rapportDeSesConcerts.printReport(concertAbsolutePath, stylesArtiste) ;
				}
			}
		}
		
		for (Album album : listeAlbum.getAlbums()) {
			URI albumUri = albumRapportPaths.getUri(album) ;
			Path absolutePath = rapportPath.resolve(albumUri.getPath()) ;
			if (! Files.exists(absolutePath)) {
				RapportAlbum rapportAlbum = new RapportAlbum(album, "../", rapportLog) ;
				rapportAlbum.printReport( absolutePath, styles) ;
			}
		}
		for (Concert concert : listeConcert.getConcerts()) {
			URI concertUri = concertRapportPaths.getUri(concert) ;
			Path absolutePath = rapportPath.resolve(concertUri.getPath()) ;
			if (! Files.exists(absolutePath)) {
        		RapportConcert rapportConcert = new RapportConcert(concert,  "../", rapportLog) ;
        		rapportConcert.printReport(absolutePath, styles) ;
        	}
		}
		for (LieuConcert lieuConcert : lieuxDesConcerts.getLieuxConcerts()) {
			URI lieuUri = lieuRapportPaths.getUri(lieuConcert) ;
			Path absolutePath = rapportPath.resolve(lieuUri.getPath()) ;
			if (! Files.exists(absolutePath)) {
				String offSet = "../" ;
				HtmlLinkList acc = new HtmlLinkList(accueils, offSet) ;
				RapportListeConcerts concertDeCeLieu = new RapportListeConcerts(lieuConcert.getConcerts().sortChrono(), lieuConcert.getLieu(), acc, offSet, rapportLog) ;
				concertDeCeLieu.printReport(absolutePath, stylesArtiste) ;
			}
		}
	}
	
	public static Charset getCharset() 					  { return charset;									}
	public static Path 	  getRapportPath() 				  {	return rapportPath;								}
	public static Path 	  getOldRapportPath() 			  {	return oldRapportPath;							}
	public static Path 	  getAbsoluteAlbumDir() 		  {	return rapportPath.resolve(albumDir) ;			}	
	public static Path 	  getAbsoluteConcertDir() 		  {	return rapportPath.resolve(concertDir) ;		}	
	public static Path    getAbsoluteArtisteAlbumDir() 	  {	return rapportPath.resolve(artisteAlbumsDir) ;	}
	public static Path    getAbsoluteArtisteConcertDir()  {	return rapportPath.resolve(artisteConcertsDir) ;}
	public static Path 	  getAbsoluteLieuDir() 		  	  {	return rapportPath.resolve(lieuDir) ;			}
	public static Path 	  getAbsoluteHomeCollectionFile() { return rapportPath.resolve(homeCollectionFile) ;}	
	public static Path 	  getAbsoluteHomeConcertFile() 	  {	return rapportPath.resolve(homeConcertFile) ;	}
	public static Path 	  getCollectionDirectoryName() 	  {	return collectionDirectoryName;					}
	public static Path 	  getConcertDirectoryName() 	  {	return concertDirectoryName;					}

	public static HtmlLinkList getAccueils() {		return accueils;	}
	
	private static String  getConcertTicketImgUri() 	  { return concertTicketImgUri;						}	
	
	private static final String styles[] = {RapportHtml.albumStyle} ;
	
	public static URI getAlbumRapportRelativePath(Album album) {
		if (album.additionnalInfo()) {
			return albumRapportPaths.getUri(album) ;
		} else {
			return null ;
		}
	}
	
	public static URI getConcertRapportRelativePath(Concert concert) {
		if (concert.additionnalInfo()) {
			return concertRapportPaths.getUri(concert) ;
		} else {
			return null ;
		}
	}

	public static URI getLieuRapportRelativePath(LieuConcert lieuConcert) {
		return lieuRapportPaths.getUri(lieuConcert) ;
	}

	private final static String stylesArtiste[] = {"main","format","rapport", "artiste"} ;

	public static URI getArtisteAlbumRapportRelativePath(Artiste artiste) {
		return artisteAlbumRapportPaths.getUri(artiste) ;
	}

	public static URI getArtisteConcertRapportRelativePath(Artiste artiste) {
		return artisteConcertRapportPaths.getUri(artiste) ;
	}
	
	public static URI getTicketImageAbsoluteUri(String relativeToPhotoDirUriStr) {
		try {
			URI absoluteUri = new URI(getConcertTicketImgUri() + relativeToPhotoDirUriStr) ;
			// check that the file exists
			if (! (new File(absoluteUri)).exists()) {
				rapportLog.warning("Le fichier ticket image suivant n'existe pas: " + absoluteUri.toString()) ;
			}
			return absoluteUri ;
		} catch (Exception e) {
			rapportLog.log(Level.SEVERE, "Wrong URI string for ticket image: " + relativeToPhotoDirUriStr, e);
			return null ;
		}
	}
}
