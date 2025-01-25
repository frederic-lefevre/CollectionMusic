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

package org.fl.collectionAlbum.utils;

import java.util.List;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.format.ContentNature;

public class AlbumUtils {
	
	public static String getSimpleHtml(Album album) {
		
		StringBuilder buf = new StringBuilder();	
		
		buf.append("<html><head><style>");
		buf.append(Control.getCssForGui());
		buf.append("</style></head><body>");
		
		buf.append("<h2>").append(album.getTitre()).append("</h2");
		
		List<Artiste> artistes = album.getAuteurs();
		if (artistes != null) {
			buf.append("<h3>Artistes</h3>");
			
			artistes.forEach(artiste ->
				buf.append("<span class=\"artiste\">").append(artiste.getPrenoms()).append(" ").append(artiste.getNom()).append("</span><br/>")
			);
			
			if (album.hasIntervenant()) {
				buf.append("Interprètes:");
				buf.append("<ul>");
				album.getChefsOrchestre().forEach(chefOrchestre ->
					buf.append("<li>Direction: ").append(chefOrchestre.getPrenoms()).append(" ").append(chefOrchestre.getNom()).append("</li>")
						);
				album.getInterpretes().forEach(interprete ->
					buf.append("<li>Interprète: ").append(interprete.getPrenoms()).append(" ").append(interprete.getNom()).append("</li>")
				);
				album.getEnsembles().forEach(ensemble ->
					buf.append("<li>Ensemble: ").append(ensemble.getPrenoms()).append(" ").append(ensemble.getNom()).append("</li>")
				);
				buf.append("</ul>");
			}
		}
		
		buf.append("<h3>Dates de composition / Dates d'enregistrement</h3>");
		buf.append("<ul><li>");
		buf.append(TemporalUtils.formatDate(album.getDebutComposition()));
		buf.append(" - ");
		buf.append(TemporalUtils.formatDate(album.getFinComposition()));
		buf.append("</li>");
		if (album.hasSpecificCompositionDates()) {
			buf.append("<li>");
			buf.append(TemporalUtils.formatDate(album.getDebutEnregistrement()));
			buf.append(" - ");
			buf.append(TemporalUtils.formatDate(album.getFinEnregistrement()));
			buf.append("</li>");
		}
		buf.append("</ul>");
		buf.append("<h3>Format</h3>");
		buf.append(album.getFormatAlbum().mediaSupportsHtmlList());
		if (album.hasMediaFiles()) {
			Stream.of(ContentNature.values()).forEach(contentNature -> {
				if (album.getFormatAlbum().hasMediaFiles(contentNature)) {
					buf.append("  <h3>Fichiers " + contentNature.getNom() + "</h3>\n");
					buf.append("  <table>\n");
					album.getFormatAlbum().getMediaFiles(contentNature).forEach(mediaFile -> {
						buf.append("    <tr><td class=\"mediadetail\">\n");
						buf.append(mediaFile.displayMediaFileDetailWithFileLink("<br/>\n", true));
						buf.append("    </td></tr>\n");							
					});
					buf.append("  </table>\n");
				}
			});
		} else {
			buf.append("<p>Aucun fichier media.</p>");
		}
		
		if (album.hasDiscogsRelease()) {
			buf.append("<h3>Release id Discogs: ").append(album.getDiscogsLink()).append("</h3>");
		}
		
		if (album.hasNotes()) {
			buf.append("<h3>Notes</h3>");
			album.getNotes().forEach(note -> buf.append("<p>").append(note).append("</p>"));
		}
		
		if (album.hasUrlLinks()) {
			album.getUrlLinks().forEach(urlLink -> buf.append(" <li><h3><a href=\"").append(urlLink.toString()).append(""));
		}

		buf.append("</body></html>");
		return buf.toString();
		
	}


}
