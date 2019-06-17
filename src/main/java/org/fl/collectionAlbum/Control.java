package org.fl.collectionAlbum;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.logging.Logger;

import org.fl.collectionAlbum.rapportHtml.RapportHtml;

import com.ibm.lge.fl.util.RunningContext;

public class Control {

	// Logger
	private static Logger albumLog ;
	
	private static String rapportPath ;
	private static String concertTicketImgPath ;
	private static String oldRapportPath ;
	private static String acDirectoryName ;
	private static String concertDirectoryName ;
	private static String absoluteAlbumDir ;
	private static String absoluteConcertDir ;
	private static String absoluteArtisteDir ;
	private static LinkList accueils ;
	private static Charset charset ;

	private final static String albumDir 		   = "albums" ;
	private final static String concertDir 		   = "concerts" ;
	private final static String artisteDir 		   = "artistes" ;
	private final static String oldSuffix 		   = "_old" ;
   	private final static String homeCollectionFile = "index.html" ;
   	private final static String homeConcertFile    = "indexConcert.html" ;
   	private final static String musicFileExtension = "json" ;
	   	
	public static void initControl(String propFile) {
		
		try {
			//access to properties and logger
			RunningContext musicRunningContext = new RunningContext("CollectionMusique", null, new URI(propFile));
		
			Properties props = musicRunningContext.getProps() ;
		    albumLog = musicRunningContext.getpLog() ;
		    albumLog.info("Properties taken from " + musicRunningContext.getPropertiesLocation()) ;
			
			// Get the root directory for the album collection and concert
			acDirectoryName = props.getProperty("album.rootDir.name", "C:/FredericPersonnel/musique/Collection") ;
			concertDirectoryName = props.getProperty("concert.rootDir.name", "C:/FredericPersonnel/musique/Concert") ;
	
			// Get the rapport directory for the album collection
			rapportPath = props.getProperty("album.rapportDir.name", "C:/FredericPersonnel/musique/RapportCollection/rapport") ;
			oldRapportPath 	   = rapportPath + oldSuffix ;
			absoluteAlbumDir   = rapportPath + "/" + albumDir + "/" ;
			absoluteConcertDir = rapportPath  + "/" + concertDir + "/" ;
			absoluteArtisteDir = rapportPath  + "/" + artisteDir + "/" ;
			
			// get the concert ticket image path
			concertTicketImgPath = props.getProperty("concert.ticketImgDir.name", "file:///C:/FredericPersonnel/photos/PhotosGravees") ;	
			
			// Get charset to read and write
			charset = Charset.forName(props.getProperty("charset", "UTF-8")) ;
			RapportHtml.initCharset(charset.name());
			
			accueils = new LinkList() ;
			accueils.addLink("Accueil Collection", homeCollectionFile) ;
			accueils.addLink("Accueil Concert",    homeConcertFile) ;
			
		} catch (URISyntaxException e) {
			System.out.println("URI syntax exception for property file: " + propFile);
			e.printStackTrace();
		}
	}

	public static Logger getAlbumLog() 				{return albumLog;				}
	public static String getRapportPath() 			{return rapportPath;			}
	public static String getConcertTicketImgPath() 	{return concertTicketImgPath;	}
	public static String getOldRapportPath() 		{return oldRapportPath;			}
	public static String getAcDirectoryName() 		{return acDirectoryName;		}
	public static String getConcertDirectoryName() 	{return concertDirectoryName;	}
	public static String getAbsoluteAlbumDir() 		{return absoluteAlbumDir;		}
	public static String getAbsoluteConcertDir() 	{return absoluteConcertDir;		}
	public static String getAbsoluteArtisteDir() 	{return absoluteArtisteDir;		}
	public static String getArtistedir() 			{return artisteDir;				}
	public static LinkList getAccueils() 			{return accueils;				}
	public static String getHomecollectionfile() 	{return homeCollectionFile;		}
	public static String getHomeconcertfile() 		{return homeConcertFile;		}
	public static String getMusicfileExtension() 	{return musicFileExtension;		}
	public static Charset getCharset() 				{return charset;				}
	
}
