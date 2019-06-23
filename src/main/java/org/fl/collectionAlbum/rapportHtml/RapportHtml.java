package org.fl.collectionAlbum.rapportHtml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;

public abstract class RapportHtml {

	// Useful HTML fragment
	private final static String ENTETE1 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN_\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
										  "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n" +
										  "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=" ;
	private final static String ENTETE6 = "\" />\n  <title>" ;
	private final static String ENTETE2 = "</title>\n" ;
	private final static String ENTETE3 = "  <link rel=\"STYLESHEET\" href=\"" ;
	private final static String ENTETE4 = ".css\" type=\"text/css\"/>\n" ;
	private final static String ENTETE5 = "</head>\n<body>\n" ;
	private final static String H2_B 	= "<h2>" ;
	private final static String H2_E 	= "</h2>\n" ;
	private final static String L_LIST1 = "<div class=\"home\">" ;
	private final static String L_LIST2 = "<span  class=\"dategen\">Généré " ;
	private final static String L_LIST3 = "</span><br/>\n" ;
	private final static String L_LIST4 = "</div>\n" ;
	private final static String END		= "</body>\n</html>" ;
	
	// Initialize a default charset
	private static String htmlBegin = ENTETE1 + StandardCharsets.UTF_8.name() + ENTETE6 ;
	
	// Initial size of the StringBuilder buffer
	private final static int TAILLE_INITIALE = 8192 ;
	
	protected final String 	     titreRapport ;
	protected final File 	     rapportFile ;
	private   final HtmlLinkList indexes ;
	private   final PrintWriter  filePrinter ;
	
	protected StringBuilder rBuilder ;
	
	// directory pour les css
	private final static String CSSOFFSET = "../css/" ;
	
	protected String urlOffset;
	
	// Styles correspondant à des fichiers css
	public final static String formatStyle  = "format" ;
	public final static String mainStyle 	= "main" ;
	public final static String albumStyle 	= "album" ;
	
	protected Logger rapportLog;

	private   boolean displayTitle ;
	protected boolean withAlphaBalises ;

	private List<String> alphaBalises ;
	
	protected RapportHtml(String titre, File rFile, HtmlLinkList idxs, String o, Logger rl) {
		
		super();
		rapportLog 	 	 = rl ;
		titreRapport 	 = titre ;
		urlOffset	 	 = o ;
		rapportFile  	 = rFile ;
		rBuilder 	 	 = new StringBuilder(TAILLE_INITIALE) ;
		indexes 	 	 = idxs ;
		displayTitle 	 = false ;
		withAlphaBalises = false ;
		
		PrintWriter pw = null ;
		try {
			if (rapportFile.createNewFile()) {				
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter( new FileOutputStream(rapportFile), Control.getCharset()))) ;
				if (rapportLog.isLoggable(Level.FINE)) {
					rapportLog.fine("Création du RapportHtml " + titreRapport + " Fichier: " + rapportFile.getAbsolutePath());
				}
			} else {
				rapportLog.severe("Le fichier " + rapportFile.getAbsolutePath() + " existe déjà") ;
			}
		} catch (Exception e) {			
		    rapportLog.log(Level.SEVERE,"Erreur dans la création du fichier " + rapportFile.getAbsolutePath(), e) ;	
		}
		filePrinter = pw ;
	}

	protected abstract void corpsRapport() ;
	
	public String printReport(String styleList[]) {
		
		try {
			enteteRapport(styleList) ;
			corpsRapport();		
			finRapport() ;
		} catch (Exception e) {
		    rapportLog.log(Level.SEVERE, "Erreur dans la création du rapport ", e) ;
		}
		return "  <li><a href=\"" + rapportFile.getName() + "\">" + titreRapport + "</a></li>\n" ;
	}
	
	private static String dateFrancePattern = "EEEE dd MMMM uuuu à HH:mm" ;

	private void enteteRapport (String styleList[]) {
			
		rBuilder.append(htmlBegin).append(titreRapport).append(ENTETE2) ;
		if (styleList != null) {
			String cssRef1 = ENTETE3 + urlOffset + CSSOFFSET ;
			for (String style : styleList) {
				rBuilder.append(cssRef1).append(style).append(ENTETE4) ;
			}
		}

		rBuilder.append(ENTETE5) ;
		if ((titreRapport != null) && (titreRapport.length() > 0) && displayTitle) {
			rBuilder.append(H2_B).append(titreRapport).append(H2_E) ;
		}
		if ((indexes != null) && (indexes.getNbLink()> 0)) {
			rBuilder.append(L_LIST1) ;
			indexes.writeLinkList(rBuilder, "") ;

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFrancePattern, Locale.FRANCE) ;
			rBuilder.append(L_LIST2).append(dateTimeFormatter.format(LocalDateTime.now())).append(L_LIST3) ;

			rBuilder.append(L_LIST4) ;
		}		
	}
   
	private void finRapport () {
	
		if (withAlphaBalises) {
			write("<table class=\"balises\">\n") ;
			for (String uneBalise : alphaBalises) {
				write("  <tr><td><a href=\"#").write(uneBalise + "\">").write(uneBalise).write("</a></td></tr>\n") ;
			}
			write("</table>\n") ;
		}
		rBuilder.append(END) ;
		filePrinter.write(rBuilder.toString()) ;
		filePrinter.flush() ;
		filePrinter.close() ;
		if (rapportLog.isLoggable(Level.FINE)) {
			rapportLog.fine("Rapport HTML écrit " + titreRapport) ;
		}
	}
	
	protected void withTitleDisplayed() {
		displayTitle = true ;
	}
	
	public void withAlphaBalises() {
		alphaBalises = new ArrayList<String>() ;
		withAlphaBalises = true ;
	}
	
	protected RapportHtml write(String s) {
		rBuilder.append(s) ;
		return this ;
	}
	
	protected RapportHtml write(int s) {
		rBuilder.append(s) ;
		return this ;
	}
	
	public static void withCharset(String cs) {
		htmlBegin = ENTETE1 + cs + ENTETE6 ;
	}
	
	protected void alphBalise(String uneBalise) {
		if (alphaBalises.isEmpty() || (! uneBalise.equals(alphaBalises.get(alphaBalises.size()-1)))) {
			write("<a name=\"").write(uneBalise).write("\"></a>") ;
			alphaBalises.add(uneBalise) ;
		}
	}
}
