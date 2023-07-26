/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.collectionAlbum.artistes;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.util.json.JsonUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Groupe extends Artiste {
	
	public Groupe(JsonObject jArtiste) {
		super() ;
		
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