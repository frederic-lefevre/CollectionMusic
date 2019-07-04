package org.fl.collectionAlbum.rapportHtml;

import java.util.ArrayList;
import java.util.List;

public class HtmlLinkList {

	private class HtmlLink {
		
		private final static String AHREF1 = "<a href=\"" ;
		private final static String AHREF2 = "\">" ;
		private final static String AHREF3 = "</a><br/>\n" ;
		
		private final String title ;
		private final String url ;
		
		public HtmlLink(String title, String url) {
			this.title = title ;
			this.url   = url ;
		}
			
		// Print the hyper link
		public void writeLink(StringBuilder rBuilder, String offset) {
			rBuilder.append(AHREF1).append(offset).append(url).append(AHREF2).append(title).append(AHREF3) ;
		}
	}
	
	private List<HtmlLink> 	   linkList ;
	private String 	   		   linkOffset ;
	
	public HtmlLinkList(HtmlLinkList otherLinks) {
		linkOffset = "" ;	
		linkList   = new ArrayList<HtmlLink>(otherLinks.linkList) ;
	}
	
	public HtmlLinkList() {
		linkOffset = "" ;
		linkList 		  = new ArrayList<HtmlLink>() ;
	}
	
	public void addLink(String t, String l) {
		linkList.add(new HtmlLink(t,l)) ;
	}
	
	public void setOffset(String offSet) {
		linkOffset = offSet ;
	}
	
	public void writeLinkList(StringBuilder rBuilder) {
		for (HtmlLink htmlLink : linkList) {
			htmlLink.writeLink(rBuilder, linkOffset) ;
		}
	}
	
	public int getNbLink() {
		return linkList.size() ;
	}
}
