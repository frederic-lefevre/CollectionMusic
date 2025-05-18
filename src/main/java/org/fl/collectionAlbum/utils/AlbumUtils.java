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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class AlbumUtils {
	
	private static final String AUTEURS_SEPARATOR = ", ";
	
	public static String getHtmlForArtistes(MusicArtefact musicArtefact) {
		
		StringBuilder buf = getStringBuilderWithHtmlBegin();
		
		List<Artiste> artistes = musicArtefact.getAuteurs();
		if ((artistes != null) && !artistes.isEmpty()) {
			appendHtmlForArtistes(buf, musicArtefact, artistes, true);
		}
		buf.append("</body></html>");
		return buf.toString();
	}
	
	private static void appendHtmlForArtistes(StringBuilder buf, MusicArtefact musicArtefact, List<Artiste> artistes, boolean compact) {
				
		String auteurCssClass;
		if (compact) {
			auteurCssClass = "artistesmall";
			buf.append("<span class=\"")
				.append(auteurCssClass).append("\">")
				.append(musicArtefact.getAuteurs().stream()
							.map(Artiste::getNomComplet)
							.collect(Collectors.joining(AUTEURS_SEPARATOR)))
				.append("</span><br/>");
		} else {
			auteurCssClass = "artiste";
			artistes.forEach(artiste ->
					buf.append("<span class=\"").append(auteurCssClass).append("\">").append(artiste.getNomComplet()).append("</span><br/>")
				);
		}
		
		if (musicArtefact.hasIntervenant()) {
			if (compact) {
				buf.append("<span class=\"interv\">");
		
				appendIntervenants(buf, "- Direction: ", musicArtefact.getChefsOrchestre(), compact);
				appendIntervenants(buf, "- Interprète: ", musicArtefact.getInterpretes(), compact);
				appendIntervenants(buf, "- Ensemble: ", musicArtefact.getEnsembles(), compact);

			} else {
				buf.append("Interprètes:");
				buf.append("<ul>");
				
				appendIntervenants(buf, "<li>Direction: ", musicArtefact.getChefsOrchestre(), compact);
				appendIntervenants(buf, "<li>Interprète: ", musicArtefact.getInterpretes(), compact);
				appendIntervenants(buf, "<li>Ensemble: ", musicArtefact.getEnsembles(), compact);
				
				buf.append("</ul>");
			}	
		}		
	}
	
	private static void appendIntervenants(StringBuilder buf, String prefix, List<Artiste> artistes, boolean compact) {
		
		if (! artistes.isEmpty()) {
			if (compact) {			
				buf.append(prefix)
					.append(artistes.stream()
							.map(Artiste::getNomComplet)
							.collect(Collectors.joining(AUTEURS_SEPARATOR)));
			} else {
				artistes.forEach(artiste -> buf.append(prefix).append(artiste.getNomComplet()));
			}
		}
	}
	
	private static StringBuilder getStringBuilderWithHtmlBegin() {
		return new StringBuilder()
				.append("<html><head><style>")
				.append(Control.getCssForGui())
				.append("</style></head><body>");
		
	}
	
	public static String getSimpleHtml(Album album) {
		
		StringBuilder buf = getStringBuilderWithHtmlBegin();	
		
		buf.append("<h1>").append(album.getTitre()).append("</h1");
		
		List<Artiste> artistes = album.getAuteurs();
		if ((artistes != null) && !artistes.isEmpty()) {
			buf.append("<h3>Artistes:</h3>");
			appendHtmlForArtistes(buf, album, artistes, false);
		}
		
		buf.append("<h3>Dates de composition / Dates d'enregistrement:</h3>");
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
		buf.append("<h3>Format:</h3>");
		buf.append(album.getFormatAlbum().mediaSupportsHtmlList());
		if (album.hasMediaFiles()) {
			Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {
				if (album.getFormatAlbum().hasMediaFiles(contentNature)) {
					buf.append("  <h3>Fichiers " + contentNature.getNom() + ":</h3>\n");
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
			buf.append("<h3>Notes:</h3>");
			album.getNotes().forEach(note -> buf.append("<p>").append(note).append("</p>"));
		}
		
		if (album.hasUrlLinks()) {
			album.getUrlLinks().forEach(urlLink -> buf.append(" <li><h3><a href=\"").append(urlLink.toString()).append(""));
		}

		buf.append("</body></html>");
		return buf.toString();		
	}
	
	public static String getHtmlForMediaFiles(Album album) {
		
		StringBuilder buf = getStringBuilderWithHtmlBegin();
		buf.append("<table border=0>");
		Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {
			if (!album.hasContentNature(contentNature)) {
				buf.append("<tr><td class=\"nomedia\">").append(contentNature.getNom())
					.append("</td><td class=\"nomedia\">Pas de contenu</td></tr>");
			} else if (album.hasMediaFiles(contentNature)) {
				 if (! MediaFilesInventories.getMediaFileInventory(contentNature).isConnected()) {
					 buf.append("<tr><td class=\"mediako\">").append(contentNature.getNom())
					 	.append("</td><td class=\"mediako\">Répertoire des fichiers non connecté</td></tr>");
				 } else if (album.hasMediaFilePathNotFound(contentNature) ||
							album.hasMissingOrInvalidMediaFilePath(contentNature)) {
					 buf.append("<tr><td class=\"mediako\">").append(contentNature.getNom())
					 	.append("</td><td class=\"mediako\">Chemin manquant ou invalides</td></tr>");
				 } else {
					 buf.append("<tr><td class=\"mediaok\">").append(contentNature.getNom())
					 	.append("</td><td class=\"mediaok\">Chemin trouvé</td></tr>");
				 }
			 } else {
				 buf.append("<tr><td class=\"nofile\">").append(contentNature.getNom())
				 	.append("</td><td class=\"nofile\">Pas de fichier</td></tr>");
			 }
		});
		buf.append("</table></body></html>");
		return buf.toString();		
	}
}
