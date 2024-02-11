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
	CD(		  "CD",  	 	"xnbcd", 		"CD",		 						1),
	K7(		  "K7",  	 	"xnbk7", 		"Cassette audio",		 			1),
	VinylLP(  "Vinyl LP", 	"xnbVinyl", 	"Vinyle de plus de 25 minutes", 	1),
	MiniVinyl("Mini Vinyl", "xnbMiniVinyl", "Vinyle de moins de 25 minutes",  0.5),
	MiniCD(	  "Mini CD",  	"xnbminicd",    "CD court, monins de 25 minutes", 0.5),
	MiniDVD(  "Mini DVD", 	"xnbminidvd",   "DVD court", 					  0.5),
	VHS(	  "VHS",		"xnbvhs", 	  	"Casssette video HS", 	 			1),
	DVD(	  "DVD",		"xnbdvd", 	  	"DVD",	 	 						1),
	BluRay(   "Blu- ray", 	"xnbblueray", 	"Blu-ray",  						1);
	
	private final String nom;
	private final String cssClass;
	private final String description;
	private final double poidsSupport;
	
	private MediaSupportCategories(String n, String cssCl, String desc, double ps) {
		nom = n;
		cssClass = cssCl;
		description = desc;
		poidsSupport = ps;
	}
	
	public String getNom() {
		return nom;
	}
	
	String getCssClass() {
		return cssClass;
	}

	public String getDescription() {
		return description;
	}

	double getPoidsSupport() {
		return poidsSupport;
	}
}