/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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
