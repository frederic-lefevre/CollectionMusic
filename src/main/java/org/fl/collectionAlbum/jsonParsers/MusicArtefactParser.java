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
	
	private List<ListeArtiste> knownArtistes ;
	private JsonObject arteFactJson ;
	private Logger mLog ;
	
	public MusicArtefactParser(JsonObject j, List<ListeArtiste> ka, Logger l) {
		super();
		
		arteFactJson  = j ;
		knownArtistes = ka ;
		mLog 		  = l ;
	}

	public List<Artiste> getListeAuteurs() {
		return processListeArtistes(Artiste.class, arteFactJson, JsonMusicProperties.AUTEUR) ;
	}
	
	public List<Artiste> getListeInterpretes() {
		return processListeArtistes(Artiste.class, arteFactJson, JsonMusicProperties.INTERPRETE) ;
	}
	
	public List<Artiste> getListeChefs() {
		return processListeArtistes(Artiste.class, arteFactJson, JsonMusicProperties.CHEF) ;
	}
	
	public List<Artiste> getListeEnsembles() {
		return processListeArtistes(Groupe.class, arteFactJson, JsonMusicProperties.ENSEMBLE) ;
	}
	
	public List<Artiste> getListeGroupes() {
		return processListeArtistes(Groupe.class, arteFactJson, JsonMusicProperties.GROUPE) ;
	}
	
	private List<Artiste> processListeArtistes(Class<? extends Artiste> cls, JsonObject arteFactJson, String artistesJprop) {
		
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
	
	private Artiste processArtiste(Class<? extends Artiste> cls, JsonObject jArtiste, List<ListeArtiste> knownArtistes, Logger mLog) {
		
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
	private Artiste createGetOrUpdateArtiste(Class<? extends Artiste> cls, JsonObject jArtiste, List<ListeArtiste> knownArtistes, Logger mLog) {
		
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
	
	public List<String> getNotes() {

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
