/*
 MIT License

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

package org.fl.collectionAlbum.format;

// Définition des différents supports physiques tels qu'affichés dans les récapitulatifs des albums et des artistes
public enum MediaSupportCategories {
	CD(		 "CD",  	 "xnbcd", 		 1),
	K7(		 "K7",  	 "xnbk7", 		 1),
	Vinyl33T("33T", 	 "xnb33T", 	 	 1),
	Vinyl45T("45T", 	 "xnb45T",     0.5),
	MiniCD(	 "Mini CD",  "xnbminicd",  0.5),
	MiniDVD( "Mini DVD", "xnbminidvd", 0.5),
	Mini33T( "Mini 33T", "xnbmini33T", 0.5),
	Maxi45T( "Maxi 45T", "xnbmaxi45T", 0.5),
	VHS(	 "VHS",		 "xnbvhs", 	 	 1),
	DVD(	 "DVD",		 "xnbdvd", 	 	 1),
	BluRay(  "Blu- ray", "xnbblueray",   1);
	
	private final String nom ;
	private final String cssClass ;
	private final double poidsSupport ;
	
	private MediaSupportCategories(String n, String cssCl, double ps) {
		nom 	 	 	 = n ;
		cssClass 	 	 = cssCl ;
		poidsSupport 	 = ps ;
	}
	
	public String getNom() {
		return nom;
	}
	
	String getCssClass() {
		return cssClass;
	}

	double getPoidsSupport() {
		return poidsSupport;
	}
}