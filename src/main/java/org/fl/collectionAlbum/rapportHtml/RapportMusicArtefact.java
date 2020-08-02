package org.fl.collectionAlbum.rapportHtml;

import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.MusicArtefact;

public class RapportMusicArtefact extends RapportHtml {

	private final MusicArtefact musicArtefact;
	
	public RapportMusicArtefact(MusicArtefact m, Logger rl) {
		super("", rl);
		musicArtefact = m;
	}

	@Override
	protected void corpsRapport() {
		
		if (musicArtefact.getNotes() != null) {
			for (String note : musicArtefact.getNotes()) {
				write("<p>").write(note).write("</p>\n") ;
			}
		}
		
		List<String> urlInfos = musicArtefact.getUrlLinks() ;
		if (urlInfos != null) {
			write("<ul>\n");
			for (String url : urlInfos) {
				write("  <li><h3><a href=\"").write(url).write("\">").write(url).write("</a></h3></li>\n") ;
			}
			write("</ul>\n");
		}		
	}

}
