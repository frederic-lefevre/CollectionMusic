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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.CollectionAlbumGui;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.metrics.CollectionMetricsHistory;
import org.fl.collectionAlbum.metrics.ConcertMetricsHistory;
import org.fl.collectionAlbum.osAction.MusicArtefactCommandParameter;
import org.fl.collectionAlbum.osAction.DiscogsReleaseCommandParameter;
import org.fl.collectionAlbum.osAction.ListOfStringCommandParameter;
import org.fl.collectionAlbum.osAction.StringCommandParameter;
import org.fl.collectionAlbum.osAction.MediaFilePathCommandParameter;
import org.fl.collectionAlbum.osAction.OsAction;
import org.fl.collectionAlbum.osAction.OsCommandAndOption;
import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;
import org.fl.util.file.FilesUtils;

public class Control {
	
	private static final String MUSIC_FILE_EXTENSION = "json";
	
	private static final Logger albumLog = Logger.getLogger(Control.class.getName());
	
	private static Control controlInstance;
	
	private Charset charset;
	private RunningContext musicRunningContext;	  
   	private AdvancedProperties collectionProperties;
	private Path collectionDirectoryName;
	private Path concertDirectoryName;
	private CollectionMetricsHistory collectionMetricsHsitory;
	private ConcertMetricsHistory concertMetricsHsitory;
	private Path rapportPath;
	private Path oldRapportPath;
	private String albumSleevesImgUri;
	private String concertTicketImgUri;
	private String musicartefactInfosUri;
	private Map<ContentNature,Path> mediaFileRootPaths;
	private Map<String, OsCommandAndOption> mapOfOsCommandsAndOptions;
	private List<OsAction<Album>> osActionsOnAlbum;
	private List<OsAction<Concert>> osActionsOnConcert;
	private List<OsAction<DiscogsAlbumRelease>> osActionsOnDiscogsRelease;
	private List<OsAction<MediaFilePath>> osActionsOnMediaFilePath;
	private OsAction<List<String>> displayUrlAction;
	private OsAction<String> displayFolderAction;
	private Path discogsCollectionCsvExportPath;
	private String discogsBaseUrlForRelease;
	private String cssForGui;
   	
	private Control() {
	}
	
	private Control(String propertyFile) {
		
		try {
			// access to properties and logger
			musicRunningContext = new RunningContext("org.fl.collectionAlbum", new URI(propertyFile));
			
			musicRunningContext.addBuildInformation("org.fl.discogsInterface", org.fl.discogsInterface.inventory.Inventory.class.getClassLoader());
		
			collectionProperties = musicRunningContext.getProps();
		    albumLog.fine(() -> "Properties taken from " + musicRunningContext.getPropertiesLocation());
				
			// Get CharSet to read music files and write rapport
		    String cs = collectionProperties.getProperty("rapport.charset", "UTF-8");
			if (Charset.isSupported(cs)) {
				charset = Charset.forName(cs);
			} else {
				charset = Charset.defaultCharset();
				albumLog.severe("Unsupported charset: " + cs + ". Default JVM charset assumed: " + charset);				
			}
			
			// Get the root directory for the album collection and concert
			collectionDirectoryName = FilesUtils.uriStringToAbsolutePath(collectionProperties.getProperty("album.rootDir.name"));
			concertDirectoryName = FilesUtils.uriStringToAbsolutePath(collectionProperties.getProperty("concert.rootDir.name"));
			
			// Get path and URI for the rapport
			rapportPath = FilesUtils.uriStringToAbsolutePath(collectionProperties.getProperty("album.rapportDirectory.name"));
			oldRapportPath = FilesUtils.uriStringToAbsolutePath(collectionProperties.getProperty("album.oldRapportDirectory.name"));
			
			// Get collection album and concert history folder path
			try {
				collectionMetricsHsitory = CollectionMetricsHistory.buildCollectionMetricsHistory(
						FilesUtils.uriStringToAbsolutePath(collectionProperties.getProperty("album.historyFolder.name")));
			} catch (IOException e) {
				albumLog.log(Level.SEVERE, "IOException accessinng album collection metrics history folder", e);
			}
			try {
				concertMetricsHsitory = ConcertMetricsHistory.buildConcertMetricsHistory(
						FilesUtils.uriStringToAbsolutePath(collectionProperties.getProperty("concert.historyFolder.name")));
			} catch (IOException e) {
				albumLog.log(Level.SEVERE, "IOException accessinng concert metrics history folder", e);
			}
			
			// get the album sleeves images path (this is storing the sleeves images when the sleeve image of the media files is
			// not similar to the album ones
			albumSleevesImgUri = convertToAbsoluteUriString(collectionProperties.getProperty("album.sleevesImgDir.name"));
			
			// get the concert ticket image path
			concertTicketImgUri = convertToAbsoluteUriString(collectionProperties.getProperty("concert.ticketImgDir.name"));	

			// get the path of additional information for concerts and albums
			musicartefactInfosUri = convertToAbsoluteUriString(collectionProperties.getProperty("musicArtefact.information.rootDir.name"));
						
			discogsCollectionCsvExportPath = FilesUtils.uriStringToAbsolutePath(collectionProperties.getProperty("album.discogs.collection.csvExport"));
			discogsBaseUrlForRelease = collectionProperties.getProperty("album.discogs.baseUrl.release");
			
			mediaFileRootPaths = new HashMap<>();
			Stream.of(ContentNature.values())
				.forEachOrdered(contentNature -> 
					mediaFileRootPaths.put(
							contentNature, 
							collectionProperties.getPathFromURI("album." + contentNature.name() + "File.rootPath")));
				
			mapOfOsCommandsAndOptions = getMapOfOsCommandsAndOptions("osCommandAndOptions.");
			
			// OsAaction identiques qur Album et Concert
			osActionsOnAlbum = getOsActionsOnAlbum("album.command.");
			osActionsOnConcert = getOsActionsOnConcert("album.command.");
			
			osActionsOnDiscogsRelease = getOsActionsOnDiscogsRelease("album.discogs.command.");
			osActionsOnMediaFilePath = getOsActionOnMediaFilePath("album.mediaFile.command.");
			
			displayUrlAction = new OsAction<List<String>>(
					collectionProperties.getProperty("album.showUrl.command.title"), 
					getOsCommandAndOption(collectionProperties.getProperty("album.showUrl.command.cmd")),
					new ListOfStringCommandParameter());
			
			displayFolderAction = new OsAction<String>(
					collectionProperties.getProperty("album.showFolder.command.title"), 
					getOsCommandAndOption(collectionProperties.getProperty("album.showFolder.command.cmd")),
					new StringCommandParameter());
			
			cssForGui = collectionProperties.getFileContentFromURI("album.cssForGui", Charset.defaultCharset());
						
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Exception during inintialisation, property file="  + propertyFile, e);
			collectionProperties = null;
		}
	}
	
	private static String convertToAbsoluteUriString(String uri) {
		 try {
			// Converting to absolute path may be necessary, in Windows OS case, to put the default C drive if it is not mentioned in the property value
			return FilesUtils.uriStringToAbsolutePath(uri)
				.toUri().toString();
		} catch (URISyntaxException e) {
			albumLog.log(Level.SEVERE, "Exception trying to convert to absolute uri: " + uri, e);
			return null;
		}  
	}
	
	private static Control getInstance() {
		if (controlInstance == null) {
			controlInstance = new Control(CollectionAlbumGui.getPropertyFile());
		}
		return controlInstance;
	}
	
	public static Control initialize(String propertyFile) {
		if (controlInstance == null) {
			controlInstance = new Control(propertyFile);
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
	
	public static CollectionMetricsHistory getCollectionMetricsHsitory() {
		return getInstance().collectionMetricsHsitory;
	}
	
	public static ConcertMetricsHistory getConcertMetricsHsitory() {
		return getInstance().concertMetricsHsitory;
	}
	
	public static String getAlbumSleevesImgUri() {
		return getInstance().albumSleevesImgUri;
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

	public static List<OsAction<Concert>> getOsActionsOnConcert() {
		return getInstance().osActionsOnConcert;
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
	
	public static OsAction<String> getDisplayFolderAction() {
		return getInstance().displayFolderAction;
	}
	
	public static Path getDiscogsCollectionCsvExportPath() {
		return getInstance().discogsCollectionCsvExportPath;
	}

	public static String getDiscogsBaseUrlForRelease() {
		return getInstance().discogsBaseUrlForRelease;
	}
	
	public static String getCssForGui() {
		return getInstance().cssForGui;
	}
	
	private Map<String, OsCommandAndOption> getMapOfOsCommandsAndOptions(String osCmdPropBase) {

		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new AbstractMap.SimpleEntry<String, OsCommandAndOption>(
						collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						new OsCommandAndOption(
								collectionProperties.getProperty(osCmdPropBase + prop + ".cmd"),
								collectionProperties.getListOfString(osCmdPropBase + prop + ".options", ","))))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	private OsCommandAndOption getOsCommandAndOption(String cmdTitle) {
		
		OsCommandAndOption osCommandAndOption = mapOfOsCommandsAndOptions.get(cmdTitle);
		if (osCommandAndOption != null) {
			return osCommandAndOption;
		} else {
			albumLog.severe("Missing OsCommandAndOption definition in property file: " + cmdTitle);
			return null;
		}
	}
	
	private List<OsAction<Concert>> getOsActionsOnConcert(String osCmdPropBase) {

		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new OsAction<Concert>(
						collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						getOsCommandAndOption(collectionProperties.getProperty(osCmdPropBase + prop + ".cmd")),
						new MusicArtefactCommandParameter<>(MusicArtefactCommandParameter.ParameterType.valueOf(collectionProperties.getProperty(osCmdPropBase + prop + ".param")))))
				.collect(Collectors.toList());
	}
	
	private List<OsAction<Album>> getOsActionsOnAlbum(String osCmdPropBase) {

		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new OsAction<Album>(
						collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						getOsCommandAndOption(collectionProperties.getProperty(osCmdPropBase + prop + ".cmd")),
						new MusicArtefactCommandParameter<>(MusicArtefactCommandParameter.ParameterType.valueOf(collectionProperties.getProperty(osCmdPropBase + prop + ".param")))))
				.collect(Collectors.toList());
	}
	
	private List<OsAction<DiscogsAlbumRelease>> getOsActionsOnDiscogsRelease(String osCmdPropBase) {

		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new OsAction<DiscogsAlbumRelease>(collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						getOsCommandAndOption(collectionProperties.getProperty(osCmdPropBase + prop + ".cmd")),
						DiscogsReleaseCommandParameter.valueOf(collectionProperties.getProperty(osCmdPropBase + prop + ".param"))))
				.collect(Collectors.toList());
	}
	
	private List<OsAction<MediaFilePath>> getOsActionOnMediaFilePath(String osCmdPropBase) {
		
		return collectionProperties.getKeysElements(osCmdPropBase).stream()
				.map(prop -> new OsAction<MediaFilePath>(collectionProperties.getProperty(osCmdPropBase + prop + ".title"),
						getOsCommandAndOption(collectionProperties.getProperty(osCmdPropBase + prop + ".cmd")),
						MediaFilePathCommandParameter.valueOf(collectionProperties.getProperty(osCmdPropBase + prop + ".param"))))
				.collect(Collectors.toList());
	}
}
