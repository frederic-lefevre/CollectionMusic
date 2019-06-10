package org.fl.collectionAlbum.concerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.HtmlReportPrintable;
import org.fl.collectionAlbum.RapportHtml;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class ListeConcert implements HtmlReportPrintable {

	private List<Concert> concerts ;
	
	private Logger listConcertLog;
	
	private static String styles[] = {"main","format","rapport"} ;
	
	public ListeConcert(Logger cl) {
		
		listConcertLog = cl ;
		concerts = new ArrayList<Concert>() ;
	}
	
	public String[] getCssStyles() {
		return styles ;
	}
	

	public void addConcert(Concert a) {
		concerts.add(a) ;
	}
	
	public void rapport(RapportHtml rapport, int typeRapport, String urlOffset) {
		
		try {
			ConcertChronoComparator compConcert = new ConcertChronoComparator(listConcertLog);
			Collections.sort(concerts, compConcert) ;
			rapportTable(rapport, concerts, urlOffset) ;
		} catch (Exception e) {
			listConcertLog.log(Level.SEVERE, "Erreur dans la création du fichier rapport ", e) ;
		}
	}
	
	// HTML fragment
	private final static String table1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td class=\"an\">Date</td>\n    <td class=\"auteur\">Artistes</td>\n    <td class=\"album\">Lieu</td>\n  </tr>\n  </table>\n</div>\n  <table>\n  <tr class=\"head\">\n    <td class=\"an\">Date</td>\n    <td class=\"auteur\">Artistes</td>\n    <td class=\"album\">Lieu</td>\n  </tr>\n" ;
	
	private void rapportTable(RapportHtml rapport, List<Concert> listeConcerts, String urlOffset) {
	
		try {
			rapport.write(table1) ;
			for (Concert unConcert : listeConcerts) {
				String dateConcert = TemporalUtils.formatDate(unConcert.getDateConcert()) ;
				
				rapport.write("  <tr>\n    <td class=\"an\">") ;
				if (unConcert.additionnalInfo()) {
					rapport.write("<a href=\"").write(unConcert.getFullPathHtmlFileName()).write("\">") ;
				}
				rapport.write(dateConcert) ;
				if (unConcert.additionnalInfo()) {
					rapport.write("</a>") ;
				}
				rapport.write("</td>\n    <td class=\"auteur\">\n") ;
				
				if (unConcert.getAuteurs() != null) {
					for (Artiste unArtiste : unConcert.getAuteurs()) {
						rapport.write("      <a href=\"").write(urlOffset).write(unArtiste.getConcertUrlHtml()).write("\">").write(unArtiste.getPrenoms()).write(" ").write(unArtiste.getNom()).write("</a><br/>\n") ;
					}
				}
				rapport.write("    </td>\n    <td class=\"album\">").write(unConcert.getLieuConcert()).write("\n") ;
				
				if (unConcert.getChefsOrchestre() != null) {
					for (Artiste unChef : unConcert.getChefsOrchestre()) {
						rapport.write("      <li>Direction: <a href=\"").write(urlOffset).write(unChef.getConcertUrlHtml()).write("\">").write(unChef.getPrenoms()).write(" ").write(unChef.getNom()).write("</a></li>\n") ;
					}
				}
				
				if (unConcert.getInterpretes() != null) {
					for (Artiste unInterprete : unConcert.getInterpretes()) {
						rapport.write("      <li>Interpr&egrave;te: <a href=\"").write(urlOffset).write(unInterprete.getConcertUrlHtml()).write("\">").write(unInterprete.getPrenoms()).write(" ").write(unInterprete.getNom()).write("</a></li>\n") ;
					}
				}
				
				if (unConcert.getEnsembles() != null) {
					for (Artiste unGroupe : unConcert.getEnsembles()) {
						rapport.write("      <li>Ensemble: <a href=\"").write(urlOffset).write(unGroupe.getConcertUrlHtml()).write("\">").write(unGroupe.getPrenoms()).write(" ").write(unGroupe.getNom()).write("</a></li>\n") ;
					}
				}
				rapport.write("    </td>\n  </tr>\n") ;
			}
			rapport.write("</table>\n") ;
		} catch (Exception e) {
			listConcertLog.log(Level.SEVERE, "Erreur dans la création du fichier rapport", e) ;
		}
	}
	
	public int getNombreConcerts() {
		return concerts.size() ;
	}
	
	public List<Concert> getConcerts() {
		return concerts;
	}

	public void rapportAdditionnalInfo() {
		
		for (Concert c : concerts) {
			c.generateHtml() ;
		}
	}
}
