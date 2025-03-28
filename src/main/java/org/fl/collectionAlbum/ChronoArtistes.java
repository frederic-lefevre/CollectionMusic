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

package org.fl.collectionAlbum;

import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.utils.AnniversaryCalendar;

public class ChronoArtistes {

	private final AnniversaryCalendar<Artiste> anniversaires ;

	public ChronoArtistes() {
		anniversaires = new AnniversaryCalendar<Artiste>() ;
	}
	
	public void reset() {
		anniversaires.reset();
	}
	
	public void add(Artiste a) {
		TemporalAccessor naissance = a.getNaissance() ;
		TemporalAccessor mort = a.getMort() ;
		if (naissance != null) {
			anniversaires.addAnniversary(a, naissance) ;
		}
		if (mort != null) {
			anniversaires.addAnniversary(a, mort) ;
		}
	}
	
	public List<Artiste> getChronoArtistes(TemporalAccessor date) {
		return anniversaires.getAnniversaries(date) ;
	}
}
