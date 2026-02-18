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

package org.fl.collectionAlbum.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.table.AlbumsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.ArtistesScrollJTablePane;
import org.fl.collectionAlbum.gui.table.ConcertsScrollJTablePane;
import org.fl.collectionAlbum.gui.table.DiscogsReleaseJTable;
import org.fl.collectionAlbum.gui.table.DisocgsReleaseTableModel;
import org.fl.collectionAlbum.gui.table.MediaFilesJTable;
import org.fl.collectionAlbum.gui.table.MediaFilesTableModel;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class GenerationPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String rText = "Lecture des fichiers albums et concerts";
	private static final String gText = "Génération du nouveau site collection";
	private static final String iText = "Arrêt";
	private static final String sText = "Aucune collection lue";
	private static final String s1Text = "Aucun site généré";

	private static final Dimension CONTROL_PANEL_DIMENSION = new Dimension(680, 224);
	private static final Dimension UTILS_PANEL_DIMENSION = new Dimension(300, 224);
	
	private final StartControl readCollectionControl;
	private final StartControl generateSiteControl;
	
	public GenerationPane(CollectionAlbumContainer collectionAlbumContainer) {

		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// collection tab: Scroll pane to contain the collection table
		AlbumsScrollJTablePane albumsScrollJTablePane = new AlbumsScrollJTablePane(collectionAlbumContainer.getCollectionAlbumsMusiques().getAlbums(), this);	
		
		ArtistesScrollJTablePane artistesScrollJTablePane = new ArtistesScrollJTablePane(collectionAlbumContainer.getCollectionArtistes().getArtistes(), this);
		ArtistesScrollJTablePane artistesConcertsScrollJTablePane = new ArtistesScrollJTablePane(collectionAlbumContainer.getConcertsArtistes().getArtistes(), this);
		
		// Control buttons panel
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		
		readCollectionControl = new StartControl(rText, iText, sText, () -> true);
		JPanel readCollectionControlPanel = readCollectionControl.getProcessControlPanel();
		readCollectionControlPanel.setPreferredSize(CONTROL_PANEL_DIMENSION);
		controlPanel.add(readCollectionControlPanel);

		UtilsPane utilsPane = new UtilsPane(this, collectionAlbumContainer);
		utilsPane.setPreferredSize(UTILS_PANEL_DIMENSION);
		utilsPane.deactivate();
		controlPanel.add(utilsPane);
		
		generateSiteControl = new StartControl(gText, iText, s1Text, StartGenerationSite.activationPredicate);
		generateSiteControl.deactivate();
		JPanel generateSiteControlPanel = generateSiteControl.getProcessControlPanel();
		generateSiteControlPanel.setPreferredSize(CONTROL_PANEL_DIMENSION);
		controlPanel.add(generateSiteControlPanel);

		List<ActivableElement> activableElements = List.of(readCollectionControl, utilsPane, generateSiteControl);

		StartReadCollection startReadCollection = new StartReadCollection(collectionAlbumContainer, readCollectionControl.getProgressInformationPanel(), activableElements);
		startReadCollection.setCollectionProcWaiter(new CollectionProcessWaiter(activableElements));
		startReadCollection.addTableModel(albumsScrollJTablePane.getAlbumsTableModel());
		startReadCollection.addTableModel(artistesScrollJTablePane.getArtistesTableModel());
		startReadCollection.addTableModel(artistesConcertsScrollJTablePane.getArtistesTableModel());
		readCollectionControl.getStartButton().addActionListener(startReadCollection);

		StartGenerationSite startGenerationSite = new StartGenerationSite(collectionAlbumContainer, generateSiteControl.getProgressInformationPanel(), activableElements);
		startGenerationSite.setCollectionProcWaiter(new CollectionProcessWaiter(activableElements));
		generateSiteControl.getStartButton().addActionListener(startGenerationSite);
		
		add(controlPanel);
		
		// Tab pane for generation of collection
		CollectionTabPanes collectionTabPanes = new CollectionTabPanes();
		
		collectionTabPanes.add(albumsScrollJTablePane, "Collection d'albums");	
		collectionTabPanes.add(artistesScrollJTablePane, "Artistes des albums");
		
		// Media files tabs
		Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {
			
			MediaFilesTableModel tm = new MediaFilesTableModel(MediaFilesInventories.getMediaFileInventory(contentNature));
			startReadCollection.addTableModel(tm);
			
			MediaFilesJTable mediaFilesJTable = new MediaFilesJTable(tm, this);
			
			// Scroll pane to contain the media path table
			JScrollPane mediaFilesScrollTable = new JScrollPane(mediaFilesJTable);
			mediaFilesScrollTable.setPreferredSize(Control.getMainSubPaneDimension());
			
			collectionTabPanes.add(mediaFilesScrollTable, "Chemins des fichiers " + contentNature.getNom());
		});
		
		// Discogs releases pane
		DisocgsReleaseTableModel dtm = new DisocgsReleaseTableModel(DiscogsInventory.getDiscogsInventory());
		startReadCollection.addTableModel(dtm);
		
		DiscogsReleaseJTable discogsReleaseJTable = new DiscogsReleaseJTable(dtm, collectionAlbumContainer, this);
		
		// Scroll pane to contain the discogs releases pane
		JScrollPane discogsReleasesScrollPane = new JScrollPane(discogsReleaseJTable);
		discogsReleasesScrollPane.setPreferredSize(Control.getMainSubPaneDimension());

		collectionTabPanes.add(discogsReleasesScrollPane, "Discogs releases");
		
		// Concert pane
		ConcertsScrollJTablePane concertsScrollJTablePane = new ConcertsScrollJTablePane(collectionAlbumContainer.getConcerts().getConcerts(), this);
		startReadCollection.addTableModel(concertsScrollJTablePane.getConcertTableModel());		
		collectionTabPanes.add(concertsScrollJTablePane, "Concerts");
		
		// Artistes concert tab
		collectionTabPanes.add(artistesConcertsScrollJTablePane, "Artistes des concerts");
		
		// Media supports tab
		MediaSupportsTabbedPane mediaSupportsTabbedPane = new MediaSupportsTabbedPane(collectionAlbumContainer, this);
		collectionTabPanes.add(mediaSupportsTabbedPane, "Supports media");
		mediaSupportsTabbedPane.getAlbumsTableModels().forEach(tableModel -> {
			startReadCollection.addTableModel(tableModel);
		});
		
		// Calendriers
		CalendarsTabbedPane calendarsTabbedPane = new CalendarsTabbedPane(collectionAlbumContainer, this);
		collectionTabPanes.add(calendarsTabbedPane, "Calendriers");
		
		// Collection metrics history
		CollectionMetricsTabbedPane collectionMetricsTabPanes = new CollectionMetricsTabbedPane(List.of(Control.getCollectionMetricsHsitory(), Control.getConcertMetricsHsitory()));

		startReadCollection.addColorableTabbedPane(List.of(collectionMetricsTabPanes, collectionTabPanes));
		startGenerationSite.addColorableTabbedPane(List.of(collectionMetricsTabPanes, collectionTabPanes));
		collectionMetricsTabPanes.getTableModels().forEach(tableModel -> {
			startReadCollection.addTableModel(tableModel);
			startGenerationSite.addTableModel(tableModel);
		});
		
		collectionTabPanes.add(collectionMetricsTabPanes, "Evolution de la collection");
		
		add(collectionTabPanes);
	}

	public void rescanNeeded() {
		
		generateSiteControl.deactivate();
		readCollectionControl.getProgressInformationPanel().setProgressInformation(new ProgressInformation("Relecture nécessaire", null, null));
	}
	
	private static class CollectionTabPanes extends AbstractColorableTabbedPane {

		private static final long serialVersionUID = 1L;

		@Override
		protected Color getBackgroundColorFor(int idx) {
			if (getComponentAt(idx) instanceof CollectionMetricsTabbedPane pane) {
				if (pane.metricsHasEvolved()) {
					return CollectionMetricsTabbedPane.METRICS_TAB_BACKGROUND_COLOR_HIGHLIGHT;
				}
				return null;
			}
			return null;
		}
		
		@Override
		protected Color getForegroundColorFor(int idx) {
			if (getComponentAt(idx) instanceof CollectionMetricsTabbedPane pane) {
				if (pane.metricsHasEvolved()) {
					return CollectionMetricsTabbedPane.METRICS_TAB_FOREGROUND_COLOR_HIGHLIGHT;
				}
				return null;
			}
			return null;
		}
	}
}
