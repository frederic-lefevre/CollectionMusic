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

package org.fl.collectionAlbum.concerts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.AuteurComparator;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class ConcertAuteurComparator implements Comparator<Concert> {

	private static final TemporalUtils.TemporalAccessorComparator temporalComparator = new TemporalUtils.TemporalAccessorComparator();
	private static final AuteurComparator auteurComparator = new AuteurComparator();
	
	@Override
	public int compare(Concert c1, Concert c2) {
		
		List<Artiste> l1 = c1.getAuteurs();
		List<Artiste> l2 = c2.getAuteurs();

		if ((l1 == null) || l1.isEmpty()) {
			if ((l2 == null) || l2.isEmpty()) {
				return temporalComparator.compare(c1.getDateConcert(), c2.getDateConcert());
			} else {
				return -1;
			}
		} else if ((l2 == null) || l2.isEmpty()) {
			return 1;
		} else {
			// Both concerts have authors
			if ((l1.size() == 1) && (l2.size() == 1)) {
				// Most frequent case, avoid sorting the lists
				return auteurComparator.compare(l1.get(0), l2.get(0));
			} else {
				Collections.sort(l1, auteurComparator);
				Collections.sort(l2, auteurComparator);
				return auteurComparator.compare(l1.get(0), l2.get(0));
			}
		}
	}
}
