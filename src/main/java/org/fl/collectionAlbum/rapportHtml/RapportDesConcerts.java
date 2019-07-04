package org.fl.collectionAlbum.rapportHtml;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.fl.collectionAlbum.CollectionAlbumContainer;

public class RapportDesConcerts extends RapportHtml {

	private int rapportIndex ;
	private CollectionAlbumContainer albumsContainer ;
	private Path rapportCollectionDir ;
	
	public RapportDesConcerts(CollectionAlbumContainer ac, Path rFile, String titre, Logger rl) {
		super(titre, rl) ;
		withTitleDisplayed();
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		albumsContainer 	 = ac ;
		rapportIndex		 = -1 ;
		rapportCollectionDir = rFile ;
	}

	@Override
	protected void corpsRapport() {
		
		 write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		 
		 RapportListeArtistesConcert artistesAlpha = new RapportListeArtistesConcert(albumsContainer.getConcertsArtistes().sortArtistesAlpha(), "Classement alphabethique", rapportLog) ;
		 artistesAlpha.withAlphaBalises() ;
		 write(artistesAlpha.printReport(getNextRapportFile(), CssStyles.stylesTableauArtistes)) ;

		 RapportListeArtistesConcert artistesPoids = new RapportListeArtistesConcert(albumsContainer.getConcertsArtistes().sortArtistesPoidsConcerts(), "Classement par nombre de concerts", rapportLog) ;
		 write(artistesPoids.printReport( getNextRapportFile(), CssStyles.stylesTableauArtistes)) ;

		 write("</ul>\n<h3>Classement des concerts</h3>\n<ul>\n") ;
		 RapportListeConcerts rapportDesConcerts = new RapportListeConcerts(albumsContainer.getConcerts().sortChrono(), "Classement chronologique", rapportLog) ;
		 write(rapportDesConcerts.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact)) ;
		 
		 RapportLieuxConcerts lieuxConcert = new RapportLieuxConcerts(albumsContainer.getLieuxDesConcerts(), "Classement par lieu", rapportLog) ;
		 write(lieuxConcert.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact)) ;
		 
		 write("  <li>Nombre de concerts: " + albumsContainer.getConcerts().getNombreConcerts());
		 write("</li>\n  <li>Nombre d'artistes, de groupes et d'ensemble: " + albumsContainer.getConcertsArtistes().getNombreArtistes());

		 write("</li>\n</ul>\n");	
	}

	 private Path getNextRapportFile() {
		 rapportIndex++ ;
		 return rapportCollectionDir.resolve("concerts" + rapportIndex + ".html") ;
	 }
}
