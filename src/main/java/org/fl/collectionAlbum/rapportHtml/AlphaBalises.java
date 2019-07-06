package org.fl.collectionAlbum.rapportHtml;

public class AlphaBalises extends Balises<String> {

	public AlphaBalises() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String extractBalise(String s) {
		return s.substring(0, 1) ;
	}

}
