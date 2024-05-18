/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RapportHtml {

	protected final static Logger rapportLog = Logger.getLogger(RapportHtml.class.getName());
	
	// Useful HTML fragment
	private final static String ENTETE1 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN_\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
										  "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n" +
										  "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=";
	private final static String ENTETE6 = "\" />\n  <title>" ;
	private final static String ENTETE2 = "</title>\n" ;
	private final static String ENTETE3 = "  <link rel=\"STYLESHEET\" href=\"" ;
	private final static String ENTETE4 = ".css\" type=\"text/css\"/>\n" ;
	private final static String ENTETE5 = "</head>\n<body>\n" ;
	private final static String H2_B 	= "<h2>" ;
	private final static String H2_E 	= "</h2>\n" ;
	private final static String L_LIST1 = "<div class=\"home\">\n" ;
	private final static String IMG_1   = "  <img  class=\"cover\" src=\"";
	private final static String L_LIST4 = "</div>\n" ;
	private final static String END		= "</body>\n</html>" ;
	
	private final static String LIST_BEGIN = "  <li";	
	private final static String CELL_BEGIN = "<td";
	private final static String CLASS_PART1 = " class=\"";
	private final static String HREF_PART = "><a href=\"";
	private final static String CSS_END_HREF_PART = "\"" + HREF_PART;
	private final static String LIST_END = "</a></li>\n";
	private final static String CELL_END = "</a></td>";
	
	public enum LinkType {
		LIST(LIST_BEGIN, LIST_END), CELL(CELL_BEGIN, CELL_END);
		
		private final String beginTag;
		private final String endTag;
		
		private LinkType(String b, String e) {
			beginTag = b;
			endTag = e;
		}
		
		private String getBeginTag(String cssClass) {
			if ((cssClass == null) || cssClass.isEmpty()) {
				return beginTag + HREF_PART;
			} else {
				return beginTag + CLASS_PART1 + cssClass + CSS_END_HREF_PART;
			}
		}
		
		private String getEndTag() {
			return endTag;
		}
	}
	
	// Initialize a default charset
	private static String htmlBegin = ENTETE1 + StandardCharsets.UTF_8.name() + ENTETE6;
	
	// Initial size of the StringBuilder buffer
	private final static int TAILLE_INITIALE = 8192;
	
	private static Charset charset;
	
	protected String titreRapport;
	private HtmlLinkList indexes;
	private String imageUri;
	
	protected final StringBuilder rBuilder;
	
	// directory pour les css
	private final static String CSSOFFSET = "../css/";
	
	protected String urlOffset;
	private LinkType linkType;
	private boolean displayTitle;
	protected Balises balises;
	
	protected RapportHtml(String titre, LinkType linkType) {
		
		super();
		titreRapport = titre;
		if (linkType == null) {
			this.linkType = LinkType.LIST;
		} else {
			this.linkType = linkType;
		}
		urlOffset = "";
		rBuilder = new StringBuilder(TAILLE_INITIALE);
		indexes = null;
		displayTitle = false;
		balises = null;
		imageUri = null;
	}

	protected abstract void corpsRapport() ;
	
	public String printReport(Path rapportFile, String styleList[], String linkTypeClass) {

		if (! Files.exists(rapportFile)) {	
			enteteRapport(styleList);
			corpsRapport();
			finalizeRapport(rapportFile);
		}
		return linkType.getBeginTag(linkTypeClass) + rapportFile.getFileName() + "\">" + titreRapport + linkType.getEndTag();
	}
	
	public String printReport(Path rapportFile, String styleList[]) {
		return printReport(rapportFile, styleList, null);
	}

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
		
		if ((indexes != null) || (imageUri != null)) {
			
			rBuilder.append(L_LIST1);
			if (indexes != null) {
				indexes.writeLinkList(rBuilder);
			}

			if (imageUri != null) {
				rBuilder.append(IMG_1).append(imageUri).append("\">\n");
			}
			
			rBuilder.append(L_LIST4) ;
		}		
	}
   
	private void finalizeRapport (Path rapportFile) {
	
		if (balises != null) {
			balises.writeBalises(rBuilder);
		}
		rBuilder.append(END) ;

		try (BufferedWriter buff = Files.newBufferedWriter(rapportFile, charset)){
								
			buff.write(rBuilder.toString()) ;
			rapportLog.fine(() -> "Création du RapportHtml " + titreRapport + " Fichier: " + rapportFile);

		} catch (Exception e) {			
		    rapportLog.log(Level.SEVERE,"Erreur dans la création du fichier " + rapportFile, e) ;
		}
	}
	
	protected RapportHtml withTitleDisplayed() {
		displayTitle = true;
		return this;
	}
	
	public RapportHtml withBalises(Balises b) {
		balises =b;
		return this;
	}
	
	protected RapportHtml write(String s) {
		rBuilder.append(s);
		return this;
	}

	protected RapportHtml write(int s) {
		rBuilder.append(s);
		return this;
	}

	protected RapportHtml withTitle(String title) {
		titreRapport = title;
		return this;
	}

	protected RapportHtml withHtmlLinkList(HtmlLinkList hll) {
		indexes = new HtmlLinkList(hll);
		indexes.setOffset(urlOffset);
		return this;
	}

	protected RapportHtml withImageUri(String iUri) {
		imageUri = iUri;
		return this;
	}
	
	public RapportHtml withLinkType(LinkType linkType) {
		this.linkType = linkType;
		return this;
	}
	
	public RapportHtml withOffset(String o) {
		urlOffset = o;
		if (indexes != null) {
			indexes.setOffset(urlOffset);
		}
		return this;
	}

	public static void withCharset(Charset cs) {
		charset = cs;
		htmlBegin = ENTETE1 + cs.name() + ENTETE6;
	}
}
