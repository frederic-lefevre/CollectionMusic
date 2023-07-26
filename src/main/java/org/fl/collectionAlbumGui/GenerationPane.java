/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GenerationPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private final static String rText  = "Lecture des fichiers albums et concerts" ;
	private final static String gText  = "Génération du nouveau site collection" ;
	private final static String iText  = "Arrêt";
	private final static String sText  = "Aucune collection lue" ;
	private final static String s1Text = "Aucun site généré" ;
	
	public GenerationPane() {
			
		super() ;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		StartControl startButton = new StartControl(rText, iText, sText) ;
		add(startButton.getProcCtrl()) ;
				
		StartControl genButton = new StartControl(gText, iText, s1Text) ;
		genButton.deactivate() ;
		add(genButton.getProcCtrl()) ;
		
		StartControl[] stCtrl = new StartControl[]{startButton, genButton} ;
		
		StartReadCollection sm = new StartReadCollection(startButton.getPip(), startButton, stCtrl) ;
		sm.setCollectionProcWaiter(new CollectionProcessWaiter(stCtrl)) ;
		startButton.getStartButton().addActionListener(sm) ;
		
		StartGenerationSite sg = new StartGenerationSite(genButton.getPip(), genButton, stCtrl) ;
		sg.setCollectionProcWaiter(new CollectionProcessWaiter(stCtrl)) ;
		genButton.getStartButton().addActionListener(sg) ;
	} 

}
