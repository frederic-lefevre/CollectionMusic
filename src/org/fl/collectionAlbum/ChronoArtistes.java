package org.fl.collectionAlbum;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;

public class ChronoArtistes  implements HtmlReportPrintable {

	private static String styles[] = {"main","rapport", "calendrier"} ;
	private ListeArtiste anniversaires[][] ;
	private final static String monthPattern = new String("MMMM") ;
	private DateTimeFormatter monthFormatter;
	private final static String dayPattern = new String("dd") ;
	private DateTimeFormatter dayFormatter;
	
	private Logger chronoLog ;
	
	/**
	 * 
	 */
	public ChronoArtistes(Logger cl) {
		super();
		chronoLog = cl ;
		monthFormatter = DateTimeFormatter.ofPattern(monthPattern, Locale.FRANCE) ;
		dayFormatter   = DateTimeFormatter.ofPattern(dayPattern,   Locale.FRANCE) ;

		int nbMonths = (int)ChronoField.MONTH_OF_YEAR.range().getMaximum() ;
		int nbDays	 = (int)ChronoField.DAY_OF_MONTH.range().getMaximum() ;
		anniversaires = new ListeArtiste[nbMonths+1][nbDays+1] ;
	}
	
	public String[] getCssStyles() {
		return styles ;
	}
	
	/**
	 * Add an artiste in stats
	 * @param a Artiste
	 */
	public void add(Artiste a) {
		TemporalAccessor naissance = a.getNaissance() ;
		TemporalAccessor mort = a.getMort() ;
		if (naissance != null) {
			addDate(a, naissance) ;
		}
		if (mort != null) {
			addDate(a, mort) ;
		}
	}
	
	private void addDate(Artiste a, TemporalAccessor date) {
		int day, month ;
		try {
			
			if (date.isSupported(ChronoField.DAY_OF_MONTH) && date.isSupported(ChronoField.MONTH_OF_YEAR)) {
				day   = date.get(ChronoField.DAY_OF_MONTH) ;
				month = date.get(ChronoField.MONTH_OF_YEAR) ;
				
				if (anniversaires[month][day] == null) {
					anniversaires[month][day] = new ListeArtiste(chronoLog) ;
				}
				anniversaires[month][day].addArtiste(a) ;
			}
		} catch (DateTimeException dte) {
			chronoLog.log(Level.SEVERE, "Unable to convert date for " + a.getNom(), dte);
		}
	}

	/**
	 * Print a stat report
	 * @param rFile : destination file
	 */
	public void rapport(RapportHtml rapport, int typeRapport, String urlOffset) {
		
		int nbMonths = (int)ChronoField.MONTH_OF_YEAR.range().getMaximum() ;
		int nbDays	 = (int)ChronoField.DAY_OF_MONTH.range().getMaximum() ;

		int day, month, year ;
		year = 1904 ;
		
		rapport.write("<div class=\"calendrier\">\n  <div class=\"mois\">\n") ;
		for (month = 1; month <= nbMonths ; month++) {
			
			LocalDate thisMonthLd = LocalDate.of(year, month, 1) ;
			String    thisMonth   = monthFormatter.format(thisMonthLd) ;
			rapport.write("    <div class=\"titremois\">").write(thisMonth).write("\n      <div class=\"jour\">\n") ;
			
			nbDays = thisMonthLd.lengthOfMonth() ;
			for (day = 1; day <= nbDays; day++) {
				chronoLog.fine("Print calendrier day=" + day);
				String thisDay = dayFormatter.format(LocalDate.of(year, month, day)) ;
				if (anniversaires[month][day] != null) {
					rapport.write("        <div class=\"birthday\">") ;
					rapport.write(thisDay) ;
					rapport.write("\n<div class=\"listea\">\n") ;
					anniversaires[month][day].rapport(rapport, ListeArtiste.rapportSimpleAlpha, "") ;
					rapport.write("</div>\n") ;
				} else {
					rapport.write("        <div class=\"noBirthday\">") ;
					rapport.write(thisDay) ;
				}
				rapport.write("        </div>\n") ;
			}
			rapport.write("      </div>\n    </div>\n") ;
		}
		rapport.write("  </div>\n</div>\n") ;
	}
}
