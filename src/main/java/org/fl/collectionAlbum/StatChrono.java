package org.fl.collectionAlbum;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatChrono implements HtmlReportPrintable {
	
	private static String styles[] = {"main","stat"} ;
	private List<StatAnnee> StatAnnuelle ; 
	private List<StatAnnee> StatDecennale ;
	private List<StatAnnee> StatSiecle ;
	private Logger statLogger;
	
    public StatChrono(Logger sl) {
        super();
        statLogger = sl ;
        StatAnnuelle  = new ArrayList<StatAnnee>(2000) ;
        StatDecennale  = new ArrayList<StatAnnee>(200) ;
        StatSiecle  = new ArrayList<StatAnnee>(20) ;
    }
    
	public String[] getCssStyles() {
		return styles ;
	}

	public void AddAlbum(TemporalAccessor dateAlbum, double poidsAlbum) {
		
		int year = getYearFromDate(dateAlbum) ;
		if (! isMemberOf(StatAnnuelle, year, poidsAlbum)) {
			StatAnnee nouvelAnnee = new StatAnnee(year, poidsAlbum) ;
			StatAnnuelle.add(nouvelAnnee) ;
		}
		int decennie = (year / 10) * 10 ;
		if (! isMemberOf(StatDecennale, decennie, poidsAlbum)) {
			StatAnnee nouvelDecennie = new StatAnnee(decennie, poidsAlbum) ;
			StatDecennale.add(nouvelDecennie) ;
		}
		int siecle = (year / 100) * 100 ;
		if (! isMemberOf(StatSiecle, siecle, poidsAlbum)) {
			StatAnnee nouveauSiecle = new StatAnnee(siecle, poidsAlbum) ;
			StatSiecle.add(nouveauSiecle) ;
		}
	}
	
	private int getYearFromDate(TemporalAccessor dd) {
		
		try {
			if (dd.isSupported(ChronoField.YEAR)) {
				return dd.get(ChronoField.YEAR) ;
			} else {
				statLogger.severe("Cannot extract year from temporal accessor");
				return 0 ;
			}
		} catch (Exception e) {			
			statLogger.log(Level.SEVERE, "Exception creating date in statChrono", e) ;
			return 0 ;
		}
	}

	private boolean isMemberOf(List<StatAnnee> stat, int an, double poids) {
		for (StatAnnee sA : stat) {
			if (sA.getAn() == an) {
				sA.incrementNombre(poids) ;
				return true ;
			}
		}
		return false ;
	}
	
	private String getStatForYears(int an, boolean decennie) {
		Iterator<StatAnnee> ea ;
		if (decennie) {
		    ea = StatDecennale.iterator() ;
		} else {
		    ea = StatAnnuelle.iterator() ;
		}
		while (ea.hasNext()) {
			StatAnnee sA = ea.next() ;
			if (sA.getAn() == an) {
				return sA.getNombre() ;
			}
		}
		String none = new String("0") ;
		return none ;
	}
	
	/**
	 * Print the html table for the statistique
	 * @param rFile
	 */
	public void rapport(RapportHtml rapport, int typeRapport, String urlOffset) {
		
		StatAnComparator statComp = new StatAnComparator() ;
		Collections.sort(StatAnnuelle,statComp) ;
		Collections.sort(StatDecennale,statComp) ;
		Collections.sort(StatSiecle,statComp) ;
		
		Iterator<StatAnnee> ed ;
		boolean plusieursSiecles = false ;
		if (StatSiecle.size() > 2) {
		    plusieursSiecles = true ;
		    ed = StatSiecle.iterator() ; 
		} else {
		    ed = StatDecennale.iterator() ;
		}   
		
		try {
			rapport.write("<table class=\"stat\">\n  <tr>\n    <td class=\"dece\"></td>\n    <td class=\"statotal\">Total</td>\n") ;
			for (int i=0; i <10; i++) {
			    int sub = i ;
			    if (plusieursSiecles) sub = i*10;
			    rapport.write("    <td class=\"anH\">").write(sub).write("</td>\n") ;
			}
			rapport.write("  </tr>\n") ;
			String cssClass = "statan" ;
			while (ed.hasNext()) {
				rapport.write("  <tr class=\"statan\">\n") ;
				StatAnnee uneSubdivision = ed.next() ; 
				rapport.write("    <td class=\"dece\"><span class=\"dece\">").write(uneSubdivision.getAn()).write("</span></td>\n") ;
				rapport.write("    <td class=\"statotal\">").write(uneSubdivision.getNombre()).write("</td>\n") ;
				for (int i=0; i <10; i++) {
				    int sub = i ;
				    if (plusieursSiecles) sub = i*10;

					int an = uneSubdivision.getAn() + sub ;
					String count = getStatForYears(uneSubdivision.getAn() + sub, plusieursSiecles) ;
					
					if (count.length() == 0) {
						cssClass = "statan0" ;
					} else {
						cssClass = "statan" ;
					}
					rapport.write("    <td class=\"").write(cssClass).write("\">").write(count).write("<span class=\"annee\">").write(an).write("</span><span class=\"count\">").write(count).write("</span></td>\n") ;
				}
				rapport.write("  </tr>\n") ;
			}
			rapport.write("</table>\n") ;
		} catch (Exception e) {
		    statLogger.log(Level.SEVERE, "Erreur dans la création du fichier rapport", e) ;
		}
	}
}
