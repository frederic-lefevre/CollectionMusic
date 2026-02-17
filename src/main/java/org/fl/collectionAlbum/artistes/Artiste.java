/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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
	private final String nomComplet;
	private TemporalAccessor naissance;
	private TemporalAccessor mort;
	private String dateNaissance;
	private String dateDeces;
	private String dateNaissanceEtDeces;
	private final ListeAlbum albums;
	private final ListeConcert concerts;
	private final Set<ArtistRole> artistRoles;
	
	private static final Logger albumLog = Logger.getLogger(Artiste.class.getName());

	public Artiste(JsonNode jArtiste, ArtistRole artisteRole) {

		super();
		albums = ListeAlbum.Builder.getBuilder().build();
		concerts = ListeConcert.Builder.getBuilder().build();
		artistRoles = new HashSet<>();
		
		nom = JsonUtils.getAsStringOrBlank(jArtiste.get(JsonMusicProperties.NOM));
		prenoms = getFirstNameOrArticle(jArtiste);

		if (nom.isBlank()) {
			albumLog.warning("Nom d'artiste null");
		}

		if (prenoms.isBlank()) {
			albumLog.fine(() -> "Prénoms d'artiste null pour le nom " + nom);
		}

		if ((prenoms == null) || (prenoms.isBlank())) {
			nomComplet = nom;
		} else {
			nomComplet = prenoms + " " + nom;
		}
		
		dateNaissance = "";
		dateDeces = "";
		dateNaissanceEtDeces = "";
		
		updateArtistRoleAndDates(jArtiste, artisteRole);

		albumLog.finest(() -> "Nouvel artiste: " + getNomCompletEtDates());
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
    
	public void updateArtistRoleAndDates(JsonNode jArtiste, ArtistRole artisteRole) {

		String birth = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.NAISSANCE));
		String death = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.MORT));
		updateDatesNaissanceEtDeces(birth, death);
		
		artistRoles.add(artisteRole);
	}
	
    private void updateDatesNaissanceEtDeces(String newNaissance, String newMort) {

    	if (newMort != null) {
    		if (this.mort != null) {
    			albumLog.warning("Date de décés définie 2 fois pour " + nomComplet);
    		} else if (newMort.isBlank()) {
    			albumLog.warning("Date de décès définie à blanc pour " + nomComplet);
    		} else {
    			 this.mort = getTemporalAccessor(newMort);
    			 dateDeces = TemporalUtils.formatDate(this.mort);
    			 if ((newNaissance == null) || (newNaissance.isBlank())) {
    				 albumLog.warning("Date de décès définie sans date de naissance pour " + nomComplet);
    			 }
    		}
    	}
    	if (newNaissance != null) {
    		if (this.naissance != null) {
    			albumLog.warning("Date de naissance définie 2 fois pour " + nomComplet);
    		} else if (newNaissance.isBlank()) {
    			albumLog.warning("Date de naissance définie à blanc pour " + nomComplet);
    		} else {
    			this.naissance = getTemporalAccessor(newNaissance);
    			dateNaissance = TemporalUtils.formatDate(this.naissance);
    			dateNaissanceEtDeces = " (" + dateNaissance + " - " + dateDeces + ")";
    		}
    	}
    }

    private TemporalAccessor getTemporalAccessor(String temporalString) {
    	
    	try {
            return TemporalUtils.parseDate(temporalString);
        } catch (Exception e) {
        	albumLog.severe("Erreur dans les dates de " + nomComplet);
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
		return dateNaissance;
	}

	public String getDateMort() {
		return dateDeces;
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
		return concerts.sortChrono();
	}

	public String getNomComplet() {
		return nomComplet;
	}

	public String getDates() {
		return dateNaissanceEtDeces;
	}
	
	public String getNomCompletEtDates() {
		return getNomComplet() + getDates();
	}
}
