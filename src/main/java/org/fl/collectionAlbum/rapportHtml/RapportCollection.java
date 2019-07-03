package org.fl.collectionAlbum.rapportHtml;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Format;

public class RapportCollection extends RapportHtml {

	private int rapportIndex ;
	private HtmlLinkList accueils ;
	private CollectionAlbumContainer albumsContainer ;
	private Path rapportCollectionDir ;
	
	public RapportCollection(CollectionAlbumContainer ac, Path rFile, String titre, HtmlLinkList idxs, Logger rl) {
		super(titre, idxs, rl) ;
		withTitleDisplayed();
		accueils 		= RapportStructuresAndNames.getAccueils() ;
		rapportIndex 	= -1 ;
		albumsContainer = ac ;
		rapportCollectionDir = rFile ;
	}

	@Override
	 protected void corpsRapport() {
		   	
		write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		
		RapportListeArtistesAlbum rapportArtistesAlbumsAlpha = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesAlpha(),  "Classement alphabethique", accueils, rapportLog) ;
		rapportArtistesAlbumsAlpha.withAlphaBalises() ; ;
		write(rapportArtistesAlbumsAlpha.printReport(getNextRapportFile(), CssStyles.stylesTableauArtistes)) ;
		
		RapportListeArtistesAlbum rapportArtistesAlbumsPoids = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesPoidsAlbums(),  "Classement par nombre d'unit&eacute;s physiques", accueils, rapportLog) ;
		write(rapportArtistesAlbumsPoids.printReport(getNextRapportFile(),  CssStyles.stylesTableauArtistes)) ;

		RapportListeArtistesAlbum rapportArtistesAlbumsChrono = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesChrono(),  "Classement chronologique", accueils, rapportLog) ;
		write(rapportArtistesAlbumsChrono.printReport(getNextRapportFile(),  CssStyles.stylesTableauArtistes)) ;

		RapportCalendrier rapportCalendrier = new RapportCalendrier(albumsContainer.getCalendrierArtistes(), "Calendrier", accueils, rapportLog) ;
		write(rapportCalendrier.printReport(getNextRapportFile(), CssStyles.stylesCalendrier)) ;
		
		write("</ul>\n<h3>Classement des albums</h3>\n<ul>\n") ;
		RapportListeAlbums rapportAlbumsEnregistrement = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoEnregistrement(), "Classement chronologique (enregistrement)", accueils, rapportLog) ;
		write(rapportAlbumsEnregistrement.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact)) ;
	
		RapportListeAlbums rapportAlbumsComposition = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoComposition(), "Classement chronologique (composition)", accueils, rapportLog) ;
		write(rapportAlbumsComposition.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact)) ;

		write("</ul>\n<h3>Rangement des albums</h3>\n<ul>\n") ;
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			RapportListeAlbums rapportAlbumsRangement = new RapportListeAlbums(albumsContainer.getRangementAlbums(rangement).sortRangementAlbum(), rangement.getDescription(), accueils, rapportLog) ;
			write(rapportAlbumsRangement.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact)) ;
		}

		write("</ul>\n<h3>Statistiques</h3>\n<ul>\n") ;
		RapportStat rapportStat1 = new RapportStat(albumsContainer.getStatChronoEnregistrement(), "Statistiques par année d'enregistrement: Nombre d'unit&eacute;s physiques", accueils, rapportLog) ;
		write(rapportStat1.printReport(getNextRapportFile(), CssStyles.stylesStat)) ;

		RapportStat rapportStat2 = new RapportStat(albumsContainer.getStatChronoComposition(), "Statistiques par décennie de composition: Nombre d'unit&eacute;s physiques", accueils, rapportLog) ;
		write(rapportStat2.printReport(getNextRapportFile(), CssStyles.stylesStat)) ;

		write("  <li>Nombre d'albums: " + albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		write("</li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
		write("</li>\n  <li>Nombre d'unit&eacute;s physiques:\n<table>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().enteteFormat(rBuilder, "total", 1) ;
		write("  </tr>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().rowFormat(rBuilder, "total") ;
		write("  </tr>\n</table>\n</li>\n</ul>\n") ;	
	}
	
	 private Path getNextRapportFile() {
		 rapportIndex++ ;
		 return rapportCollectionDir.resolve("albums" + rapportIndex + ".html") ;
	 }
	 
}
