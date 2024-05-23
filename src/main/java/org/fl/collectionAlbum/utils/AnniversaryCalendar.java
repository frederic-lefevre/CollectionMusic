/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

package org.fl.collectionAlbum.utils;

import java.time.MonthDay;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnniversaryCalendar<T> {

	private Map<MonthDay, List<T>> anniversaires;

	public AnniversaryCalendar() {
		anniversaires = new HashMap<>();
	}

	public MonthDay addAnniversary(T a, TemporalAccessor date) {

		try {
			MonthDay monthDay = MonthDay.from(date);
			List<T> annivs = anniversaires.get(monthDay);
			if (annivs == null) {
				annivs = new ArrayList<T>();
				anniversaires.put(monthDay, annivs);
			}
			annivs.add(a);
			return monthDay;
		} catch (Exception e) {
			return null;
		}
	}

	public List<T> getAnniversaries(TemporalAccessor date) {
		try {
			MonthDay monthDay = MonthDay.from(date);
			return anniversaires.get(monthDay);
		} catch (Exception e) {
			return null;
		}
	}

	public int getNbAnniversaryDate() {
		return anniversaires.size();
	}

}
