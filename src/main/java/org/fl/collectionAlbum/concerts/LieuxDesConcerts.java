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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LieuxDesConcerts {

	private static Map<String, LieuConcert> lieuxConcerts ;
	
	public LieuxDesConcerts() {
		lieuxConcerts = new HashMap<String, LieuConcert>() ;
	}
	
	public void reset() {
		lieuxConcerts.clear();
	}
	
	public LieuConcert addLieuDunConcert(String lieu) {
		LieuConcert lieuConcert = lieuxConcerts.get(lieu) ;
		if (lieuConcert == null) {
			lieuConcert = new LieuConcert(lieu) ;
			lieuxConcerts.put(lieu, lieuConcert) ;
		}
		return lieuConcert ;
	}
	
	public List<LieuConcert> getLieuxConcerts() {
		LieuxPoidsComparator lieuxComparator = new LieuxPoidsComparator() ;
		List<LieuConcert> lieux = new ArrayList<LieuConcert>(lieuxConcerts.values()) ;
		Collections.sort(lieux, lieuxComparator) ;
		return lieux ;
	}
}
