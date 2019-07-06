package org.fl.collectionAlbum.rapportHtml;

import java.util.ArrayList;
import java.util.List;

import org.fl.collectionAlbum.Format;

public class Balises {

	public enum BalisesType { POIDS, ALPHA } ;
	
	private List<String> balises ;
	
	private BalisesType balisesType ;
	
	private static final int MINIMUN_NB_BEFORE_CHANGE = 20 ;
	
	private int nbSinceLastChange ;
	
	protected Balises(BalisesType bt) {
		balises 		  = new ArrayList<>() ;
		balisesType 	  = bt ;
		nbSinceLastChange = 0 ;
	}
	
	public void writeBalises(StringBuilder fragment) {
		fragment.append("<table class=\"balises\">\n") ;
		for (String uneBalise : balises) {
			fragment.append("  <tr><td><a href=\"#").append(uneBalise + "\">").append(uneBalise).append("</a></td></tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
	
	protected void addCheckBaliseString(StringBuilder fragment, String s) {
		addCheckBalise(fragment, s.substring(0, 1)) ;
	}
	
	protected void addCheckBalisePoids(StringBuilder fragment, Format f) {
		if (nbSinceLastChange > MINIMUN_NB_BEFORE_CHANGE) {
			addCheckBalise(fragment, f.displayPoidsTotal()) ;
		} else {
			nbSinceLastChange++ ;
		}
	}
	
	private void addCheckBalise(StringBuilder fragment, String uneBalise) {
		if (balises.isEmpty() || (! uneBalise.equals(balises.get(balises.size()-1)))) {
			fragment.append("<a name=\"").append(uneBalise).append("\"></a>") ;
			balises.add(uneBalise) ;
			nbSinceLastChange = 0 ;
		}
	}

	public BalisesType getBalisesType() {
		return balisesType;
	}	
}
