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

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.gui.UpdatableElement;
import org.fl.collectionAlbum.mediaFile.MediaFile;

public class MediaFileTableModel extends AbstractTableModel implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	private final List<TableColumnParameter<MediaFile>> mediaColumnsParameters;
	private final int nbColumns;
	
	private final List<MediaFile> mediaFiles;
	
	public MediaFileTableModel(List<MediaFile> mediaFiles, ContentNature contentNature) {
		super();
		this.mediaFiles = mediaFiles;
		this.mediaColumnsParameters = MediaFileTableColumns.mediaColumnsParameters(contentNature);
		this.nbColumns = mediaColumnsParameters.size();		
	}
	
	@Override
	public int getRowCount() {
		if (mediaFiles == null) {
			return 0;
		} else {
			return mediaFiles.size();
		}
	}

	@Override
	public int getColumnCount() {
		return nbColumns;
	}

	@Override
	public String getColumnName(int col) {
	    return mediaColumnsParameters.get(col).name();
	}
	
    @Override
    public Class<?> getColumnClass(int columnIndex) {
    	return mediaColumnsParameters.get(columnIndex).valueClass();
    }
    
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if (rowIndex < mediaFiles.size()) {
			MediaFile mediaFile = mediaFiles.get(rowIndex);
			
			return mediaColumnsParameters.get(columnIndex).valueGetter().apply(mediaFile);

		} else {
			return null;
		}
	}

	public List<TableColumnParameter<MediaFile>> getMediaColumnsParameters() {
		return mediaColumnsParameters;
	}

	@Override
	public void updateElement() {
		fireTableDataChanged();
	}
}
