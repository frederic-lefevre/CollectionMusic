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
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupports;

public class RapportCollection extends RapportHtml {

	private int rapportIndex;
	private final CollectionAlbumContainer albumsContainer;
	private final Path rapportCollectionDir;

	private static final boolean DONT_APPEND_AUDIO_FILE = false;

	public RapportCollection(CollectionAlbumContainer ac, Path rFile, String titre) {
		super(titre, null);
		withTitleDisplayed();
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		rapportIndex = -1;
		albumsContainer = ac;
		rapportCollectionDir = rFile;
	}

	@Override
	 protected void corpsRapport() {
		
		write("<table>\n<tr>\n<td class=\"mainpage\">\n<h3>Classements des auteurs, interprètes et chefs d'orchestre</h3>\n<ul>\n");
		
		RapportListeArtistesAlbum rapportArtistesAlbumsAlpha = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesAlpha(),  "Classement alphabéthique", LinkType.LIST);
		rapportArtistesAlbumsAlpha.withBalises(new Balises(Balises.BalisesType.ALPHA));
		write(rapportArtistesAlbumsAlpha.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
		
		RapportListeArtistesAlbum rapportArtistesAlbumsPoids = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesPoidsAlbums(),  "Classement par nombre d'unit&eacute;s physiques", LinkType.LIST);
		rapportArtistesAlbumsPoids.withBalises(new Balises(Balises.BalisesType.POIDS));
		write(rapportArtistesAlbumsPoids.printReport(getNextRapportFile(),  CssStyles.stylesTableauAvecBalise));

		RapportListeArtistesAlbum rapportArtistesAlbumsChrono = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesChrono(),  "Classement chronologique", LinkType.LIST);
		rapportArtistesAlbumsChrono.withBalises(new Balises(Balises.BalisesType.TEMPORAL));
		write(rapportArtistesAlbumsChrono.printReport(getNextRapportFile(),  CssStyles.stylesTableauAvecBalise));

		RapportCalendrier rapportCalendrier = new RapportCalendrier(albumsContainer.getCalendrierArtistes(), "Calendrier", LinkType.LIST);
		write(rapportCalendrier.printReport(getNextRapportFile(), CssStyles.stylesCalendrier));
		
		write("</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Classements chronologiques des albums</h3>\n<ul>\n");
		RapportListeAlbums rapportAlbumsEnregistrement = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoEnregistrement(), "Classement chronologique (enregistrement)", LinkType.LIST);
		rapportAlbumsEnregistrement.withBalises(new Balises(Balises.BalisesType.TEMPORAL));
		write(rapportAlbumsEnregistrement.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));
	
		RapportListeAlbums rapportAlbumsComposition = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoComposition(), "Classement chronologique (composition)", LinkType.LIST);
		rapportAlbumsComposition.withBalises(new Balises(Balises.BalisesType.TEMPORAL_COMPOSITION));
		write(rapportAlbumsComposition.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise));

		write("</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Albums par nature de contenu</h3>\n<table>\n");
		Arrays.stream(ContentNature.values()).forEach(contentNature -> {
			writeListeAlbumsRow(albumsContainer.getAlbumsWithOnlyContentNature(contentNature).sortRangementAlbum(), "Albums avec seulement du contenu " + contentNature.getNom());
		});
		writeListeAlbumsRow(albumsContainer.getAlbumsWithMixedContentNature().sortRangementAlbum(), "Albums avec plusieurs types de contenus");
		
		write("</table>\n</td>\n</tr>\n<tr>\n<td rowspan=2 class=\"mainpage\">\n<h3>Albums par support media</h3>\n<table>\n");
		Arrays.stream(MediaSupports.values()).forEach(mediaSupport -> {
			writeListeAlbumsRow(albumsContainer.getAlbumsWithMediaSupport(mediaSupport).sortRangementAlbum(), "Albums avec " + mediaSupport.getDescription());
		});
		
		write("</table>\n</td>\n<td class=\"mainpage\">\n<h3>Rangement des albums</h3>\n<table>\n");
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			writeListeAlbumsRow(albumsContainer.getRangementAlbums(rangement).sortRangementAlbum(), rangement.getOrdreDescription());
		}
		
		write("</table>\n</td>\n<td class=\"mainpage\">\n<h3>Albums avec et sans fichier audio ou vidéo</h3>\n<table>\n");
		
		writeListeAlbumsRow(albumsContainer.getAlbumsWithAudioFile().sortRangementAlbum(), "Albums avec fichier audio");
		writeListeAlbumsRow(albumsContainer.getAlbumsWithHighResAudio().sortRangementAlbum(), "Albums avec fichier audio haute résolution");
		writeListeAlbumsRow(albumsContainer.getAlbumsWithLowResAudio().sortRangementAlbum(), "Albums avec fichier audio avec perte (basse qualité)");
		writeListeAlbumsRow(albumsContainer.getAlbumsMissingAudioFile().sortRangementAlbum(), "Albums manquant de fichier audio");
		writeListeAlbumsRow(albumsContainer.getAlbumsWithVideoFile().sortRangementAlbum(), "Albums avec fichier vidéo");
		writeListeAlbumsRow(albumsContainer.getAlbumsMissingVideoFile().sortRangementAlbum(), "Albums manquant de fichier vidéo");	

		write("</table>\n</td>\n</tr>\n<tr>\n<td class=\"mainpage\">\n<h3>Statistiques</h3>\n<ul>\n");
		RapportStat rapportStat1 = new RapportStat(albumsContainer.getStatChronoEnregistrement(), "Statistiques par année d'enregistrement", LinkType.LIST);
		write(rapportStat1.printReport(getNextRapportFile(), CssStyles.stylesStat));

		RapportStat rapportStat2 = new RapportStat(albumsContainer.getStatChronoComposition(), "Statistiques par décennie de composition", LinkType.LIST);
		write(rapportStat2.printReport(getNextRapportFile(), CssStyles.stylesStat));

		write("  <li>Nombre d'artistes, de groupes et d'ensembles: ");
		write(albumsContainer.getCollectionArtistes().getNombreArtistes());
		write("</li>\n  <li>Nombre total d'albums: ");
		write(albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums());
		write("</li>\n  <li>Nombre d'unit&eacute;s physiques:\n<table>\n  <tr>\n");
		Format.enteteFormat(rBuilder, "total", 1, DONT_APPEND_AUDIO_FILE);
		write("  </tr>\n  <tr>\n");
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().rowFormat(rBuilder, "total", DONT_APPEND_AUDIO_FILE);
		
		write("  </tr>\n</table>\n</li>\n</ul>\n</td>\n<td class=\"mainpage\">\n<h3>Albums avec et sans release discogs</h3>\n<table>\n");
		writeListeAlbumsRow(albumsContainer.getAlbumsWithDiscogsRelease().sortRangementAlbum(), "Albums avec release discogs");
		writeListeAlbumsRow(albumsContainer.getAlbumsMissingDiscogsRelease().sortRangementAlbum(), "Albums sans release discogs");
		write("</table>\n</td>\n</tr>\n</table>\n");
	}
	
	private void writeListeAlbumsRow(ListeAlbum listeAlbum, String title) {
		
		write("  <tr>");
		RapportListeAlbums rapportAlbums = new RapportListeAlbums(listeAlbum, title, LinkType.CELL);
		write(rapportAlbums.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact, "albumListTitle"));
		write("<td class=\"albumListStat\">");
		write(listeAlbum.getNombreAlbums());
		write("</td></tr>\n");
		
	}
	 private Path getNextRapportFile() {
		 rapportIndex++;
		 return rapportCollectionDir.resolve("albums" + rapportIndex + ".html");
	 }

}
