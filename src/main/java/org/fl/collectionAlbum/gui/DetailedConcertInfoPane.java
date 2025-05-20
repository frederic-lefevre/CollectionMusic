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

package org.fl.collectionAlbum.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.util.file.FilesUtils;

public class DetailedConcertInfoPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private static final int PREFERRED_WIDTH = 1700;
	private static final int PREFERRED_HEIGHT = 900;
	
	private static final int MAX_TICKET_WIDTH = 1400;
	private static final int MAX_TICKET_HEIGHT = 800;
	
	private static final Font monospaced = new Font("monospaced", Font.BOLD, 14);
	
	public DetailedConcertInfoPane(Concert concert) {
		super();
		
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		JPanel infosPane = new JPanel();
		infosPane.setLayout(new BoxLayout(infosPane, BoxLayout.Y_AXIS));
		
		JEditorPane concertTextInfo =  new JEditorPane();
		concertTextInfo.setContentType("text/html");
		concertTextInfo.setText(CollectionUtils.getSimpleHtml(concert));
		concertTextInfo.setEditable(false);
		concertTextInfo.setFont(monospaced);
		infosPane.add(concertTextInfo);
		
		String ticketImagesRoot = Control.getConcertTicketImgUri();
		concert.getTicketImages().stream()
			.map(relativeStringPath -> {
				try {
					return FilesUtils.uriStringToAbsolutePath(ticketImagesRoot + relativeStringPath);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			})
			.filter(Objects::nonNull)
			.forEach(ticketImagePath -> infosPane.add(getTicketImage(ticketImagePath)));
			
		setViewportView(infosPane);
	}
	
	private JLabel getTicketImage(Path ticketImagePath) {
		return CollectionUtils.getAdjustedImageLabel(ticketImagePath, MAX_TICKET_WIDTH, MAX_TICKET_HEIGHT);
	}
}
