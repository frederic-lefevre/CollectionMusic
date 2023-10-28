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
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;

public class FragmentIntervenants {

	public static void printIntervenant(MusicArtefact musicArtefact, StringBuilder fragment, String urlOffset) {	

		if (musicArtefact.hasIntervenant()) {
		
			fragment.append("      <ul class=\"interv\">") ;

			if (musicArtefact.getChefsOrchestre() != null) {
				for (Artiste unChef : musicArtefact.getChefsOrchestre()) {
					fragment.append("      <li>Direction: ") ;
					appendLinkAlbumArtiste(unChef, musicArtefact.getClass(), fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}
			
			if (musicArtefact.getInterpretes() != null) {
				for (Artiste unInterprete : musicArtefact.getInterpretes()) {
					fragment.append("      <li>Interpr&egrave;te: ") ;
					appendLinkAlbumArtiste(unInterprete,  musicArtefact.getClass(), fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}	
			
			if (musicArtefact.getEnsembles() != null) {
				for (Artiste unGroupe : musicArtefact.getEnsembles()) {
					fragment.append("      <li>Ensemble: ") ;
					appendLinkAlbumArtiste(unGroupe,  musicArtefact.getClass(), fragment, urlOffset) ;
					fragment.append("</li>\n") ;
				}
			}
			fragment.append("      </ul>") ;
		}
	}
	
	private static void appendLinkAlbumArtiste(Artiste unArtiste, Class<? extends MusicArtefact> artefactsClass, StringBuilder fragment,  String urlOffset) {
		URI artefactsUri = null ;
		if (artefactsClass.equals(Album.class)) {
			artefactsUri = RapportStructuresAndNames.getArtisteAlbumRapportRelativeUri(unArtiste) ;
		} else if (artefactsClass.equals(Concert.class)) {
			artefactsUri = RapportStructuresAndNames.getArtisteConcertRapportRelativeUri(unArtiste) ;
		}
		if (artefactsUri != null) {
			fragment.append("      <a href=\"").append(urlOffset).append(artefactsUri.toString()).append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;
		}
	}
}
