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

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.adapter.AlbumMouseAdapter;

public class AlbumsJTable extends MusicArtefactTable<Album> {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Logger.getLogger(AlbumsJTable.class.getName());
	
	private final List<AlbumTableColumn> albumTableColumns;
	
	public AlbumsJTable(AbstractAlbumsTableModel albumsTableModel, GenerationPane generationPane) {
		super(albumsTableModel);
		
		this.albumTableColumns = albumsTableModel.getAlbumTableColumns();
		
		setFillsViewportHeight(true);
		
		setRowHeight(ContentNature.values().length*25);
		
		for (int colIdx = 0; colIdx < albumTableColumns.size(); colIdx++) {
			TableCellRenderer renderer =  albumTableColumns.get(colIdx).getCellRenderer();
			if (renderer != null) {
				 getColumnModel().getColumn(colIdx).setCellRenderer(renderer);
			}
		}

		for (int colIdx = 0; colIdx < albumTableColumns.size(); colIdx++) {
			 getColumnModel().getColumn(colIdx).setPreferredWidth(albumTableColumns.get(colIdx).getWidth());
		}
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new AlbumMouseAdapter(this, Control.getOsActionsOnAlbum(), generationPane));
		
		// Row sorter
		TableRowSorter<AbstractAlbumsTableModel> sorter = new TableRowSorter<>(albumsTableModel);
		for (int colIdx = 0; colIdx < albumTableColumns.size(); colIdx++) {
			Comparator<?> comparator =  albumTableColumns.get(colIdx).getComparator();
			if (comparator != null) {
				sorter.setComparator(colIdx, comparator);
			}
		}
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
		return ((AbstractAlbumsTableModel)getModel()).getAlbumAt(convertRowIndexToModel(rowIdxs[0]));
	}
	
	@Override
	public boolean isArtistsColumnSelected() {
		return albumTableColumns.get(getSelectedColumn()) == AlbumTableColumn.AUTEURS;
	}

	@Override
	public boolean isDiscogsReleaseColumnSelected() {
		return albumTableColumns.get(getSelectedColumn()) == AlbumTableColumn.DISCOGS;
	}

	@Override
	public boolean isLieuColumnSelected() {
		return false;
	}
}
