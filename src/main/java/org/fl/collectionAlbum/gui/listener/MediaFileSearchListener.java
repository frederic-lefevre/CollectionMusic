/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.collectionAlbum.gui.listener;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.MediaFilesSearchPane;

public class MediaFileSearchListener implements java.awt.event.ActionListener {

	
	private final ContentNature contentNature;

	public MediaFileSearchListener(ContentNature contentNature) {
		
		this.contentNature = contentNature;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		MediaFilesSearchPane mediaFilesSearchPane = new MediaFilesSearchPane(contentNature);
		
		JOptionPane.showMessageDialog(null, mediaFilesSearchPane, "Recherche de fichiers " + contentNature.getNom(), JOptionPane.PLAIN_MESSAGE);
	}

}
