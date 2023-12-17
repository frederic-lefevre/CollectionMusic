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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.mediaPath.MediaFileInventory;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;

public class MediaFilesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	public final static int PATH_COL_IDX = 0;
	
	private final static String[] entetes = {"Chemins"};
	
	private final List<MediaFilePath> mediaFilePaths;
	
	public MediaFilesTableModel(MediaFileInventory mediaFileInventory) {
		super();
		this.mediaFilePaths = mediaFileInventory.getMediaFilePathList();
	}

	@Override
	public int getRowCount() {
		if (mediaFilePaths == null) {
			return 0;
		} else {
			return mediaFilePaths.size();
		}
	}

	@Override
	public int getColumnCount() {
		return entetes.length;
	}

	@Override
	public String getColumnName(int col) {
	    return entetes[col];
	}
	
    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		return switch(columnIndex){
			case PATH_COL_IDX -> mediaFilePaths.get(rowIndex).getPath().toString();
			default -> null;
		};
	}

}
