package org.fl.collectionAlbum.rapportHtml;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Format;

public class RapportCollection extends RapportHtml {

	private int rapportIndex ;
	private final CollectionAlbumContainer albumsContainer ;
	private final Path rapportCollectionDir ;
	
	public RapportCollection(CollectionAlbumContainer ac, Path rFile, String titre, Logger rl) {
		super(titre, rl) ;
		withTitleDisplayed();
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		rapportIndex 	= -1 ;
		albumsContainer = ac ;
		rapportCollectionDir = rFile ;
	}

	@Override
	 protected void corpsRapport() {
		   	
		write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		
		RapportListeArtistesAlbum rapportArtistesAlbumsAlpha = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesAlpha(),  "Classement alphabethique", rapportLog) ;
		rapportArtistesAlbumsAlpha.withBalises(new Balises(Balises.BalisesType.ALPHA)) ;
		write(rapportArtistesAlbumsAlpha.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise)) ;
		
		RapportListeArtistesAlbum rapportArtistesAlbumsPoids = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesPoidsAlbums(),  "Classement par nombre d'unit&eacute;s physiques", rapportLog) ;
		rapportArtistesAlbumsPoids.withBalises(new Balises(Balises.BalisesType.POIDS)) ;
		write(rapportArtistesAlbumsPoids.printReport(getNextRapportFile(),  CssStyles.stylesTableauAvecBalise)) ;

		RapportListeArtistesAlbum rapportArtistesAlbumsChrono = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesChrono(),  "Classement chronologique", rapportLog) ;
		rapportArtistesAlbumsChrono.withBalises(new Balises(Balises.BalisesType.TEMPORAL)) ;
		write(rapportArtistesAlbumsChrono.printReport(getNextRapportFile(),  CssStyles.stylesTableauAvecBalise)) ;

		RapportCalendrier rapportCalendrier = new RapportCalendrier(albumsContainer.getCalendrierArtistes(), "Calendrier", rapportLog) ;
		write(rapportCalendrier.printReport(getNextRapportFile(), CssStyles.stylesCalendrier)) ;
		
		write("</ul>\n<h3>Classement des albums</h3>\n<ul>\n") ;
		RapportListeAlbums rapportAlbumsEnregistrement = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoEnregistrement(), "Classement chronologique (enregistrement)", rapportLog) ;
		rapportAlbumsEnregistrement.withBalises(new Balises(Balises.BalisesType.TEMPORAL)) ;
		write(rapportAlbumsEnregistrement.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise)) ;
	
		RapportListeAlbums rapportAlbumsComposition = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoComposition(), "Classement chronologique (composition)", rapportLog) ;
		rapportAlbumsComposition.withBalises(new Balises(Balises.BalisesType.TEMPORAL_COMPOSITION)) ;
		write(rapportAlbumsComposition.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise)) ;

		write("</ul>\n<h3>Rangement des albums</h3>\n<ul>\n") ;
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			RapportListeAlbums rapportAlbumsRangement = new RapportListeAlbums(albumsContainer.getRangementAlbums(rangement).sortRangementAlbum(), rangement.getDescription(), rapportLog) ;
			write(rapportAlbumsRangement.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact)) ;
		}

		write("</ul>\n<h3>Albums avec et sans fichier audio</h3>\n<ul>\n") ;
		RapportListeAlbums rapportAlbumsWithAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithAudioFile().sortRangementAlbum(), "Albums avec fichiers audio", rapportLog) ;
		write(rapportAlbumsWithAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise)) ;
		
		RapportListeAlbums rapportAlbumsWithoutAudioFile = new RapportListeAlbums(albumsContainer.getAlbumsWithoutAudioFile().sortRangementAlbum(), "Albums sans fichiers audio", rapportLog) ;
		write(rapportAlbumsWithoutAudioFile.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise)) ;
		
		write("</ul>\n<h3>Statistiques</h3>\n<ul>\n") ;
		RapportStat rapportStat1 = new RapportStat(albumsContainer.getStatChronoEnregistrement(), "Statistiques par année d'enregistrement: Nombre d'unit&eacute;s physiques", rapportLog) ;
		write(rapportStat1.printReport(getNextRapportFile(), CssStyles.stylesStat)) ;

		RapportStat rapportStat2 = new RapportStat(albumsContainer.getStatChronoComposition(), "Statistiques par décennie de composition: Nombre d'unit&eacute;s physiques", rapportLog) ;
		write(rapportStat2.printReport(getNextRapportFile(), CssStyles.stylesStat)) ;

		write("  <li>Nombre d'albums: " + albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		write("\n  <ul>\n    <li>avec fichiers audio: " + albumsContainer.getAlbumsWithAudioFile().getNombreAlbums()) ;
		write("</li>\n    <li>sans fichiers audio: " + albumsContainer.getAlbumsWithoutAudioFile().getNombreAlbums()) ;
		write("</li>\n  </ul>\n  </li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
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
