/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

package org.fl.collectionAlbum.rapportHtml;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.util.AdvancedProperties;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

public class RapportStructuresAndNames {

	private final static Logger rapportLog = Logger.getLogger(RapportStructuresAndNames.class.getName());
	
	private final static String albumDir 		   = "albums";
	private final static String concertDir 		   = "concerts";
	private final static String artisteAlbumsDir   = "artistes/albums";
	private final static String artisteConcertsDir = "artistes/concerts";
	private final static String lieuDir 		   = "lieux";
   	private final static String homeCollectionFile = "index.html";
   	private final static String homeConcertFile    = "indexConcert.html";
   	private final static String homeCsvDir		   = "rapportCsv";
   	private final static String csvAudioFiles      = homeCsvDir + "/audioFiles.csv";
   	private final static String csvHdAudioFiles    = homeCsvDir + "/highResAudioFiles.csv";
   	private final static String buildInfoFile	   = "buildInfo.html";

	private static Path 		rapportPath;
	private static Path 		oldRapportPath;
	private static HtmlLinkList accueils;
	private static String 		concertTicketImgUri;
	private static String 		musicartefactInfosUri;

	private static RapportMap<Album> 		albumRapportPaths;
	private static RapportMap<Artiste> 		artisteAlbumRapportPaths;
	private static RapportMap<Artiste> 		artisteConcertRapportPaths;
	private static RapportMap<Concert>  	concertRapportPaths;
	private static RapportMap<LieuConcert> 	lieuRapportPaths;
	
	public static void init() {

		AdvancedProperties collectionProperties = Control.getCollectionProperties();
		
		rapportPath = collectionProperties.getPathFromURI("album.rapportDirectory.name");
		oldRapportPath = collectionProperties.getPathFromURI("album.oldRapportDirectory.name");

		accueils = new HtmlLinkList();
		accueils.addLink("Accueil Collection", homeCollectionFile);
		accueils.addLink("Accueil Concert", homeConcertFile);

		// get the concert ticket image path
		concertTicketImgUri = collectionProperties.getProperty("concert.ticketImgDir.name") ;	

		// get the path of additional information for concerts and albums
		musicartefactInfosUri = collectionProperties.getProperty("musicArtefact.information.rootDir.name") ;

		albumRapportPaths   	   = new RapportMap<>(rapportPath, getAbsoluteAlbumDir()) ;
		artisteAlbumRapportPaths   = new RapportMap<>(rapportPath, getAbsoluteArtisteAlbumDir()) ;
		artisteConcertRapportPaths = new RapportMap<>(rapportPath, getAbsoluteArtisteConcertDir()) ;
		concertRapportPaths 	   = new RapportMap<>(rapportPath, getAbsoluteConcertDir()) ;
		lieuRapportPaths		   = new RapportMap<>(rapportPath, getAbsoluteLieuDir()) ;
	}

	public static Path 	  getRapportPath() 				  {	return rapportPath;							   }
	public static Path 	  getOldRapportPath() 			  {	return oldRapportPath;						   }
	public static Path 	  getAbsoluteAlbumDir() 		  {	return rapportPath.resolve(albumDir);		   }	
	public static Path 	  getAbsoluteConcertDir() 		  {	return rapportPath.resolve(concertDir);		   }	
	public static Path    getAbsoluteArtisteAlbumDir() 	  {	return rapportPath.resolve(artisteAlbumsDir);  }
	public static Path    getAbsoluteArtisteConcertDir()  {	return rapportPath.resolve(artisteConcertsDir);}
	public static Path 	  getAbsoluteLieuDir() 		  	  {	return rapportPath.resolve(lieuDir);		   }
	public static Path 	  getAbsoluteHomeCollectionFile() { return rapportPath.resolve(homeCollectionFile);}	
	public static Path 	  getAbsoluteHomeConcertFile() 	  {	return rapportPath.resolve(homeConcertFile);   }
	public static Path 	  getAbsoluteCsvAudioFiles() 	  {	return rapportPath.resolve(csvAudioFiles);	   }
	public static Path 	  getAbsoluteCsvHdAudioFiles() 	  {	return rapportPath.resolve(csvHdAudioFiles);   }
	public static Path 	  getAbsoluteBuildInfoFile() 	  { return rapportPath.resolve(buildInfoFile);	   }
	
	private static String  getMusicartefactInfosUri() 	  { return musicartefactInfosUri;}	
	private static String  getConcertTicketImgUri() 	  { return concertTicketImgUri;	 }
	
	public static HtmlLinkList getAccueils() {		return accueils;	}	
	
	public static URI getAlbumRapportRelativeUri(Album album) {
		if (album.additionnalInfo()) {
			return albumRapportPaths.getUri(album) ;
		} else {
			return null ;
		}
	}
	
	public static Path getAlbumRapportAbsolutePath(Album album) {
		if (album.additionnalInfo()) {
			return rapportPath.resolve(getAlbumRapportRelativeUri(album).getPath());
		} else {
			return null ;
		}
	}
	
	public static URI getConcertRapportRelativeUri(Concert concert) {
		if (concert.additionnalInfo()) {
			return concertRapportPaths.getUri(concert) ;
		} else {
			return null ;
		}
	}

	public static Path getConcertRapportAbsolutePath(Concert concert) {
		if (concert.additionnalInfo()) {
			return rapportPath.resolve(getConcertRapportRelativeUri(concert).getPath());
		} else {
			return null ;
		}
	}
	
	public static URI getLieuRapportRelativeUri(LieuConcert lieuConcert) {
		return lieuRapportPaths.getUri(lieuConcert) ;
	}

	public static Path getLieuRapportAbsolutePath(LieuConcert lieuConcert) {
		return rapportPath.resolve(getLieuRapportRelativeUri(lieuConcert).getPath());
	}
	
	public static URI getArtisteAlbumRapportRelativeUri(Artiste artiste) {
		return artisteAlbumRapportPaths.getUri(artiste) ;
	}

	public static Path getArtisteAlbumRapportAbsolutePath(Artiste artiste) {
		return rapportPath.resolve(getArtisteAlbumRapportRelativeUri(artiste).getPath()) ;
	}
	
	public static URI getArtisteConcertRapportRelativeUri(Artiste artiste) {
		return artisteConcertRapportPaths.getUri(artiste) ;
	}
	
	public static Path getArtisteConcertRapportAbsolutePath(Artiste artiste) {
		return rapportPath.resolve(getArtisteConcertRapportRelativeUri(artiste).getPath()) ;
	}
	
	public static URI getTicketImageAbsoluteUri(String relativeToPhotoDirUriStr) {
		return getUri(getConcertTicketImgUri(), relativeToPhotoDirUriStr) ;
	}
	
	public static URI getArtefactInfosAbsoluteUri(String relativeToInfosDirUriStr) {
		return getUri(getMusicartefactInfosUri(), relativeToInfosDirUriStr) ;
	}
	
	private static URI getUri(String rootPath, String relativeDirUriStr) {
		try {
			URI absoluteUri = new URI(rootPath + relativeDirUriStr) ;
			// check that the file exists
			if (! (Files.exists(Paths.get(absoluteUri)))) {
				rapportLog.warning("Le fichier suivant n'existe pas: " + absoluteUri.toString()) ;
			}
			return absoluteUri ;
		} catch (Exception e) {
			rapportLog.log(Level.SEVERE, "Wrong URI string for file: " + relativeDirUriStr, e);
			return null ;
		}
	}
}
