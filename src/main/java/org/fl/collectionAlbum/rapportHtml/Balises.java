package org.fl.collectionAlbum.rapportHtml;

import java.util.ArrayList;
import java.util.List;

public abstract class Balises<T> {

	private List<String> balises ;
	
	protected Balises() {
		balises = new ArrayList<>() ;
	}
	
	protected void writeBalises(StringBuilder fragment) {
		fragment.append("<table class=\"balises\">\n") ;
		for (String uneBalise : balises) {
			fragment.append("  <tr><td><a href=\"#").append(uneBalise + "\">").append(uneBalise).append("</a></td></tr>\n") ;
		}
		fragment.append("</table>\n") ;
	}
	
	protected void addCheckBalise(StringBuilder fragment, T t) {
		String uneBalise = extractBalise(t) ;
		if (balises.isEmpty() || (! uneBalise.equals(balises.get(balises.size()-1)))) {
			fragment.append("<a name=\"").append(uneBalise).append("\"></a>") ;
			balises.add(uneBalise) ;
		}
	}
	
	protected abstract String extractBalise(T t) ;
}
