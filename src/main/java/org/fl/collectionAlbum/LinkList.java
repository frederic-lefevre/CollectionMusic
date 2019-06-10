package org.fl.collectionAlbum;

import java.util.ArrayList;

public class LinkList {

	private class Link {
		
		private final static String AHREF1 = "<a href=\"" ;
		private final static String AHREF2 = "\">" ;
		private final static String AHREF3 = "</a><br/>\n" ;
		
		private final String title ;
		private final String url ;
		
		public Link(String title, String url) {
			super();
			this.title = title;
			this.url = url;
		}
		
		
		/**
		 * Print the hyper link
		 * @param rFile
		 * @param offset
		 */
		public void writeLink(StringBuilder rBuilder, String offset) {
			rBuilder.append(AHREF1).append(offset).append(url).append(AHREF2).append(title).append(AHREF3) ;
		}
	}
	
	private ArrayList<Link> linkList ;
	private LinkList subLinks ;
	private String subListLinkOffset ;
	
	public LinkList(LinkList otherLinks, String offSet) {
		subLinks = otherLinks ;
		subListLinkOffset = offSet ;
		linkList = new ArrayList<Link>() ;
	}
	
	public LinkList() {
		linkList = new ArrayList<Link>() ;
	}
	
	public void addLink(String t, String l) {
		linkList.add(new Link(t,l)) ;
	}
	
	public void writeLinkList(StringBuilder rBuilder, String linkOffSet) {
		for (int i=0; i < linkList.size(); i++) {
			((Link)(linkList.get(i))).writeLink(rBuilder, linkOffSet) ;
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
