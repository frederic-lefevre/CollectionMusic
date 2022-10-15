package org.fl.collectionAlbum.rapportHtml;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private static Charset charset ;
	
	protected 		String 	     titreRapport ;
	private  		HtmlLinkList indexes ;
	
	protected StringBuilder rBuilder ;
	
	// directory pour les css
	private final static String CSSOFFSET = "../css/" ;
	
	protected String urlOffset;
	
	protected final Logger rapportLog;

	private boolean displayTitle ;

	protected Balises balises ;
	
	protected RapportHtml(String titre, Logger rl) {
		
		super();
		rapportLog 	 	 = rl ;
		titreRapport 	 = titre ;
		urlOffset	 	 = "" ;
		rBuilder 	 	 = new StringBuilder(TAILLE_INITIALE) ;
		indexes 	 	 = null ;
		displayTitle 	 = false ;
		balises		 	 = null ;
	}

	protected abstract void corpsRapport() ;
	
	public String printReport(Path rapportFile, String styleList[]) {

		if (! Files.exists(rapportFile)) {	
			enteteRapport(styleList);
			corpsRapport();
			finalizeRapport(rapportFile);
		}
		return "  <li><a href=\"" + rapportFile.getFileName() + "\">" + titreRapport + "</a></li>\n";
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
			indexes.writeLinkList(rBuilder) ;

			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFrancePattern, Locale.FRANCE) ;
			rBuilder.append(L_LIST2).append(dateTimeFormatter.format(LocalDateTime.now())).append(L_LIST3) ;

			rBuilder.append(L_LIST4) ;
		}		
	}
   
	private void finalizeRapport (Path rapportFile) {
	
		if (balises != null) {
			balises.writeBalises(rBuilder);
		}
		rBuilder.append(END) ;

		try (BufferedWriter buff = Files.newBufferedWriter(rapportFile, RapportStructuresAndNames.getCharset())){
								
			buff.write(rBuilder.toString()) ;
			rapportLog.fine(() -> "Création du RapportHtml " + titreRapport + " Fichier: " + rapportFile);

		} catch (Exception e) {			
		    rapportLog.log(Level.SEVERE,"Erreur dans la création du fichier " + rapportFile, e) ;
		}
	}
	
	protected void withTitleDisplayed() {
		displayTitle = true ;
	}
	
	public void withBalises(Balises b) {
		balises =b ;
	}
	
	protected RapportHtml write(String s) {
		rBuilder.append(s) ;
		return this ;
	}
	
	protected RapportHtml write(int s) {
		rBuilder.append(s) ;
		return this ;
	}
	
	protected void withTitle(String title) {
		titreRapport = title ;
	}
	
	protected void withHtmlLinkList(HtmlLinkList hll) {
		indexes = new HtmlLinkList(hll) ;
		indexes.setOffset(urlOffset) ;
	}
	
	protected void withOffset(String o) {
		urlOffset = o ;
		if (indexes != null) {
			indexes.setOffset(urlOffset) ;
		}
	}
	
	public static void withCharset(String cs, Logger rLog) {
		if (Charset.isSupported(cs)) {
			charset = Charset.forName(cs) ;
		} else {
			charset = Charset.defaultCharset() ;
			rLog.severe("Unsupported charset: " + cs + ". Default JVM charset assumed: " + charset) ;				
		}
		htmlBegin = ENTETE1 + cs + ENTETE6 ;
	}
}
