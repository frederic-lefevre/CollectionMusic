package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.ListeArtiste;

public class RapportCollection extends RapportHtml {

	private int rapportIndex ;
	private HtmlLinkList accueils ;
	
	public RapportCollection(String titre, boolean dt, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, dt, rFile, idxs, o, rl);
		accueils 		= Control.getAccueils() ;
		rapportIndex 	= -1 ;
	}

	 public void rapportHtml(CollectionAlbumContainer albumsContainer, File rapportDir) {	
		   	
		albumsContainer.getCollectionAlbumsMusiques().rapportAdditionnalInfo() ;
	   
	   	String styles[] = { RapportHtml.mainStyle, RapportHtml.formatStyle } ;
	     	
		String rapportFileName = new String(rapportDir.getAbsolutePath() + File.separator + Control.getHomecollectionfile()) ;
		File rapportFile = new File(rapportFileName) ;
		RapportHtml rapport = new RapportHtml("Collections d'albums", true, rapportFile, accueils, "", rapportLog) ;
			
		rapport.enteteRapport(styles) ;
		rapport.write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		
		generationRapportHtml(rapport, rapportDir, "Classement alphabethique", 						  albumsContainer.getCollectionArtistes(), ListeArtiste.rapportAlpha,  "") ;
		generationRapportHtml(rapport, rapportDir, "Classement par nombre d'unit&eacute;s physiques", albumsContainer.getCollectionArtistes(), ListeArtiste.rapportPoids,  "") ;
		generationRapportHtml(rapport, rapportDir, "Classement chronologique", 						  albumsContainer.getCollectionArtistes(), ListeArtiste.rapportChrono, "") ;
		generationRapportHtml(rapport, rapportDir, "Calendrier", 						  			  albumsContainer.getCalendrierArtistes(), 0, "") ;
		
		rapport.write("</ul>\n<h3>Classement des albums</h3>\n<ul>\n") ;
		generationRapportHtml(rapport, rapportDir, "Classement chronologique (enregistrement)", albumsContainer.getCollectionAlbumsMusiques(), ListeAlbum.rapportChronoEnregistrement,  "") ;
		generationRapportHtml(rapport, rapportDir, "Classement chronologique (composition)", 	albumsContainer.getCollectionAlbumsMusiques(), ListeAlbum.rapportChronoComposition,  "") ;

		rapport.write("</ul>\n<h3>Rangement des albums</h3>\n<ul>\n") ;
		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
			generationRapportHtml(rapport, rapportDir, rangement.getDescription(), albumsContainer.getRangementAlbums(rangement), ListeAlbum.rapportRangement, "") ;
		}

		rapport.write("</ul>\n<h3>Statistiques</h3>\n<ul>\n") ;
		generationRapportHtml(rapport, rapportDir, "Statistiques par année d'enregistrement: Nombre d'unit&eacute;s physiques",  albumsContainer.getStatChronoEnregistrement(), 0, "") ;
		generationRapportHtml(rapport, rapportDir, "Statistiques par décennie de composition: Nombre d'unit&eacute;s physiques", albumsContainer.getStatChronoComposition(), 	0, "") ;

		rapport.write("  <li>Nombre d'albums: " + albumsContainer.getCollectionAlbumsMusiques().getNombreAlbums()) ;
		rapport.write("</li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getCollectionArtistes().getNombreArtistes()) ;
		rapport.write("</li>\n  <li>Nombre d'unit&eacute;s physiques:\n<table>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().enteteFormat(rapport, "total", 1) ;
		rapport.write("  </tr>\n  <tr>\n") ;
		albumsContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().rowFormat(rapport, "total") ;
		rapport.write("  </tr>\n</table>\n</li>\n</ul>\n") ;
		rapport.finRapport() ;			
	}
	
	 private void generationRapportHtml(RapportHtml rapport, File rapportDir, String titre, HtmlReportPrintable hpr, int typeRapport, String urlOffset) {
		 RapportHtml rapHtml = new RapportHtml(titre, true, getNextRapportFile(rapportDir), accueils,  "", rapportLog) ;
		 rapport.write(rapHtml.printReport(hpr, typeRapport, urlOffset)) ;
	 }
	 
	 private File getNextRapportFile(File rapportDir) {
		 rapportIndex++ ;
		 return new File(rapportDir + File.separator +  "r" + rapportIndex + ".html") ;
	 }
}
