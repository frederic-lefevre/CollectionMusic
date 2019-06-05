package org.fl.collectionAlbum;

public interface HtmlReportPrintable {

	public void rapport(RapportHtml rapport, int typeRapport, String urlOffset) ;
	
	public String[] getCssStyles() ;
}
