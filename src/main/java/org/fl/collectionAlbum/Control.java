/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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


package org.fl.collectionAlbum;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;

public class Control {

	private final static String musicFileExtension = "json";
	
	private static Logger albumLog;	
	private static Charset charset;
	private static RunningContext musicRunningContext;	  
   	private static AdvancedProperties collectionProperties;
	private static Path collectionDirectoryName;
	private static Path concertDirectoryName;
   	
	public static void initControl(String propFile) {
		
		try {
			// access to properties and logger
			musicRunningContext = new RunningContext("CollectionMusique", null, new URI(propFile));
		
			collectionProperties = musicRunningContext.getProps();
		    albumLog = musicRunningContext.getpLog();
		    albumLog.info("Properties taken from " + musicRunningContext.getPropertiesLocation());
				
			// Get CharSet to read music files
			charset = Charset.forName(collectionProperties.getProperty("rapport.charset", "UTF-8"));
						
			// Get the root directory for the album collection and concert
			collectionDirectoryName = collectionProperties.getPathFromURI("album.rootDir.name");
			concertDirectoryName 	= collectionProperties.getPathFromURI("concert.rootDir.name");
			
		} catch (URISyntaxException e) {
			System.out.println("URI syntax exception for property file: " + propFile);
			e.printStackTrace();
			collectionProperties = null;
		}
	}

	public static Logger getAlbumLog() { 
		return albumLog; 
	}
	
	public static String getMusicfileExtension() { 
		return musicFileExtension;
	}
	
	public static AdvancedProperties getCollectionProperties() { 
		return collectionProperties; 
	}
	
	public static Charset getCharset() { 
		return charset; 
	}
	
	public static RunningContext getMusicRunningContext() { 
		return musicRunningContext; 
	}
	
	public static Path getCollectionDirectoryName() {
		return collectionDirectoryName;	
	}
	
	public static Path getConcertDirectoryName() {	
		return concertDirectoryName;
	}
}
