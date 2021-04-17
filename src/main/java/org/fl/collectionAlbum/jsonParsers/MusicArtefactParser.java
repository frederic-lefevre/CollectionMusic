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
	
	private ListeArtiste auteurs ;
	private ListeArtiste interpretes ;
	private ListeArtiste chefs ;
	private ListeArtiste ensembles ;
	private ListeArtiste groupes ;
	
	public MusicArtefactParser(JsonObject j, List<ListeArtiste> currentKnownArtistes, Logger l) {
		super();
		
		arteFactJson  = j ;
		mLog 		  = l ;
		knownArtistes = new ArrayList<ListeArtiste>() ;
		currentKnownArtistes.stream().forEach(la -> knownArtistes.add(la));
		
		auteurs 	= processListeArtistes(Artiste.class, JsonMusicProperties.AUTEUR) ;
		knownArtistes.add(auteurs) ;
		interpretes = processListeArtistes(Artiste.class, JsonMusicProperties.INTERPRETE) ;
		knownArtistes.add(interpretes) ;
		chefs		= processListeArtistes(Artiste.class, JsonMusicProperties.CHEF) ;
		knownArtistes.add(chefs) ;
		ensembles	= processListeArtistes(Groupe.class,  JsonMusicProperties.ENSEMBLE) ;
		knownArtistes.add(ensembles) ;
		groupes		= processListeArtistes(Groupe.class,  JsonMusicProperties.GROUPE) ;
		knownArtistes.add(groupes) ;
	}

	public List<Artiste> getListeAuteurs() 	   { return auteurs.getArtistes() ;	  }	
	public List<Artiste> getListeInterpretes() { return interpretes.getArtistes() ; }	
	public List<Artiste> getListeChefs() 	   { return chefs.getArtistes() ;		  }	
	public List<Artiste> getListeEnsembles()   { return ensembles.getArtistes() ;	  }	
	public List<Artiste> getListeGroupes() 	   { return groupes.getArtistes() ;     }
	
	private ListeArtiste processListeArtistes(Class<? extends Artiste> cls, String artistesJprop) {
		
		ListeArtiste artistes = new ListeArtiste(mLog) ;
		JsonElement jElem = arteFactJson.get(artistesJprop) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				JsonArray jArtistes = jElem.getAsJsonArray() ;
				
				for (JsonElement jArtiste : jArtistes) {

					try {
						artistes.addArtiste(createGetOrUpdateArtiste(cls, jArtiste.getAsJsonObject())) ;
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
		return getArrayAttribute(JsonMusicProperties.NOTES);
	}

	public List<String> getUrlLinks() {		
		return getArrayAttribute(JsonMusicProperties.LIENS);
	}
	
	private List<String> getArrayAttribute(String jsonMusicProperty) {

        JsonElement jElem = arteFactJson.get(jsonMusicProperty) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				List<String> result = new ArrayList<String>() ;
				JsonArray jArray = jElem.getAsJsonArray() ; 
				for (JsonElement e : jArray) {
					result.add(e.getAsString()) ;
				}
				mLog.finest(() -> "Nombre de " + jsonMusicProperty + " " + result.size()) ;
				return result ;
			} else {
				mLog.warning(jsonMusicProperty + " n'est pas un JsonArray pour l'artefact " + arteFactJson) ;
			}
		}
		return new ArrayList<String>() ;
	}
}
