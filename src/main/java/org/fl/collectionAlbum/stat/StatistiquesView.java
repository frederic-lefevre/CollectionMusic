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

	private static final int UN_AN = 1;
	private static final int DIX_AN = 10;
	
	private static final String DECENNIE = "Décennie";
	private static final String SIECLE = "Siecle";
	
	private final StatChrono statChrono;
	private final TreeMap<Integer, Double> statisquesMap;
	private final int pas;
	private final int lineNumber;
	private final int lineSpanOfYears;
	private final int beginYear;
	private Function<Double, String> statToStringFunction;
	
	public StatistiquesView(StatChrono statChrono, int maxNumbers, Function<Double, String> statToStringFunction) {
		
		this.statChrono = statChrono;
		this.statToStringFunction = statToStringFunction;
		
		if (statChrono.getMaxYear() - statChrono.getMinYear() > maxNumbers) {
			statisquesMap = statChrono.getStatistiqueSiecle();
			pas = DIX_AN;
		} else {
			statisquesMap = statChrono.getStatistiqueDecennale();
			pas = UN_AN;
		}
		
		if (statisquesMap.isEmpty()) {
			lineNumber = 0;
			lineSpanOfYears = 0;
			beginYear = 0;
		} else {
			lineSpanOfYears = 10*pas;
			lineNumber = 1 + (statChrono.getMaxYear()/lineSpanOfYears - statChrono.getMinYear()/lineSpanOfYears);
			beginYear = (statChrono.getMinYear() / lineSpanOfYears) * lineSpanOfYears;
		}
	}
	
	public TreeMap<Integer, Double> getStatisquesMap() {
		return statisquesMap;
	}
	
	public int getPas() {
		return pas;
	}
	
	public String getSubdivisionName() {
		
		if (pas == UN_AN) {
			return DECENNIE;
		} else if (pas == DIX_AN) {
			return SIECLE;
		} else {
			throw new IllegalStateException("pas should be equal to 1 or 10. It is " + pas);
		}
	}
	
	public String getStatFor(int an) {
		if (pas == UN_AN) {
			return getStatAsString(statChrono.getStatForYear(an));
		} else if (pas == DIX_AN) {
			return getStatAsString(statChrono.getStatForDecennie(an));
		} else {
			throw new IllegalStateException("pas should be equal to 1 or 10. It is " + pas);
		}
	}
	
	public String getAccumulationStatFor(int an) {
		if (pas == UN_AN) {
			return getStatAsString(statChrono.getStatForDecennie(an));
		} else if (pas == DIX_AN) {
			return getStatAsString(statChrono.getStatForSiecle(an));
		} else {
			throw new IllegalStateException("pas should be equal to 1 or 10. It is " + pas);
		}
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
