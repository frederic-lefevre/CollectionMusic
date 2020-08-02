package org.fl.collectionAlbum;

import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.jsonParsers.MusicArtefactParser;

import com.google.gson.JsonObject;

public abstract class MusicArtefact {

    // liste des auteurs (artiste ou groupe)
    private final List<Artiste> auteurs ;
    private final List<Artiste> interpretes ;  
    private final List<Artiste> ensembles	;
    private final List<Artiste> chefsOrchestres ;
    
    private final List<String> notes;
    private final List<String> urlLinks;

    protected final Logger artefactLog ;
    
    protected final JsonObject arteFactJson ;
    
    protected MusicArtefact(JsonObject afj, List<ListeArtiste> knownArtistes, Logger al) {
    	artefactLog 	 = al ;
    	arteFactJson     = afj ;
    	
    	MusicArtefactParser musicParser = new MusicArtefactParser(arteFactJson, knownArtistes, artefactLog) ;
    	
    	auteurs 	     = musicParser.getListeAuteurs() ;
    	interpretes      = musicParser.getListeInterpretes() ;
    	ensembles 	     = musicParser.getListeEnsembles() ;
    	chefsOrchestres  = musicParser.getListeChefs() ;
    	
    	auteurs.addAll(    musicParser.getListeGroupes()) ;
    	
    	notes 		     = musicParser.getNotes() ;    	  	
    	urlLinks		 = musicParser.getUrlLinks();
    }
    
	public void addMusicArtfactArtistesToList(ListeArtiste artistes) {
		       
        artistes.addAllArtistes(auteurs, this) ;
        artistes.addAllArtistes(interpretes, this) ;
        artistes.addAllArtistes(chefsOrchestres, this) ;
        artistes.addAllArtistes(ensembles, this) ;   
	}
	
    public List<Artiste> getAuteurs() 		 { return auteurs		  ; }
    public List<Artiste> getInterpretes() 	 { return interpretes	  ; }
    public List<Artiste> getEnsembles() 	 { return ensembles		  ; }
    public List<Artiste> getChefsOrchestre() { return chefsOrchestres ; }        
    public List<String>  getNotes() 		 { return notes			  ; }
    public List<String>  getUrlLinks() 		 { return urlLinks		  ; }
    
    public JsonObject getJson() { return arteFactJson ; }
    
	public boolean additionnalInfo() {
		return ((notes != null) && (notes.size() > 0)) ;
	}
}
