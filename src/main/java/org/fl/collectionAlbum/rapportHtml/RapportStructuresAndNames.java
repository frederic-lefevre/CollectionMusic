package org.fl.collectionAlbum.rapportHtml;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

import com.ibm.lge.fl.util.AdvancedProperties;

public class RapportStructuresAndNames {

	private final static String albumDir 		   = "albums" ;
	private final static String concertDir 		   = "concerts" ;
	private final static String artisteDir 		   = "artistes" ;
	private final static String oldSuffix 		   = "_old" ;
   	private final static String homeCollectionFile = "index.html" ;
   	private final static String homeConcertFile    = "indexConcert.html" ;

	private static Path rapportPath ;
	private static Path concertTicketImgPath ;
	private static Path oldRapportPath ;
	private static Path absoluteAlbumDir ;
	private static Path absoluteConcertDir ;
	private static Path absoluteArtisteDir ;
	private static HtmlLinkList accueils ;
	private static Charset charset ;

	private static boolean isInitialized = false ;
	
	private static Logger rapportLog ;
	
	public static boolean init() {
		
		rapportLog = Control.getAlbumLog() ;
		AdvancedProperties collectionProperties = Control.getCollectionProperties() ;
		
		rapportPath 		 = collectionProperties.getPathFromURI("album.rapportDirectory.name") ;
		concertTicketImgPath = collectionProperties.getPathFromURI("concert.ticketImgDir.name") ;
		
		isInitialized = true ;
		return isInitialized ;
	}

	public static Path getRapportPath() {
		return rapportPath;
	}
	
	public static Path getAbsoluteAlbumDir() {
		return rapportPath.resolve(albumDir) ;
	}
	
	public static Path getAbsoluteConcertDir() {
		return rapportPath.resolve(concertDir) ;
	}
	
	public static Path getAbsoluteArtisteDir() {
		return rapportPath.resolve(artisteDir) ;
	}
}
