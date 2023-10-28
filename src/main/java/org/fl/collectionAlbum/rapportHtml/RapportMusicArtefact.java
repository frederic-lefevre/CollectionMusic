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

import java.net.URI;
import java.util.List;

import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.Artiste;

public class RapportMusicArtefact extends RapportHtml {

	private final MusicArtefact musicArtefact;
	
	protected RapportMusicArtefact(MusicArtefact m) {
		super("");
		musicArtefact = m;
	}

	@Override
	protected void corpsRapport() {
		
		List<Artiste> artistes = musicArtefact.getAuteurs();
		if (artistes != null) {
			write("  <h3>Artistes</h3>\n");
			write("  <ul><b>\n");
			for (Artiste unArtiste : artistes) {
				write("    <li>");
				write(unArtiste.getPrenoms());
				write(" ");
				write(unArtiste.getNom());
				write("</li>\n");
			}
			write("    </b>\n");
			if (musicArtefact.hasIntervenant()) {
				write("    <li>Interprètes:\n");
				FragmentIntervenants.printIntervenant(musicArtefact, rBuilder, "../");
				write("    </li>\n");
			}
			write("  </ul>\n");
		}
		
		if (musicArtefact.getNotes() != null) {
			write("  <h3>Notes</h3>\n");
			for (String note : musicArtefact.getNotes()) {
				write("<p>").write(note).write("</p>\n") ;
			}
		}
		
		List<String> urlInfos = musicArtefact.getUrlLinks() ;
		if (urlInfos != null) {
			write("<ul>\n");
			for (String url : urlInfos) {
				URI infosUri = RapportStructuresAndNames.getArtefactInfosAbsoluteUri(url) ;
				write("  <li><h3><a href=\"").write(infosUri.toString()).write("\">").write(url).write("</a></h3></li>\n") ;
			}
			write("</ul>\n");
		}		
	}

}
