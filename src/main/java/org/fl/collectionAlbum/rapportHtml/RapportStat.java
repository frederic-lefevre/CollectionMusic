/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.stat.StatAnnee;
import org.fl.collectionAlbum.stat.StatChrono;

public class RapportStat extends RapportHtml {
		
	private final StatChrono statChrono ;
	
	public RapportStat(StatChrono sc, String titre, Logger rl) {
		super(titre, rl) ;
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
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
