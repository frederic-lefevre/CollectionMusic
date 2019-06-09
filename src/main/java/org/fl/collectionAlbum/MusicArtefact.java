package org.fl.collectionAlbum;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;

import com.google.gson.JsonObject;

public abstract class MusicArtefact {

    // liste des auteurs (artiste ou groupe)
    private List<Artiste> auteurs ;
    private List<Artiste> interpretes ;  
    private List<Artiste> ensembles	;
    private List<Artiste> chefsOrchestres ;
    
    protected List<String> notes;

    protected String artefactHtml ;
    
    protected Logger artefactLog ;
    
    protected JsonObject arteFactJson ;
    
    protected MusicArtefact(JsonObject afj, Logger al) {
    	auteurs 	    = new ArrayList<Artiste>() ;
    	interpretes     = new ArrayList<Artiste>() ;
    	ensembles 	    = new ArrayList<Artiste>() ;
    	chefsOrchestres = new ArrayList<Artiste>() ;
    	notes 		    = new ArrayList<String> () ;
    	
    	artefactLog 	= al ;
    	arteFactJson    = afj ;
    	artefactHtml    = null ; 
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
    public void addNotes(		   List<String>  ns      ) { notes.			 addAll(ns		) ; }
    
    public JsonObject getJson() { return arteFactJson ; }
    
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
