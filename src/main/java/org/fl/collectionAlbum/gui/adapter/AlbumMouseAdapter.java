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

package org.fl.collectionAlbum.gui.adapter;

import java.util.List;
import java.util.stream.Stream;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.gui.GenerationPane;
import org.fl.collectionAlbum.gui.MusicArtefactTable;
import org.fl.collectionAlbum.gui.listener.AlbumCustomActionListener;
import org.fl.collectionAlbum.gui.listener.AlbumCustomActionListener.CustomAction;
import org.fl.collectionAlbum.osAction.OsAction;

public class AlbumMouseAdapter extends MusicArtefactMouseAdapter<Album> {
	
	public AlbumMouseAdapter(MusicArtefactTable<Album> albumsTable, List<OsAction<Album>>  osActions, GenerationPane generationPane) {
		
		super(albumsTable, osActions);

		Stream.of(CustomAction.values()).forEachOrdered(customAction -> 
			musicArtefactMenuItems.addMenuItem(
						customAction.getActionTitle(), 
						new AlbumCustomActionListener(albumsTable, customAction, generationPane), 
						customAction.getDisplayable(),
						localJPopupMenu));
	}
}
