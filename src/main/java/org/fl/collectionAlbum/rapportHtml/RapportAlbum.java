/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

package org.fl.collectionAlbum.rapportHtml;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class RapportAlbum extends RapportMusicArtefact {

	private final Album album ;
	
	private RapportAlbum(Album a) {
		super(a);
		album = a;
		withTitle(album.getTitre());
		withTitleDisplayed();
	}

	@Override
	protected void corpsRapport() {
		
		write("  <h3>Dates de composition / Dates d'enregistrement</h3>\n");
		write("  <ul>\n");
		write("    <li>");
		write(TemporalUtils.formatDate(album.getDebutComposition()));
		write (" - ");
		write(TemporalUtils.formatDate(album.getFinComposition()));
		write("    </li>\n");
		if (album.hasSpecificCompositionDates()) {
			write("    <li>");
			write(TemporalUtils.formatDate(album.getDebutEnregistrement()));
			write (" - ");
			write(TemporalUtils.formatDate(album.getFinEnregistrement()));
			write("    </li>\n");
		}
		write("  </ul>\n");
		super.corpsRapport();
	}
	
	public static RapportAlbum createRapportAlbum(Album a) {
		return new RapportAlbum(a);
	}
}
