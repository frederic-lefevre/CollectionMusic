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

package org.fl.collectionAlbum.rapportHtml;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuConcert;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

public class RapportStructuresAndNames {

	private static final Logger rapportLog = Logger.getLogger(RapportStructuresAndNames.class.getName());
	
	private static final String albumDir = "albums";
	private static final String concertDir = "concerts";
	private static final String artisteAlbumsDir = "artistes/albums";
	private static final String artisteConcertsDir = "artistes/concerts";
	private static final String lieuDir = "lieux";
	private static final String homeCollectionFile = "index.html";
	private static final String homeConcertFile = "indexConcert.html";
	private static final String homeCsvDir = "rapportCsv";
	private static final String csvAudioFiles = homeCsvDir + "/audioFiles.csv";
	private static final String csvHdAudioFiles = homeCsvDir + "/highResAudioFiles.csv";
	private static final String buildInfoFile = "buildInfo.html";

	private static final String dateFrancePattern = "EEEE dd MMMM uuuu à HH:mm";
	
	private static final String L_LIST2 = "  <span  class=\"dategen\">Généré ";
	private static final String L_LIST3 = "</span><br/>\n";
	
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFrancePattern, Locale.FRANCE);
	
	private static RapportStructuresAndNames instance;
	
	private final Path rapportPath;
	private final Path oldRapportPath;
	private final String concertTicketImgUri;
	private final String musicartefactInfosUri;

	private RapportMap<Album> albumRapportPaths;
	private RapportMap<Artiste> artisteAlbumRapportPaths;
	private RapportMap<Artiste> artisteConcertRapportPaths;
	private RapportMap<Concert> concertRapportPaths;
	private RapportMap<LieuConcert> lieuRapportPaths;
	
	private HtmlLinkList accueils;
	
	private static RapportStructuresAndNames getInstance() {
		if (instance == null) {
			instance = new RapportStructuresAndNames();
		}
		return instance;
	}
	
	private RapportStructuresAndNames() {
		
		rapportPath = Control.getRapportPath();
		oldRapportPath = Control.getOldRapportPath();

		accueils = getAccueil();

		// get the concert ticket image path
		concertTicketImgUri = Control.getConcertTicketImgUri();

		// get the path of additional information for concerts and albums
		musicartefactInfosUri = Control.getMusicartefactInfosUri();

		renewRapportMap();
	}

	private static HtmlLinkList getAccueil() {
		
		HtmlLinkList acc = new HtmlLinkList();
		acc.addLink("Accueil Collection", homeCollectionFile);
		acc.addLink("Accueil Concert", homeConcertFile);
		acc.addLink(L_LIST2 + dateTimeFormatter.format(LocalDateTime.now()) + L_LIST3, buildInfoFile);
		return acc;
	}
	
	private void renewRapportMap() {
		
		albumRapportPaths = new RapportMap<>(rapportPath, rapportPath.resolve(albumDir));
		artisteAlbumRapportPaths = new RapportMap<>(rapportPath, rapportPath.resolve(artisteAlbumsDir));
		artisteConcertRapportPaths = new RapportMap<>(rapportPath, rapportPath.resolve(artisteConcertsDir));
		concertRapportPaths = new RapportMap<>(rapportPath, rapportPath.resolve(concertDir));
		lieuRapportPaths = new RapportMap<>(rapportPath, rapportPath.resolve(lieuDir));
	}
	
	public static void renew() {
		getInstance().accueils = getAccueil();
		getInstance().renewRapportMap();
	}
	
	public static Path getRapportPath() {
		return getInstance().rapportPath;
	}

	public static Path getOldRapportPath() {
		return getInstance().oldRapportPath;
	}

	public static Path getAbsoluteAlbumDir() {
		return getInstance().rapportPath.resolve(albumDir);
	}

	public static Path getAbsoluteConcertDir() {
		return getInstance().rapportPath.resolve(concertDir);
	}

	public static Path getAbsoluteArtisteAlbumDir() {
		return getInstance().rapportPath.resolve(artisteAlbumsDir);
	}

	public static Path getAbsoluteArtisteConcertDir() {
		return getInstance().rapportPath.resolve(artisteConcertsDir);
	}

	public static Path getAbsoluteLieuDir() {
		return getInstance().rapportPath.resolve(lieuDir);
	}

	public static Path getAbsoluteHomeCollectionFile() {
		return getInstance().rapportPath.resolve(homeCollectionFile);
	}

	public static String getAbsoluteHomeCollectionUrl() {
		return getAbsoluteHomeCollectionFile().toUri().toString();
	}
	
	public static Path getAbsoluteHomeConcertFile() {
		return getInstance().rapportPath.resolve(homeConcertFile);
	}

	public static Path getAbsoluteCsvAudioFiles() {
		return getInstance().rapportPath.resolve(csvAudioFiles);
	}

	public static Path getAbsoluteCsvHdAudioFiles() {
		return getInstance().rapportPath.resolve(csvHdAudioFiles);
	}

	public static Path getAbsoluteBuildInfoFile() {
		return getInstance().rapportPath.resolve(buildInfoFile);
	}
	
	private static String getMusicartefactInfosUri() {
		return getInstance().musicartefactInfosUri;
	}

	private static String getConcertTicketImgUri() {
		return getInstance().concertTicketImgUri;
	}

	public static HtmlLinkList getAccueils() {
		return getInstance().accueils;
	}

	public static URI getAlbumRapportRelativeUri(Album album) {
		if (album.hasAdditionnalInfo()) {
			return getInstance().albumRapportPaths.getUri(album);
		} else {
			return null;
		}
	}

	public static Path getAlbumRapportAbsolutePath(Album album) {
		if (album.hasAdditionnalInfo()) {
			return getInstance().rapportPath.resolve(getAlbumRapportRelativeUri(album).getPath());
		} else {
			return null;
		}
	}

	public static URI getConcertRapportRelativeUri(Concert concert) {
		if (concert.hasAdditionnalInfo()) {
			return getInstance().concertRapportPaths.getUri(concert);
		} else {
			return null;
		}
	}

	public static Path getConcertRapportAbsolutePath(Concert concert) {
		if (concert.hasAdditionnalInfo()) {
			return getInstance().rapportPath.resolve(getConcertRapportRelativeUri(concert).getPath());
		} else {
			return null;
		}
	}

	public static URI getLieuRapportRelativeUri(LieuConcert lieuConcert) {
		return getInstance().lieuRapportPaths.getUri(lieuConcert);
	}

	public static Path getLieuRapportAbsolutePath(LieuConcert lieuConcert) {
		return getInstance().rapportPath.resolve(getLieuRapportRelativeUri(lieuConcert).getPath());
	}

	public static URI getArtisteAlbumRapportRelativeUri(Artiste artiste) {
		return getInstance().artisteAlbumRapportPaths.getUri(artiste);
	}

	public static Path getArtisteAlbumRapportAbsolutePath(Artiste artiste) {
		return getInstance().rapportPath.resolve(getArtisteAlbumRapportRelativeUri(artiste).getPath());
	}

	public static URI getArtisteConcertRapportRelativeUri(Artiste artiste) {
		return getInstance().artisteConcertRapportPaths.getUri(artiste);
	}

	public static Path getArtisteConcertRapportAbsolutePath(Artiste artiste) {
		return getInstance().rapportPath.resolve(getArtisteConcertRapportRelativeUri(artiste).getPath());
	}

	public static URI getTicketImageAbsoluteUri(String relativeToPhotoDirUriStr) {
		return getUri(getConcertTicketImgUri(), relativeToPhotoDirUriStr);
	}

	public static URI getArtefactInfosAbsoluteUri(String relativeToInfosDirUriStr) {
		return getUri(getMusicartefactInfosUri(), relativeToInfosDirUriStr);
	}

	private static URI getUri(String rootPath, String relativeDirUriStr) {
		try {
			URI absoluteUri = new URI(rootPath + relativeDirUriStr);
			// check that the file exists
			if (!(Files.exists(Paths.get(absoluteUri)))) {
				rapportLog.warning("Le fichier suivant n'existe pas: " + absoluteUri.toString());
			}
			return absoluteUri;
		} catch (Exception e) {
			rapportLog.log(Level.SEVERE, "Wrong URI string for file: " + relativeDirUriStr, e);
			return null;
		}
	}
}
