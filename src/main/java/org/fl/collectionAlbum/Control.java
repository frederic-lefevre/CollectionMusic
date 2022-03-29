package org.fl.collectionAlbum;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;

public class Control {

	private final static String musicFileExtension = "json" ;
	
	private static Logger 		  	  albumLog ;	
	private static Charset 		  	  charset ;
	private static RunningContext 	  musicRunningContext ;	  
   	private static AdvancedProperties collectionProperties ;
   	
	public static void initControl(String propFile) {
		
		try {
			//access to properties and logger
			musicRunningContext = new RunningContext("CollectionMusique", null, new URI(propFile));
		
			collectionProperties = musicRunningContext.getProps() ;
		    albumLog = musicRunningContext.getpLog() ;
		    albumLog.info("Properties taken from " + musicRunningContext.getPropertiesLocation()) ;
				
			// Get charset to read music files
			charset = Charset.forName(collectionProperties.getProperty("rapport.charset", "UTF-8")) ;
						
		} catch (URISyntaxException e) {
			System.out.println("URI syntax exception for property file: " + propFile);
			e.printStackTrace();
			collectionProperties = null ;
		}
	}

	public static Logger 			 getAlbumLog() 			   { return albumLog			; }
	public static String 			 getMusicfileExtension()   { return musicFileExtension  ; }
	public static AdvancedProperties getCollectionProperties() { return collectionProperties; }
	public static Charset 			 getCharset() 			   { return charset				; }
	public static RunningContext 	 getMusicRunningContext()  { return musicRunningContext ; }		
}
