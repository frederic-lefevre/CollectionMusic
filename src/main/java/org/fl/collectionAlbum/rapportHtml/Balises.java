package org.fl.collectionAlbum.rapportHtml;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class Balises {

	public enum BalisesType { POIDS, ALPHA, TEMPORAL, TEMPORAL_COMPOSITION } ;
	
	private List<String> balises ;
	
	private BalisesType balisesType ;
	
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
