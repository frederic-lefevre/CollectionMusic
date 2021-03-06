package org.fl.collectionAlbum.artistes;

import java.util.ArrayList;

import java.util.List;
import java.time.temporal.TemporalAccessor;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.utils.TemporalUtils;
import org.fl.util.json.JsonUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Artiste {
	
	protected String 		   nom ;
	protected String 		   prenoms ;
	protected TemporalAccessor naissance;
	protected TemporalAccessor mort ;
	protected List<String> 	   instruments;
	protected ListeAlbum 	   albums ;
	protected ListeConcert 	   concerts ;
	
	private Logger artisteLog;

	public Artiste(JsonObject jArtiste, Logger al) {
		
		super();
    	artisteLog = al ;
    	
		JsonElement jNom    	= jArtiste.get(JsonMusicProperties.NOM) ;
		JsonElement jPrenom 	= jArtiste.get(JsonMusicProperties.PRENOM) ;
		JsonElement jNaissance 	= jArtiste.get(JsonMusicProperties.NAISSANCE) ;
		JsonElement jMort      	= jArtiste.get(JsonMusicProperties.MORT) ;
		
		String lastName  = JsonUtils.getAsStringOrNull(jNom) ;
		String firstName = JsonUtils.getAsStringOrNull(jPrenom) ;
		String birth 	 = JsonUtils.getAsStringOrNull(jNaissance) ;
		String death	 = JsonUtils.getAsStringOrNull(jMort) ;
		
		setArtiste(lastName, firstName, birth, death) ;
		
		JsonElement jElem = jArtiste.get(JsonMusicProperties.INSTRUMENTS) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				instruments = new ArrayList<String>() ;
				JsonArray jInstruments = jElem.getAsJsonArray() ; 
				for (JsonElement jInstrument : jInstruments) {
					instruments.add(jInstrument.getAsString()) ;
				}
			} else {
				artisteLog.warning(JsonMusicProperties.INSTRUMENTS + " n'est pas un JsonArray pour l'artiste " + jArtiste) ;
			}
		}	
		
	}
	
	public boolean isSameArtiste(JsonObject jArtiste) {
		
		JsonElement jNom    = jArtiste.get(JsonMusicProperties.NOM) ;
		JsonElement jPrenom = jArtiste.get(JsonMusicProperties.PRENOM) ;
		
		String lastName  = JsonUtils.getAsStringOrBlank(jNom) ;
		String firstName = JsonUtils.getAsStringOrBlank(jPrenom) ;
		
		return (nom.equals(lastName) && prenoms.equals(firstName)) ;
	}
	
    public Artiste(Logger al) {
		super();
    	artisteLog = al ;
		setArtiste("", "", null, null) ;
    }
    
    protected void setArtiste(String aNom, String aPrenoms, String n, String m) {
		
    	albums  	 = new ListeAlbum(artisteLog) ;
    	concerts  	 = new ListeConcert(artisteLog) ;
		if (aNom == null) {
			artisteLog.warning("Nom d'artiste null") ;
			aNom = "" ;
		}
		
		if (aPrenoms == null) {
			artisteLog.info("Pr??noms d'artiste null pour le nom " + aNom) ;
			aPrenoms = "" ;
		}

		nom 	= aNom ;
		prenoms = aPrenoms ;
		
		try {
            naissance = TemporalUtils.parseDate(n) ;
            mort 	  = TemporalUtils.parseDate(m) ;
        } catch (Exception e) {
        	artisteLog.severe("Erreur dans les dates de " + aPrenoms + " " + aNom);
        }

		artisteLog.finest(() -> "Nouvel artiste: " + nom + " " + prenoms + " (" + getDateNaissance() + " - " + getDateMort() + ")") ;
	}
    
    public void update(String n, String m) {
    	if ((n != null) && (! n.isEmpty())) {
    		if (naissance != null) {
    			artisteLog.warning("Date de naissance d??finie 2 fois pour " + prenoms + " " + nom) ;
    		} else {
    			try {
    	            naissance = TemporalUtils.parseDate(n) ;
    	        } catch (Exception e) {
    	        	artisteLog.severe("Erreur dans les dates de " + prenoms + " " + nom);
    	        }
    		}
    	}
    	if ((m != null) && (! m.isEmpty())) {
    		if (mort != null) {
    			artisteLog.warning("Date de d??c??s d??finie 2 fois pour " + prenoms + " " + nom) ;
    		} else {
    			try {
    	            mort = TemporalUtils.parseDate(m) ;
    	        } catch (Exception e) {
    	        	artisteLog.severe("Erreur dans les dates de " + prenoms + " " + nom);
    	        }
    		}
    	}
    }
    
    public void update(JsonObject jArtiste) {
    	
		JsonElement jNaissance 	= jArtiste.get(JsonMusicProperties.NAISSANCE) ;
		JsonElement jMort      	= jArtiste.get(JsonMusicProperties.MORT) ;

		String birth = JsonUtils.getAsStringOrNull(jNaissance) ;
		String death = JsonUtils.getAsStringOrNull(jMort) ;
		update(birth, death) ;
    }
        
    public void addArteFact(MusicArtefact musicArtefact) {
    	if (musicArtefact instanceof Album) {
    		albums.addAlbum((Album)musicArtefact) ;
    	} else if (musicArtefact instanceof Concert) {
    		concerts.addConcert((Concert)musicArtefact) ;
    	} else {
    		artisteLog.severe("MusicArteFact n'est ni un Album, ni un concert: " + musicArtefact.getClass().getName());
    	}
    }
    
    public Format 			getAlbumsFormat()		 { return albums.getFormatListeAlbum() 		  ; }
    public int 	  			getNbConcert() 	 		 { return concerts.getNombreConcerts() 		  ; }
    public int 	  			getNbAlbum() 		 	 { return albums.getNombreAlbums() 			  ; }
    public String 			getDateNaissance() 		 { return TemporalUtils.formatDate(naissance) ; }
	public String 			getDateMort() 	 		 { return TemporalUtils.formatDate(mort) 	  ;	}
	public ListeAlbum 		getAlbums() 			 { return albums.sortChronoComposition()	  ; }	
	public TemporalAccessor getNaissance() 	 		 { return naissance   						  ; }   
	public TemporalAccessor getMort() 	   	 		 { return mort		  						  ; }
	public String 			getNom() 	   	 		 { return nom		  						  ; }
	public String 			getPrenoms()   	 		 { return prenoms	  						  ; }
	public List<String> 	getInstruments() 		 { return instruments 						  ; }
	public ListeConcert 	getConcerts() 	 		 { return concerts	  						  ;	}

}
