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
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class GenerationPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private final static String rText = "Lecture des fichiers albums et concerts";
	private final static String gText = "Génération du nouveau site collection";
	private final static String iText = "Arrêt";
	private final static String sText = "Aucune collection lue";
	private final static String s1Text = "Aucun site généré";

	private final StartControl readCollectionControl;
	private final StartControl generateSiteControl;
	
	public GenerationPane() {

		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		AlbumsTableModel albumsTableModel = new AlbumsTableModel(CollectionAlbumContainer.getInstance().getCollectionAlbumsMusiques().getAlbums());
		
		AlbumsJTable albumsJTable = new AlbumsJTable(albumsTableModel, this);
		
		// Control buttons panel
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		
		readCollectionControl = new StartControl(rText, iText, sText, () -> true);
		controlPanel.add(readCollectionControl.getProcCtrl());

		UtilsPane utilsPane = new UtilsPane(this);
		utilsPane.deactivate();
		controlPanel.add(utilsPane);
		
		generateSiteControl = new StartControl(gText, iText, s1Text, StartGenerationSite.activationPredicate);
		generateSiteControl.deactivate();
		controlPanel.add(generateSiteControl.getProcCtrl());

		List<ActivableElement> activableElements = List.of(readCollectionControl, utilsPane, generateSiteControl);

		StartReadCollection startReadCollection = new StartReadCollection(readCollectionControl.getPip(), activableElements);
		startReadCollection.setCollectionProcWaiter(new CollectionProcessWaiter(activableElements));
		startReadCollection.addTableModel(albumsTableModel);
		readCollectionControl.getStartButton().addActionListener(startReadCollection);

		StartGenerationSite sg = new StartGenerationSite(generateSiteControl.getPip(), activableElements);
		sg.setCollectionProcWaiter(new CollectionProcessWaiter(activableElements));
		generateSiteControl.getStartButton().addActionListener(sg);
		
		add(controlPanel);
		
		// Tab pane for generation of collection
		JTabbedPane collectionTabPanes = new JTabbedPane();
		
		// collection tab
		// Scroll pane to contain the collection table
		JScrollPane albumsScrollTable = new JScrollPane(albumsJTable);
		albumsScrollTable.setPreferredSize(new Dimension(1800,700));
		
		collectionTabPanes.add(albumsScrollTable, "Collection d'albums", 0);
		
		// Media files tabs
		Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {
			
			MediaFilesTableModel tm = new MediaFilesTableModel(MediaFilesInventories.getMediaFileInventory(contentNature));
			startReadCollection.addTableModel(tm);
			
			MediaFilesJTable mediaFilesJTable = new MediaFilesJTable(tm);
			
			// Scroll pane to contain the media path table
			JScrollPane mediaFilesScrollTable = new JScrollPane(mediaFilesJTable);
			mediaFilesScrollTable.setPreferredSize(new Dimension(1800,700));
			
			collectionTabPanes.add(mediaFilesScrollTable, "Chemins des fichiers " + contentNature.getNom());
		});
		
		// Discogs releases pane
		DisocgsReleaseTableModel dtm = new DisocgsReleaseTableModel(DiscogsInventory.getDiscogsInventory());
		startReadCollection.addTableModel(dtm);
		
		DiscogsReleaseJTable discogsReleaseJTable = new DiscogsReleaseJTable(dtm, CollectionAlbumContainer.getInstance(), this);
		
		// Scroll pane to contain the discogs releases pane
		JScrollPane discogsReleasesScrollPane = new JScrollPane(discogsReleaseJTable);
		discogsReleasesScrollPane.setPreferredSize(new Dimension(1800,700));

		collectionTabPanes.add(discogsReleasesScrollPane, "Discogs releases");
		
		// Concert pane
		ConcertTableModel concertTableModel = new ConcertTableModel(CollectionAlbumContainer.getInstance().getConcerts().getConcerts());
		startReadCollection.addTableModel(concertTableModel);
		
		ConcertsJTable concertsJTable = new ConcertsJTable(concertTableModel);
		
		JScrollPane concertsScrollPane = new JScrollPane(concertsJTable);
		concertsScrollPane.setPreferredSize(new Dimension(1800,700));
		
		collectionTabPanes.add(concertsScrollPane, "Concerts");
		
		// Collection metrics history
		JTabbedPane collectionMetricsTabPanes = new JTabbedPane();
		
		JTable collectionMetricsHistoryTable = new JTable(new MetricsHistoryTableModel(Control.getCollectionMetricsHsitory()));
		JTable concertMetricsHistoryTable = new JTable(new MetricsHistoryTableModel(Control.getConcertMetricsHsitory()));
		
		JScrollPane collectionHistoryScrollPane = new JScrollPane(collectionMetricsHistoryTable);
		JScrollPane concertHistoryScrollPane = new JScrollPane(concertMetricsHistoryTable);
		
		collectionMetricsTabPanes.add(collectionHistoryScrollPane, "Evolution des albums");
		collectionMetricsTabPanes.add(concertHistoryScrollPane, "Evolution des concerts");
		
		collectionTabPanes.add(collectionMetricsTabPanes, "Evolution de la collection");
		
		add(collectionTabPanes);
	}

	public void rescanNeeded() {
		
		generateSiteControl.deactivate();
		readCollectionControl.getPip().setProcessStatus("Relecture nécessaire");
	}
}
