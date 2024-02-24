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

import org.fl.collectionAlbum.CollectionAlbumContainer;

public class RapportDesConcerts extends RapportHtml {

	private int rapportIndex ;
	private final CollectionAlbumContainer albumsContainer ;
	private final Path rapportCollectionDir ;
	
	public RapportDesConcerts(CollectionAlbumContainer ac, Path rFile, String titre) {
		super(titre, null) ;
		withTitleDisplayed();
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		albumsContainer 	 = ac ;
		rapportIndex		 = -1 ;
		rapportCollectionDir = rFile ;
	}

	@Override
	protected void corpsRapport() {
		
		 write("<h3>Classement des auteurs, interpretes et chefs d'orchestre (artistes, groupes, ensembles)</h3>\n<ul>\n") ;
		 
		 RapportListeArtistesConcert artistesAlpha = new RapportListeArtistesConcert(albumsContainer.getConcertsArtistes().sortArtistesAlpha(), "Classement alphabethique", LinkType.LIST) ;
		 artistesAlpha.withBalises(new Balises(Balises.BalisesType.ALPHA)) ;
		 write(artistesAlpha.printReport(getNextRapportFile(), CssStyles.stylesTableauAvecBalise)) ;

		 RapportListeArtistesConcert artistesPoids = new RapportListeArtistesConcert(albumsContainer.getConcertsArtistes().sortArtistesPoidsConcerts(), "Classement par nombre de concerts", LinkType.LIST) ;
		 write(artistesPoids.printReport( getNextRapportFile(), CssStyles.stylesTableauAvecBalise)) ;

		 write("</ul>\n<h3>Classement des concerts</h3>\n<ul>\n") ;
		 RapportListeConcerts rapportDesConcerts = new RapportListeConcerts(albumsContainer.getConcerts().sortChrono(), "Classement chronologique", LinkType.LIST) ;
		 write(rapportDesConcerts.printReport(getNextRapportFile(), CssStyles.stylesTableauMusicArtefact)) ;
		 
		 RapportLieuxConcerts lieuxConcert = new RapportLieuxConcerts(albumsContainer.getLieuxDesConcerts(), "Classement par lieu", LinkType.LIST) ;
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
