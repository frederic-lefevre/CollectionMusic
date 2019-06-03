package org.fl.collectionAlbum;

import java.util.ArrayList;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;

public class ListeAlbum implements HtmlReportPrintable {
	
	private ArrayList<Album> albums ;
	
	private Format formatListeAlbum ;
	
	public static int rapportChronoEnregistrement = 0 ;
	public static int rapportRangement			  = 1 ;
	public static int rapportChronoComposition 	  = 5 ;
	
	private Logger listeAlbumLog;
	
	private static String styles[] = {"main","format","rapport"} ;

	public ListeAlbum(Logger al) {
		
		listeAlbumLog = al ;
		formatListeAlbum = new Format(null, listeAlbumLog) ;
		albums  = new ArrayList<Album>() ;
	}
	
	public String[] getCssStyles() {
		return styles ;
	}
	
	/**
	 * Add an album
	 * @param a Album
	 */
	public void addAlbum(Album a) {
		if (! albums.contains(a)) {
			albums.add(a) ;
			formatListeAlbum.incrementFormat(a.getFormatAlbum()) ;
		}
	}
	
	/**
	 * Print a report on this liste
	 * @param rFile destination file
	 * @param typeRapport report type
	 * @param urlOffset relative url
	 */
	public void rapport(RapportHtml rapport, int typeRapport, String urlOffset) {
		
		try {
			if (typeRapport == rapportChronoEnregistrement) {
				AlbumEnregistrementComparator compAlbum = new AlbumEnregistrementComparator(listeAlbumLog);
				Collections.sort(albums, compAlbum) ;
				rapportTable(rapport, albums, urlOffset) ;
			} else 	if (typeRapport == rapportChronoComposition) {
				AlbumCompositionComparator compAlbum = new AlbumCompositionComparator(listeAlbumLog);
				Collections.sort(albums, compAlbum) ;
				rapportTable(rapport, albums, urlOffset) ;
			} else if (typeRapport == rapportRangement) {
				RangementComparator rangeComp = new RangementComparator(listeAlbumLog) ;
				Collections.sort(albums, rangeComp) ;
				rapportTable(rapport, albums, urlOffset) ;
			} else { 
				rapport.write("<h2>Type de rapport inconnu</h2>") ;
				listeAlbumLog.severe("Type de rapport inconnu: " + typeRapport) ;
			}
		} catch (Exception e) {
			listeAlbumLog.log(Level.SEVERE, "Erreur dans la création du fichier rapport", e) ;
		}
	}
	
	// HTML fragments
	private final static String table1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td colspan=\"2\" class=\"an2\">Dates de composition /<br/>Dates d'enregistrement</td>\n    <td rowspan=\"2\" class=\"auteur\">Auteurs</td>\n    <td rowspan=\"2\" class=\"album\">Titres</td>\n" ;
	private final static String table2 = "  </tr>\n  <tr>\n    <td class=\"an\">D�but</td>\n    <td class=\"an\">Fin</td>\n  </tr>\n\n  </table>\n</div>\n<table>\n <tr class=\"head\">\n    <td colspan=\"2\" class=\"an2\">Dates de composition /<br/>Dates d'enregistrement</td>\n    <td rowspan=\"2\" class=\"auteur\">Auteurs</td>\n    <td rowspan=\"2\" class=\"album\">Titres</td>\n" ;
	private final static String table3 = "  </tr>\n  <tr class=\"head\">\n    <td class=\"an\">D�but</td>\n    <td class=\"an\">Fin</td>\n  </tr>\n" ;
	
	private void rapportTable(RapportHtml rapport, ArrayList<Album> listeAlbums, String urlOffset) {
		
		try {
			rapport.write(table1) ;
	        formatListeAlbum.enteteFormat(rapport, false, 2) ;
	        rapport.write(table2) ;
			formatListeAlbum.enteteFormat(rapport, false, 2) ;
			rapport.write(table3) ;
			for (Album unAlbum : listeAlbums) {
				
				rapport.write("  <tr>\n") ;
				TemporalAccessor debutComp = unAlbum.getDebutComposition() ;
				TemporalAccessor finComp   = unAlbum.getFinComposition() ;
				TemporalAccessor debutEnr  = unAlbum.getDebutEnregistrement() ;
				TemporalAccessor finEnr    = unAlbum.getFinEnregistrement() ;
				
				boolean displayDateEnregistrement =  unAlbum.isSpecifiedCompositionDate() ;
				
				rapport.write("    <td class=\"an\">").write(Control.formatDate(debutComp)) ;
				if (displayDateEnregistrement) {
					rapport.write("<br/>").write( Control.formatDate(debutEnr)) ;
				}
				rapport.write("</td>\n") ;
				
				rapport.write("    <td class=\"an\">").write(Control.formatDate(finComp)) ;
				if (displayDateEnregistrement) {
					rapport.write("<br/>").write(Control.formatDate(finEnr)) ;
				}
				rapport.write("</td>\n    <td class=\"auteur\">\n") ;
				
				if (unAlbum.getAuteurs() != null) {
					for (Artiste unArtiste : unAlbum.getAuteurs()) {
						rapport.write("      <a href=\"").write(urlOffset).write(unArtiste.getUrlHtml()).write("\">") ;
						rapport.write(unArtiste.getPrenoms()).write(" ").write(unArtiste.getNom()).write("</a><br/>\n") ;
					}
				}				rapport.write("    </td>\n    <td class=\"album\">") ; 
				if (unAlbum.additionnalInfo()) {
					rapport.write("<a href=\"").write(unAlbum.getFullPathHtmlFileName()).write("\">") ;
				}
				rapport.write(unAlbum.getTitre()) ;
				if (unAlbum.additionnalInfo()) {
					rapport.write("</a>\n") ;
				}
				printIntervenant( rapport, unAlbum, urlOffset) ;
				rapport.write("    </td>\n") ;
				unAlbum.getFormatAlbum().rowFormat(rapport, false) ;
				rapport.write("  </tr>\n") ;
			}
			rapport.write("</table>\n") ;
		} catch (Exception e) {
			listeAlbumLog.log(Level.SEVERE, "Erreur dans la création du fichier rapport", e) ;
		}
	}
	
	private void printIntervenant(RapportHtml rapport, Album unAlbum, String urlOffset) {	

		if ((unAlbum.getChefsOrchestre() != null) || (unAlbum.getInterpretes() != null) || (unAlbum.getEnsembles() != null)) {
		
			rapport.write("      <ul class=\"interv\">") ;

			if (unAlbum.getChefsOrchestre() != null) {
				for (Artiste unChef : unAlbum.getChefsOrchestre()) {
					rapport.write("      <li>Direction: <a href=\"").write(urlOffset).write(unChef.getUrlHtml()).write("\">").write(unChef.getPrenoms()).write(" ").write(unChef.getNom()).write("</a></li>\n") ;
				}
			}
			
			if (unAlbum.getInterpretes() != null) {
				for (Artiste unInterprete : unAlbum.getInterpretes()) {
					rapport.write("      <li>Interpr&egrave;te: <a href=\"").write(urlOffset).write(unInterprete.getUrlHtml()).write("\">").write(unInterprete.getPrenoms()).write(" ").write(unInterprete.getNom()).write("</a></li>\n") ;
				}
			}	
			
			if (unAlbum.getEnsembles() != null) {
				for (Artiste unGroupe : unAlbum.getEnsembles()) {
					rapport.write("      <li>Ensemble: <a href=\"").write(urlOffset).write(unGroupe.getUrlHtml()).write("\">").write(unGroupe.getPrenoms()).write(" ").write(unGroupe.getNom()).write("</a></li>\n") ;
				}
			}
			rapport.write("      </ul>") ;
		}
	}
	
	/**
	 * @return liste album format
	 */
	public Format getFormatListeAlbum() {
		return formatListeAlbum;
	}
	
	/**
	 * @return Nombre d'albums
	 */
	public int getNombreAlbums() {
		return albums.size() ;
	}
	
	public ArrayList<Album> getAlbums() {
		return albums;
	}

	public void rapportAdditionnalInfo() {
		
		for (Album abl : albums) {
			abl.generateHtml() ;
		}
	}

}
