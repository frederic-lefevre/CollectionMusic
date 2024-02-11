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

import java.nio.file.Path;
import java.util.Arrays;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupports;

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
		   	
		write("<table>\n<tr>\n<td class=\"mainpage\">\n<h3>Classements des auteurs, interpretes et chefs d'orchestre</h3>\n<ul>\n");
		
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
		
		write("</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Classements chronologiques des albums</h3>\n<ul>\n");
		RapportListeAlbums rapportAlbumsEnregistrement = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoEnregistrement(), "Classement chronologique (enregistrement)");
		rapportAlbumsEnregistrement.withBalises(new Balises(Balises.BalisesType.TEMPORAL));
		write(rapportAlbumsEnregistrement.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
	
		RapportListeAlbums rapportAlbumsComposition = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoComposition(), "Classement chronologique (composition)");
		rapportAlbumsComposition.withBalises(new Balises(Balises.BalisesType.TEMPORAL_COMPOSITION));
		write(rapportAlbumsComposition.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));

		write("</ul>\n</td>\n</tr>\n<tr>\n<td class=\"mainpage\">\n<h3>Rangement des albums</h3>\n<ul>\n");
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			RapportListeAlbums rapportAlbumsRangement = new RapportListeAlbums(albumsContainer.getRangementAlbums(rangement).sortRangementAlbum(), rangement.getOrdreDescription());
			write(rapportAlbumsRangement.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact));
		}

		write("</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Albums avec et sans fichier audio ou video</h3>\n<ul>\n");
		RapportListeAlbums rapportAlbumsWithAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithAudioFile().sortRangementAlbum(), "Albums avec fichier audio");
		write(rapportAlbumsWithAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithHighResAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithHighResAudio().sortRangementAlbum(), "Albums avec fichier audio haute résolution");
		write(rapportAlbumsWithHighResAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithLowResAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithLowResAudio().sortRangementAlbum(), "Albums avec fichier audio avec perte (basse qualité)");
		write(rapportAlbumsWithLowResAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithoutAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsMissingAudioFile().sortRangementAlbum(), "Albums manquant de fichier audio");
		write(rapportAlbumsWithoutAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithVideoFile = new RapportListeAlbums(albumsContainer.getAlbumsWithVideoFile().sortRangementAlbum(), "Albums avec fichier video");
		write(rapportAlbumsWithVideoFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeAlbums rapportAlbumsWithoutVideoFile = new RapportListeAlbums(albumsContainer.getAlbumsMissingVideoFile().sortRangementAlbum(), "Albums manquant de fichier video");
		write(rapportAlbumsWithoutVideoFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		write("</ul>\n</td>\n</tr>\n<tr>\n<td class=\"mainpage\">\n<h3>Albums par support media</h3>\n<ul>\n");
		Arrays.stream(MediaSupports.values()).forEach(mediaSupport -> {
			RapportListeAlbums mediaSupportAlbum = new RapportListeAlbums(albumsContainer.getAlbumWithMediaSupport(mediaSupport).sortRangementAlbum(), "Albums avec " + mediaSupport.getDescription());
			write(mediaSupportAlbum.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact));
		});
		
		write("</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Albums XXXX A venir</h3>\n<ul>\n");
		
		write("</ul>\n</td>\n</tr>\n<tr>\n<td class=\"mainpage\">\n<h3>Statistiques</h3>\n<ul>\n");
		RapportStat rapportStat1 = new RapportStat(albumsContainer.getStatChronoEnregistrement(), "Statistiques par année d'enregistrement");
		write(rapportStat1.printReport(getNextRapportFile(), CssStyles.stylesStat));

		RapportStat rapportStat2 = new RapportStat(albumsContainer.getStatChronoComposition(), "Statistiques par décennie de composition");
		write(rapportStat2.printReport(getNextRapportFile(), CssStyles.stylesStat));

		write("  <li>Nombre d'artistes, de groupes et d'ensembles: " + albumsContainer.getCollectionArtistes().getNombreArtistes());
		write("</li>\n  <li>Nombre d'unit&eacute;s physiques:\n<table>\n  <tr>\n");
		Format.enteteFormat(rBuilder, "total", 1, DONT_APPEND_AUDIO_FILE);
		write("  </tr>\n  <tr>\n");
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().rowFormat(rBuilder, "total", DONT_APPEND_AUDIO_FILE);
		
		write("  </tr>\n</table>\n</li>\n</ul>\n</td>\n<td class=\"mainpage\">\n");
		writeStatAlbum();
		write("</td>\n</tr>\n</table>\n");	
	}
	
	 private Path getNextRapportFile() {
		 rapportIndex++;
		 return rapportCollectionDir.resolve("albums" + rapportIndex + ".html");
	 }
	 
	 private void writeStatAlbum() {
		 
			write("<ul>\n  <li>Nombre d'albums");
			write("\n  <table>\n    <tr><td class=\"albumstatTitle\">Total</td><td class=\"albumstat\">" + albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums());
			write("</td></tr>\n    <tr><td class=\"albumstatTitle\">Avec fichiers audio</td><td class=\"albumstat\">" + albumsContainer.getAlbumsWithAudioFile().getNombreAlbums());
			write("</td></tr>\n    <tr><td class=\"albumstatTitle\">Haute résolution audio</td><td class=\"albumstat\">" + albumsContainer.getAlbumsWithHighResAudio().getNombreAlbums());
			write("</td></tr>\n    <tr><td class=\"albumstatTitle\">Basse résolution audio (avec perte)</td><td class=\"albumstat\">" + albumsContainer.getAlbumsWithLowResAudio().getNombreAlbums());
			write("</td></tr>\n    <tr><td class=\"albumstatTitle\">Manquant de fichiers audio</td><td class=\"albumstat\">" + albumsContainer.getAlbumsMissingAudioFile().getNombreAlbums());
			write("</td></tr>\n    <tr><td class=\"albumstatTitle\">Avec fichiers video</td><td class=\"albumstat\">" + albumsContainer.getAlbumsWithVideoFile().getNombreAlbums());
			write("</td></tr>\n    <tr><td class=\"albumstatTitle\">Manquant de fichiers video</td><td class=\"albumstat\">" + albumsContainer.getAlbumsMissingVideoFile().getNombreAlbums());
			write("</td></tr>\n  </table>\n  <table>\n  <tr><td>\n");
			for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
				write("</td></tr>\n    <tr><td class=\"albumstatTitle\">");
				write(rangement.getDescription());
				write("</td><td class=\"albumstat\">");
				write(albumsContainer.getRangementAlbums(rangement).getNombreAlbums());
			}
			write("</td></tr>\n  </table>\n  </li>\n</ul>\n");
	 }
}
