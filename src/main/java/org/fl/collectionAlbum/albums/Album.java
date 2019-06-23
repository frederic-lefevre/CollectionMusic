package org.fl.collectionAlbum.albums;

import java.time.temporal.TemporalAccessor;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.MusicArtefact;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.lge.fl.util.date.FuzzyPeriod;

public class Album extends MusicArtefact {

    private String titre;
    
    private FuzzyPeriod periodeEnregistrement ;
    private FuzzyPeriod periodeComposition ;
    
    private Format formatAlbum;
    
    private boolean specifiedCompositionDate ;
    
    public Album(JsonObject albumJson, Logger aLog) {
    	super(albumJson, aLog) ;
    }
    
    public boolean additionnalInfo() {   
    	return ((notes != null) && (notes.size() > 0)) ;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String t) {
    	titre = t ;
    }
    
	public TemporalAccessor getDebutEnregistrement() {
    	if ((periodeEnregistrement != null) && periodeEnregistrement.isValid()) {
    		return periodeEnregistrement.getDebut() ;
    	} else {
    		return null ;
    	}
    }

    public TemporalAccessor getFinEnregistrement() {
    	if ((periodeEnregistrement != null) && periodeEnregistrement.isValid()) {
    		return periodeEnregistrement.getFin() ;
    	} else {
    		return null ;
    	}
    }


    public TemporalAccessor getDebutComposition() {
    	if ((periodeComposition != null)  && periodeComposition.isValid()) {
    		return periodeComposition.getDebut() ;
    	} else {
    		return null ;
    	}
    }

    public TemporalAccessor getFinComposition() {
    	if ((periodeComposition != null) && periodeComposition.isValid()) {
    		return periodeComposition.getFin() ;
    	} else {
    		return null ;
    	}
    }

    public Format getFormatAlbum() {
    	if (formatAlbum == null) {
            // traitement du format
    		JsonElement jElem = arteFactJson.get(JsonMusicProperties.FORMAT) ;
            if (jElem == null) {
            	artefactLog.warning("Format d'album null pour l'album " + arteFactJson) ;
                formatAlbum = new Format(null, artefactLog) ;
            } else {
            	formatAlbum = new Format(jElem.getAsJsonObject(), artefactLog) ;
            }
    	}
        return formatAlbum;
    }
    
	public String getFullPathHtmlFileName() {
		return "file://" + Control.getAbsoluteAlbumDir() + artefactHtmlName ;
	}


	public boolean isSpecifiedCompositionDate() {
		return specifiedCompositionDate;
	}

	public void setPeriodeEnregistrementEtComposition(FuzzyPeriod pe, FuzzyPeriod pc) {
		periodeEnregistrement = pe;
        if (periodeEnregistrement == null) {
        	artefactLog.warning(" Pas de dates d'enregistrement pour l'album " + arteFactJson);
        }

		periodeComposition = pc;
		specifiedCompositionDate = true ;
        if ((periodeComposition == null) && (periodeEnregistrement != null) && (periodeEnregistrement.isValid())) {   
        	periodeComposition = periodeEnregistrement ;
        	specifiedCompositionDate = false ;
        } 
	}
	
}