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

import java.util.TreeMap;
import java.util.function.Function;

public class StatistiquesView {

	public enum Granularite {
		PAR_AN(1, "Décennie"),
		PAR_DECENNIE(10, "Siecle");
		
		private final int pas;
		private final String rowName;
		
		Granularite(int pas, String rowName) {
			this.pas = pas;
			this.rowName = rowName;
		}

		public int getPas() {
			return pas;
		}

		public String getRowName() {
			return rowName;
		}
	}

	private final StatChrono statChrono;
	private final TreeMap<Integer, Double> statisquesMap;
	private final Granularite granularite;
	private final int lineNumber;
	private final int lineSpanOfYears;
	private final int beginYear;
	private Function<Double, String> statToStringFunction;
	
	public StatistiquesView(StatChrono statChrono, Granularite granularite, Function<Double, String> statToStringFunction) {
		
		this.statChrono = statChrono;
		this.statToStringFunction = statToStringFunction;
		this.granularite = granularite;
		
		if (granularite == Granularite.PAR_DECENNIE) {
			statisquesMap = statChrono.getStatistiqueSiecle();
		} else {
			statisquesMap = statChrono.getStatistiqueDecennale();
		}
		
		if (statisquesMap.isEmpty()) {
			lineNumber = 0;
			lineSpanOfYears = 0;
			beginYear = 0;
		} else {
			lineSpanOfYears = 10*granularite.getPas();
			lineNumber = 1 + (statChrono.getMaxYear()/lineSpanOfYears - statChrono.getMinYear()/lineSpanOfYears);
			beginYear = (statChrono.getMinYear() / lineSpanOfYears) * lineSpanOfYears;
		}
	}
	
	public TreeMap<Integer, Double> getStatisquesMap() {
		return statisquesMap;
	}
	
	public int getPas() {
		return granularite.getPas();
	}
	
	
	public String getSubdivisionName() {
		return granularite.getRowName();
	}
	
	public String getStatFor(int an) {
		return switch (granularite) {
			case PAR_AN -> getStatAsString(statChrono.getStatForYear(an));
			case PAR_DECENNIE -> getStatAsString(statChrono.getStatForDecennie(an));
		};
	}
	
	public String getAccumulationStatFor(int an) {
		return switch (granularite) {
			case PAR_AN -> getStatAsString(statChrono.getStatForDecennie(an));
			case PAR_DECENNIE -> getStatAsString(statChrono.getStatForSiecle(an));
		};
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public int getYearForLine(int lineIndex) {
		
		if (lineNumber == 0) {
			throw new IllegalArgumentException("The statistics is empty. There is no lines");
		} else if ((lineNumber < lineIndex + 1) || (lineIndex < 0)) {
			throw new IllegalArgumentException("Out of bound line index: " + lineIndex);
		} else {
			return beginYear + lineIndex*lineSpanOfYears;
		}
	}
	
	private String getStatAsString(Double statAn) {
		return statToStringFunction.apply(statAn);
	}
}
