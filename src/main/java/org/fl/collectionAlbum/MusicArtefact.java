package org.fl.collectionAlbum;

import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.jsonParsers.MusicArtefactParser;

import com.google.gson.JsonObject;

public abstract class MusicArtefact {

    // liste des auteurs (artiste ou groupe)
    private List<Artiste> auteurs ;
    private List<Artiste> interpretes ;  
    private List<Artiste> ensembles	;
    private List<Artiste> chefsOrchestres ;
    
    protected List<String> notes;

    protected Logger artefactLog ;
    
    protected JsonObject arteFactJson ;
    
    protected MusicArtefact(JsonObject afj, List<ListeArtiste> knownArtistes, Logger al) {
    	artefactLog 	 = al ;
    	arteFactJson     = afj ;
    	
    	auteurs 	     = MusicArtefactParser.getListeAuteurs(	   arteFactJson, knownArtistes, artefactLog) ;
    	interpretes      = MusicArtefactParser.getListeInterpretes(arteFactJson, knownArtistes, artefactLog) ;
    	ensembles 	     = MusicArtefactParser.getListeEnsembles(  arteFactJson, knownArtistes, artefactLog) ;
    	chefsOrchestres  = MusicArtefactParser.getListeChefs(	   arteFactJson, knownArtistes, artefactLog) ;
    	
    	auteurs.addAll(    MusicArtefactParser.getListeGroupes(	   arteFactJson, knownArtistes, artefactLog)) ;
    	
    	notes 		     = MusicArtefactParser.getNotes(arteFactJson, artefactLog) ;    	  	
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
    
    public void addAuteurs(		   List<Artiste> artistes) { auteurs.		 addAll(artistes) ; }
    public void addInterpretes(	   List<Artiste> artistes) { interpretes.	 addAll(artistes) ; }
    public void addEnsembles(	   List<Artiste> artistes) { ensembles.      addAll(artistes) ; }
    public void addChefsOrchestres(List<Artiste> artistes) { chefsOrchestres.addAll(artistes) ; }
    
    public JsonObject getJson() { return arteFactJson ; }
    
	protected abstract boolean additionnalInfo() ;
}
