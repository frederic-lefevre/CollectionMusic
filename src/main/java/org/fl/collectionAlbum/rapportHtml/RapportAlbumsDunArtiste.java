package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;

public class RapportAlbumsDunArtiste extends RapportHtml {

	private final Artiste artiste ;
	
	public RapportAlbumsDunArtiste(Artiste a, String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rFile, idxs, o, rl);
		artiste = a ;
	}
	@Override
	protected void corpsRapport() {
		
		write("<table class=\"auteurTab\">\n  <tr>\n    <td rowspan=\"2\" class=\"auteurTitre\"><span class=\"auteurTitre\">") ;
		write(artiste.getPrenoms()).write(" ").write(artiste.getNom()) ;
		write("</span> (").write(artiste.getDateNaissance()).write(" - ").write(artiste.getDateMort()).write(")</td>\n") ;
		artiste.getAlbumsFormat().enteteFormat(rBuilder, "total", 1) ;
		write("  </tr>\n  <tr>\n") ;
		artiste.getAlbumsFormat().rowFormat(rBuilder, "artotal") ;
		write("  </tr>\n</table>\n") ;
		FragmentListeAlbums.buildTable(artiste.getAlbums(), rBuilder, "../../");		
	}
}
