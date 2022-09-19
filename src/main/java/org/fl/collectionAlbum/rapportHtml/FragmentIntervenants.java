package org.fl.collectionAlbum.rapportHtml;

import java.net.URI;
import java.util.List;

import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.concerts.Concert;

public class FragmentIntervenants {

	public static void printIntervenant(MusicArtefact musicArtefact, StringBuilder fragment, String urlOffset) {	

		if (hasIntervenant(musicArtefact)) {
		
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
	
	private static boolean hasIntervenant(MusicArtefact musicArtefact) {
		return (notEmpty(musicArtefact.getChefsOrchestre())) || 
			   (notEmpty(musicArtefact.getInterpretes()))    || 
			   (notEmpty(musicArtefact.getEnsembles()));
	}
	
	private static boolean notEmpty(List<?> l) {
		return (l != null) && (! l.isEmpty());
	}
	
	private static void appendLinkAlbumArtiste(Artiste unArtiste, Class<? extends MusicArtefact> artefactsClass, StringBuilder fragment,  String urlOffset) {
		URI artefactsUri = null ;
		if (artefactsClass.equals(Album.class)) {
			artefactsUri = RapportStructuresAndNames.getArtisteAlbumRapportRelativePath(unArtiste) ;
		} else if (artefactsClass.equals(Concert.class)) {
			artefactsUri = RapportStructuresAndNames.getArtisteConcertRapportRelativePath(unArtiste) ;
		}
		if (artefactsUri != null) {
			fragment.append("      <a href=\"").append(urlOffset).append(artefactsUri.toString()).append("\">").append(unArtiste.getPrenoms()).append(" ").append(unArtiste.getNom()).append("</a><br/>\n") ;
		}
	}
}
