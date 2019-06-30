package org.fl.collectionAlbum.rapportHtml;

import java.util.Collection;
import java.util.logging.Logger;

import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;

public class RapportLieuxConcerts  extends RapportHtml {

	private final static String F1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n" ;
	private final static String F2 = "    <td class=\"album\">Lieu</td>\n" ;
	private final static String F3 = "    <td class=\"total\">Nombre<br/>concerts</td>\n" ;
	private final static String F4 = "</tr>\n  </table>\n</div>\n  <table>\n  <tr class=\"head\">\n" ;
	private final static String F5 = "  </tr>\n" ;
	private final static String table1 = F1 + F2 + F3 + F4 + F2 + F3 + F5 ;

	private LieuxDesConcerts lieuxDesConcerts ;
	
	protected RapportLieuxConcerts(LieuxDesConcerts ldc, String titre, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, idxs, o, rl);
		withTitleDisplayed() ;
		lieuxDesConcerts = ldc ;
	}

	@Override
	protected void corpsRapport() {
		
		write(table1) ;
		
		Collection<LieuConcert> lieux = lieuxDesConcerts.getLieuxConcerts() ;
		for (LieuConcert unLieu : lieux) {
			
			write("  <tr>\n    <td class=\"album\">").write(unLieu.getLieu()).write("    </td>\n") ;
			write("    <td class=\"total\">").write(unLieu.getNombreConcert()).write("</td>\n  </tr>\n") ;
		}
		write("</table>\n") ;
	}

}
