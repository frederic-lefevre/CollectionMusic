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

package org.fl.collectionAlbum.albums;

import java.time.temporal.TemporalAccessor;
import java.util.Comparator;

import org.fl.collectionAlbum.utils.TemporalUtils;

public class AlbumEnregistrementComparator implements Comparator<Album> {

	public int compare(Album arg0, Album arg1) {
		
		TemporalAccessor d0 = arg0.getDebutEnregistrement();
		TemporalAccessor d1 = arg1.getDebutEnregistrement();

		int comp = TemporalUtils.compareTemporal(d0, d1);

		if (comp == 0) {
			TemporalAccessor d2 = arg0.getFinEnregistrement();
			TemporalAccessor d3 = arg1.getFinEnregistrement();
			comp = TemporalUtils.compareTemporal(d2, d3);
		}
		return comp;
	}
}
