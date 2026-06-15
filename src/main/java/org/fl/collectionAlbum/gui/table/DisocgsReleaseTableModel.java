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
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.disocgs.DiscogsAlbumRelease;
import org.fl.collectionAlbum.disocgs.FormatCompatibilityResult;
import org.fl.collectionAlbum.gui.UpdatableElement;
import org.fl.collectionAlbum.gui.renderer.CollectionBooleanRenderer;
import org.fl.collectionAlbum.gui.renderer.DateRenderer;
import org.fl.collectionAlbum.gui.renderer.FormatCompatibilityRenderer;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class DisocgsReleaseTableModel extends AbstractCollectionTableModel<DiscogsAlbumRelease> implements UpdatableElement {

	private static final long serialVersionUID = 1L;

	private static final Function<TemporalAccessor, String> discogsDateFormatter = t -> TemporalUtils.formatDateNumeric((TemporalAccessor)t);
	private static final FormatCompatibilityResult.FormatCompatibilityComparator FORMAT_COMPATIBILITY_COMPARATOR = new FormatCompatibilityResult.FormatCompatibilityComparator();
	
	private static final TableColumnParameter<DiscogsAlbumRelease> ID = 
			new TableColumnParameter<>("Id", null, 70, null, null, String.class, (d) -> d.getInventoryCsvAlbum().getReleaseId());
	private static final TableColumnParameter<DiscogsAlbumRelease> ARTISTES = 
			new TableColumnParameter<>("Auteurs", null, 700, null, null, String.class, (d) -> d.getInventoryCsvAlbum().getArtists().stream().collect(Collectors.joining(",")));
	private static final TableColumnParameter<DiscogsAlbumRelease> TITLE = 
			new TableColumnParameter<>("Titre de l'album", null, 580, null, null, String.class, (d) -> d.getInventoryCsvAlbum().getTitle());
	private static final TableColumnParameter<DiscogsAlbumRelease> FORMAT = 
			new TableColumnParameter<>("Formats", null, 200, null, null, String.class, (d) -> d.getInventoryCsvAlbum().getFormats().stream().collect(Collectors.joining(",")));
	private static final TableColumnParameter<DiscogsAlbumRelease> DATE_AJOUT = 
			new TableColumnParameter<>("Date ajout", null, 90, new DateRenderer(discogsDateFormatter), null, String.class, (d) -> d.getInventoryCsvAlbum().getDateAdded());
	private static final TableColumnParameter<DiscogsAlbumRelease> ALBUM_LINKED = 
			new TableColumnParameter<>("Lié à un album", null, 100, new CollectionBooleanRenderer(), null, Boolean.class, (d) -> d.isLinkedToAlbum());
	private static final TableColumnParameter<DiscogsAlbumRelease> FORMAT_MATCH = 
			new TableColumnParameter<>("Format Ok", null, 80, new FormatCompatibilityRenderer(), FORMAT_COMPATIBILITY_COMPARATOR, FormatCompatibilityResult.class, (d) -> d.formatCompatibility());
	
	public static final GenericTableColumns<DiscogsAlbumRelease> REGULAR_COLUMNS = 
			new GenericTableColumns<>(List.of(ID, ARTISTES, TITLE, FORMAT, DATE_AJOUT, ALBUM_LINKED, FORMAT_MATCH), 0);
	
	public DisocgsReleaseTableModel(List<DiscogsAlbumRelease> discogsAlbumReleases,  GenericTableColumns<DiscogsAlbumRelease> columns) {
		super(columns, discogsAlbumReleases);
	}

	@Override
	public void updateElement() {
		fireTableDataChanged();
	}
}
