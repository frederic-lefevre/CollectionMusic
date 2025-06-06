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

import java.util.ArrayList;
import java.util.List;

public class HtmlLinkList {

	private class HtmlLink {
		
		private static final String AHREF1 = "  <a href=\"";
		private static final String AHREF2 = "\">";
		private static final String AHREF3 = "</a><br/>\n";
		
		private final String title;
		private final String url;

		public HtmlLink(String title, String url) {
			this.title = title;
			this.url = url;
		}

		// Print the hyper link
		public void writeLink(StringBuilder rBuilder, String offset) {
			rBuilder.append(AHREF1).append(offset).append(url).append(AHREF2).append(title).append(AHREF3);
		}
	}

	private final List<HtmlLink> linkList;
	private String linkOffset;

	public HtmlLinkList(HtmlLinkList otherLinks) {
		linkOffset = "";
		linkList = new ArrayList<>(otherLinks.linkList);
	}

	public HtmlLinkList() {
		linkOffset = "";
		linkList = new ArrayList<>();
	}

	public void addLink(String t, String l) {
		linkList.add(new HtmlLink(t, l));
	}

	public void setOffset(String offSet) {
		linkOffset = offSet;
	}
	
	public void writeLinkList(StringBuilder rBuilder) {
		for (HtmlLink htmlLink : linkList) {
			htmlLink.writeLink(rBuilder, linkOffset);
		}

	}
	
	public int getNbLink() {
		return linkList.size();
	}
}
