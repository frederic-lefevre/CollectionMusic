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

package org.fl.collectionAlbum.utils;

import java.time.temporal.TemporalAccessor;
import java.util.logging.Logger;

public class FuzzyPeriod {

	private static final Logger logger = Logger.getLogger(FuzzyPeriod.class.getName());
	
	private final TemporalAccessor debut;
	private final TemporalAccessor fin;
	private final boolean isValid;

	public FuzzyPeriod(TemporalAccessor d, TemporalAccessor f) {

		debut = d;
		fin = f;

		if ((debut != null) && (fin != null)) {
			if (TemporalUtils.compareTemporal(debut, fin) > 0) {
				isValid = false;
				logger.warning("Période de dates invalides (début après fin)");
			} else {
				isValid = true;
			}
		} else {
			isValid = false;
			logger.warning("Période de dates invalides (début ou fin invalides)");
		}
	}

	public TemporalAccessor getDebut() {
		return debut;
	}

	public TemporalAccessor getFin() {
		return fin;
	}

	public boolean isValid() {
		return isValid;
	}

}
