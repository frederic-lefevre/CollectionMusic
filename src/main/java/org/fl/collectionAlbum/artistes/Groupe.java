package org.fl.collectionAlbum.artistes;

import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ibm.lge.fl.util.json.JsonUtils;

public class Groupe extends Artiste {
	
	public Groupe(JsonObject jArtiste, Logger gl) {
		super(gl) ;
		
		JsonElement jNom    	= jArtiste.get(JsonMusicProperties.NOM) ;
		JsonElement jArticle 	= jArtiste.get(JsonMusicProperties.ARTICLE) ;
		JsonElement jDebut	 	= jArtiste.get(JsonMusicProperties.DEBUT) ;
		JsonElement jFin      	= jArtiste.get(JsonMusicProperties.FIN) ;
		
		String name  	 = JsonUtils.getAsStringOrNull(jNom) ;
		String article 	 = JsonUtils.getAsStringOrNull(jArticle) ;
		String begin 	 = JsonUtils.getAsStringOrNull(jDebut) ;
		String end		 = JsonUtils.getAsStringOrNull(jFin) ;
		
		setArtiste(name, article, begin, end) ;
	}
	
	@Override
	public boolean isSameArtiste(JsonObject jArtiste) {
		
		JsonElement jNom    	= jArtiste.get(JsonMusicProperties.NOM) ;
		JsonElement jArticle 	= jArtiste.get(JsonMusicProperties.ARTICLE) ;
		
		String lastName  = JsonUtils.getAsStringOrBlank(jNom) ;
		String firstName = JsonUtils.getAsStringOrBlank(jArticle) ;
		
		return (nom.equals(lastName) && prenoms.equals(firstName)) ;
	}
	
}