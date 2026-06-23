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

import org.fl.collectionAlbum.gui.UpdatableElement;
import org.fl.collectionAlbum.mediaFile.MediaFile;

public class MediaFileTableModel extends AbstractCollectionTableModel<MediaFile> implements UpdatableElement {

	private static final long serialVersionUID = 1L;
	
	private final List<MediaFile> mediaFiles;
	
	public MediaFileTableModel(List<MediaFile> mediaFiles, GenericTableColumns<MediaFile> columns) {
		super(columns, mediaFiles);
		this.mediaFiles = mediaFiles;	
	}

	public MediaFile getMediaFileAt(int rowIndex) {
		return  mediaFiles.get(rowIndex);
	}
	
	public List<MediaFile> getMediaFileList() {
		return getItemList();
	}
	
	@Override
	public void updateElement() {
		fireTableDataChanged();
	}
}
