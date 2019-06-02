package org.fl.collectionAlbum;

import java.io.File;
import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.lge.fl.util.date.FuzzyPeriod;

public class Album extends MusicArtefact {

    // titre de l'album
    private String titre;
    
    private FuzzyPeriod periodeEnregistrement ;
    private FuzzyPeriod periodeComposition ;
    
    private Format formatAlbum;
    
    private boolean specifiedCompositionDate ;
    
	public Album(Path srcFile, ListeArtiste listeArtistes, Logger aLog) {
		
		super(srcFile, listeArtistes, aLog) ;

        // traitement du titre
		JsonElement jElem = arteFactJson.get(JsonMusicProperties.TITRE) ;
		if (jElem == null) {
			artefactLog.warning("Titre de l'album null pour l'album " + srcFile);
            titre  = "";
        } else {
        	titre = jElem.getAsString() ;
        }
		if (artefactLog.isLoggable(Level.FINEST)) {
			artefactLog.finest("Titre: " + titre);
		}
		
        // Traitement des dates d'enregistrement
        periodeEnregistrement = processPeriod(arteFactJson, JsonMusicProperties.ENREGISTREMENT) ;
        if (periodeEnregistrement == null) {
        	artefactLog.warning(" Pas de dates d'enregistrement pour l'album " + arteFactJson);
        }
        
        // Traitement des dates de composition
        periodeComposition = processPeriod(arteFactJson, JsonMusicProperties.COMPOSITION) ;
        if (periodeComposition == null) {   
        	periodeComposition = periodeEnregistrement ;
        	specifiedCompositionDate = false ;
        } else {
	        if (periodeComposition.getDebut() == null) {
	        	periodeComposition.setDebut(periodeEnregistrement.getDebut()) ;
	        	 if (periodeComposition.getFin() == null) {
	        		 specifiedCompositionDate = false ;
	        	 } else {
	        		 specifiedCompositionDate = true ;
	        	 }
	        } else if (periodeComposition.getFin() == null) {
	        	periodeComposition.setFin(periodeEnregistrement.getFin()) ;
	        	specifiedCompositionDate = true ;
	        } else {
	        	specifiedCompositionDate = true ;
	        }
        }
	}
	
	
	private FuzzyPeriod processPeriod(JsonObject albumJson, String periodProp) {
		
		FuzzyPeriod period = null ;
		JsonElement jElem = albumJson.get(periodProp) ;
		if (jElem != null) {
			if (!jElem.isJsonArray()) {
				artefactLog.warning(periodProp + " n'est pas un JsonArray pour l'album " + albumJson);
			} else {
				JsonArray jDates = jElem.getAsJsonArray();
				if (jDates.size() != 2) {
					artefactLog.warning(periodProp + " ne contient pas 2 éléments pour l'album " + albumJson);
				} else {

					try {
						String debut = jDates.get(0).getAsString();
						String fin = jDates.get(1).getAsString();

						period = new FuzzyPeriod(Control.parseDate(debut), Control.parseDate(fin), artefactLog) ;
						
						if (! period.isValid()) {
							artefactLog.warning(periodProp + " : Erreur dans les dates de l'album " + albumJson);
						}
					} catch (Exception e) {
						artefactLog.log(Level.SEVERE, periodProp + " : Erreur dans les dates de " + albumJson, e);
					}					
				}
			}
		}
		return period ;
	}
	
	public void generateHtml() {

		if (additionnalInfo()) {

			File htmlFile = new File(Control.getAbsoluteAlbumDir() + artefactHtml) ;
			if (! htmlFile.exists()) {
				RapportHtml rapport = new RapportHtml("", false, htmlFile, null,  "../", artefactLog);

				rapport.enteteRapport(new String[]{RapportHtml.albumStyle}) ;
				if (notes != null) {
					for (String note : notes) {
						rapport.write("<p>").write(note).write("</p>") ;
					}
				}
				rapport.finRapport() ;    			
			}
		}    	
	}
    
    public boolean additionnalInfo() {   
    	return ((notes != null) && (notes.size() > 0)) ;
    }

    public String getTitre() {
        return titre;
    }

	public TemporalAccessor getDebutEnregistrement() {
    	if (periodeEnregistrement != null) {
    		return periodeEnregistrement.getDebut() ;
    	} else {
    		return null ;
    	}
    }

    public TemporalAccessor getFinEnregistrement() {
    	if (periodeEnregistrement != null) {
    		return periodeEnregistrement.getFin() ;
    	} else {
    		return null ;
    	}
    }


    public TemporalAccessor getDebutComposition() {
    	if (periodeComposition != null) {
    		return periodeComposition.getDebut() ;
    	} else {
    		return null ;
    	}
    }

    public TemporalAccessor getFinComposition() {
    	if (periodeComposition != null) {
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
                formatAlbum = new Format(artefactLog) ;
            } else {
            	formatAlbum = new Format(jElem.getAsJsonObject(), artefactLog) ;
            }
    	}
        return formatAlbum;
    }
    
	public String getFullPathHtmlFileName() {
		return "file://" + Control.getAbsoluteAlbumDir() + artefactHtml ;
	}


	public boolean isSpecifiedCompositionDate() {
		return specifiedCompositionDate;
	}
	
}