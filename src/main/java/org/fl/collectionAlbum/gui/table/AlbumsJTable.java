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
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.adapter.AlbumMouseAdapter;

public class AlbumsJTable extends MusicArtefactTable<Album> {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Logger.getLogger(AlbumsJTable.class.getName());
	
	private final AlbumTableColumns albumTableColumns;
	
	public record AlbumColumnSort(TableColumnParameter<Album> albumTableColumnToSort, SortOrder sortOrder) {}
	
	public AlbumsJTable(AbstractAlbumsTableModel albumsTableModel, GenerationPane generationPane, AlbumColumnSort albumColumnSort) {
		super(albumsTableModel);
		
		this.albumTableColumns = albumsTableModel.getAlbumTableColumns();
		
		setFillsViewportHeight(true);
		
		setRowHeight(albumTableColumns.rowHeight());
		
		// Row sorter
		TableRowSorter<AbstractAlbumsTableModel> sorter = new TableRowSorter<>(albumsTableModel);
		
		for (int colIdx = 0; colIdx < albumTableColumns.tableColumnParameters().size(); colIdx++) {
			TableCellRenderer renderer =  albumTableColumns.tableColumnParameters().get(colIdx).cellRenderer();
			if (renderer != null) {
				 getColumnModel().getColumn(colIdx).setCellRenderer(renderer);
			}
			getColumnModel().getColumn(colIdx).setPreferredWidth(albumTableColumns.tableColumnParameters().get(colIdx).width());
			
			Comparator<?> comparator =  albumTableColumns.tableColumnParameters().get(colIdx).comparator();
			if (comparator != null) {
				sorter.setComparator(colIdx, comparator);
			}
		}
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		addMouseListener(new AlbumMouseAdapter(this, Control.getOsActionsOnAlbum(), generationPane));

		setRowSorter(sorter);
		
		if (albumColumnSort != null) {
			int columnIdx = getColumnNumber(albumColumnSort.albumTableColumnToSort);
			if (columnIdx >= 0) {
				sorter.setSortKeys(List.of(new RowSorter.SortKey(columnIdx, albumColumnSort.sortOrder())));
			}
		}	
	}
	
	private int getColumnNumber(TableColumnParameter<Album> albumTableColumn) {
		
		for (int i=0; i < albumTableColumns.tableColumnParameters().size(); i++) {
			if (albumTableColumns.tableColumnParameters().get(i) == albumTableColumn) {
				return i;
			}
		}
		return -1;	
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
		return albumTableColumns.tableColumnParameters().get(getSelectedColumn()) == AlbumTableColumns.AUTEURS;
	}

	@Override
	public boolean isDiscogsReleaseColumnSelected() {
		return albumTableColumns.tableColumnParameters().get(getSelectedColumn()) == AlbumTableColumns.DISCOGS;
	}

	@Override
	public boolean isLieuColumnSelected() {
		return false;
	}
}
