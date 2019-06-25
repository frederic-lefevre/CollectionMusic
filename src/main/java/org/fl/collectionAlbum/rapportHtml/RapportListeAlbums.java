package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;

public class RapportListeAlbums extends RapportHtml {

	private static final String styles[] = {RapportHtml.albumStyle} ;
	
	private final ListeAlbum listeAlbums ;
	
	public RapportListeAlbums(ListeAlbum la, String titre, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, idxs, o, rl);
		withTitleDisplayed() ;
		listeAlbums = la ;
		
		// Generer les rapports de chaque album, si ils n'existent pas déjà
		for (Album album : listeAlbums.getAlbums()) {
			if (album.additionnalInfo()) {
				File htmlFile = new File(Control.getAbsoluteAlbumDir() + album.getArtefactHtmlName()) ;
				if (! htmlFile.exists()) {
					RapportAlbum rapportAlbum = new RapportAlbum(album, "../", rapportLog) ;
					rapportAlbum.printReport( htmlFile, styles) ;
				}
			}
		}
	}

	@Override
	protected void corpsRapport() {
	
		FragmentListeAlbums.buildTable(listeAlbums, rBuilder, urlOffset) ;
	}
	

}
