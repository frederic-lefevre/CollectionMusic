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

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

import org.fl.collectionAlbum.ChronoArtistes;
import org.fl.collectionAlbum.artistes.Artiste;

public class RapportCalendrier  extends RapportHtml {

	private static final String monthPattern = new String("MMMM");
	private static final String dayPattern = new String("dd");

	private final DateTimeFormatter monthFormatter;
	private final DateTimeFormatter dayFormatter;

	private final ChronoArtistes rappochronoArtistes;

	public RapportCalendrier(ChronoArtistes rca, String titre, LinkType linkType) {
		super(titre, linkType);
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		withTitleDisplayed();
		monthFormatter = DateTimeFormatter.ofPattern(monthPattern, Locale.FRANCE);
		dayFormatter = DateTimeFormatter.ofPattern(dayPattern, Locale.FRANCE);
		rappochronoArtistes = rca;
	}

	protected void corpsRapport() {

		int nbMonths = (int) ChronoField.MONTH_OF_YEAR.range().getMaximum();
		int nbDays = (int) ChronoField.DAY_OF_MONTH.range().getMaximum();

		int day, month, year;
		year = 1904;

		write("<div class=\"calendrier\">\n  <div class=\"mois\">\n");
		for (month = 1; month <= nbMonths; month++) {

			LocalDate thisMonthLd = LocalDate.of(year, month, 1);
			String thisMonth = monthFormatter.format(thisMonthLd);
			write("    <div class=\"titremois\">").write(thisMonth).write("\n      <div class=\"jour\">\n");

			nbDays = thisMonthLd.lengthOfMonth();
			for (day = 1; day <= nbDays; day++) {
				rapportLog.fine("Print calendrier day=" + day);
				LocalDate thisDay = LocalDate.of(year, month, day);
				List<Artiste> artistesDuJour = rappochronoArtistes.getChronoArtistes(MonthDay.from(thisDay));
				if (artistesDuJour != null) {
					write("        <div class=\"birthday\">");
					write(dayFormatter.format(thisDay));
					write("\n<div class=\"listea\">\n");
					FragmentListeArtistesSimple.buildTable(artistesDuJour, rBuilder);
					write("</div>\n");
				} else {
					write("        <div class=\"noBirthday\">");
					write(dayFormatter.format(thisDay));
				}
				write("        </div>\n");
			}
			write("      </div>\n    </div>\n");
		}
		write("  </div>\n</div>\n");
	}
}
