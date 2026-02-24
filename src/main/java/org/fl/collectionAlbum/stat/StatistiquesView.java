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

public class StatistiquesView {

	private static final int UN_AN = 1;
	private static final int DIX_AN = 10;
	
	private final StatChrono statChrono;
	private final TreeMap<Integer, Double> statisquesMap;
	private final int pas;
	
	public StatistiquesView(StatChrono statChrono, int maxNumbers) {
		this.statChrono = statChrono;
		
		if (statChrono.getMaxYear() - statChrono.getMinYear() > maxNumbers) {
			statisquesMap = statChrono.getStatistiqueSiecle();
			pas = 10;
		} else {
			statisquesMap = statChrono.getStatistiqueDecennale();
			pas = UN_AN;
		}
	}
	
	public TreeMap<Integer, Double> getStatisquesMap() {
		return statisquesMap;
	}
	
	public int getPas() {
		return pas;
	}
	
	public String getStatFor(int an) {
		if (pas == UN_AN) {
			return statChrono.getStatForYear(an);
		} else if (pas == DIX_AN) {
			return statChrono.getStatForDecennie(an);
		} else {
			return "";
		}
	}
	
	public int getLineNumber() {
		
		if (statisquesMap.isEmpty()) {
			return 0;
		} else {
			int lineSpanOfYears = 10*pas;
			return 1 + (statChrono.getMaxYear()/lineSpanOfYears - statChrono.getMinYear()/lineSpanOfYears);
		}
	}
}
