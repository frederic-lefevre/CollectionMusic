/*
 MIT License

Copyright (c) 2017, 2022 Frederic Lefevre

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

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.fl.collectionAlbum.Control;
import org.fl.util.swing.ApplicationTabbedPane;

public class CollectionAlbumGui  extends JFrame {
	
	public static final int WINDOW_WIDTH  = 1880;
	public static final int WINDOW_HEIGHT = 1000;
	
	private static final long serialVersionUID = 8726429353709418534L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CollectionAlbumGui window = new CollectionAlbumGui();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
		
	public CollectionAlbumGui() {
   		
   		// init main window
   		setBounds(20, 20, WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Collection d'albums") ;
		
		ApplicationTabbedPane collectionTabs = new ApplicationTabbedPane(Control.getMusicRunningContext()) ;
		
		// init panel de lecture et génération de site
		GenerationPane gPane   = new GenerationPane() ;
		
		collectionTabs.add(gPane, "Génération", 0) ;
		
		collectionTabs.setSelectedIndex(0) ;
		getContentPane().add(collectionTabs) ;
		
	}

}