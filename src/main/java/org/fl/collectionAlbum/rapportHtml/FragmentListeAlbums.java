package org.fl.collectionAlbum.rapportHtml;

import java.nio.file.Path;
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
					appendLinkAlbumArtiste(unArtiste, fragment, urlOffSet) ;
				}
			}
			fragment.append("    </td>\n    <td class=\"album\">") ; 
			
			Path aPath = RapportStructuresAndNames.getAlbumRapportPath(unAlbum) ;
			if (aPath != null) {
				fragment.append("<a href=\"").append(aPath.toString()).append("\">") ;
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
					fragment.append("      <li>Direction: ") ;
					appendLinkAlbumArtiste(unChef, fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}
			
			if (unAlbum.getInterpretes() != null) {
				for (Artiste unInterprete : unAlbum.getInterpretes()) {
					fragment.append("      <li>Interpr&egrave;te: ") ;
					appendLinkAlbumArtiste(unInterprete, fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}	
			
			if (unAlbum.getEnsembles() != null) {
				for (Artiste unGroupe : unAlbum.getEnsembles()) {
					fragment.append("      <li>Ensemble: ") ;
					appendLinkAlbumArtiste(unGroupe, fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}
			fragment.append("      </ul>") ;
		}
	}
	
	private static void appendLinkAlbumArtiste(Artiste unArtiste, StringBuilder fragment,  String urlOffset) {
		Path albumPath = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(unArtiste) ;
		if (albumPath != null) {
			fragment.append("      <a href=\"").append(urlOffset).append(albumPath.toString()).append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;
		}
	}
}
