package org.fl.collectionAlbum.artistes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.PoidsComparator;
import org.fl.collectionAlbum.concerts.ConcertPoidsComparator;
import org.fl.collectionAlbum.rapportHtml.HtmlReportPrintable;
import org.fl.collectionAlbum.rapportHtml.RapportHtml;

import com.google.gson.JsonObject;

public class ListeArtiste implements HtmlReportPrintable {
	
	public static int rapportAlpha 		  = 0 ;
	public static int rapportPoids 		  = 1 ;
	public static int rapportChrono 	  = 2 ;
	public static int rapportSimpleAlpha  = 3 ;
	public static int rapportConcertAlpha = 4 ;
	public static int rapportConcertPoids = 5 ;
	
	private Logger listeArtisteLog;
	
	private static String styles[] = {"main","format","rapport","chrono"} ;
	
	private List<Artiste> artistes;
	private List<Character>    balises  ;
		

	public ListeArtiste(Logger laLog) {
		super();
		listeArtisteLog = laLog;
		artistes  		= new ArrayList<Artiste>() ;
		balises 		= new ArrayList<Character>() ;
	}

	public String[] getCssStyles() {
		return styles ;
	}
	
	public void addArtiste(Artiste a) {
		
		if (! artistes.contains(a)) {
			artistes.add(a) ;
		}
		if (listeArtisteLog.isLoggable(Level.FINEST)) {
			listeArtisteLog.finest("  Nom: " + a.getNom() + "  Prenoms: " + a.getPrenoms()) ;
		}
	}
	
	public void addAllArtistes(List<Artiste> artistes) {
		artistes.stream().forEach(a -> addArtiste(a));
	}
	
	public Artiste getArtisteKnown(String nom, String prenom) {
		if (nom    == null) nom    = "" ;
		if (prenom == null) prenom = "" ;
		for (Artiste a : artistes) {
			if (nom.equals(a.getNom()) && (prenom.equals(a.getPrenoms()))) {
				return a ;
			}
		}
		return null ;
	}
	
	public Optional<Artiste> getArtisteKnown(JsonObject jArtiste) {
		return artistes.stream().filter(a -> a.isSameArtiste(jArtiste)).findFirst() ;
	}
	
	public void rapport(RapportHtml rapport, int typeRapport, String urlOffset) {
		
		try {
		  if (typeRapport == rapportAlpha || typeRapport == rapportSimpleAlpha || typeRapport == rapportConcertAlpha) {
		  	AuteurComparator compAuteur = new AuteurComparator();
			Collections.sort(artistes, compAuteur) ;
		  } else if (typeRapport == rapportPoids) {
			PoidsComparator compPoids = new PoidsComparator();
			Collections.sort(artistes, compPoids) ;
		  } else if (typeRapport == rapportConcertPoids) {
			ConcertPoidsComparator compConcertPoids = new ConcertPoidsComparator();
			Collections.sort(artistes, compConcertPoids) ;
		  } else if (typeRapport == rapportChrono) {
			AuteurDateComparator compChrono = new AuteurDateComparator(listeArtisteLog);
			Collections.sort(artistes, compChrono) ;
		  } else { 
			  rapport.write("<h2>Type de rapport inconnu</h2>") ;
			listeArtisteLog.severe("Type de rapport inconnu") ;
		  }
		} catch (Exception e) {
			listeArtisteLog.log(Level.SEVERE, "Erreur dans la cr�ation du fichier rapport", e) ;
		}
		rapportTable(rapport, artistes, typeRapport) ;
	}
	
	// HTML fragments
	private final static String F1 = "<div class=\"mhc\">\n  <table>\n  <tr>\n    <td class=\"auteur\">Auteurs</td>\n" +
									"    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n" ;
	private final static String F2 = "    <td class=\"total\">Nombre<br/>concerts</td>\n" ;
	private final static String F3 = "  </tr>\n  </table>\n</div>\n<table>\n  <tr class=\"head\">\n    <td class=\"auteur\">Auteurs</td>\n" +
									 "    <td class=\"an\">Naissance</td>\n    <td class=\"an\">Mort</td>\n" ;
	
	private void rapportTable(RapportHtml rapport, List<Artiste> Auteurs, int typeRapport) {
   	
		Format entete = new Format(null, listeArtisteLog) ;
		try {
			
			if (typeRapport != rapportSimpleAlpha) {			    
				rapport.write(F1) ;
				if (isConcert(typeRapport)) {
					rapport.write(F2) ;
				} else {
					entete.enteteFormat(rapport, "total", 1) ;
				}
				rapport.write(F3) ;
				if (isConcert(typeRapport)) {
					rapport.write("    <td class=\"total\">Nombre<br/>concerts</td>\n") ;
				} else {
					entete.enteteFormat(rapport, "total", 1) ;
				}
				rapport.write("  </tr>\n") ;
			} else {
				rapport.write("<table>\n") ;
			}

		   Character idx = new Character((char)0) ;
		   
		   for (Artiste unArtiste : Auteurs) {
			 unArtiste.generateHtml() ;
			 rapport.write("  <tr>\n    <td class=\"auteur\">") ;
			 if (sortAlpha(typeRapport)) {
			   idx = balise(rapport, idx, unArtiste.getNom()) ;
			 }
			 rapport.write("<a href=\"") ;
			 if (isConcert(typeRapport)) {
				 rapport.write(unArtiste.getConcertUrlHtml()) ;
			 } else if (unArtiste.getNbAlbum() > 0){
				 rapport.write(unArtiste.getUrlHtml()) ;
			 } else {
				 rapport.write(unArtiste.getConcertUrlHtml()) ;
			 }
			 rapport.write("\">").write(unArtiste.getPrenoms()).write(" ").write(unArtiste.getNom()).write("</a></td>\n") ;
			 rapport.write("    <td class=\"an\">").write(unArtiste.getDateNaissance()).write("</td>\n") ;
			 rapport.write("    <td class=\"an\">").write(unArtiste.getDateMort()).write("</td>\n") ;
			if (typeRapport != rapportSimpleAlpha) {
				if (isConcert(typeRapport)) {
					rapport.write("    <td class=\"total\">").write(unArtiste.getNbConcert()).write("</td>\n") ;
				} else {
					unArtiste.getAlbumsFormat().rowFormat(rapport, "total") ;
				}
			}
			rapport.write("  </tr>\n") ;
		   }
		   rapport.write("</table>\n") ;
		   if (sortAlpha(typeRapport)) {
			   rapport.write("<table class=\"balises\">\n") ;
		     for (Character uneBalise : balises) {
		    	 rapport.write("  <tr><td><a href=\"#").write(uneBalise + "\">").write(uneBalise).write("</a></td></tr>\n") ;
		     }
		     rapport.write("</table>\n") ;
		   }
		 } catch (Exception e) {
			 listeArtisteLog.log(Level.SEVERE, "Erreur dans la création du fichier rapport: ",  e) ;
		 }
	
	   }
	
	private Character balise(RapportHtml rapport, Character bl, String nom) {
		if (nom.charAt(0) != bl.charValue()) {
			bl = new Character(nom.charAt(0)) ;
			rapport.write("<a name=\"").write(bl).write("\"></a>") ;
			balises.add(bl) ;
		}
		return bl ;
	}
	
	private boolean isConcert(int typeRapport) {
		return ((typeRapport == rapportConcertPoids) || (typeRapport == rapportConcertAlpha)) ;
	}
	
	private boolean sortAlpha(int typeRapport) {
		return ((typeRapport == rapportAlpha) || (typeRapport == rapportConcertAlpha)) ;
	}
	
	public List<Artiste> getArtistes() {
		return artistes;
	}

	public int getNombreArtistes() {
		return (artistes.size()) ;
	}
	
	public ListeArtiste getReunion(ListeArtiste la) {
		
		ListeArtiste artistesRes = cloneListe() ;
		for (Artiste a : la.artistes) {
			artistesRes.addArtiste(a) ;
		}
		return artistesRes ;
	}
	
	private ListeArtiste cloneListe() {
		
		ListeArtiste artistesRes = new ListeArtiste(listeArtisteLog) ;
		for (Artiste a : artistes) {
			artistesRes.addArtiste(a) ;
		}
		return artistesRes ;
	}
	
}
