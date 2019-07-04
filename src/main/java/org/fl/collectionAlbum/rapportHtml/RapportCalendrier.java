package org.fl.collectionAlbum.rapportHtml;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.fl.collectionAlbum.ChronoArtistes;
import org.fl.collectionAlbum.artistes.Artiste;

public class RapportCalendrier  extends RapportHtml {

	private final static String monthPattern = new String("MMMM") ;
	private final static String dayPattern 	 = new String("dd"  ) ;
	
	private final DateTimeFormatter monthFormatter;
	private final DateTimeFormatter dayFormatter;
	
	private final ChronoArtistes rappochronoArtistes ;
	
	public RapportCalendrier(ChronoArtistes rca, String titre, Logger rl) {
		super(titre, rl);
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		withTitleDisplayed() ;
		monthFormatter 		= DateTimeFormatter.ofPattern(monthPattern, Locale.FRANCE) ;
		dayFormatter   		= DateTimeFormatter.ofPattern(dayPattern,   Locale.FRANCE) ;
		rappochronoArtistes = rca ;
	}

	protected void corpsRapport() {
		
		int nbMonths = (int)ChronoField.MONTH_OF_YEAR.range().getMaximum() ;
		int nbDays	 = (int)ChronoField.DAY_OF_MONTH.range().getMaximum() ;

		int day, month, year ;
		year = 1904 ;
		
		write("<div class=\"calendrier\">\n  <div class=\"mois\">\n") ;
		for (month = 1; month <= nbMonths ; month++) {
			
			LocalDate thisMonthLd = LocalDate.of(year, month, 1) ;
			String    thisMonth   = monthFormatter.format(thisMonthLd) ;
			write("    <div class=\"titremois\">").write(thisMonth).write("\n      <div class=\"jour\">\n") ;
			
			nbDays = thisMonthLd.lengthOfMonth() ;
			for (day = 1; day <= nbDays; day++) {
				rapportLog.fine("Print calendrier day=" + day);
				LocalDate thisDay = LocalDate.of(year, month, day) ;
				List<Artiste> artistesDuJour = rappochronoArtistes.getChronoArtistes(MonthDay.from(thisDay)) ;
				if (artistesDuJour != null) {
					write("        <div class=\"birthday\">") ;
					write(dayFormatter.format(thisDay)) ;
					write("\n<div class=\"listea\">\n") ;
					FragmentListeArtistesSimple.buildTable(artistesDuJour, rBuilder) ;
					write("</div>\n") ;
				} else {
					write("        <div class=\"noBirthday\">") ;
					write(dayFormatter.format(thisDay)) ;
				}
				write("        </div>\n") ;
			}
			write("      </div>\n    </div>\n") ;
		}
		write("  </div>\n</div>\n") ;		
	}	
}
