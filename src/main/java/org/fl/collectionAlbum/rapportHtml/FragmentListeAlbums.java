package org.fl.collectionAlbum.rapportHtml;

import java.time.temporal.TemporalAccessor;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class FragmentListeAlbums {

	// HTML fragments
	private final static String TABLE1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td colspan=\"2\" class=\"an2\">Dates de composition /<br/>Dates d'enregistrement</td>\n    <td rowspan=\"2\" class=\"auteur\">Auteurs</td>\n    <td rowspan=\"2\" class=\"album\">Titres</td>\n" ;
	private final static String TABLE2 = "  </tr>\n  <tr>\n    <td class=\"an\">Début</td>\n    <td class=\"an\">Fin</td>\n  </tr>\n\n  </table>\n</div>\n<table>\n <tr class=\"head\">\n    <td colspan=\"2\" class=\"an2\">Dates de composition /<br/>Dates d'enregistrement</td>\n    <td rowspan=\"2\" class=\"auteur\">Auteurs</td>\n    <td rowspan=\"2\" class=\"album\">Titres</td>\n" ;
	private final static String TABLE3 = "  </tr>\n  <tr class=\"head\">\n    <td class=\"an\">Début</td>\n    <td class=\"an\">Fin</td>\n  </tr>\n" ;

	public static void buildTable(ListeAlbum listeAlbums, StringBuilder fragment, String urlOffSet) {
		
		fragment.append(TABLE1) ;
		listeAlbums.getFormatListeAlbum().enteteFormat(fragment, null, 2) ;
		fragment.append(TABLE2) ;
        listeAlbums.getFormatListeAlbum().enteteFormat(fragment, null, 2) ;
        fragment.append(TABLE3) ;
		for (Album unAlbum : listeAlbums.getAlbums()) {
			
			fragment.append("  <tr>\n") ;
			TemporalAccessor debutComp = unAlbum.getDebutComposition() ;
			TemporalAccessor finComp   = unAlbum.getFinComposition() ;
			TemporalAccessor debutEnr  = unAlbum.getDebutEnregistrement() ;
			TemporalAccessor finEnr    = unAlbum.getFinEnregistrement() ;
			
			boolean displayDateEnregistrement =  unAlbum.isSpecifiedCompositionDate() ;
			
			fragment.append("    <td class=\"an\">").append(TemporalUtils.formatDate(debutComp)) ;
			if (displayDateEnregistrement) {
				fragment.append("<br/>").append( TemporalUtils.formatDate(debutEnr)) ;
			}
			fragment.append("</td>\n") ;
			
			fragment.append("    <td class=\"an\">").append(TemporalUtils.formatDate(finComp)) ;
			if (displayDateEnregistrement) {
				fragment.append("<br/>").append(TemporalUtils.formatDate(finEnr)) ;
			}
			fragment.append("</td>\n    <td class=\"auteur\">\n") ;
			
			if (unAlbum.getAuteurs() != null) {
				for (Artiste unArtiste : unAlbum.getAuteurs()) {
					fragment.append("      <a href=\"").append(urlOffSet).append(unArtiste.getUrlHtml()).append("\">") ;
					fragment.append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;
				}
			}
			fragment.append("    </td>\n    <td class=\"album\">") ; 
			if (unAlbum.additionnalInfo()) {
				fragment.append("<a href=\"").append(unAlbum.getFullPathHtmlFileName()).append("\">") ;
			}
			fragment.append(unAlbum.getTitre()) ;
			if (unAlbum.additionnalInfo()) {
				fragment.append("</a>\n") ;
			}
			printIntervenant(unAlbum, fragment, urlOffSet) ;
			fragment.append("    </td>\n") ;
			unAlbum.getFormatAlbum().rowFormat(fragment, null) ;
			fragment.append("  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}

	private static void printIntervenant(Album unAlbum, StringBuilder fragment, String urlOffset) {	

		if ((unAlbum.getChefsOrchestre() != null) || (unAlbum.getInterpretes() != null) || (unAlbum.getEnsembles() != null)) {
		
			fragment.append("      <ul class=\"interv\">") ;

			if (unAlbum.getChefsOrchestre() != null) {
				for (Artiste unChef : unAlbum.getChefsOrchestre()) {
					fragment.append("      <li>Direction: <a href=\"")
							.append(urlOffset)
							.append(unChef.getUrlHtml())
							.append("\">")
							.append(unChef.getPrenoms())
							.append(" ")
							.append(unChef.getNom())
							.append("</a></li>\n") ;
				}
			}
			
			if (unAlbum.getInterpretes() != null) {
				for (Artiste unInterprete : unAlbum.getInterpretes()) {
					fragment.append("      <li>Interpr&egrave;te: <a href=\"")
							.append(urlOffset)
							.append(unInterprete.getUrlHtml())
							.append("\">")
							.append(unInterprete.getPrenoms())
							.append(" ")
							.append(unInterprete.getNom())
							.append("</a></li>\n") ;
				}
			}	
			
			if (unAlbum.getEnsembles() != null) {
				for (Artiste unGroupe : unAlbum.getEnsembles()) {
					fragment.append("      <li>Ensemble: <a href=\"")
							.append(urlOffset)
							.append(unGroupe.getUrlHtml())
							.append("\">")
							.append(unGroupe.getPrenoms())
							.append(" ")
							.append(unGroupe.getNom())
							.append("</a></li>\n") ;
				}
			}
			fragment.append("      </ul>") ;
		}
	}
}
