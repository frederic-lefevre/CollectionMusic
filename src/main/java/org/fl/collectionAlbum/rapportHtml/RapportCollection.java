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

import java.nio.file.Path;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Format;

public class RapportCollection extends RapportHtml {

	private int rapportIndex;
	private final CollectionAlbumContainer albumsContainer;
	private final Path rapportCollectionDir;

	private static final boolean DONT_APPEND_AUDIO_FILE = false;

	public RapportCollection(CollectionAlbumContainer ac, Path rFile, String titre) {
		super(titre);
		withTitleDisplayed();
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		rapportIndex = -1;
		albumsContainer = ac;
		rapportCollectionDir = rFile;
	}

	@Override
	 protected void corpsRapport() {
		   	
		write("<table>\n<tr>\n<td class=\"mainpage\">\n<h3>Classement des auteurs, interpretes et chefs d'orchestre</h3>\n<ul>\n");
		
		RapportListeArtistesAlbum rapportArtistesAlbumsAlpha = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesAlpha(),  "Classement alphabethique");
		rapportArtistesAlbumsAlpha.withBalises(new Balises(Balises.BalisesType.ALPHA));
		write(rapportArtistesAlbumsAlpha.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeArtistesAlbum rapportArtistesAlbumsPoids = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesPoidsAlbums(),  "Classement par nombre d'unit&eacute;s physiques");
		rapportArtistesAlbumsPoids.withBalises(new Balises(Balises.BalisesType.POIDS));
		write(rapportArtistesAlbumsPoids.printReport(getNextRapportFile(),  CssStyles.stylesTableauAvecBalise));

		RapportListeArtistesAlbum rapportArtistesAlbumsChrono = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesChrono(),  "Classement chronologique");
		rapportArtistesAlbumsChrono.withBalises(new Balises(Balises.BalisesType.TEMPORAL));
		write(rapportArtistesAlbumsChrono.printReport(getNextRapportFile(),  CssStyles.stylesTableauAvecBalise));

		RapportCalendrier rapportCalendrier = new RapportCalendrier(albumsContainer.getCalendrierArtistes(), "Calendrier");
		write(rapportCalendrier.printReport(getNextRapportFile(), CssStyles.stylesCalendrier));
		
		write("</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Classement des albums</h3>\n<ul>\n");
		RapportListeAlbums rapportAlbumsEnregistrement = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoEnregistrement(), "Classement chronologique (enregistrement)");
		rapportAlbumsEnregistrement.withBalises(new Balises(Balises.BalisesType.TEMPORAL));
		write(rapportAlbumsEnregistrement.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
	
		RapportListeAlbums rapportAlbumsComposition = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoComposition(), "Classement chronologique (composition)");
		rapportAlbumsComposition.withBalises(new Balises(Balises.BalisesType.TEMPORAL_COMPOSITION));
		write(rapportAlbumsComposition.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));

		write("</ul>\n</td>\n</tr>\n<tr>\n<td class=\"mainpage\">\n<h3>Rangement des albums</h3>\n<ul>\n");
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			RapportListeAlbums rapportAlbumsRangement = new RapportListeAlbums(albumsContainer.getRangementAlbums(rangement).sortRangementAlbum(), rangement.getDescription());
			write(rapportAlbumsRangement.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact));
		}

		write("</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Albums avec et sans fichier audio ou video</h3>\n<ul>\n");
		RapportListeAlbums rapportAlbumsWithAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithAudioFile().sortRangementAlbum(), "Albums avec fichiers audio");
		write(rapportAlbumsWithAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithHighResAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithHighResAudio().sortRangementAlbum(), "Albums avec fichier audio haute résolution");
		write(rapportAlbumsWithHighResAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithLowResAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithLowResAudio().sortRangementAlbum(), "Albums avec fichier audio avec perte (basse qualité)");
		write(rapportAlbumsWithLowResAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithoutAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsMissingAudioFile().sortRangementAlbum(), "Albums manquant de fichier audio");
		write(rapportAlbumsWithoutAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithVideoFile = new RapportListeAlbums(albumsContainer.getAlbumsWithVideoFile().sortRangementAlbum(), "Albums avec fichiers video");
		write(rapportAlbumsWithVideoFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithoutVideoFile = new RapportListeAlbums(albumsContainer.getAlbumsMissingVideoFile().sortRangementAlbum(), "Albums manquant de fichier video");
		write(rapportAlbumsWithoutVideoFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		write("</ul>\n</td>\n</tr>\n<tr>\n<td colspan=2>\n<h3>Statistiques</h3>\n</td>\n</tr>\n<tr>\n<td class=\"mainpage\">\n<ul>\n");
		RapportStat rapportStat1 = new RapportStat(albumsContainer.getStatChronoEnregistrement(), "Statistiques par année d'enregistrement");
		write(rapportStat1.printReport(getNextRapportFile(), CssStyles.stylesStat));

		RapportStat rapportStat2 = new RapportStat(albumsContainer.getStatChronoComposition(), "Statistiques par décennie de composition");
		write(rapportStat2.printReport(getNextRapportFile(), CssStyles.stylesStat));

		writeStatAlbum();
		write("  </ul>\n  </li>\n</ul>\n</td>\n<td class=\"mainpage\">\n<ul>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getCollectionArtistes().getNombreArtistes());
		write("</li>\n  <li>Nombre d'unit&eacute;s physiques:\n<table>\n  <tr>\n");
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().enteteFormat(rBuilder, "total", 1, DONT_APPEND_AUDIO_FILE);
		write("  </tr>\n  <tr>\n");
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().rowFormat(rBuilder, "total", DONT_APPEND_AUDIO_FILE);
		write("  </tr>\n</table>\n</li>\n</ul>\n</td>\n</tr>\n</table>\n");	
	}
	
	 private Path getNextRapportFile() {
		 rapportIndex++;
		 return rapportCollectionDir.resolve("albums" + rapportIndex + ".html");
	 }
	 
	 private void writeStatAlbum() {
		 
			write("  <li>Nombre d'albums: " + albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums());
			write("\n  <ul>\n    <li>avec fichiers audio: " + albumsContainer.getAlbumsWithAudioFile().getNombreAlbums());
			write("</li>\n    <li>manquant de  fichiers audio: " + albumsContainer.getAlbumsMissingAudioFile().getNombreAlbums());
			write("</li>\n    <li>haute résolution audio: " + albumsContainer.getAlbumsWithHighResAudio().getNombreAlbums());
			write("</li>\n    <li>basse résolution audio (avec perte): " + albumsContainer.getAlbumsWithLowResAudio().getNombreAlbums());
			write("</li>\n    <li>avec  fichiers video: " + albumsContainer.getAlbumsWithVideoFile().getNombreAlbums());
			write("</li>\n    <li>manquant de  fichiers video: " + albumsContainer.getAlbumsMissingVideoFile().getNombreAlbums());
			write("</li>\n");
	 }
}
