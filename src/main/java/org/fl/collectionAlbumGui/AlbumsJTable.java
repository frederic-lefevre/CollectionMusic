package org.fl.collectionAlbumGui;

import java.util.logging.Logger;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;

public class AlbumsJTable extends JTable {

	private static final long serialVersionUID = 1L;

	private static final Logger tLog = Control.getAlbumLog();
	
	public AlbumsJTable(TableModel dm) {
		super(dm);
		init();
	}

	private void init() {
		
		setFillsViewportHeight(true);
		setAutoCreateRowSorter(true);
		
		// Allow single row selection only
		ListSelectionModel listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectionModel(listSelectionModel);
		
		getColumnModel().getColumn(AlbumsTableModel.TITRE_COL_IDX).setPreferredWidth(500);
		getColumnModel().getColumn(AlbumsTableModel.AUTEUR_COL_IDX).setPreferredWidth(500);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
		
		setAutoCreateRowSorter(true);
	}

	// Get the selected album
	public Album getSelectedAlbum() {
		
		int[] rowIdxs = getSelectedRows();
		if (rowIdxs.length == 0) {
			return null;
		} else if (rowIdxs.length > 1) {
			tLog.severe("Found several selected rows for AlbumJTable. Number of selected rows: " + rowIdxs.length);
		}
		return ((AlbumsTableModel)getModel()).getAlbumAt(convertRowIndexToModel(rowIdxs[0]));
	}
}
