/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import java.util.Comparator;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.AuteurComparator;

public class ConcertPoidsComparator implements Comparator<Artiste> {
	
	public int compare(Artiste arg0, Artiste arg1) {
		
		float poids0 = arg0.getNbConcert()  ;
		float poids1 = arg1.getNbConcert() ;
		
		int ordreNom = 0 ;
		if (poids0 == poids1) {
			ordreNom = (new AuteurComparator()).compare(arg0,arg1) ;	
		} else if (poids0 < poids1) {
			ordreNom = 1 ;
		} else if (poids0 > poids1) {
			ordreNom = -1 ;
		}
		return ordreNom ;
	}
}
