/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.time.temporal.TemporalAccessor;
import java.util.logging.Logger;

import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.utils.TemporalUtils;
import org.fl.util.json.JsonUtils;

import com.fasterxml.jackson.databind.JsonNode;

public class Artiste {
	
	private final String nom;
	private final String prenoms;
	private TemporalAccessor naissance;
	private TemporalAccessor mort;
	private final ListeAlbum albums;
	private final ListeConcert concerts;
	private final Set<ArtistRole> artistRoles;
	
	private static final Logger albumLog = Logger.getLogger(Artiste.class.getName());

	public Artiste(JsonNode jArtiste, ArtistRole artisteRole) {

		super();
		albums = ListeAlbum.Builder.getBuilder().build();
		concerts = new ListeConcert();
		artistRoles = new HashSet<>();
		artistRoles.add(artisteRole);
		
		nom = JsonUtils.getAsStringOrBlank(jArtiste.get(JsonMusicProperties.NOM));
		prenoms = getFirstNameOrArticle(jArtiste);

		if (nom.isBlank()) {
			albumLog.warning("Nom d'artiste null");
		}

		if (prenoms.isBlank()) {
			albumLog.info("Prénoms d'artiste null pour le nom " + nom);
		}

		naissance = getTemporalAccessor(JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.NAISSANCE)));
		mort = getTemporalAccessor(JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.MORT)));

		albumLog.finest(() -> "Nouvel artiste: " + nom + " " + prenoms + " (" + getDateNaissance() + " - "
				+ getDateMort() + ")");
	}
	
	public boolean isSameArtiste(JsonNode jArtiste) {

		String lastName = JsonUtils.getAsStringOrBlank(jArtiste.get(JsonMusicProperties.NOM));
		String firstName = getFirstNameOrArticle(jArtiste);
		return (nom.equals(lastName) && prenoms.equals(firstName));
	}

	private String getFirstNameOrArticle(JsonNode jArtiste) {
		if (jArtiste.has(JsonMusicProperties.PRENOM)) {
			return JsonUtils.getAsStringOrBlank(jArtiste.get(JsonMusicProperties.PRENOM));
		} else if (jArtiste.has(JsonMusicProperties.ARTICLE)) {
			return JsonUtils.getAsStringOrBlank(jArtiste.get(JsonMusicProperties.ARTICLE));
		} else {
			return "";
		}
	}
    
	public void update(JsonNode jArtiste, ArtistRole artisteRole) {

		String birth = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.NAISSANCE));
		String death = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.MORT));
		update(birth, death);
		
		artistRoles.add(artisteRole);
	}
	
    private void update(String newNaissance, String newMort) {
    	if ((newNaissance != null) && (! newNaissance.isEmpty())) {
    		if (this.naissance != null) {
    			albumLog.warning("Date de naissance définie 2 fois pour " + prenoms + " " + nom) ;
    		} else {
    			this.naissance = getTemporalAccessor(newNaissance);
    		}
    	}
    	if ((newMort != null) && (! newMort.isEmpty())) {
    		if (this.mort != null) {
    			albumLog.warning("Date de décés définie 2 fois pour " + prenoms + " " + nom) ;
    		} else {
    			 this.mort = getTemporalAccessor(newMort);
    		}
    	}
    }

    private TemporalAccessor getTemporalAccessor(String temporalString) {
    	
    	if ((temporalString == null) || temporalString.isBlank()) {
    		return null;
    	}
    	try {
            return TemporalUtils.parseDate(temporalString);
        } catch (Exception e) {
        	albumLog.severe("Erreur dans les dates de " + prenoms + " " + nom);
        	return null;
        }
    }
    
	public void addArteFact(MusicArtefact musicArtefact) {
		if (musicArtefact instanceof Album album) {
			albums.addAlbum(album);
		} else if (musicArtefact instanceof Concert concert) {
			concerts.addConcert(concert);
		} else {
			albumLog.severe("MusicArteFact n'est ni un Album, ni un concert: " + musicArtefact.getClass().getName());
		}
	}

	public Format getAlbumsFormat() {
		return albums.getFormatListeAlbum();
	}

	public int getNbConcert() {
		return concerts.getNombreConcerts();
	}

	public int getNbAlbum() {
		return albums.getNombreAlbums();
	}

	public String getDateNaissance() {
		return TemporalUtils.formatDate(naissance);
	}

	public String getDateMort() {
		return TemporalUtils.formatDate(mort);
	}

	public ListeAlbum getAlbums() {
		return albums.sortChronoComposition();
	}

	public TemporalAccessor getNaissance() {
		return naissance;
	}

	public TemporalAccessor getMort() {
		return mort;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenoms() {
		return prenoms;
	}

	public Set<ArtistRole> getArtistRoles() {
		return artistRoles;
	}

	public boolean hasRole(ArtistRole artistRole) {
		return artistRoles.contains(artistRole);
	}
	
	public boolean hasAnyRole(ArtistRole... roles) {
		return Arrays.stream(roles).anyMatch(role -> artistRoles.contains(role));
	}
	
	public ListeConcert getConcerts() {
		return concerts;
	}

	public String getNomComplet() {
		if ((getPrenoms() == null) || (getPrenoms().isEmpty())) {
			return getNom();
		} else {
			return getPrenoms() + " " + getNom();
		}
	}

}
