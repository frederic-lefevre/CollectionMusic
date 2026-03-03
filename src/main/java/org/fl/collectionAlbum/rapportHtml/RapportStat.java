/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.stat.StatChrono;
import org.fl.collectionAlbum.stat.StatistiquesView;
import org.fl.collectionAlbum.stat.StatistiquesView.Granularite;

public class RapportStat extends RapportHtml {
		
	private static final Function<Double, String> statToStringFunction = (d) ->  Optional.ofNullable(d).map(poids -> Format.poidsToString(poids)).orElse("0");
	
	private final StatChrono statChrono;
	private final Granularite granularite;

	public RapportStat(StatChrono sc, String titre, LinkType linkType, Granularite granularite) {
		super(titre, linkType);
		withHtmlLinkList(RapportStructuresAndNames.getAccueils());
		withTitleDisplayed();
		statChrono = sc;
		this.granularite = granularite;
	}

	// Return a html hyper to this rapport
	protected void corpsRapport() {

		StatistiquesView statistiquesView = new StatistiquesView(statChrono, granularite, statToStringFunction);
		TreeMap<Integer, Double> statisquesMap = statistiquesView.getStatisquesMap();
		int pas = statistiquesView.getPas();

		write("<table class=\"stat\">\n  <tr>\n    <td class=\"dece\"></td>\n    <td class=\"statotal\">Total</td>\n");
		for (int i = 0; i < 10; i++) {
			write("    <td class=\"anH\">").write(i*pas).write("</td>\n");
		}
		write("  </tr>\n");

		statisquesMap.forEach((anDebut, poids) -> {
			write("  <tr class=\"statan\">\n");
			write("    <td class=\"dece\"><span class=\"dece\">").write(anDebut).write("</span></td>\n");
			write("    <td class=\"statotal\">").write(Format.poidsToString(poids)).write("</td>\n");
			for (int i = 0; i < 10; i++) {
				
				int an = anDebut + i*pas;
				String count = statistiquesView.getStatFor(an);
				
				String cssClass;
				if (count.length() == 0) {
					cssClass = "statan0";
				} else {
					cssClass = "statan";
				}
				write("    <td class=\"").write(cssClass).write("\">").write(count).write("<span class=\"annee\">")
						.write(an).write("</span><span class=\"count\">").write(count).write("</span></td>\n");
			}
			write("  </tr>\n");
		});
		write("</table>\n");
	}
}
