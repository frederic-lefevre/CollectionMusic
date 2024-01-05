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

package org.fl.collectionAlbum.albums;

import java.util.function.Function;
import java.util.function.Predicate;

import org.fl.collectionAlbum.Control;

public enum AlbumCommandParameter {

	JSON(
			(album) -> album.getJsonFilePath().toAbsolutePath().toString(), 
			(album) -> true),
	DISCOGS_RELEASE_INFO(
			(album) -> Control.getDiscogsBaseUrlForRelease() + album.getDiscogsLink(), 
			(album) -> (album.getDiscogsLink() != null) && !album.getDiscogsLink().isEmpty());
	
	private final Function<Album,String> parametersGetter;
	private final Predicate<Album> actionValidityPredicate;
	
	private AlbumCommandParameter(Function<Album,String> gp, Predicate<Album> vp) {
		parametersGetter = gp;
		actionValidityPredicate = vp;
	}

	public Function<Album, String> getParametersGetter() {
		return parametersGetter;
	}

	public Predicate<Album> getActionValidityPredicate() {
		return actionValidityPredicate;
	}

}
