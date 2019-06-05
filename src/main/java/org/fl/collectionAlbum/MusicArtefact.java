package org.fl.collectionAlbum;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.Groupe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.lge.fl.util.json.JsonUtils;

public abstract class MusicArtefact {

    // liste des auteurs (artiste ou groupe)
    private List<Artiste> auteurs ;
    private List<Artiste> interpretes ;  
    private List<Artiste> ensembles	;
    private List<Artiste> chefsOrchestre ;
    
    protected List<String> notes;

    protected String artefactHtml ;
    
    protected Logger artefactLog ;
    
    protected JsonObject arteFactJson ;
    
	protected MusicArtefact(Path srcFile, ListeArtiste listeArtistes, Logger al) {
		
		artefactLog = al ;
		
		arteFactJson = JsonUtils.getJsonObjectFromPath(srcFile, Control.getCharset(), artefactLog) ;
		
       // Traitement des interpretes
		interpretes = new ArrayList<Artiste>() ;
        processListeArtiste(Artiste.class, JsonMusicProperties.INTERPRETE, interpretes,    listeArtistes) ;
 
        // Traitement des chefs d'orchestre
        chefsOrchestre = new ArrayList<Artiste>() ;
        processListeArtiste(Artiste.class, JsonMusicProperties.CHEF, 	   chefsOrchestre, listeArtistes) ;
        
        // Traitement des artistes auteurs
        auteurs = new ArrayList<Artiste>() ;
        processListeArtiste(Artiste.class, JsonMusicProperties.AUTEUR, 	   auteurs,		   listeArtistes) ;

        // Traitement des ensembles
        ensembles = new ArrayList<Artiste>() ;
        processListeArtiste(Groupe.class,  JsonMusicProperties.ENSEMBLE,   ensembles, 	   listeArtistes) ;
       
        // Traitement des auteurs groupes
        processListeArtiste(Groupe.class,  JsonMusicProperties.GROUPE, 	   auteurs, 	   listeArtistes) ;
        
        // Traitement des notes
        JsonElement jElem = arteFactJson.get(JsonMusicProperties.NOTES) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				notes = new ArrayList<String>() ;
				JsonArray jNotes = jElem.getAsJsonArray() ; 
				for (JsonElement jNote : jNotes) {
					notes.add(jNote.getAsString()) ;
				}
				if (artefactLog.isLoggable(Level.FINEST)) {
					artefactLog.finest("Nombre de note: " + notes.size()) ;
				}
			} else {
				artefactLog.warning(JsonMusicProperties.NOTES + " n'est pas un JsonArray pour l'artefact " + arteFactJson) ;
			}
		}

		artefactHtml = null ;     
	}

	private void processListeArtiste(Class<? extends Artiste> cls, String artistesJprop, List<Artiste> artistes, ListeArtiste listeArtistes) {

		JsonElement jElem = arteFactJson.get(artistesJprop) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				JsonArray jArtistes = jElem.getAsJsonArray() ;
				
				for (JsonElement jArtiste : jArtistes) {

					try {
						processArtiste(cls, jArtiste.getAsJsonObject(), artistes, listeArtistes) ;
					} catch (IllegalStateException e) {
						artefactLog.log(Level.WARNING, "un artiste n'est pas un objet json pour l'arteFact " + arteFactJson, e) ;
					}
				}
			} else {
				artefactLog.warning(artistesJprop + " n'est pas un tableau json pour l'arteFact " + arteFactJson) ;
			}
		}
	}
	
	private void processArtiste(Class<? extends Artiste> cls, JsonObject jArtiste, List<Artiste> artistes, ListeArtiste listeArtistes) {
				
		Artiste unArtiste ;
		if (cls == Groupe.class) {
			unArtiste = (Groupe)Control.getCollectionContainer().createGetOrUpdateArtiste(cls, jArtiste) ;
		} else {
			unArtiste = Control.getCollectionContainer().createGetOrUpdateArtiste(cls, jArtiste) ;
		}
		
		listeArtistes.addArtiste(unArtiste);

		unArtiste.addArteFact(this);
		artistes.add(unArtiste);
	}
	
	
    public List<Artiste> getAuteurs() {
        return auteurs;
    }

    public List<Artiste> getInterpretes() {
        return interpretes;
    }

    public List<Artiste> getEnsembles() {
        return ensembles;
    }

    public List<Artiste> getChefsOrchestre() {
        return chefsOrchestre;
    }
        
    public List<String> getNotes() {
		return notes;
	}
    
	public int setHtmlName(int id) {
		if ((artefactHtml == null) && (additionnalInfo())) {
			artefactHtml = "i" + id + ".html";
    		return id + 1 ;
        } else {
        	return id ;
        }
	}
	
	protected abstract boolean additionnalInfo() ;
}
