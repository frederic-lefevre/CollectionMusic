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

import java.util.ArrayList;

import java.util.List;
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
	
	protected String nom;
	protected String prenoms;
	protected TemporalAccessor naissance;
	protected TemporalAccessor mort;
	protected List<String> instruments;
	protected ListeAlbum albums;
	protected ListeConcert concerts;
	
	private final static Logger albumLog = Logger.getLogger(Artiste.class.getName());

	public Artiste(JsonNode jArtiste) {

		super();

		String lastName = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.NOM));
		String firstName = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.PRENOM));
		String birth = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.NAISSANCE));
		String death = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.MORT));

		setArtiste(lastName, firstName, birth, death);

		JsonNode jElem = jArtiste.get(JsonMusicProperties.INSTRUMENTS);
		if (jElem != null) {
			if (jElem.isArray()) {
				instruments = new ArrayList<String>();

				for (JsonNode jInstrument : jElem) {
					instruments.add(jInstrument.asText());
				}
			} else {
				albumLog.warning(
						JsonMusicProperties.INSTRUMENTS + " n'est pas un JsonArray pour l'artiste " + jArtiste);
			}
		}

	}
	
	public boolean isSameArtiste(JsonNode jArtiste) {

		String lastName = JsonUtils.getAsStringOrBlank(jArtiste.get(JsonMusicProperties.NOM));
		String firstName = JsonUtils.getAsStringOrBlank(jArtiste.get(JsonMusicProperties.PRENOM));

		return (nom.equals(lastName) && prenoms.equals(firstName));
	}

	public Artiste() {
		super();
		setArtiste("", "", null, null);
	}
    
	protected void setArtiste(String aNom, String aPrenoms, String n, String m) {

		albums = ListeAlbum.Builder.getBuilder().build();
		concerts = new ListeConcert();
		if (aNom == null) {
			albumLog.warning("Nom d'artiste null");
			aNom = "";
		}

		if (aPrenoms == null) {
			albumLog.info("Prénoms d'artiste null pour le nom " + aNom);
			aPrenoms = "";
		}

		nom = aNom;
		prenoms = aPrenoms;

		try {
			naissance = TemporalUtils.parseDate(n);
			mort = TemporalUtils.parseDate(m);
		} catch (Exception e) {
			albumLog.severe("Erreur dans les dates de " + aPrenoms + " " + aNom);
		}

		albumLog.finest(() -> "Nouvel artiste: " + nom + " " + prenoms + " (" + getDateNaissance() + " - "
				+ getDateMort() + ")");
	}
    
    public void update(String n, String m) {
    	if ((n != null) && (! n.isEmpty())) {
    		if (naissance != null) {
    			albumLog.warning("Date de naissance définie 2 fois pour " + prenoms + " " + nom) ;
    		} else {
    			try {
    	            naissance = TemporalUtils.parseDate(n) ;
    	        } catch (Exception e) {
    	        	albumLog.severe("Erreur dans les dates de " + prenoms + " " + nom);
    	        }
    		}
    	}
    	if ((m != null) && (! m.isEmpty())) {
    		if (mort != null) {
    			albumLog.warning("Date de décés définie 2 fois pour " + prenoms + " " + nom) ;
    		} else {
    			try {
    	            mort = TemporalUtils.parseDate(m) ;
    	        } catch (Exception e) {
    	        	albumLog.severe("Erreur dans les dates de " + prenoms + " " + nom);
    	        }
    		}
    	}
    }
    
	public void update(JsonNode jArtiste) {

		String birth = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.NAISSANCE));
		String death = JsonUtils.getAsStringOrNull(jArtiste.get(JsonMusicProperties.MORT));
		update(birth, death);
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

	public List<String> getInstruments() {
		return instruments;
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
