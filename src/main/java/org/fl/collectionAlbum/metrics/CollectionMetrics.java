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

package org.fl.collectionAlbum.metrics;

import java.util.HashMap;
import java.util.Map;

import org.fl.collectionAlbum.CollectionAlbumContainer;

public class CollectionMetrics {

	private static final String TOTAL = "totalPhysique";
	private static final String NB_ARTISTE = "nombreArtiste";
	private static final String NB_ALBUM = "nombreAlbum";
	
	public static Metrics buildCollectionMetrics(long ts, CollectionAlbumContainer collectionAlbumContainer) {
		
		Map<String, Double> collectionMetrics = new HashMap<>();
		
		collectionMetrics.put(TOTAL, (double)collectionAlbumContainer.getCollectionAlbumsMusiques().getFormatListeAlbum().getPoids());
		collectionMetrics.put(NB_ARTISTE, (double)collectionAlbumContainer.getCollectionArtistes().getNombreArtistes());
		collectionMetrics.put(NB_ALBUM, (double)collectionAlbumContainer.getCollectionAlbumsMusiques().getNombreAlbums());
		
		collectionAlbumContainer
			.getCollectionAlbumsMusiques()
			.getFormatListeAlbum()
			.getSupportsPhysiquesNumbers()
			.entrySet()
			.stream()
			.forEachOrdered(entry -> collectionMetrics.put(entry.getKey().getId(), entry.getValue()));		

		return new Metrics(ts, collectionMetrics);
	}
}
