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

package org.fl.collectionAlbum.rapportHtml;

import java.util.List;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.Artiste;

public class RapportMusicArtefact extends RapportHtml {

	private static final String DISCOGS_LINK1 = "<p><a href=\"";
	private static final String DISCOGS_LINK2 = "\">";
	private static final String DISCOGS_LINK3 = "</a></p>\n";
	
	private final MusicArtefact musicArtefact;
	
	protected RapportMusicArtefact(MusicArtefact m) {
		super("", null);
		musicArtefact = m;
	}

	@Override
	protected void corpsRapport() {
		
		List<Artiste> artistes = musicArtefact.getAuteurs();
		if (artistes != null) {
			write("  <h3>Artistes</h3>\n");
			FragmentIntervenants.printAuteurs(musicArtefact, rBuilder, "../");

			if (musicArtefact.hasIntervenant()) {
				write("    Interprètes:\n");
				FragmentIntervenants.printIntervenant(musicArtefact, rBuilder, "../");
			}
		}
		
		if (musicArtefact.hasDiscogsRelease()) {
			String discogsReleaseUrl = Control.getDiscogsBaseUrlForRelease() + musicArtefact.getDiscogsLink();
			write("  <h3>Discogs link</h3>\n");
			write(DISCOGS_LINK1);
			write(discogsReleaseUrl);
			write(DISCOGS_LINK2);
			write(discogsReleaseUrl);
			write(DISCOGS_LINK3);
		}
		
		if ( musicArtefact.hasNotes()) {
			write("  <h3>Notes</h3>\n");
			for (String note : musicArtefact.getNotes()) {
				write("<p>").write(note).write("</p>\n");
			}
		}
		
		if (musicArtefact.hasUrlLinks()) {
			write("  <h3>Autres liens</h3>\n");
			write("<ul>\n");
			musicArtefact.getUrlLinks().forEach(infosUri -> {
				String uriString = infosUri.toString();
				write("  <li><a href=\"").write(uriString).write("\">").write(uriString).write("</a></li>\n");			
			});
			write("</ul>\n");
		}		
	}

}
