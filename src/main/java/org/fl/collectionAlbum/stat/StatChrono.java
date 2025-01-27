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

package org.fl.collectionAlbum.stat;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatChrono {
	
	private final static Logger statLogger = Logger.getLogger(StatChrono.class.getName());

	private final List<StatAnnee> statAnnuelle;
	private final List<StatAnnee> statDecennale;
	private final List<StatAnnee> statSiecle;

	private static final StatAnComparator statComp = new StatAnComparator();

	public StatChrono() {
		super();
		statAnnuelle = new ArrayList<StatAnnee>();
		statDecennale = new ArrayList<StatAnnee>();
		statSiecle = new ArrayList<StatAnnee>();
	}

	public void reset() {
		statAnnuelle.clear();
		statDecennale.clear();
		statSiecle.clear();
	}
	
	public void AddAlbum(TemporalAccessor dateAlbum, double poidsAlbum) {

		int year = getYearFromDate(dateAlbum);
		int decennie = getDecennie(year);
		int siecle = getSiecle(year);

		incrementStat(statAnnuelle, year, poidsAlbum);
		incrementStat(statDecennale, decennie, poidsAlbum);
		incrementStat(statSiecle, siecle, poidsAlbum);
	}

	private int getDecennie(int year) {
		return (year / 10) * 10;

	}

	private int getSiecle(int year) {
		return (year / 100) * 100;

	}

	private int getYearFromDate(TemporalAccessor dd) {

		try {
			if (dd.isSupported(ChronoField.YEAR)) {
				return dd.get(ChronoField.YEAR);
			} else {
				statLogger.severe("Cannot extract year from temporal accessor");
				return 0;
			}
		} catch (Exception e) {
			statLogger.log(Level.SEVERE, "Exception creating date in statChrono", e);
			return 0;
		}
	}

	private void incrementStat(List<StatAnnee> stat, int an, double poids) {
		for (StatAnnee sA : stat) {
			if (sA.getAn() == an) {
				sA.incrementNombre(poids);
				return;
			}
		}
		stat.add(new StatAnnee(an, poids));
	}

	public String getStatForYear(int an) {
		return getStat(statAnnuelle, an);
	}

	public String getStatForDecennie(int an) {
		return getStat(statDecennale, getDecennie(an));
	}

	private String getStat(List<StatAnnee> statAns, int an) {
		return statAns.stream().filter(sA -> sA.getAn() == an).findFirst().map(sA -> sA.getNombre())
				.orElse(new String("0"));
	}

	public List<StatAnnee> getStatAnnuelle() {
		Collections.sort(statAnnuelle, statComp);
		return statAnnuelle;
	}

	public List<StatAnnee> getStatDecennale() {
		Collections.sort(statDecennale, statComp);
		return statDecennale;
	}

	public List<StatAnnee> getStatSiecle() {
		Collections.sort(statSiecle, statComp);
		return statSiecle;
	}

}
