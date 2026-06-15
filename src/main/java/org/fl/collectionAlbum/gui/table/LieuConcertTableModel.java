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

import org.fl.collectionAlbum.concerts.LieuConcert;
import org.fl.collectionAlbum.gui.UpdatableElement;
import org.fl.collectionAlbum.gui.renderer.CollectionNumberRenderer;
import org.fl.collectionAlbum.utils.CollectionUtils;

public class LieuConcertTableModel extends AbstractCollectionTableModel<LieuConcert> implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	private static final TableColumnParameter<LieuConcert> LIEU =
			new TableColumnParameter<>("Lieu", null, 600, null, null, String.class, (l) -> l.getLieu());
	private static final TableColumnParameter<LieuConcert> NOMBRE =
			new TableColumnParameter<>("Nombre", null, 200, new CollectionNumberRenderer(), new CollectionUtils.IntegerComparator(), Integer.class, (l) -> l.getNombreConcert());
	
	public static final GenericTableColumns<LieuConcert> REGULAR_COLUMNS = new GenericTableColumns<>(List.of(LIEU, NOMBRE), 30);
	
	public LieuConcertTableModel(List<LieuConcert> lieuxConcerts, GenericTableColumns<LieuConcert>  columns) {
		super(columns, lieuxConcerts);
	}

	@Override
	public void updateElement() {
		fireTableDataChanged();
	}
}
