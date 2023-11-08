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

package org.fl.collectionAlbum;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.jsonParsers.MusicArtefactParser;
import org.fl.util.json.JsonUtils;

import com.google.gson.JsonObject;

public abstract class MusicArtefact {

	protected final static Logger mLog = Control.getAlbumLog();
	
	// liste des auteurs (artiste ou groupe)
	private final List<Artiste> auteurs;
	private final List<Artiste> interpretes;
	private final List<Artiste> ensembles;
	private final List<Artiste> chefsOrchestres;

	// Additional information (optional)
	private final List<String> notes;
	private final List<String> urlLinks;
	private final String discogsLink;
	private final boolean hasAdditionalInfo;

	protected final JsonObject arteFactJson;
	private final Path jsonFilePath;

	protected MusicArtefact(JsonObject afj, List<ListeArtiste> knownArtistes, Path jsonFilePath) {
		
		arteFactJson = afj;
		this.jsonFilePath = jsonFilePath;

		MusicArtefactParser musicParser = new MusicArtefactParser(arteFactJson, knownArtistes);

		auteurs = musicParser.getListeAuteurs();
		interpretes = musicParser.getListeInterpretes();
		ensembles = musicParser.getListeEnsembles();
		chefsOrchestres = musicParser.getListeChefs();

		auteurs.addAll(musicParser.getListeGroupes());

		notes = musicParser.getNotes();
		urlLinks = musicParser.getUrlLinks();
		discogsLink = musicParser.getDisocgs();
		
		hasAdditionalInfo = 
				((notes != null) && (! notes.isEmpty())) ||
				((urlLinks != null) && (! urlLinks.isEmpty()))  ||
				((discogsLink != null) && (! discogsLink.isEmpty()));
	}

	public void addMusicArtfactArtistesToList(ListeArtiste artistes) {

		artistes.addAllArtistes(auteurs, this);
		artistes.addAllArtistes(interpretes, this);
		artistes.addAllArtistes(chefsOrchestres, this);
		artistes.addAllArtistes(ensembles, this);
	}

	public List<Artiste> getAuteurs() {
		return auteurs;
	}

	public List<Artiste> getInterpretes() {
		return interpretes;
	}

	public List<Artiste> getEnsembles() {
		return ensembles;
	}

	public List<Artiste> getChefsOrchestre() {
		return chefsOrchestres;
	}

	public List<String> getNotes() {
		return notes;
	}

	public List<String> getUrlLinks() {
		return urlLinks;
	}

	public String getDiscogsLink() {
		return discogsLink;
	}

	public JsonObject getJson() {
		return arteFactJson;
	}

	public boolean additionnalInfo() {
		return hasAdditionalInfo;
	}
	
	public boolean hasIntervenant() {
		return (notEmpty(getChefsOrchestre())) || 
			   (notEmpty(getInterpretes()))    || 
			   (notEmpty(getEnsembles()));
		}
	
	private static boolean notEmpty(List<?> l) {
		return (l != null) && (! l.isEmpty());
	}
	
	public void writeJson() {
		
		try (BufferedWriter buff = Files.newBufferedWriter(jsonFilePath, Control.getCharset())) {
			
			buff.write(JsonUtils.jsonPrettyPrint(arteFactJson)) ;
			mLog.fine(() -> "Ecriture du fichier json: " + jsonFilePath);

		} catch (Exception e) {			
			mLog.log(Level.SEVERE,"Erreur dans l'écriture du fichier json" + jsonFilePath, e) ;
		}
	}
}
