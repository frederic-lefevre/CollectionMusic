package org.fl.collectionAlbum.jsonParsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.Groupe;
import org.fl.collectionAlbum.artistes.ListeArtiste;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MusicArtefactParser {
	
	public static List<Artiste> getListeAuteurs(JsonObject arteFactJson, List<ListeArtiste> knownArtistes, Logger mLog) {
		return processListeArtistes(Artiste.class, arteFactJson, JsonMusicProperties.AUTEUR, knownArtistes, mLog) ;
	}
	
	public static List<Artiste> getListeInterpretes(JsonObject arteFactJson, List<ListeArtiste> knownArtistes, Logger mLog) {
		return processListeArtistes(Artiste.class, arteFactJson, JsonMusicProperties.INTERPRETE, knownArtistes, mLog) ;
	}
	
	public static List<Artiste> getListeChefs(JsonObject arteFactJson, List<ListeArtiste> knownArtistes, Logger mLog) {
		return processListeArtistes(Artiste.class, arteFactJson, JsonMusicProperties.CHEF, knownArtistes, mLog) ;
	}
	
	public static List<Artiste> getListeEnsembles(JsonObject arteFactJson, List<ListeArtiste> knownArtistes, Logger mLog) {
		return processListeArtistes(Groupe.class, arteFactJson, JsonMusicProperties.ENSEMBLE, knownArtistes, mLog) ;
	}
	
	public static List<Artiste> getListeGroupes(JsonObject arteFactJson, List<ListeArtiste> knownArtistes, Logger mLog) {
		return processListeArtistes(Groupe.class, arteFactJson, JsonMusicProperties.GROUPE, knownArtistes, mLog) ;
	}
	
	private static List<Artiste> processListeArtistes(Class<? extends Artiste> cls, JsonObject arteFactJson, String artistesJprop, List<ListeArtiste> knownArtistes, Logger mLog) {
		
		List<Artiste> artistes = new ArrayList<Artiste>() ;
		JsonElement jElem = arteFactJson.get(artistesJprop) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				JsonArray jArtistes = jElem.getAsJsonArray() ;
				
				for (JsonElement jArtiste : jArtistes) {

					try {
						artistes.add(processArtiste(cls, jArtiste.getAsJsonObject(), knownArtistes, mLog)) ;
					} catch (IllegalStateException e) {
						mLog.log(Level.WARNING, "un artiste n'est pas un objet json pour l'arteFact " + arteFactJson, e) ;
					}
				}
			} else {
				mLog.warning(artistesJprop + " n'est pas un tableau json pour l'arteFact " + arteFactJson) ;
			}
		}
		return artistes ;
	}
	
	private static Artiste processArtiste(Class<? extends Artiste> cls, JsonObject jArtiste, List<ListeArtiste> knownArtistes, Logger mLog) {
		
		Artiste unArtiste ;
		if (cls == Groupe.class) {
			unArtiste = (Groupe)createGetOrUpdateArtiste(cls, jArtiste, knownArtistes, mLog) ;
		} else {
			unArtiste = createGetOrUpdateArtiste(cls, jArtiste, knownArtistes, mLog) ;
		}
		return unArtiste ;
	}
	
	// Get an artiste, if it exists, return the existing one eventually updated
	// if it does not exists, create it
	private static Artiste createGetOrUpdateArtiste(Class<? extends Artiste> cls, JsonObject jArtiste, List<ListeArtiste> knownArtistes, Logger mLog) {
		
		Artiste artiste;
		Optional<Artiste> eventualArtiste =	knownArtistes.stream()
														  .map(listeArtiste -> listeArtiste.getArtisteKnown(jArtiste))
														  .filter(a -> a.isPresent())
														  .map(a -> a.get())
														  .findFirst() ;
		
		if (! eventualArtiste.isPresent()) {
			if (cls == Groupe.class) {
				artiste = new Groupe(jArtiste, mLog) ;
			} else {
				artiste = new Artiste(jArtiste, mLog) ;
			}
		} else {
			artiste = eventualArtiste.get() ;
			artiste.update(jArtiste);
		}
		return artiste ;
	}
	
	public static List<String> getNotes(JsonObject arteFactJson, Logger mLog) {

		List<String> notes = new ArrayList<String>() ;
        JsonElement jElem = arteFactJson.get(JsonMusicProperties.NOTES) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				notes = new ArrayList<String>() ;
				JsonArray jNotes = jElem.getAsJsonArray() ; 
				for (JsonElement jNote : jNotes) {
					notes.add(jNote.getAsString()) ;
				}
				if (mLog.isLoggable(Level.FINEST)) {
					mLog.finest("Nombre de note: " + notes.size()) ;
				}
			} else {
				mLog.warning(JsonMusicProperties.NOTES + " n'est pas un JsonArray pour l'artefact " + arteFactJson) ;
			}
		}
		return notes ;
	}
}
