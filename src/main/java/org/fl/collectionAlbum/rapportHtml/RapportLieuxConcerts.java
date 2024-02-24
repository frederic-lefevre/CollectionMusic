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

import java.net.URI;
import java.util.Collection;

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
	
	protected RapportLieuxConcerts(LieuxDesConcerts ldc, String titre, LinkType linkType) {
		super(titre, linkType);
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
