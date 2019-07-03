package org.fl.collectionAlbum.rapportHtml;

final class CssStyles {

	// -----
	// Dans RapportHtml
	// Styles correspondant à des fichiers css
	public final static String formatStyle  = "format" ;
	
	// Pour le body, couleurs, fonts et les liens généraux (accueils)
	public final static String mainStyle 	= "main" ;
	
	// Pour les tickets de concerts
	public final static String albumStyle 	= "album" ;
	// -----

	// Dans RapportStructure...
	private static final String styles[] = {RapportHtml.albumStyle} ;
	private final static String stylesArtiste[] = {"main","format","rapport", "artiste"} ;
	
	// Dans RapportDesConcerts
	 String stylesArtistes[] = {"main","format","rapport","chrono"} ;
	 String stylesConcert[] = {"main","format","rapport"} ;

	 // Dans RapportCollection
	 String styles2[] = { RapportHtml.mainStyle, RapportHtml.formatStyle, "rapport", "chrono" } ;
	 String stylesStat[] = {"main","stat"} ;
	 String stylesCalendrier[] = {"main","rapport", "calendrier"} ;
	 String stylesAlbum[] = {"main","format","rapport"} ;
	 
	 // Dans GenerationSiteCollection
	 String styles3[] = { RapportHtml.mainStyle, RapportHtml.formatStyle } ;
	 String styles4[] = { RapportHtml.mainStyle, RapportHtml.formatStyle };

}
