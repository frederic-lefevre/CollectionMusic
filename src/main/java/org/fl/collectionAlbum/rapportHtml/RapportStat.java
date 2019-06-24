package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.stat.StatAnnee;
import org.fl.collectionAlbum.stat.StatChrono;

public class RapportStat extends RapportHtml {
		
	private final StatChrono statChrono ;
	
	public RapportStat(StatChrono sc, String titre, File rDir, HtmlLinkList idxs, String o, Logger rl) {
		super(titre, rDir, idxs, o, rl) ;
		withTitleDisplayed();
		statChrono = sc ;
	}

	// Return a html hyper to this rapport
	protected void corpsRapport() {
		
		List<StatAnnee> statDecennale = statChrono.getStatDecennale() ;
		List<StatAnnee> statSiecle	  = statChrono.getStatSiecle() 	  ;
		
		Iterator<StatAnnee> ed ;
		boolean plusieursSiecles = false ;
		if (statSiecle.size() > 2) {
		    plusieursSiecles = true ;
		    ed = statSiecle.iterator() ; 
		} else {
		    ed = statDecennale.iterator() ;
		}   

		write("<table class=\"stat\">\n  <tr>\n    <td class=\"dece\"></td>\n    <td class=\"statotal\">Total</td>\n") ;
		for (int i=0; i <10; i++) {
			int sub = i ;
			if (plusieursSiecles) sub = i*10;
			write("    <td class=\"anH\">").write(sub).write("</td>\n") ;
		}
		write("  </tr>\n") ;
		String cssClass = "statan" ;
		while (ed.hasNext()) {
			write("  <tr class=\"statan\">\n") ;
			StatAnnee uneSubdivision = ed.next() ; 
			write("    <td class=\"dece\"><span class=\"dece\">").write(uneSubdivision.getAn()).write("</span></td>\n") ;
			write("    <td class=\"statotal\">").write(uneSubdivision.getNombre()).write("</td>\n") ;
			for (int i=0; i <10; i++) {
				int an = uneSubdivision.getAn()	;			
				String count ;
				if (plusieursSiecles) {
					an = an + i*10 ;
					count = statChrono.getStatForDecennie(an) ;
				} else {
					an = an + i ;
					count = statChrono.getStatForYear(an) ;
				}

				if (count.length() == 0) {
					cssClass = "statan0" ;
				} else {
					cssClass = "statan" ;
				}
				write("    <td class=\"").write(cssClass).write("\">").write(count).write("<span class=\"annee\">").write(an).write("</span><span class=\"count\">").write(count).write("</span></td>\n") ;
			}
			write("  </tr>\n") ;
		}
		write("</table>\n") ;
	}
}
