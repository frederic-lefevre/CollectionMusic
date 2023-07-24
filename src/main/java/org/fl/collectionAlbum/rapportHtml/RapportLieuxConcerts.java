package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
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

	private final LieuxDesConcerts lieuxDesConcerts ;
	
	protected RapportLieuxConcerts(LieuxDesConcerts ldc, String titre, Logger rl) {
		super(titre, rl);
		withTitleDisplayed() ;
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		lieuxDesConcerts = ldc ;
	}

	@Override
	protected void corpsRapport() {
		
		write(table1) ;
		
		Collection<LieuConcert> lieux = lieuxDesConcerts.getLieuxConcerts() ;
		for (LieuConcert unLieu : lieux) {
			write("  <tr>\n    <td class=\"album\">") ;
			
			URI aPath = RapportStructuresAndNames.getLieuRapportRelativeUri(unLieu) ;
			if (aPath != null) {
				write("<a href=\"").write(aPath.toString()).write("\">").write(unLieu.getLieu()).write("</a>") ;
			} else {
				write(unLieu.getLieu()) ;
			}
			write("    </td>\n") ;
			write("    <td class=\"total\">").write(unLieu.getNombreConcert()).write("</td>\n  </tr>\n") ;
		}
		write("</table>\n") ;
	}

}
