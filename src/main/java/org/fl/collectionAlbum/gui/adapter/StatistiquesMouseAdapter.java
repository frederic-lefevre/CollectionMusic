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

package org.fl.collectionAlbum.gui.adapter;

import java.awt.event.MouseAdapter;

import org.fl.collectionAlbum.CollectionAlbumContainer;
import org.fl.collectionAlbum.gui.GenerationPane;

public class StatistiquesMouseAdapter extends MouseAdapter {

	protected int anneeDebut;
	protected int anneeFin;
	protected CollectionAlbumContainer collectionAlbumContainer;
	protected GenerationPane generationPane;
	
	protected StatistiquesMouseAdapter(int anneeDebut, int anneeFin,
			CollectionAlbumContainer collectionAlbumContainer, GenerationPane generationPane) {
		this.anneeDebut = anneeDebut;
		this.anneeFin = anneeFin;
		this.collectionAlbumContainer = collectionAlbumContainer;
		this.generationPane = generationPane;
	}
	
	protected boolean isBetween(int val, int minInclusive, int maxExclusive) {
		return (val >= minInclusive && val < maxExclusive);
	}
	
	protected String getWindowsTitle(String name) {
		String windowTitle = name + anneeDebut;
		if (anneeDebut + 1 < anneeFin) {
			windowTitle = windowTitle + "-" + (anneeFin-1);
		}
		return windowTitle;
	}
}
