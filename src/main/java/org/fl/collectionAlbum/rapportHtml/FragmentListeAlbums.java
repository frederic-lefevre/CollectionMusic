package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
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

	public static void buildTable(ListeAlbum listeAlbums, StringBuilder fragment, String urlOffSet, Balises balises) {
		
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
			
			boolean displayDateEnregistrement =  unAlbum.hasSpecificCompositionDates() ;
			
			fragment.append("    <td class=\"an\">") ;
			if (balises != null) {
				if (balises.getBalisesType() == Balises.BalisesType.TEMPORAL) {
					balises.addCheckBaliseTemporal(fragment, debutEnr) ;
				} else if (balises.getBalisesType() == Balises.BalisesType.TEMPORAL_COMPOSITION) {
					balises.addCheckBaliseTemporal(fragment, debutComp) ;
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
			
			if (unAlbum.getAuteurs() != null) {
				for (Artiste unArtiste : unAlbum.getAuteurs()) {
					appendLinkAlbumArtiste(unArtiste, fragment, urlOffSet) ;
				}
			}
			fragment.append("    </td>\n    <td class=\"album\">") ; 
			
			if (unAlbum.additionnalInfo()) {
				URI aPath = RapportStructuresAndNames.getAlbumRapportRelativePath(unAlbum) ;			
				fragment.append("<a href=\"").append(urlOffSet).append(aPath.toString()).append("\">") ;
				fragment.append(unAlbum.getTitre()) ;			
				fragment.append("</a>\n") ;
			} else {
				fragment.append(unAlbum.getTitre()) ;
			}
			
			FragmentIntervenants.printIntervenant(unAlbum, fragment, urlOffSet) ;
			fragment.append("    </td>\n") ;
			unAlbum.getFormatAlbum().rowFormat(fragment, null) ;
			fragment.append("  </tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
	
	private static void appendLinkAlbumArtiste(Artiste unArtiste, StringBuilder fragment,  String urlOffset) {
		URI albumUri = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(unArtiste) ;
		if (albumUri != null) {
			fragment.append("      <a href=\"").append(urlOffset).append(albumUri.toString()).append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;
		}
	}
}
