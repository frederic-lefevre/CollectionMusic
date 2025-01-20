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

package org.fl.collectionAlbumGui;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.fl.collectionAlbum.OsAction;
import org.fl.collectionAlbum.ListOfStringCommandParameter;

public class UtilsPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public UtilsPane() {
		
		super();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createMatteBorder(10,0,10,0,Color.BLACK));
		
		JButton showCollectionButton = new JButton("Montrer la collection");
		Font font = new Font("Verdana", Font.BOLD, 14);
		showCollectionButton.setFont(font);
		showCollectionButton.setBackground(Color.GREEN);
		
		add(showCollectionButton);
		
		OsAction<List<String>> showCollectionAction = new OsAction<List<String>>(
				"Show collection", 
				"C:\\FredericPersonnel\\Program\\PortableApps\\FirefoxPortable\\App\\firefox64\\firefox.exe", 
				List.of("-profile", "C:\\FredericPersonnel\\Program\\PortableApps\\FirefoxPortable\\Data\\profile", "-url"),
				new ListOfStringCommandParameter());
		
		OsActionListener<List<String>> showCollectionListener = new OsActionListener<>(List.of("file:///C:/FredericPersonnel/Loisirs/musique/RapportCollection/rapport/index.html"), showCollectionAction);
		
		showCollectionButton.addActionListener(showCollectionListener);
		
	}


}
