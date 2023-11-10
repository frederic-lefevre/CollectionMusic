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
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;

public class Control {

	private static final String MUSIQUE_DIRECTORY_URI = "file:///C:/FredericPersonnel/Loisirs/musique/";
	private static final String DEFAULT_PROP_FILE = MUSIQUE_DIRECTORY_URI + "RapportCollection/albumCollection.properties";
	
	private static final String MUSIC_FILE_EXTENSION = "json";
	
	private static Logger albumLog;	
	private static Charset charset;
	private static RunningContext musicRunningContext;	  
   	private static AdvancedProperties collectionProperties;
	private static Path collectionDirectoryName;
	private static Path concertDirectoryName;
	private static boolean initialized = false;
	private static Path audioFileRootPath;
	private static Path videoFileRootPath;
	private static List<OsAction> osActions;
   	
	private Control() {
	}
	
	public static void initControl() {
		
		try {
			// access to properties and logger
			musicRunningContext = new RunningContext("CollectionMusique", null, new URI(DEFAULT_PROP_FILE));
		
			collectionProperties = musicRunningContext.getProps();
		    albumLog = musicRunningContext.getpLog();
		    albumLog.info("Properties taken from " + musicRunningContext.getPropertiesLocation());
				
			// Get CharSet to read music files and write rapport
		    String cs = collectionProperties.getProperty("rapport.charset", "UTF-8");
			if (Charset.isSupported(cs)) {
				charset = Charset.forName(cs) ;
			} else {
				charset = Charset.defaultCharset() ;
				albumLog.severe("Unsupported charset: " + cs + ". Default JVM charset assumed: " + charset) ;				
			}
		
			// Get the root directory for the album collection and concert
			collectionDirectoryName = collectionProperties.getPathFromURI("album.rootDir.name");
			concertDirectoryName 	= collectionProperties.getPathFromURI("concert.rootDir.name");
			
			audioFileRootPath = collectionProperties.getPathFromURI("album.audioFile.rootPath");
			videoFileRootPath = collectionProperties.getPathFromURI("album.videoFile.rootPath");
			
			String osCmdPropBase = "album.command.";
			osActions = collectionProperties.getKeysElements("album.command.").stream()
				.map(prop -> new OsAction(
						collectionProperties.getProperty(osCmdPropBase + prop + ".title"), 
						collectionProperties.getProperty(osCmdPropBase + prop + ".cmd")))
				.collect(Collectors.toList());
			
		} catch (URISyntaxException e) {
			System.out.println("URI syntax exception for property file: " + DEFAULT_PROP_FILE);
			e.printStackTrace();
			collectionProperties = null;
		}
		initialized = true;
	}

	public static Logger getAlbumLog() {
		if (!initialized) {
			initControl();
		}
		return albumLog; 
	}
	
	public static String getMusicfileExtension() {
		return MUSIC_FILE_EXTENSION;
	}
	
	public static AdvancedProperties getCollectionProperties() { 
		if (!initialized) {
			initControl();
		}
		return collectionProperties; 
	}
	
	public static Charset getCharset() {
		if (!initialized) {
			initControl();
		}
		return charset; 
	}
	
	public static RunningContext getMusicRunningContext() {
		if (!initialized) {
			initControl();
		}
		return musicRunningContext; 
	}
	
	public static Path getCollectionDirectoryName() {
		if (!initialized) {
			initControl();
		}
		return collectionDirectoryName;	
	}
	
	public static Path getConcertDirectoryName() {
		if (!initialized) {
			initControl();
		}
		return concertDirectoryName;
	}
	
	public static Path getAudioFileRootPath() {
		if (!initialized) {
			initControl();
		}
		return audioFileRootPath;
	}
	
	public static Path getVideoFileRootPath() {
		if (!initialized) {
			initControl();
		}
		return videoFileRootPath;
	}
	
	public static List<OsAction> getOsActions() {
		if (!initialized) {
			initControl();
		}
		return osActions;
	}
}
