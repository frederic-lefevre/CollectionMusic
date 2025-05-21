/*
 MIT License

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

package org.fl.collectionAlbum.osAction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.MusicArtefact;

public class MusicArtefactCommandParameter<T extends MusicArtefact> implements OsActionCommandParameter<T> {
	
	Function<T,List<String>> parametersGetter;
	Predicate<T> actionValidityPredicate;
	
	public enum ParameterType { JSON, DISCOGS_RELEASE_INFO}
	
	public MusicArtefactCommandParameter(ParameterType parameterType) {
		
		switch (parameterType) {
		case JSON -> {
			parametersGetter = (musicArtefact) -> List.of(musicArtefact.getJsonFilePath().toAbsolutePath().toString());
			actionValidityPredicate = (musicArtefact) -> true;
		}
		case DISCOGS_RELEASE_INFO -> {
			parametersGetter = (musicArtefact) -> List.of(Control.getDiscogsBaseUrlForRelease() + musicArtefact.getDiscogsLink());
			actionValidityPredicate = (musicArtefact) -> (musicArtefact.getDiscogsLink() != null) && !musicArtefact.getDiscogsLink().isEmpty();
		}
		};
	}
	
	@Override
	public Function<T, List<String>> getParametersGetter() {
		return parametersGetter;
	}
	
	@Override
	public Predicate<T> getActionValidityPredicate() {
		return actionValidityPredicate;
	}
}
