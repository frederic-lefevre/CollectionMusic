package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ListeConcert;

public class RapportDesConcerts extends RapportHtml {

	private int rapportIndex ;
	private HtmlLinkList accueils ;
	private CollectionAlbumContainer albumsContainer ;
	private File rapportCollectionDir ;
	
	public RapportDesConcerts(CollectionAlbumContainer ac, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl) ;
		withTitleDisplayed();
		accueils 			 = Control.getAccueils() ;
		albumsContainer 	 = ac ;
		rapportIndex		 = -1 ;
		rapportCollectionDir = rFile.getParentFile() ;
	}

	@Override
	protected void corpsRapport() {
		
		genererLesRapportsDeChaqueConcert(albumsContainer.getConcerts()) ;
		genererLesRapportsConcertsDeChaqueArtiste(albumsContainer.getConcertsArtistes()) ;
		
		 String stylesArtistes[] = {"main","format","rapport","chrono"} ;
		 String stylesConcert[] = {"main","format","rapport"} ;

		 write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		 
		 RapportListeArtistesConcert artistesAlpha = new RapportListeArtistesConcert(albumsContainer.getConcertsArtistes().sortArtistesAlpha(), "Classement alphabethique", getNextRapportFile(), accueils, "", rapportLog) ;
		 write(artistesAlpha.printReport(stylesArtistes)) ;

		 RapportListeArtistesConcert artistesPoids = new RapportListeArtistesConcert(albumsContainer.getConcertsArtistes().sortArtistesPoidsConcerts(), "Classement par nombre de concerts", getNextRapportFile(), accueils, "", rapportLog) ;
		 write(artistesPoids.printReport(stylesArtistes)) ;

		 write("</ul>\n<h3>Classement des concerts</h3>\n<ul>\n") ;
		 RapportListeConcerts rapportDesConcerts = new RapportListeConcerts(albumsContainer.getConcerts().sortChrono(), "Classement chronologique", getNextRapportFile(), accueils, "", rapportLog) ;
		 write(rapportDesConcerts.printReport(stylesConcert)) ;
		 
		 write("  <li>Nombre de concerts: " + albumsContainer.getConcerts().getNombreConcerts());
		 write("</li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getConcertsArtistes().getNombreArtistes());

		 write("</li>\n</ul>\n");
		
	}

	 private File getNextRapportFile() {
		 rapportIndex++ ;
		 return new File(rapportCollectionDir + File.separator +  "concerts" + rapportIndex + ".html") ;
	 }

	 private void genererLesRapportsDeChaqueConcert(ListeConcert concerts) {
		 String styles[] = {RapportHtml.albumStyle} ;
		 for (Concert concert : concerts.getConcerts()) {
	    	if (concert.additionnalInfo()) {		    				
	        	File htmlFile = new File(Control.getAbsoluteConcertDir() + concert.getArtefactHtmlName()) ;	        	
	        	if (! htmlFile.exists()) {
	        		RapportConcert rapportConcert = new RapportConcert(concert, "", htmlFile, null,  "../", rapportLog) ;
	        		rapportConcert.printReport(styles) ;
	        	}
	    	}
		 }
	 }
	 
	 private void genererLesRapportsConcertsDeChaqueArtiste(ListeArtiste listeArtistes) {
		 String styles[] = {"main","format","rapport", "artiste"} ;
		 for (Artiste artiste : listeArtistes.getArtistes()) {
			 if (artiste.getNbConcert() > 0) {
				 String titre = artiste.getPrenoms() + " " + artiste.getNom() ;
				 HtmlLinkList albumLink = new HtmlLinkList(Control.getAccueils(), "../../") ;
				 if (artiste.getConcerts().getNombreConcerts() > 0) {
					 albumLink.addLink("Albums", artiste.getHtmlFileName()) ;						
				 }
				 RapportConcertsDunArtiste rapportDeSesConcerts = new RapportConcertsDunArtiste(artiste, titre, artiste.getHtmlConcertFile(), albumLink, "../../", rapportLog) ;
				 rapportDeSesConcerts.printReport(styles) ;
			 }
		 }
	 }
}
