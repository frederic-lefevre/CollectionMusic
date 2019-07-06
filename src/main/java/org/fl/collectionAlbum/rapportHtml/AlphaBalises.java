package org.fl.collectionAlbum.rapportHtml;

public class AlphaBalises extends Balises<String> {

	public AlphaBalises() {
		super() ;
	}

	@Override
	protected String extractBalise(String s) {
		return s.substring(0, 1) ;
	}

}
