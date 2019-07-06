package org.fl.collectionAlbum.rapportHtml;

import java.util.ArrayList;
import java.util.List;

public class Balises {

	private List<String> balises ;
	
	protected Balises() {
		balises = new ArrayList<>() ;
	}
	
	public void writeBalises(StringBuilder fragment) {
		fragment.append("<table class=\"balises\">\n") ;
		for (String uneBalise : balises) {
			fragment.append("  <tr><td><a href=\"#").append(uneBalise + "\">").append(uneBalise).append("</a></td></tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
	
	protected void addCheckBalise(StringBuilder fragment, String s) {
		addCheckBaliseWord(fragment, s.substring(0, 1)) ;
	}
	
	private void addCheckBaliseWord(StringBuilder fragment, String uneBalise) {
		if (balises.isEmpty() || (! uneBalise.equals(balises.get(balises.size()-1)))) {
			fragment.append("<a name=\"").append(uneBalise).append("\"></a>") ;
			balises.add(uneBalise) ;
		}
	}
	

}
