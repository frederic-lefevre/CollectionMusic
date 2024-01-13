/*
 MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

package org.fl.collectionAlbum.disocgs;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.OsActionCommandParameter;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;

public enum DiscogsReleaseCommandParameter implements OsActionCommandParameter<DiscogsAlbumRelease> {

	DISCOGS_RELEASE_INFO( 
			(release) -> Control.getDiscogsBaseUrlForRelease() + release.getInventoryCsvAlbum().getReleaseId(),
			(release) -> release.getInventoryCsvAlbum().getReleaseId() != null
			),
	ALBUMS_JSON(
			(release) -> release.getCollectionAlbums().stream()
				.map(album -> album.getJsonFilePath().toAbsolutePath().toString())
				.collect(Collectors.joining(" ")),
			(release) -> ((release.getCollectionAlbums() != null) && !release.getCollectionAlbums().isEmpty())
			);
	
	private final Function<DiscogsAlbumRelease,String> parametersGetter;
	private final Predicate<DiscogsAlbumRelease> actionValidityPredicate;
	
	private DiscogsReleaseCommandParameter(Function<DiscogsAlbumRelease,String> pg, Predicate<DiscogsAlbumRelease> vp) {
		parametersGetter = pg;
		actionValidityPredicate = vp;
	}

	@Override
	public Function<DiscogsAlbumRelease, String> getParametersGetter() {
		return parametersGetter;
	}

	@Override
	public Predicate<DiscogsAlbumRelease> getActionValidityPredicate() {
		return actionValidityPredicate;
	}

}
