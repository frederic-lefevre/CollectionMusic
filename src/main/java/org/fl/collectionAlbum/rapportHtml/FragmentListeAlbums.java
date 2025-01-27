/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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
import java.time.temporal.TemporalAccessor;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class FragmentListeAlbums {

	// HTML fragments
	private static final String TABLE1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td colspan=\"2\" class=\"an2\">Dates de composition /<br/>Dates d'enregistrement</td>\n    <td rowspan=\"2\" class=\"auteur\">Auteurs</td>\n    <td rowspan=\"2\" class=\"album\">Titres</td>\n" ;
	private static final String TABLE2 = "  </tr>\n  <tr>\n    <td class=\"an\">Début</td>\n    <td class=\"an\">Fin</td>\n  </tr>\n\n  </table>\n</div>\n<table>\n <tr class=\"head\">\n    <td colspan=\"2\" class=\"an2\">Dates de composition /<br/>Dates d'enregistrement</td>\n    <td rowspan=\"2\" class=\"auteur\">Auteurs</td>\n    <td rowspan=\"2\" class=\"album\">Titres</td>\n" ;
	private static final String TABLE3 = "  </tr>\n  <tr class=\"head\">\n    <td class=\"an\">Début</td>\n    <td class=\"an\">Fin</td>\n  </tr>\n" ;

	private static final boolean APPEND_AUDIO_FILE = true;
	
	public static void buildTable(ListeAlbum listeAlbums, StringBuilder fragment, String urlOffSet, Balises balises) {
		
		fragment.append(TABLE1) ;
		Format.enteteFormat(fragment, null, 2, APPEND_AUDIO_FILE);
		fragment.append(TABLE2) ;
        Format.enteteFormat(fragment, null, 2, APPEND_AUDIO_FILE);
        fragment.append(TABLE3) ;
		for (Album unAlbum : listeAlbums.getAlbums()) {
			
			fragment.append("  <tr>\n") ;
			TemporalAccessor debutComp = unAlbum.getDebutComposition() ;
			TemporalAccessor finComp   = unAlbum.getFinComposition() ;
			TemporalAccessor debutEnr  = unAlbum.getDebutEnregistrement() ;
			TemporalAccessor finEnr    = unAlbum.getFinEnregistrement() ;
			
			boolean displayDateEnregistrement =  unAlbum.hasSpecificCompositionDates() ;
			
			fragment.append("    <td class=\"an\">") ;
			if (balises != null) {
				if (balises.getBalisesType() == Balises.BalisesType.TEMPORAL) {
					balises.addCheckBaliseTemporal(fragment, debutEnr) ;
				} else if (balises.getBalisesType() == Balises.BalisesType.TEMPORAL_COMPOSITION) {
					balises.addCheckBaliseTemporal(fragment, debutComp) ;
				} else if (balises.getBalisesType() == Balises.BalisesType.ALPHA) {
					balises.addCheckBaliseString(fragment, unAlbum.getTitre()) ;
				}
			}
			fragment.append(TemporalUtils.formatDate(debutComp)) ;
			if (displayDateEnregistrement) {
				fragment.append("<br/>").append( TemporalUtils.formatDate(debutEnr)) ;
			}
			fragment.append("</td>\n") ;
			
			fragment.append("    <td class=\"an\">").append(TemporalUtils.formatDate(finComp)) ;
			if (displayDateEnregistrement) {
				fragment.append("<br/>").append(TemporalUtils.formatDate(finEnr)) ;
			}
			fragment.append("</td>\n    <td class=\"auteur\">\n") ;
			
			FragmentIntervenants.printAuteurs(unAlbum, fragment, urlOffSet);
			fragment.append("    </td>\n    <td class=\"album\">") ; 
			
			if (unAlbum.hasAdditionnalInfo()) {
				URI aPath = RapportStructuresAndNames.getAlbumRapportRelativeUri(unAlbum) ;			
				fragment.append("<a href=\"").append(urlOffSet).append(aPath.toString()).append("\">") ;
				fragment.append(unAlbum.getTitre()) ;			
				fragment.append("</a>\n") ;
			} else {
				fragment.append(unAlbum.getTitre()) ;
			}
			
			FragmentIntervenants.printIntervenant(unAlbum, fragment, urlOffSet) ;
			fragment.append("\n    </td>\n") ;
			unAlbum.getFormatAlbum().rowFormat(fragment, null, APPEND_AUDIO_FILE) ;
			fragment.append("  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
	
}
