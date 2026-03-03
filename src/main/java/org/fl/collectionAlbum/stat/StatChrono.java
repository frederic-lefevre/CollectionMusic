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

package org.fl.collectionAlbum.stat;

import java.time.Year;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatChrono {
	
	private static final Logger statLogger = Logger.getLogger(StatChrono.class.getName());

	private final TreeMap<Integer, Double> statistiqueAnnuelle;
	private final TreeMap<Integer, Double> statistiqueDecennale;
	private final TreeMap<Integer, Double> statistiqueSiecle;
	
	private int minYear;
	private int maxYear;

	public StatChrono() {
		super();
		
		statistiqueAnnuelle = new TreeMap<>();
		statistiqueDecennale = new TreeMap<>();
		statistiqueSiecle = new TreeMap<>();
		
		minYear = Year.MAX_VALUE;
		maxYear = Year.MIN_VALUE;
	}

	public void reset() {
		statistiqueAnnuelle.clear();
		statistiqueDecennale.clear();
		statistiqueSiecle.clear();	
	}
	
	public void addToStatistic(TemporalAccessor date, double poids) {

		int year = getYearFromDate(date);
		int decennie = getDecennie(year);
		int siecle = getSiecle(year);
		
		incrementStat(statistiqueAnnuelle, year, poids);
		incrementStat(statistiqueDecennale, decennie, poids);
		incrementStat(statistiqueSiecle, siecle, poids);
		
		if (year < minYear) {
			minYear = year;
		}
		if (year > maxYear) {
			maxYear = year;
		}
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

	private void incrementStat(Map<Integer, Double> stat, int an, double poids) {
		
		if (stat.containsKey(an)) {
			stat.put(an, stat.get(an) + poids);
		} else {
			stat.put(an, poids);
		}
	}
	
	protected TreeMap<Integer, Double> getStatistiqueDecennale() {
		return statistiqueDecennale;
	}

	protected TreeMap<Integer, Double> getStatistiqueSiecle() {
		return statistiqueSiecle;
	}

	public Double getStatForYear(int an) {
		return statistiqueAnnuelle.get(an);
	}

	public Double getStatForDecennie(int an) {
		return statistiqueDecennale.get(getDecennie(an));
	}
	
	public Double getStatForSiecle(int an) {
		return statistiqueSiecle.get(getSiecle(an));
	}

	public int getMinYear() {
		return minYear;
	}

	public int getMaxYear() {
		return maxYear;
	}

}
