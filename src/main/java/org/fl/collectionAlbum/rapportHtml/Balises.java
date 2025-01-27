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

package org.fl.collectionAlbum.rapportHtml;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class Balises {

	public enum BalisesType { POIDS, ALPHA, ALPHA_ARTIST, TEMPORAL, TEMPORAL_COMPOSITION } ;
	
	private List<String> balises ;
	
	private final BalisesType balisesType ;
	
	private static final int MINIMUN_NB_BEFORE_ADD = 20 ;
	
	private int nbSinceLastAdd ;
	
	private String lastBaliseWritten ;
	
	protected Balises(BalisesType bt) {
		balises 		  = new ArrayList<>() ;
		balisesType 	  = bt ;
		nbSinceLastAdd 	  = MINIMUN_NB_BEFORE_ADD + 1 ;
		lastBaliseWritten = null ;
	}
	
	public void writeBalises(StringBuilder fragment) {
		fragment.append("<table class=\"balises\">\n  <tbody class=\"balises\">\n") ;
		for (String uneBalise : balises) {
			fragment.append("  <tr><td class=\"balises\"><a href=\"#").append(uneBalise + "\">").append(uneBalise).append("</a></td></tr>\n") ;
		}
		fragment.append("  </tbody>\n</table>\n") ;
	}
	
	protected void addCheckBaliseString(StringBuilder fragment, String s) {
		addCheckBalise(fragment, s.substring(0, 1)) ;
	}
	
	protected void addCheckBalisePoids(StringBuilder fragment, Format f) {	
			addCheckBalise(fragment, f.displayPoidsTotal()) ;		
	}
	
	public void addCheckBaliseTemporal(StringBuilder fragment, TemporalAccessor tempsAccessor) {
		addCheckBalise(fragment, TemporalUtils.formatYear(tempsAccessor)) ;	
	}	
	
	private void addCheckBalise(StringBuilder fragment, String uneBalise) {
		
		if ( (balises.isEmpty()) || (! uneBalise.equals(lastBaliseWritten))) {				
			fragment.append("<a name=\"").append(uneBalise).append("\"></a>") ;
			lastBaliseWritten = uneBalise ;
		}
		nbSinceLastAdd++ ;
		if ((nbSinceLastAdd > MINIMUN_NB_BEFORE_ADD) && 
			( (balises.isEmpty()) ||	
			  (! uneBalise.equals(balises.get(balises.size() - 1))))) {
			balises.add(uneBalise) ;
			nbSinceLastAdd = 0 ;
		} 		
	}

	public BalisesType getBalisesType() {
		return balisesType;
	}

}
