/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

	private static final String FORMAT  = "format" 		; // Styles pour les formats CD, 33T ...etc, y compris les totaux	
	private static final String MAIN 	= "main" 		; // Pour le body, couleurs, fonts et les liens généraux (accueils)	
	private static final String TICKET 	= "ticket" 		; // Pour les tickets de concerts
	private static final String TABLE 	= "table" 		; // Pour les rapports avec tables
	private static final String ARTISTE = "artiste" 	; // Pour les entetes de table avec des artistes
	private static final String BALISES = "balise" 		; // Pour les balises (liens internes)
	private static final String STAT 	= "stat" 		; // Pour les stat
	private static final String CAL	 	= "calendrier" 	; // Pour les calendriers
	
	public static final String main[] 						= { MAIN } ;
	public static final String mainFormat[] 				= { MAIN, FORMAT } ;
	public static final String stylesTableauMusicArtefact[] = { MAIN, FORMAT, TABLE} ;
	public static final String stylesTableauDunArtiste[] 	= { MAIN, FORMAT, TABLE, ARTISTE} ;
	public static final String stylesTableauAvecBalise[]	= { MAIN, FORMAT, TABLE, BALISES} ;
	public static final String stylesStat[] 				= { MAIN, STAT } ;
	public static final String ticket[] 					= { MAIN, TICKET } ;
	public static final String stylesCalendrier[] 			= { MAIN, TABLE, CAL} ;
}
