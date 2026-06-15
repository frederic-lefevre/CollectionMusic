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

import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ConcertAuteurComparator;
import org.fl.collectionAlbum.gui.renderer.AuteurListRenderer;
import org.fl.collectionAlbum.gui.renderer.DateRenderer;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class ConcertTableColumns {

	private static final Function<TemporalAccessor, String> concertDateFormatter = t -> TemporalUtils.formatDate((TemporalAccessor)t);
	
	private static final TableColumnParameter<Concert> DATE =
			new TableColumnParameter<>("Date", null, 200, new DateRenderer(concertDateFormatter), 
					new TemporalUtils.TemporalAccessorComparator(), TemporalAccessor.class, (c) -> c.getDateConcert());
	public static final TableColumnParameter<Concert> ARTISTE =
			new TableColumnParameter<>("Artistes", null, 700, new AuteurListRenderer(), new ConcertAuteurComparator(), Concert.class, (c) -> c);
	public static final TableColumnParameter<Concert> LIEU =
			new TableColumnParameter<>("Lieu", null, 500, null, null, String.class, (c) -> c.getLieuConcert().getLieu());
	
	public static final GenericTableColumns<Concert> REGULAR_COLUMNS = 
			new GenericTableColumns<>(List.of(DATE, ARTISTE, LIEU), 50);
}
