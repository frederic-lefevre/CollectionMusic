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


package org.fl.collectionAlbum.rapportHtml;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.util.AdvancedProperties;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

public class RapportStructuresAndNames {

	private final static String albumDir 		   = "albums" ;
	private final static String concertDir 		   = "concerts" ;
	private final static String artisteAlbumsDir   = "artistes/albums" ;
	private final static String artisteConcertsDir = "artistes/concerts" ;
	private final static String lieuDir 		   = "lieux" ;
   	private final static String homeCollectionFile = "index.html" ;
   	private final static String homeConcertFile    = "indexConcert.html" ;
   	private final static String homeCsvDir		   = "rapportCsv";
   	private final static String csvAudioFiles      = homeCsvDir + "/audioFiles.csv";
   	private final static String csvHdAudioFiles    = homeCsvDir + "/highResAudioFiles.csv";

	private static Path 		rapportPath ;
	private static Path 		oldRapportPath ;
	private static HtmlLinkList accueils ;
	private static String 		concertTicketImgUri ;
	private static String 		musicartefactInfosUri ;

	private static Charset 		charset ;

	private static RapportMap<Album> 		albumRapportPaths ;
	private static RapportMap<Artiste> 		artisteAlbumRapportPaths ;
	private static RapportMap<Artiste> 		artisteConcertRapportPaths ;
	private static RapportMap<Concert>  	concertRapportPaths ;
	private static RapportMap<LieuConcert> 	lieuRapportPaths ;
	
	private static Logger rapportLog ;
	
	public static void init() {
		
		rapportLog = Control.getAlbumLog() ;
		AdvancedProperties collectionProperties = Control.getCollectionProperties() ;
		
		rapportPath 		 = collectionProperties.getPathFromURI("album.rapportDirectory.name") ;
		oldRapportPath		 = collectionProperties.getPathFromURI("album.oldRapportDirectory.name") ;

		accueils = new HtmlLinkList() ;
		accueils.addLink("Accueil Collection", homeCollectionFile) ;
		accueils.addLink("Accueil Concert",    homeConcertFile) ;

		// get the concert ticket image path
		concertTicketImgUri = collectionProperties.getProperty("concert.ticketImgDir.name") ;	

		// get the path of additional information for concerts and albums
		musicartefactInfosUri = collectionProperties.getProperty("musicArtefact.information.rootDir.name") ;
		
		// Get charset to write rapport
		charset = Charset.forName(collectionProperties.getProperty("rapport.charset", "UTF-8")) ;
		RapportHtml.withCharset(charset.name(), rapportLog);
			
		albumRapportPaths   	   = new RapportMap<>(rapportPath, getAbsoluteAlbumDir()) ;
		artisteAlbumRapportPaths   = new RapportMap<>(rapportPath, getAbsoluteArtisteAlbumDir()) ;
		artisteConcertRapportPaths = new RapportMap<>(rapportPath, getAbsoluteArtisteConcertDir()) ;
		concertRapportPaths 	   = new RapportMap<>(rapportPath, getAbsoluteConcertDir()) ;
		lieuRapportPaths		   = new RapportMap<>(rapportPath, getAbsoluteLieuDir()) ;
	}

	public static void createRapports(ListeArtiste 	listeArtiste, 
							   ListeAlbum 		listeAlbum, 
							   ListeConcert 	listeConcert, 
							   LieuxDesConcerts lieuxDesConcerts) {

		for (Artiste artiste : listeArtiste.getArtistes()) {	
			if (artiste.getNbAlbum() > 0) {
				URI artisteAlbumUri   = artisteAlbumRapportPaths.getUri(artiste) ;
				Path albumAbsolutePath   = rapportPath.resolve(artisteAlbumUri.getPath()) ;
				if (! Files.exists(albumAbsolutePath)) {
					RapportAlbumsDunArtiste rapportDeSesAlbums = new RapportAlbumsDunArtiste(artiste, getOffset(rapportPath, albumAbsolutePath.getParent()) , rapportLog) ;
					rapportDeSesAlbums.printReport(albumAbsolutePath, CssStyles.stylesTableauDunArtiste) ;
				}
			}
			
			if (artiste.getNbConcert() > 0) {
				URI artisteConcertUri = artisteConcertRapportPaths.getUri(artiste) ;
				Path concertAbsolutePath = rapportPath.resolve(artisteConcertUri.getPath()) ;
				if (! Files.exists(concertAbsolutePath)) {
					 RapportConcertsDunArtiste rapportDeSesConcerts = new RapportConcertsDunArtiste(artiste, getOffset(rapportPath, concertAbsolutePath.getParent()), rapportLog) ;
					 rapportDeSesConcerts.printReport(concertAbsolutePath, CssStyles.stylesTableauDunArtiste) ;
				}
			}
		}
		
		for (Album album : listeAlbum.getAlbums()) {
			if (album.additionnalInfo()) {
				URI albumUri = albumRapportPaths.getUri(album) ;
				Path absolutePath = rapportPath.resolve(albumUri.getPath()) ;
				if (! Files.exists(absolutePath)) {
					RapportAlbum rapportAlbum = new RapportAlbum(album, rapportLog) ;
					rapportAlbum.withOffset( getOffset(rapportPath, absolutePath.getParent())) ;
					rapportAlbum.printReport( absolutePath, CssStyles.main) ;
				}
			}
		}
		for (Concert concert : listeConcert.getConcerts()) {
			if (concert.additionnalInfo()) {
				URI concertUri = concertRapportPaths.getUri(concert) ;
				Path absolutePath = rapportPath.resolve(concertUri.getPath()) ;
				if (! Files.exists(absolutePath)) {
	        		RapportConcert rapportConcert = new RapportConcert(concert, rapportLog) ;
	        		rapportConcert.withOffset(getOffset(rapportPath, absolutePath.getParent())) ;
	        		rapportConcert.printReport(absolutePath, CssStyles.ticket) ;
	        	}
			}
		}
		for (LieuConcert lieuConcert : lieuxDesConcerts.getLieuxConcerts()) {
			URI lieuUri = lieuRapportPaths.getUri(lieuConcert) ;
			Path absolutePath = rapportPath.resolve(lieuUri.getPath()) ;
			if (! Files.exists(absolutePath)) {
				String offSet = getOffset(rapportPath, absolutePath.getParent()) ;
				RapportListeConcerts concertDeCeLieu = new RapportListeConcerts(lieuConcert.getConcerts().sortChrono(), lieuConcert.getLieu(), rapportLog) ;
				concertDeCeLieu.withOffset(offSet);
				concertDeCeLieu.printReport(absolutePath, CssStyles.stylesTableauMusicArtefact) ;
			}
		}
	}
	
	private static final String OFFSET_ELEMENT = "../" ;
	
	private static String getOffset(Path rootPath, Path targetPath) {
		
		int diffPath = targetPath.getNameCount() - rootPath.getNameCount() ;	
		if (diffPath <= 0) {
			return "" ;
		} else {
			return getOffset(rootPath, targetPath.getParent()) + OFFSET_ELEMENT ;
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
	public static Path 	  getAbsoluteCsvAudioFiles() 	  {	return rapportPath.resolve(csvAudioFiles);		}
	public static Path 	  getAbsoluteCsvHdAudioFiles() 	  {	return rapportPath.resolve(csvHdAudioFiles);	}
	
	private static String  getMusicartefactInfosUri() 	  { return musicartefactInfosUri;					}	
	private static String  getConcertTicketImgUri() 	  { return concertTicketImgUri;						}
	
	public static HtmlLinkList getAccueils() {		return accueils;	}	
	
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

	public static URI getArtisteAlbumRapportRelativePath(Artiste artiste) {
		return artisteAlbumRapportPaths.getUri(artiste) ;
	}

	public static URI getArtisteConcertRapportRelativePath(Artiste artiste) {
		return artisteConcertRapportPaths.getUri(artiste) ;
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
