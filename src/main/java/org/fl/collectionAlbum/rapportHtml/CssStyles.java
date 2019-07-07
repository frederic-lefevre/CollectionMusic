package org.fl.collectionAlbum.rapportHtml;

public final class CssStyles {

	private final static String FORMAT  = "format" 		; // Styles pour les formats CD, 33T ...etc, y compris les totaux	
	private final static String MAIN 	= "main" 		; // Pour le body, couleurs, fonts et les liens généraux (accueils)	
	private final static String TICKET 	= "ticket" 		; // Pour les tickets de concerts
	private final static String TABLE 	= "table" 		; // Pour les rapports avec tables
	private final static String ARTISTE = "artiste" 	; // Pour les entetes de table avec des artistes
	private final static String BALISES = "balise" 		; // Pour les balises (liens internes)
	private final static String STAT 	= "stat" 		; // Pour les stat
	private final static String CAL	 	= "calendrier" 	; // Pour les calendriers
	
	public final static String main[] 						= { MAIN } ;
	public final static String mainFormat[] 				= { MAIN, FORMAT } ;
	public final static String stylesTableauMusicArtefact[] = { MAIN, FORMAT, TABLE} ;
	public final static String stylesTableauDunArtiste[] 	= { MAIN, FORMAT, TABLE, ARTISTE} ;
	public final static String stylesTableauAvecBalise[]	= { MAIN, FORMAT, TABLE, BALISES} ;
	public final static String stylesStat[] 				= { MAIN, STAT } ;
	public final static String ticket[] 					= { MAIN, TICKET } ;
	public final static String stylesCalendrier[] 			= { MAIN, TABLE, CAL} ;
	
}
