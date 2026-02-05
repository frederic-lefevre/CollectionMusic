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

import java.time.temporal.TemporalAccessor;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.AuteurComparator;
import org.fl.collectionAlbum.artistes.AuteurDateComparator;
import org.fl.collectionAlbum.artistes.AuteurDateDecesComparator;
import org.fl.collectionAlbum.gui.adapter.ArtisteMouseAdapter;
import org.fl.collectionAlbum.gui.renderer.AuteurDateRenderer;
import org.fl.collectionAlbum.gui.renderer.AuteurRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class ArtistesJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ArtistesJTable.class.getName());
	
	private static final AuteurComparator AUTEUR_COMPARATOR = new AuteurComparator();
	private static final AuteurDateComparator AUTEUR_DATE_COMPARATOR = new AuteurDateComparator();
	private static final AuteurDateDecesComparator AUTEUR_DECES_COMPARATOR = new AuteurDateDecesComparator();
	private static final CollectionUtils.IntegerComparator INTEGER_COMPARATOR = new CollectionUtils.IntegerComparator();
	private static final CollectionUtils.DoubleComparator DOUBLE_COMPARATOR = new CollectionUtils.DoubleComparator();
	
	private static final Function<Artiste, TemporalAccessor> artisteNaissanceGetter = a -> a.getNaissance();
	private static final Function<Artiste, TemporalAccessor> artisteDecesGetter = a -> a.getMort();
	private static final Function<TemporalAccessor, String> dateFormatterFunction = t -> TemporalUtils.formatDate((TemporalAccessor)t);
	
	public ArtistesJTable(ArtistesTableModel artistesTableModel, GenerationPane generationPane) {
		super(artistesTableModel);
		
		setFillsViewportHeight(true);
		
		getColumnModel().getColumn(ArtistesTableModel.NOM_COL_IDX).setCellRenderer(new AuteurRenderer());
		getColumnModel().getColumn(ArtistesTableModel.NAISSANCE_COL_IDX).setCellRenderer(new AuteurDateRenderer(artisteNaissanceGetter, dateFormatterFunction));
		getColumnModel().getColumn(ArtistesTableModel.DECES_COL_IDX).setCellRenderer(new AuteurDateRenderer(artisteDecesGetter, dateFormatterFunction));
		getColumnModel().getColumn(ArtistesTableModel.NB_CONCERTS_COL_IDX).setCellRenderer(new CollectionNumberRenderer());
		getColumnModel().getColumn(ArtistesTableModel.NB_ALBUMS_COL_IDX).setCellRenderer(new CollectionNumberRenderer());
		
		getColumnModel().getColumn(ArtistesTableModel.NOM_COL_IDX).setPreferredWidth(400);
		getColumnModel().getColumn(ArtistesTableModel.NAISSANCE_COL_IDX).setPreferredWidth(150);
		getColumnModel().getColumn(ArtistesTableModel.DECES_COL_IDX).setPreferredWidth(150);
		getColumnModel().getColumn(ArtistesTableModel.NB_CONCERTS_COL_IDX).setPreferredWidth(100);
		getColumnModel().getColumn(ArtistesTableModel.NB_ALBUMS_COL_IDX).setPreferredWidth(100);
				
		for (int columnIndex = artistesTableModel.getFirstEntetesNumber(); columnIndex < artistesTableModel.getColumnCount(); columnIndex++) {
			getColumnModel().getColumn(columnIndex).setCellRenderer(new CollectionNumberRenderer());
			getColumnModel().getColumn(columnIndex).setPreferredWidth(80);
		}
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new ArtisteMouseAdapter(this, generationPane));
		
		// Row sorter
		TableRowSorter<ArtistesTableModel> sorter = new TableRowSorter<>(artistesTableModel);	
		sorter.setComparator(ArtistesTableModel.NOM_COL_IDX, AUTEUR_COMPARATOR);
		sorter.setComparator(ArtistesTableModel.NAISSANCE_COL_IDX, AUTEUR_DATE_COMPARATOR);
		sorter.setComparator(ArtistesTableModel.DECES_COL_IDX, AUTEUR_DECES_COMPARATOR);
		sorter.setComparator(ArtistesTableModel.NB_ALBUMS_COL_IDX, INTEGER_COMPARATOR);
		sorter.setComparator(ArtistesTableModel.NB_CONCERTS_COL_IDX, INTEGER_COMPARATOR);
		for (int columnIndex = artistesTableModel.getFirstEntetesNumber(); columnIndex < artistesTableModel.getColumnCount(); columnIndex++) {
			sorter.setComparator(columnIndex, DOUBLE_COMPARATOR);
		}
		setRowSorter(sorter);
	}
	
	public Artiste getSelectedArtiste() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			logger.severe("Found several selected rows for ArtistesJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((ArtistesTableModel)getModel()).getArtisteAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
