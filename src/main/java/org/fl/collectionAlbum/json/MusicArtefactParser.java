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

package org.fl.collectionAlbum.json;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.JsonMusicProperties;
import org.fl.collectionAlbum.artistes.ArtistRole;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;

import com.fasterxml.jackson.databind.JsonNode;

public class MusicArtefactParser {
	
	private final static Logger albumLog = Logger.getLogger(MusicArtefactParser.class.getName());
	
	private List<ListeArtiste> knownArtistes;
	private JsonNode arteFactJson;

	private ListeArtiste auteurs;
	private ListeArtiste interpretes;
	private ListeArtiste chefs;
	private ListeArtiste ensembles;
	private ListeArtiste groupes;
	
	public MusicArtefactParser(JsonNode j, List<ListeArtiste> currentKnownArtistes) {
		super();

		arteFactJson = j;
		knownArtistes = new ArrayList<ListeArtiste>();
		currentKnownArtistes.forEach(la -> knownArtistes.add(la));

		auteurs = processListeArtistes(ArtistRole.AUTEUR);
		knownArtistes.add(auteurs);
		interpretes = processListeArtistes(ArtistRole.INTERPRETE);
		knownArtistes.add(interpretes);
		chefs = processListeArtistes(ArtistRole.CHEF_ORCHESTRE);
		knownArtistes.add(chefs);
		ensembles = processListeArtistes(ArtistRole.ENSEMBLE);
		knownArtistes.add(ensembles);
		groupes = processListeArtistes(ArtistRole.GROUPE);
		knownArtistes.add(groupes);
	}

	public List<Artiste> getListeAuteurs() {
		return auteurs.getArtistes();
	}

	public List<Artiste> getListeInterpretes() {
		return interpretes.getArtistes();
	}

	public List<Artiste> getListeChefs() {
		return chefs.getArtistes();
	}

	public List<Artiste> getListeEnsembles() {
		return ensembles.getArtistes();
	}

	public List<Artiste> getListeGroupes() {
		return groupes.getArtistes();
	}
	
	private ListeArtiste processListeArtistes(ArtistRole artisteRole) {
		
		String artistesJprop = artisteRole.getJsonProperty();
		ListeArtiste artistes = new ListeArtiste();
		JsonNode jElem = arteFactJson.get(artistesJprop);
		if (jElem != null) {
			if (jElem.isArray()) {
				
				for (JsonNode jArtiste : jElem) {

					try {
						artistes.addArtiste(createGetOrUpdateArtiste(jArtiste, artisteRole)) ;
					} catch (IllegalStateException e) {
						albumLog.log(Level.WARNING, "un artiste n'est pas un objet json pour l'arteFact " + arteFactJson, e);
					}
				}
			} else {
				albumLog.warning(artistesJprop + " n'est pas un tableau json pour l'arteFact " + arteFactJson);
			}
		}
		return artistes;
	}
	
	// Get an artiste, if it exists, return the existing one eventually updated
	// if it does not exists, create it
	private Artiste createGetOrUpdateArtiste(JsonNode jArtiste, ArtistRole artisteRole) {
		
		Artiste artiste;
		Optional<Artiste> eventualArtiste =	knownArtistes.stream()
														  .map(listeArtiste -> listeArtiste.getArtisteKnown(jArtiste))
														  .filter(a -> a.isPresent())
														  .map(a -> a.get())
														  .findFirst();
		
		if (!eventualArtiste.isPresent()) {
			artiste = new Artiste(jArtiste, artisteRole);
		} else {
			artiste = eventualArtiste.get();
			artiste.update(jArtiste, artisteRole);
		}
		return artiste;
	}
	
	
	public String getDisocgs() {
		return ParserHelpers.parseStringProperty(arteFactJson, JsonMusicProperties.DISCOGS, false);
	}
	
	public boolean getDisocgsFormatValidation() {
		return ParserHelpers.parseBooleanProperty(arteFactJson, JsonMusicProperties.DISCOGS_VALID, false);
	}
	
	public List<String> getNotes() {
		return ParserHelpers.getArrayAttribute(arteFactJson, JsonMusicProperties.NOTES);
	}

	public List<URI> getUrlLinks() {		
		return getUrisList(arteFactJson, JsonMusicProperties.LIENS, Control.getMusicartefactInfosUri());
	}

	protected static List<URI> getUrisList(JsonNode arteFactJson, String jsonProperty, String rootUri) {
		return ParserHelpers.getArrayAttribute(arteFactJson, jsonProperty).stream()
				.map(relativeUriString -> getAbsoluteUri(rootUri, relativeUriString, arteFactJson))
				.filter(Objects::nonNull)
				.toList();
	}
	
	private static URI getAbsoluteUri(String rootUri, String relativeDirUriStr, JsonNode arteFactJson) {
		try {
			URI absoluteUri = new URI(rootUri + relativeDirUriStr);
			// check that the file exists
			if (!(Files.exists(Paths.get(absoluteUri)))) {
				albumLog.warning("Le fichier suivant n'existe pas: " + absoluteUri.toString() +  "\nMusicArtefact JSON:\n" + arteFactJson);
			}
			return absoluteUri;
		} catch (Exception e) {
			albumLog.log(Level.SEVERE, "Wrong URI string\nFile relative URI: " + relativeDirUriStr + "\nRoot URI: " + rootUri + "\nMusicArtefact JSON:\n" + arteFactJson, e);
			return null;
		}
	}
	
	public static int getVersion(JsonNode json) {
		
		return Optional.ofNullable(json.get(JsonMusicProperties.JSON_VERSION))
				.map(JsonNode::asInt)
				.orElse(0);

	}
}
