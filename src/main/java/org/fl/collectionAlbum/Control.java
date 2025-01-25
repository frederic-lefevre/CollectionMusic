/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.osAction.AlbumCommandParameter;
import org.fl.collectionAlbum.osAction.DiscogsReleaseCommandParameter;
import org.fl.collectionAlbum.osAction.ListOfStringCommandParameter;
import org.fl.collectionAlbum.osAction.MediaFilePathCommandParameter;
import org.fl.collectionAlbum.osAction.OsAction;
import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;

public class Control {

	private static final String MUSIQUE_DIRECTORY_URI = "file:///C:/FredericPersonnel/Loisirs/musique/";
	private static final String DEFAULT_PROP_FILE = MUSIQUE_DIRECTORY_URI + "RapportCollection/albumCollection.properties";
	
	private static final String MUSIC_FILE_EXTENSION = "json";
	
	private static final Logger albumLog = Logger.getLogger(Control.class.getName());
	
	private static Control controlInstance;
	
	private Charset charset;
	private RunningContext musicRunningContext;	  
   	private AdvancedProperties collectionProperties;
	private Path collectionDirectoryName;
	private Path concertDirectoryName;
	private Path rapportPath;
	private Path oldRapportPath;
	private String concertTicketImgUri;
	private String musicartefactInfosUri;
	private Map<ContentNature,Path> mediaFileRootPaths;
	private List<OsAction<Album>> osActionsOnAlbum;
	private List<OsAction<DiscogsAlbumRelease>> osActionsOnDiscogsRelease;
	private List<OsAction<MediaFilePath>> osActionsOnMediaFilePath;
	private OsAction<List<String>> displayUrlAction;
	private Path discogsCollectionCsvExportPath;
	private String discogsBaseUrlForRelease;
   	
	private Control() {
		
		try {
			// access to properties and logger
			musicRunningContext = new RunningContext("org.fl.collectionAlbum", null, new URI(DEFAULT_PROP_FILE));
		
			collectionProperties = musicRunningContext.getProps();
		    albumLog.info("Properties taken from " + musicRunningContext.getPropertiesLocation());
				
			// Get CharSet to read music files and write rapport
		    String cs = collectionProperties.getProperty("rapport.charset", "UTF-8");
			if (Charset.isSupported(cs)) {
				charset = Charset.forName(cs);
			} else {
				charset = Charset.defaultCharset();
				albumLog.severe("Unsupported charset: " + cs + ". Default JVM charset assumed: " + charset);				
			}
		
			// Get the root directory for the album collection and concert
			collectionDirectoryName = collectionProperties.getPathFromURI("album.rootDir.name");
			concertDirectoryName = collectionProperties.getPathFromURI("concert.rootDir.name");
			
			// Get path and URI for the rapport
			rapportPath = collectionProperties.getPathFromURI("album.rapportDirectory.name");
			oldRapportPath = collectionProperties.getPathFromURI("album.oldRapportDirectory.name");
			
			// get the concert ticket image path
			concertTicketImgUri = collectionProperties.getProperty("concert.ticketImgDir.name");	

			// get the path of additional information for concerts and albums
			musicartefactInfosUri = collectionProperties.getProperty("musicArtefact.information.rootDir.name");
			
			
			discogsCollectionCsvExportPath = collectionProperties.getPathFromURI("album.discogs.collection.csvExport");
			discogsBaseUrlForRelease = collectionProperties.getProperty("album.discogs.baseUrl.release");
			
			mediaFileRootPaths = new HashMap<>();
			Stream.of(ContentNature.values())
				.forEach(contentNature -> 
					mediaFileRootPaths.put(
							contentNature, 
							collectionProperties.getPathFromURI("album." + contentNature.name() + "File.rootPath")));
				
			osActionsOnAlbum = getOsActionsOnAlbum("album.command.");
			osActionsOnDiscogsRelease = getOsActionsOnDiscogsRelease("album.discogs.command.");
			osActionsOnMediaFilePath = getOsActionOnMediaFilePath("album.mediaFile.command.");
			
			displayUrlAction = new OsAction<List<String>>(
					collectionProperties.getProperty("album.showUrl.command.title"), 
					collectionProperties.getProperty("album.showUrl.command.cmd"),
					collectionProperties.getListOfString("album.showUrl.command.options", ","),
					new ListOfStringCommandParameter());
			
		} catch (URISyntaxException e) {
			System.out.println("URI syntax exception for property file: " + DEFAULT_PROP_FILE);
			e.printStackTrace();
			collectionProperties = null;
		}
	}
	
	private static Control getInstance() {
		if (controlInstance == null) {
			controlInstance = new Control();
		}
		return controlInstance;
	}
	
	public static String getMusicfileExtension() {
		return MUSIC_FILE_EXTENSION;
	}
	
	public static Charset getCharset() {
		return getInstance().charset; 
	}
	
	public static RunningContext getMusicRunningContext() {
		return getInstance().musicRunningContext; 
	}
	
	public static Path getCollectionDirectoryName() {
		return getInstance().collectionDirectoryName;	
	}
	
	public static Path getConcertDirectoryName() {
		return getInstance().concertDirectoryName;
	}
	
	public static Path getRapportPath() {
		return getInstance().rapportPath;
	}
	
	public static Path getOldRapportPath() {
		return getInstance().oldRapportPath;
	}
	
	public static String getConcertTicketImgUri() {
		return getInstance().concertTicketImgUri;
	}
	
	public static String getMusicartefactInfosUri() {
		return getInstance(). musicartefactInfosUri;
	}
	
	public static Path getMediaFileRootPath(ContentNature contentNature) {
		return getInstance().mediaFileRootPaths.get(contentNature);
	}
	
	public static List<OsAction<Album>> getOsActionsOnAlbum() {
		return getInstance().osActionsOnAlbum;
	}

	public static List<OsAction<DiscogsAlbumRelease>> getOsActionOnDiscogsRelease() {
		return getInstance().osActionsOnDiscogsRelease;
	}
	
	public static List<OsAction<MediaFilePath>> getOsActionOnMediaFilePath() {
		return getInstance().osActionsOnMediaFilePath;
	}
	
	public static OsAction<List<String>> getDisplayUrlAction() {
		return getInstance().displayUrlAction;
	}
	
	public static Path getDiscogsCollectionCsvExportPath() {
		return getInstance().discogsCollectionCsvExportPath;
	}

	public static String getDiscogsBaseUrlForRelease() {
		return getInstance().discogsBaseUrlForRelease;
	}
	
	private List<OsAction<Album>> getOsActionsOnAlbum(String osCmdPropBase) {

		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new OsAction<Album>(collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						collectionProperties.getProperty(osCmdPropBase + prop + ".cmd"),
						collectionProperties.getListOfString(osCmdPropBase + prop + ".options", ","),
						AlbumCommandParameter.valueOf(collectionProperties.getProperty(osCmdPropBase + prop + ".param"))))
				.collect(Collectors.toList());
	}
	
	private List<OsAction<DiscogsAlbumRelease>> getOsActionsOnDiscogsRelease(String osCmdPropBase) {

		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new OsAction<DiscogsAlbumRelease>(collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						collectionProperties.getProperty(osCmdPropBase + prop + ".cmd"),
						collectionProperties.getListOfString(osCmdPropBase + prop + ".options", ","),
						DiscogsReleaseCommandParameter.valueOf(collectionProperties.getProperty(osCmdPropBase + prop + ".param"))))
				.collect(Collectors.toList());
	}
	
	private List<OsAction<MediaFilePath>> getOsActionOnMediaFilePath(String osCmdPropBase) {
		
		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new OsAction<MediaFilePath>(collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						collectionProperties.getProperty(osCmdPropBase + prop + ".cmd"),
						collectionProperties.getListOfString(osCmdPropBase + prop + ".options", ","),
						MediaFilePathCommandParameter.valueOf(collectionProperties.getProperty(osCmdPropBase + prop + ".param"))))
				.collect(Collectors.toList());
	}
}
