package org.fl.collectionAlbum;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.Groupe;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.stat.StatChrono;
import org.fl.collectionAlbum.utils.TemporalUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ibm.lge.fl.util.date.FuzzyPeriod;

public class CollectionAlbumContainer {

	// Liste d'artistes pour les albums
	private ListeArtiste collectionArtistes;
	// Liste d'artistes pour les concerts
	private ListeArtiste concertsArtistes ;
	
	// Liste de tous les albums
	private ListeAlbum collectionAlbumsMusiques;
	
	// Listes des albums par rangement
	private EnumMap<Format.RangementSupportPhysique, ListeAlbum> rangementsAlbums ;	
	
	private ListeConcert   concerts ;	
	private ChronoArtistes calendrierArtistes ;
	
	private StatChrono statChronoEnregistrement ;
	private StatChrono statChronoComposition ;
	
	private Logger albumLog ;
	
	private static CollectionAlbumContainer collectionAlbumContainer ;
	
	public static CollectionAlbumContainer getEmptyInstance(Logger aLog) {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer(aLog) ;
		}
		collectionAlbumContainer.reset() ;
		return collectionAlbumContainer ;
	}
	
	public static CollectionAlbumContainer getInstance(Logger aLog) {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer(aLog) ;
		}
		return collectionAlbumContainer ;
	}
	
	private CollectionAlbumContainer(Logger aLog) {		
		albumLog = aLog;	
	}

	public void addAlbum(JsonObject arteFactJson) {
		
		Album album = new Album(arteFactJson, albumLog) ;
		setAlbumTitre(album) ;
		FuzzyPeriod periodeEnregistrement = processPeriod(arteFactJson, JsonMusicProperties.ENREGISTREMENT) ;
		FuzzyPeriod periodeComposition 	  = processPeriod(arteFactJson, JsonMusicProperties.COMPOSITION) ;
		album.setPeriodeEnregistrementEtComposition(periodeEnregistrement, periodeComposition) ;
		
		processNewMusicArtfact(album, collectionArtistes) ;
		
		collectionAlbumsMusiques.addAlbum(album) ;
				
		Format.RangementSupportPhysique rangement = album.getFormatAlbum().getRangement() ;
		if (rangement != null) {
			rangementsAlbums.get(rangement).addAlbum(album) ;
		} else {
			albumLog.warning("Album impossible à ranger: " + album.getTitre()) ;
		}
			
		statChronoEnregistrement.AddAlbum(album.getDebutEnregistrement(), album.getFormatAlbum().getPoids());
	    statChronoComposition.AddAlbum(album.getDebutComposition(), album.getFormatAlbum().getPoids());
	}
	
	public void addConcert(JsonObject arteFactJson) { 
		
		Concert concert = new Concert(arteFactJson, albumLog) ;
		
		processNewMusicArtfact(concert, concertsArtistes) ;
		processConcert(concert) ;
		
		concerts.addConcert(concert) ; 
		
	}
	
	private void processNewMusicArtfact(MusicArtefact musicArtefact, ListeArtiste artistes) {
		
	    // Traitement des artistes auteurs,  interpretes, chefs d'orchestre,  ensembles, des auteurs groupes
        List<Artiste> auteurs 		  = processListeArtistes(Artiste.class, musicArtefact, JsonMusicProperties.AUTEUR	) ;
        List<Artiste> interpretes 	  = processListeArtistes(Artiste.class, musicArtefact, JsonMusicProperties.INTERPRETE) ; 
        List<Artiste> chefsOrchestres = processListeArtistes(Artiste.class, musicArtefact, JsonMusicProperties.CHEF		) ;
        List<Artiste> ensembles 	  = processListeArtistes(Groupe.class,  musicArtefact, JsonMusicProperties.ENSEMBLE	) ;
        List<Artiste> groupes		  = processListeArtistes(Groupe.class,  musicArtefact, JsonMusicProperties.GROUPE	) ;
        List<String> notes			  = processNotes(musicArtefact) ;
         
        artistes.addAllArtistes(auteurs) ;
        artistes.addAllArtistes(interpretes) ;
        artistes.addAllArtistes(chefsOrchestres) ;
        artistes.addAllArtistes(ensembles) ;
        artistes.addAllArtistes(groupes) ;
        
        musicArtefact.addAuteurs(auteurs) ;
        musicArtefact.addInterpretes(interpretes) ;
        musicArtefact.addChefsOrchestres(chefsOrchestres) ;
        musicArtefact.addEnsembles(ensembles);
        musicArtefact.addAuteurs(groupes);
        musicArtefact.addNotes(notes);     
	}
	
	private void setAlbumTitre(Album album) {
		
		JsonElement jElem = album.getJson().get(JsonMusicProperties.TITRE) ;
		if (jElem == null) {
			albumLog.warning("Titre de l'album null pour l'album " + album.getJson());
            album.setTitre("") ;
        } else {
        	album.setTitre(jElem.getAsString()) ;
        }
		if (albumLog.isLoggable(Level.FINEST)) {
			albumLog.finest("Titre: " + album.getTitre());
		}
	}
	
	private FuzzyPeriod processPeriod(JsonObject albumJson, String periodProp) {
		
		FuzzyPeriod period = null ;
		JsonElement jElem = albumJson.get(periodProp) ;
		if (jElem != null) {
			if (!jElem.isJsonArray()) {
				albumLog.warning(periodProp + " n'est pas un JsonArray pour l'album " + albumJson);
			} else {
				JsonArray jDates = jElem.getAsJsonArray();
				if (jDates.size() != 2) {
					albumLog.warning(periodProp + " ne contient pas 2 éléments pour l'album " + albumJson);
				} else {

					try {
						String debut = jDates.get(0).getAsString();
						String fin = jDates.get(1).getAsString();

						period = new FuzzyPeriod(TemporalUtils.parseDate(debut), TemporalUtils.parseDate(fin), albumLog) ;
						
						if (! period.isValid()) {
							albumLog.warning(periodProp + " : Erreur dans les dates de l'album " + albumJson);
						}
					} catch (Exception e) {
						albumLog.log(Level.SEVERE, periodProp + " : Erreur dans les dates de " + albumJson, e);
					}					
				}
			}
		}
		return period ;
	}
	
	private List<Artiste> processListeArtistes(Class<? extends Artiste> cls, MusicArtefact musicArteFact, String artistesJprop) {
		
		List<Artiste> artistes = new ArrayList<Artiste>() ;
		JsonObject arteFactJson = musicArteFact.getJson() ;
		JsonElement jElem = arteFactJson.get(artistesJprop) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				JsonArray jArtistes = jElem.getAsJsonArray() ;
				
				for (JsonElement jArtiste : jArtistes) {

					try {
						artistes.add(processArtiste(cls, musicArteFact, jArtiste.getAsJsonObject())) ;
					} catch (IllegalStateException e) {
						albumLog.log(Level.WARNING, "un artiste n'est pas un objet json pour l'arteFact " + arteFactJson, e) ;
					}
				}
			} else {
				albumLog.warning(artistesJprop + " n'est pas un tableau json pour l'arteFact " + arteFactJson) ;
			}
		}
		return artistes ;
	}
	
	private Artiste processArtiste(Class<? extends Artiste> cls, MusicArtefact musicArteFact, JsonObject jArtiste) {
		
		Artiste unArtiste ;
		if (cls == Groupe.class) {
			unArtiste = (Groupe)createGetOrUpdateArtiste(cls, jArtiste) ;
		} else {
			unArtiste = createGetOrUpdateArtiste(cls, jArtiste) ;
		}
		unArtiste.addArteFact(musicArteFact) ;
		return unArtiste ;
	}
	
	private List<String> processNotes(MusicArtefact musicArteFact) {

		List<String> notes = new ArrayList<String>() ;
		JsonObject arteFactJson = musicArteFact.getJson() ;
        JsonElement jElem = arteFactJson.get(JsonMusicProperties.NOTES) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {
				notes = new ArrayList<String>() ;
				JsonArray jNotes = jElem.getAsJsonArray() ; 
				for (JsonElement jNote : jNotes) {
					notes.add(jNote.getAsString()) ;
				}
				if (albumLog.isLoggable(Level.FINEST)) {
					albumLog.finest("Nombre de note: " + notes.size()) ;
				}
			} else {
				albumLog.warning(JsonMusicProperties.NOTES + " n'est pas un JsonArray pour l'artefact " + arteFactJson) ;
			}
		}
		return notes ;
	}
	
	private void processConcert(Concert concert) {

		JsonObject arteFactJson = concert.getJson() ;
		JsonElement jElem ;

		// Traitement de la date
		TemporalAccessor dateConcert = null ;
		jElem = arteFactJson.get(JsonMusicProperties.DATE) ;
		if (jElem == null) {
			albumLog.warning("Pas de date dans le concert " + arteFactJson);
		} else {
			try {
				dateConcert = TemporalUtils.parseDate(jElem.getAsString()) ;
			} catch (Exception e) {
				albumLog.log(Level.SEVERE, "Erreur dans les dates du concert " + arteFactJson, e) ;
			}
		}
		concert.setDateConcert(dateConcert) ;

		// traitement du lieu
		String lieuConcert = null ;
		jElem = arteFactJson.get(JsonMusicProperties.LIEU) ;
		if (jElem == null) {
			albumLog.warning("Pas de lieu dans le concert " + arteFactJson);
		} else {
			lieuConcert = jElem.getAsString() ;
		}
		concert.setLieuConcert(lieuConcert) ;

		// traitement de l'url d'information
		String urlInfos = null ;
		jElem = arteFactJson.get(JsonMusicProperties.URL_INFOS) ;
		if (jElem != null) {
			urlInfos = jElem.getAsString() ;
		}
		concert.setUrlInfos(urlInfos) ;

		Type listType = new TypeToken<List<String>>() {}.getType();

		// Traitement des titres
		List<String> titres = null;
		jElem = arteFactJson.get(JsonMusicProperties.MORCEAUX) ;
		if (jElem != null) {
			if (jElem.isJsonArray()) {				
				JsonArray jTitres = jElem.getAsJsonArray() ;			
				titres = new Gson().fromJson(jTitres, listType);
			} else {
				albumLog.warning(JsonMusicProperties.MORCEAUX + " n'est pas un JsonArray pour le concert " + arteFactJson) ;
			}
		}
		concert.setTitres(titres) ;

		// Traitement des images des tickets
		List<String> ticketImages = null ;
		jElem = arteFactJson.get(JsonMusicProperties.TICKET_IMG) ;
		if (jElem == null) {
			albumLog.info("Pas de ticket pour le concert " + arteFactJson) ;
		} else {
			if (jElem.isJsonArray()) {
				JsonArray jTickets = jElem.getAsJsonArray() ; 
				ticketImages =  new Gson().fromJson(jTickets,  listType);
			} else {
				albumLog.warning(JsonMusicProperties.TICKET_IMG + " n'est pas un JsonArray pour le concert " + arteFactJson) ;
			}
		}  
		concert.setTicketImages(ticketImages) ;
	}

	public ListeAlbum getRangementAlbums(Format.RangementSupportPhysique sPhys) { return rangementsAlbums.get(sPhys) ; }
	
	public ListeArtiste   getCollectionArtistes() 		{ return collectionArtistes		 ; }
	public ListeAlbum 	  getCollectionAlbumsMusiques() { return collectionAlbumsMusiques; }
	public ListeArtiste   getConcertsArtistes() 		{ return concertsArtistes		 ; }
	public ListeConcert   getConcerts() 				{ return concerts				 ; }
	public ChronoArtistes getCalendrierArtistes() 		{ return calendrierArtistes		 ; }
	public StatChrono 	  getStatChronoComposition() 	{ return statChronoComposition	 ; }
	public StatChrono 	  getStatChronoEnregistrement() { return statChronoEnregistrement; }
	
	private void reset() {
		
   		collectionAlbumsMusiques = new ListeAlbum(albumLog) ;
		collectionArtistes 		 = new ListeArtiste(albumLog) ;
   		concertsArtistes 		 = new ListeArtiste(albumLog) ;   		
   		concerts 				 = new ListeConcert(albumLog) ; 		
   		statChronoEnregistrement = new StatChrono(albumLog) ;
   		statChronoComposition 	 = new StatChrono(albumLog) ;   		
   		calendrierArtistes 		 = new ChronoArtistes() ;
   		rangementsAlbums 		 = new EnumMap<Format.RangementSupportPhysique, ListeAlbum>(Format.RangementSupportPhysique.class) ;
   		for (Format.RangementSupportPhysique rangement : Format.RangementSupportPhysique.values()) {
   			rangementsAlbums.put(rangement, new ListeAlbum(albumLog)) ;
   		}
	}
	
	public Artiste getArtisteKnown(String nom, String prenom) {
		
		Artiste a = collectionArtistes.getArtisteKnown(nom, prenom) ;
		if (a == null) {
			a = concertsArtistes.getArtisteKnown(nom, prenom) ;
		}
		return a ;
		
	}

	// Get an artiste, if it exists, return the existing one eventually updated
	// if it does not exists, create it
	public Artiste createGetOrUpdateArtiste(Class<? extends Artiste> cls, JsonObject jArtiste) {
		
		Artiste artiste;
		Optional<Artiste> eventualArtiste = collectionArtistes.getArtisteKnown(jArtiste) ;
		if (! eventualArtiste.isPresent()) {
			eventualArtiste = concertsArtistes.getArtisteKnown(jArtiste) ;
		} 
		
		if (! eventualArtiste.isPresent()) {
			if (cls == Groupe.class) {
				artiste = new Groupe(jArtiste, albumLog) ;
			} else {
				artiste = new Artiste(jArtiste, albumLog) ;
			}
		} else {
			artiste = eventualArtiste.get() ;
			artiste.update(jArtiste);
		}
		return artiste ;
	}
	
}
