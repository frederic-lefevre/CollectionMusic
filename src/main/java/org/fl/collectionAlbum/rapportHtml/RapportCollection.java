package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;

public class RapportCollection extends RapportHtml {

	private int rapportIndex ;
	private HtmlLinkList accueils ;
	private CollectionAlbumContainer albumsContainer ;
	private File rapportCollectionDir ;
	
	public RapportCollection(CollectionAlbumContainer ac, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl) ;
		withTitleDisplayed();
		accueils 		= Control.getAccueils() ;
		rapportIndex 	= -1 ;
		albumsContainer = ac ;
		rapportCollectionDir = rFile.getParentFile() ;
	}

	@Override
	 protected void corpsRapport() {
		   	
		genererLesRapportsDeChaqueAlbum(albumsContainer.getCollectionAlbumsMusiques()) ;
	   
	   	String styles[] = { RapportHtml.mainStyle, RapportHtml.formatStyle, "rapport", "chrono" } ;
	   	String stylesStat[] = {"main","stat"} ;
		String stylesCalendrier[] = {"main","rapport", "calendrier"} ;
		String stylesAlbum[] = {"main","format","rapport"} ;
	     				
		write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		
		RapportListeArtistesAlbum rapportArtistesAlbumsAlpha = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesAlpha(),  "Classement alphabethique", getNextRapportFile(), accueils, "", rapportLog) ;
		rapportArtistesAlbumsAlpha.withAlphaBalises() ; ;
		write(rapportArtistesAlbumsAlpha.printReport(styles)) ;
		
		RapportListeArtistesAlbum rapportArtistesAlbumsPoids = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesPoidsAlbums(),  "Classement par nombre d'unit&eacute;s physiques", getNextRapportFile(), accueils, "", rapportLog) ;
		write(rapportArtistesAlbumsPoids.printReport(styles)) ;

		RapportListeArtistesAlbum rapportArtistesAlbumsChrono = new RapportListeArtistesAlbum(albumsContainer.getCollectionArtistes().sortArtistesChrono(),  "Classement chronologique", getNextRapportFile(), accueils, "", rapportLog) ;
		write(rapportArtistesAlbumsChrono.printReport(styles)) ;

		RapportCalendrier rapportCalendrier = new RapportCalendrier(albumsContainer.getCalendrierArtistes(), "Calendrier", getNextRapportFile(), accueils, "", rapportLog) ;
		write(rapportCalendrier.printReport(stylesCalendrier)) ;
		
		write("</ul>\n<h3>Classement des albums</h3>\n<ul>\n") ;
		RapportListeAlbums rapportAlbumsEnregistrement = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoEnregistrement(), "Classement chronologique (enregistrement)", getNextRapportFile(), accueils, "", rapportLog) ;
		write(rapportAlbumsEnregistrement.printReport(stylesAlbum)) ;
	
		RapportListeAlbums rapportAlbumsComposition = new RapportListeAlbums(albumsContainer.getCollectionAlbumsMusiques().sortChronoComposition(), "Classement chronologique (composition)", getNextRapportFile(), accueils, "", rapportLog) ;
		write(rapportAlbumsComposition.printReport(stylesAlbum)) ;

		write("</ul>\n<h3>Rangement des albums</h3>\n<ul>\n") ;
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			RapportListeAlbums rapportAlbumsRangement = new RapportListeAlbums(albumsContainer.getRangementAlbums(rangement).sortRangementAlbum(), rangement.getDescription(), getNextRapportFile(), accueils, "", rapportLog) ;
			write(rapportAlbumsRangement.printReport(stylesAlbum)) ;
		}

		write("</ul>\n<h3>Statistiques</h3>\n<ul>\n") ;
		RapportStat rapportStat1 = new RapportStat(albumsContainer.getStatChronoEnregistrement(), "Statistiques par année d'enregistrement: Nombre d'unit&eacute;s physiques", getNextRapportFile(), accueils, "", rapportLog) ;
		write(rapportStat1.printReport(stylesStat)) ;

		RapportStat rapportStat2 = new RapportStat(albumsContainer.getStatChronoComposition(), "Statistiques par décennie de composition: Nombre d'unit&eacute;s physiques", getNextRapportFile(), accueils, "", rapportLog) ;
		write(rapportStat2.printReport(stylesStat)) ;

		write("  <li>Nombre d'albums: " + albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		write("</li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
		write("</li>\n  <li>Nombre d'unit&eacute;s physiques:\n<table>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().enteteFormat(rBuilder, "total", 1) ;
		write("  </tr>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().rowFormat(rBuilder, "total") ;
		write("  </tr>\n</table>\n</li>\n</ul>\n") ;
		
	}
	
	 private File getNextRapportFile() {
		 rapportIndex++ ;
		 return new File(rapportCollectionDir + File.separator +  "albums" + rapportIndex + ".html") ;
	 }
	 
	private void genererLesRapportsDeChaqueAlbum(ListeAlbum listeAlbums) {	
		String styles[] = {RapportHtml.albumStyle} ;
		for (Album album : listeAlbums.getAlbums()) {
			if (album.additionnalInfo()) {
				File htmlFile = new File(Control.getAbsoluteAlbumDir() + album.getArtefactHtmlName()) ;
				if (! htmlFile.exists()) {
					RapportAlbum rapportAlbum = new RapportAlbum(album, htmlFile,  null,  "../", rapportLog) ;
					rapportAlbum.printReport(styles) ;
				}
			}
		}
	}
}
