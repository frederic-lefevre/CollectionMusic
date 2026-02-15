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

package org.fl.collectionAlbum.gui.table;

import java.time.temporal.TemporalAccessor;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.RangementComparator;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.AlbumCompositionComparator;
import org.fl.collectionAlbum.albums.AlbumEnregistrementComparator;
import org.fl.collectionAlbum.albums.AlbumMediaFilesStatusComparator;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.adapter.AlbumMouseAdapter;
import org.fl.collectionAlbum.gui.renderer.AuteurListRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.gui.renderer.DatesAlbumRenderer;
import org.fl.collectionAlbum.gui.renderer.MediaFilesRenderer;
import org.fl.collectionAlbum.gui.renderer.StringToHtmlRenderer;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class AlbumsJTable extends MusicArtefactTable<Album> {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Logger.getLogger(AlbumsJTable.class.getName());
	
	private static final RangementComparator RANGEMENT_COMPARATOR = new RangementComparator();
	private static final AlbumMediaFilesStatusComparator ALBUM_MEDIA_FILES_STATUS_COMPARATOR = new AlbumMediaFilesStatusComparator();
	private static final CollectionUtils.DoubleComparator DOUBLE_COMPARATOR = new CollectionUtils.DoubleComparator();
	private static final AlbumEnregistrementComparator ENREGISTREMENT_COMPARATOR = new AlbumEnregistrementComparator();
	private static final AlbumCompositionComparator COMPOSITION_COMPARATOR = new AlbumCompositionComparator();
	
	
	private static final Function<TemporalAccessor, String> dateFormatterFunction = t -> TemporalUtils.formatDate((TemporalAccessor)t);
	private static final Function<Album, TemporalAccessor> beginEnregistrementGetter = a -> a.getDebutEnregistrement();
	private static final Function<Album, TemporalAccessor> finEnregistrementGetter = a -> a.getFinEnregistrement();
	private static final Function<Album, TemporalAccessor> beginCompositionGetter = a -> a.getDebutComposition();
	private static final Function<Album, TemporalAccessor> endCompositionGetter = a -> a.getFinComposition();
	
	public AlbumsJTable(AlbumsTableModel albumsTableModel, GenerationPane generationPane) {
		super(albumsTableModel);
		
		setFillsViewportHeight(true);
		
		setRowHeight(ContentNature.values().length*25);
		
		getColumnModel().getColumn(AlbumsTableModel.TITRE_COL_IDX).setCellRenderer(new StringToHtmlRenderer());
		getColumnModel().getColumn(AlbumsTableModel.AUTEUR_COL_IDX).setCellRenderer(new AuteurListRenderer());
		getColumnModel().getColumn(AlbumsTableModel.MEDIA_FILES_COL_IDX).setCellRenderer(new MediaFilesRenderer());
		getColumnModel().getColumn(AlbumsTableModel.PROBLEM_COL_IDX).setCellRenderer(new CollectionBooleanRenderer());
		getColumnModel().getColumn(AlbumsTableModel.POIDS_COL_IDX).setCellRenderer(new CollectionNumberRenderer());
		getColumnModel().getColumn(AlbumsTableModel.ENREGISTREMENT_COL_IDX)
			.setCellRenderer(new DatesAlbumRenderer(beginEnregistrementGetter, finEnregistrementGetter, dateFormatterFunction));
		getColumnModel().getColumn(AlbumsTableModel.COMPOSITION_COL_IDX)
			.setCellRenderer(new DatesAlbumRenderer(beginCompositionGetter, endCompositionGetter, dateFormatterFunction));
		
		getColumnModel().getColumn(AlbumsTableModel.TITRE_COL_IDX).setPreferredWidth(250);
		getColumnModel().getColumn(AlbumsTableModel.AUTEUR_COL_IDX).setPreferredWidth(550);
		getColumnModel().getColumn(AlbumsTableModel.FORMAT_COL_IDX).setPreferredWidth(100);
		getColumnModel().getColumn(AlbumsTableModel.MEDIA_FILES_COL_IDX).setPreferredWidth(140);
		getColumnModel().getColumn(AlbumsTableModel.PROBLEM_COL_IDX).setPreferredWidth(70);
		getColumnModel().getColumn(AlbumsTableModel.DISCOGS_COL_IDX).setPreferredWidth(110);
		getColumnModel().getColumn(AlbumsTableModel.POIDS_COL_IDX).setPreferredWidth(50);
		getColumnModel().getColumn(AlbumsTableModel.ENREGISTREMENT_COL_IDX).setPreferredWidth(260);
		getColumnModel().getColumn(AlbumsTableModel.COMPOSITION_COL_IDX).setPreferredWidth(260);
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new AlbumMouseAdapter(this, Control.getOsActionsOnAlbum(), generationPane));
		
		// Row sorter
		TableRowSorter<AlbumsTableModel> sorter = new TableRowSorter<>(albumsTableModel);
		sorter.setComparator(AlbumsTableModel.AUTEUR_COL_IDX, RANGEMENT_COMPARATOR);
		sorter.setComparator(AlbumsTableModel.MEDIA_FILES_COL_IDX, ALBUM_MEDIA_FILES_STATUS_COMPARATOR);
		sorter.setComparator(AlbumsTableModel.POIDS_COL_IDX, DOUBLE_COMPARATOR);
		sorter.setComparator(AlbumsTableModel.ENREGISTREMENT_COL_IDX, ENREGISTREMENT_COMPARATOR);
		sorter.setComparator(AlbumsTableModel.COMPOSITION_COL_IDX, COMPOSITION_COMPARATOR);
		setRowSorter(sorter);
	}

	// Get the selected album
	@Override
	public Album getSelectedMusicArtefact() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			tLog.severe("Found several selected rows for AlbumJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((AlbumsTableModel)getModel()).getAlbumAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
