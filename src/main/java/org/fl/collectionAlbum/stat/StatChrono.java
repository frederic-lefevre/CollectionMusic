package org.fl.collectionAlbum.stat;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.rapportHtml.HtmlReportPrintable;
import org.fl.collectionAlbum.rapportHtml.RapportHtml;

public class StatChrono implements HtmlReportPrintable {
	
	private static String styles[] = {"main","stat"} ;
	private List<StatAnnee> statAnnuelle ; 
	private List<StatAnnee> statDecennale ;
	private List<StatAnnee> statSiecle ;
	private Logger 			statLogger;
	
    public StatChrono(Logger sl) {
        super();
        statLogger = sl ;
        statAnnuelle  = new ArrayList<StatAnnee>(2000) ;
        statDecennale = new ArrayList<StatAnnee>(200) ;
        statSiecle    = new ArrayList<StatAnnee>(20) ;
    }
    
	public String[] getCssStyles() {
		return styles ;
	}

	public void AddAlbum(TemporalAccessor dateAlbum, double poidsAlbum) {
		
		int year 	 = getYearFromDate(dateAlbum) ;
		int decennie = getDecennie(year) ;
		int siecle 	 = getSiecle(year) ;

		incrementStat(statAnnuelle,  year, 	   poidsAlbum) ;
		incrementStat(statDecennale, decennie, poidsAlbum) ;
		incrementStat(statSiecle, 	 siecle,   poidsAlbum) ;
	}
	
	private int getDecennie(int year) {
		return (year / 10)  * 10 ;
		
	}
	
	private int getSiecle(int year) {
		return (year / 100)  * 100 ;
		
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

	private void incrementStat(List<StatAnnee> stat, int an, double poids) {
		for (StatAnnee sA : stat) {
			if (sA.getAn() == an) {
				sA.incrementNombre(poids) ;
				return ;
			}
		}
		stat.add(new StatAnnee(an, poids)) ;
	}
	
	public String getStatForYear    (int an) { return getStat(statAnnuelle,  an) 			  ; }
	public String getStatForDecennie(int an) { return getStat(statDecennale, getDecennie(an)) ;	}
	
	private String getStat(List<StatAnnee> statAns, int an) {
		return statAns.stream()
				.filter(sA -> sA.getAn() == an)
				.findFirst()
				.map( sA -> sA.getNombre())
				.orElse(new String("0")) ;
	}
	
	public List<StatAnnee> getStatAnnuelle()  { return statAnnuelle	 ; }
	public List<StatAnnee> getStatDecennale() {	return statDecennale ; }
	public List<StatAnnee> getStatSiecle()	  {	return statSiecle	 ; }

	/**
	 * Print the html table for the statistique
	 * @param rFile
	 */
	public void rapport(RapportHtml rapport, int typeRapport, String urlOffset) {
		
		StatAnComparator statComp = new StatAnComparator() ;
		Collections.sort(statAnnuelle,statComp) ;
		Collections.sort(statDecennale,statComp) ;
		Collections.sort(statSiecle,statComp) ;
		
		Iterator<StatAnnee> ed ;
		boolean plusieursSiecles = false ;
		if (statSiecle.size() > 2) {
		    plusieursSiecles = true ;
		    ed = statSiecle.iterator() ; 
		} else {
		    ed = statDecennale.iterator() ;
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
					int an = uneSubdivision.getAn()	;			
					String count ;
					if (plusieursSiecles) {
						an = an + i*10 ;
						count = getStatForDecennie(an) ;
					} else {
						an = an + i ;
						count = getStatForYear(an) ;
					}
					
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
