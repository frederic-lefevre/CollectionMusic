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
	private final HtmlLinkList subLinks ;
	private String 	   		   subListLinkOffset ;
	
	public HtmlLinkList(HtmlLinkList otherLinks) {
		subLinks 		  = otherLinks ;
		subListLinkOffset = "" ;
		linkList 		  = new ArrayList<HtmlLink>() ;
	}
	
	public HtmlLinkList() {
		subLinks 		  = null ;
		subListLinkOffset = "" ;
		linkList 		  = new ArrayList<HtmlLink>() ;
	}
	
	public void addLink(String t, String l) {
		linkList.add(new HtmlLink(t,l)) ;
	}
	
	public void setOffset(String offSet) {
		subListLinkOffset = offSet ;
	}
	
	public void writeLinkList(StringBuilder rBuilder, String linkOffSet) {
		for (HtmlLink htmlLink : linkList) {
			htmlLink.writeLink(rBuilder, linkOffSet) ;
		}
		if (subLinks != null) {
			subLinks.writeLinkList(rBuilder, linkOffSet + subListLinkOffset) ;
		}
	}
	
	public int getNbLink() {
		if (subLinks != null) {
			return subLinks.getNbLink() + linkList.size() ;
		} else {
			return linkList.size() ;
		}
	}
}
