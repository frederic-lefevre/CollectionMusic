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
	
	private List<Artiste> auteurs ;
	private List<Artiste> interpretes ;
	private List<Artiste> chefs ;
	private List<Artiste> ensembles ;
	private List<Artiste> groupes ;
	
	public MusicArtefactParser(JsonObject j, List<ListeArtiste> ka, Logger l) {
		super();
		
		arteFactJson  = j ;
		knownArtistes = ka ;
		mLog 		  = l ;
		
		auteurs 	= processListeArtistes(Artiste.class, JsonMusicProperties.AUTEUR) ;
		interpretes = processListeArtistes(Artiste.class, JsonMusicProperties.INTERPRETE) ;
		chefs		= processListeArtistes(Artiste.class, JsonMusicProperties.CHEF) ;
		ensembles	= processListeArtistes(Groupe.class,  JsonMusicProperties.ENSEMBLE) ;
		groupes		= processListeArtistes(Groupe.class,  JsonMusicProperties.GROUPE) ;
	}

	public List<Artiste> getListeAuteurs() 	   { return auteurs ;	  }	
	public List<Artiste> getListeInterpretes() { return interpretes ; }	
	public List<Artiste> getListeChefs() 	   { return chefs ;		  }	
	public List<Artiste> getListeEnsembles()   { return ensembles ;	  }	
	public List<Artiste> getListeGroupes() 	   { return groupes ;     }
	
	private List<Artiste> processListeArtistes(Class<? extends Artiste> cls, String artistesJprop) {
		
		List<Artiste> artistes = new ArrayList<Artiste>() ;
		JsonElement jElem = arteFactJson.get(artistesJprop) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				JsonArray jArtistes = jElem.getAsJsonArray() ;
				
				for (JsonElement jArtiste : jArtistes) {

					try {
						artistes.add(processArtiste(cls, jArtiste.getAsJsonObject())) ;
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
	
	private Artiste processArtiste(Class<? extends Artiste> cls, JsonObject jArtiste) {
		
		Artiste unArtiste ;
		if (cls == Groupe.class) {
			unArtiste = (Groupe)createGetOrUpdateArtiste(cls, jArtiste) ;
		} else {
			unArtiste = createGetOrUpdateArtiste(cls, jArtiste) ;
		}
		return unArtiste ;
	}
	
	// Get an artiste, if it exists, return the existing one eventually updated
	// if it does not exists, create it
	private Artiste createGetOrUpdateArtiste(Class<? extends Artiste> cls, JsonObject jArtiste) {
		
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
