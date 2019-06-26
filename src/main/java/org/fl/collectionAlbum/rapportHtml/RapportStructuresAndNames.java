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
	
	private static boolean init() {
		
		rapportLog = Control.getAlbumLog() ;
		AdvancedProperties collectionProperties = Control.getCollectionProperties() ;
		isInitialized = true ;
		return isInitialized ;
	}
}
