/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.concerts.Concert;
import org.fl.collectionAlbum.concerts.LieuxDesConcerts;
import org.fl.collectionAlbum.concerts.ListeConcert;
import org.fl.collectionAlbum.disocgs.DiscogsInventory;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupports;
import org.fl.collectionAlbum.stat.StatChrono;

import com.google.gson.JsonObject;

public class CollectionAlbumContainer {

	private final static Logger albumLog = Logger.getLogger(CollectionAlbumContainer.class.getName());
	
	// Liste d'artistes pour les albums
	private ListeArtiste collectionArtistes;
	// Liste d'artistes pour les concerts
	private ListeArtiste concertsArtistes;
	
	private List<ListeArtiste> allArtistes;
	
	// Liste de tous les albums
	private ListeAlbum collectionAlbumsMusiques;
	
	// Listes des albums par rangement
	private EnumMap<Format.RangementSupportPhysique, ListeAlbum> rangementsAlbums;
	
	// Listes des albums par MediaSupports
	private EnumMap<MediaSupports, ListeAlbum> albumsPerMediaSupports;
	
	// Listes des albums avec une seule nature de contenu
	private EnumMap<ContentNature, ListeAlbum> albumsWithOnlyContentNature;
	
	private ListeAlbum albumsWithMixedContentNature;
	
	private ListeAlbum albumWithAudioFile;
	private ListeAlbum albumMissingAudioFile;
	private ListeAlbum albumWithVideoFile;
	private ListeAlbum albumMissingVideoFile;
	private ListeAlbum albumWithHighResAudio;
	private ListeAlbum albumWithLowResAudio;
	
	private ListeAlbum albumWithDiscogsRelease;
	private ListeAlbum albumMissingDiscogsRelease;
	
	private ListeConcert concerts;	
	private ChronoArtistes calendrierArtistes;
	
	private StatChrono statChronoEnregistrement;
	private StatChrono statChronoComposition;
	
	private LieuxDesConcerts lieuxDesConcerts ;
	
	private static CollectionAlbumContainer collectionAlbumContainer;
	
	public static CollectionAlbumContainer getEmptyInstance() {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer();
		}
		collectionAlbumContainer.reset();
		return collectionAlbumContainer;
	}
	
	public static CollectionAlbumContainer getInstance() {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer();
			collectionAlbumContainer.reset();
		}
		return collectionAlbumContainer;
	}
	
	private CollectionAlbumContainer() {		
	}

	public void addAlbum(JsonObject arteFactJson, Path jsonFile) {
		
		Album album = new Album(arteFactJson, allArtistes, jsonFile);
		
		album.addMusicArtfactArtistesToList(collectionArtistes);
		
		// Add the album to each media files that it references
		album.getAllMediaFiles().stream()
			.map(mediaFile -> mediaFile.getMediaFilePaths())
			.filter(Objects::nonNull)
			.flatMap(mediaFileList -> mediaFileList.stream())
			.forEach(mediaFile -> mediaFile.addAlbum(album));
		
		collectionAlbumsMusiques.addAlbum(album);
				
		Format.RangementSupportPhysique rangement = album.getRangement();
		if (rangement != null) {
			rangementsAlbums.get(rangement).addAlbum(album);
		} else {
			albumLog.warning("Album impossible à ranger: " + album.getTitre());
		}
			
		Arrays.stream(MediaSupports.values())
			.filter(mediaSupport -> album.hasMediaSupport(mediaSupport))
			.forEach(mediaSupport -> albumsPerMediaSupports.get(mediaSupport).addAlbum(album));
		
		Set<ContentNature> contentNatures = album.getContentNatures();
		if ((contentNatures == null) || contentNatures.isEmpty()) {
			albumLog.warning("Album sans nature de contenu trouvé : " + album.getTitre());
		} else if (contentNatures.size() == 1) {
			albumsWithOnlyContentNature.get(contentNatures.iterator().next()).addAlbum(album);
		} else {
			albumsWithMixedContentNature.addAlbum(album);
		}
		
		if (album.missesAudioFile()) {
			albumMissingAudioFile.addAlbum(album);
		} else if (album.hasAudioFiles()){
			albumWithAudioFile.addAlbum(album);
		}
		
		if (album.missesVideoFile()) {
			albumMissingVideoFile.addAlbum(album);
		} else if (album.hasVideoFiles()){
			albumWithVideoFile.addAlbum(album);
		}
		
		if (album.hasHighResAudio()) {
			albumWithHighResAudio.addAlbum(album);
		}
		if (album.hasAudioFiles() && !album.hasOnlyLossLessAudio()) {
			albumWithLowResAudio.addAlbum(album);
		}
		
		// Add the album to the discogs inventory if a discogs release is referenced
		if (album.hasDiscogsRelease()) {
			DiscogsInventory.linkToAlbum(album.getDiscogsLink(), album);
			albumWithDiscogsRelease.addAlbum(album);
		} else {
			albumMissingDiscogsRelease.addAlbum(album);
		}
		
		statChronoEnregistrement.AddAlbum(album.getDebutEnregistrement(), album.getFormatAlbum().getPoids());
	    statChronoComposition.AddAlbum(album.getDebutComposition(), album.getFormatAlbum().getPoids());
	}
	
	public void addConcert(JsonObject arteFactJson, Path jsonFile) { 
		
		Concert concert = new Concert(arteFactJson, allArtistes, lieuxDesConcerts, jsonFile);
		
		concert.getLieuConcert().addConcert(concert);
		concert.addMusicArtfactArtistesToList(concertsArtistes);
		
		concerts.addConcert(concert); 	
	}
	
	public ListeAlbum getRangementAlbums(Format.RangementSupportPhysique sPhys) {
		return rangementsAlbums.get(sPhys);
	}
	
	public ListeAlbum getAlbumsWithMediaSupport(MediaSupports mediaSupport) {
		return albumsPerMediaSupports.get(mediaSupport);
	}
	
	public ListeAlbum getAlbumsWithOnlyContentNature(ContentNature contentNature) {
		return albumsWithOnlyContentNature.get(contentNature);
	}
		
	public ListeArtiste     getCollectionArtistes() 	  	  { return collectionArtistes			; }
	public ListeAlbum 	  	getCollectionAlbumsMusiques() 	  { return collectionAlbumsMusiques 	; }
	public ListeArtiste   	getConcertsArtistes() 		  	  { return concertsArtistes		 		; }
	public ListeConcert   	getConcerts() 				  	  { return concerts				 		; }
	public ChronoArtistes 	getCalendrierArtistes() 	  	  { return calendrierArtistes			; }
	public StatChrono 	  	getStatChronoComposition() 	  	  { return statChronoComposition		; }
	public StatChrono 	  	getStatChronoEnregistrement() 	  { return statChronoEnregistrement 	; }
	public LieuxDesConcerts getLieuxDesConcerts() 		  	  { return lieuxDesConcerts				; }
	public ListeAlbum 	  	getAlbumsWithAudioFile() 	  	  { return albumWithAudioFile 			; }
	public ListeAlbum 	  	getAlbumsMissingAudioFile()   	  { return albumMissingAudioFile 		; }
	public ListeAlbum 	  	getAlbumsWithVideoFile() 	  	  { return albumWithVideoFile 			; }
	public ListeAlbum 	  	getAlbumsMissingVideoFile()   	  { return albumMissingVideoFile 		; }
	public ListeAlbum 	  	getAlbumsWithHighResAudio()   	  { return albumWithHighResAudio 		; }
	public ListeAlbum 	  	getAlbumsWithLowResAudio() 	  	  { return albumWithLowResAudio 		; }
	public ListeAlbum 	  	getAlbumsWithMixedContentNature() { return albumsWithMixedContentNature	; }
	public ListeAlbum 	  	getAlbumsWithDiscogsRelease() 	  { return albumWithDiscogsRelease 		; }
	public ListeAlbum 	  	getAlbumsMissingDiscogsRelease()  { return albumMissingDiscogsRelease 	; }

	private void reset() {
		
   		collectionAlbumsMusiques 	 = new ListeAlbum();
		collectionArtistes 		 	 = new ListeArtiste();
   		concertsArtistes 		 	 = new ListeArtiste();   		
   		concerts 				 	 = new ListeConcert(); 		
   		statChronoEnregistrement 	 = new StatChrono();
   		statChronoComposition 	 	 = new StatChrono();   		
   		calendrierArtistes 		 	 = new ChronoArtistes();
   		lieuxDesConcerts		 	 = new LieuxDesConcerts();
   		allArtistes				 	 = new ArrayList<ListeArtiste>();
   		albumWithAudioFile		 	 = new ListeAlbum();
   		albumMissingAudioFile	 	 = new ListeAlbum();
   		albumWithVideoFile		 	 = new ListeAlbum();
   		albumMissingVideoFile	 	 = new ListeAlbum();
   		albumWithHighResAudio	 	 = new ListeAlbum();
   		albumWithLowResAudio	 	 = new ListeAlbum();
   		albumsWithMixedContentNature = new ListeAlbum();
   		albumWithDiscogsRelease	 	 = new ListeAlbum();
   		albumMissingDiscogsRelease   = new ListeAlbum();
   		rangementsAlbums 		 	 = new EnumMap<Format.RangementSupportPhysique, ListeAlbum>(Format.RangementSupportPhysique.class);
   		albumsPerMediaSupports	 	 = new EnumMap<MediaSupports, ListeAlbum>(MediaSupports.class);
   		albumsWithOnlyContentNature  = new EnumMap<ContentNature, ListeAlbum>(ContentNature.class);

   		Arrays.stream(Format.RangementSupportPhysique.values())
   			.forEach(rangement -> rangementsAlbums.put(rangement, new ListeAlbum()));
   		Arrays.stream(MediaSupports.values())
   			.forEach(mediaSupport -> albumsPerMediaSupports.put(mediaSupport, new ListeAlbum()));
   		Arrays.stream(ContentNature.values())
   			.forEach(contentNature -> albumsWithOnlyContentNature.put(contentNature, new ListeAlbum()));
   		
   		allArtistes.add(collectionArtistes);
   		allArtistes.add(concertsArtistes);
	}
	
	public Artiste getArtisteKnown(String nom, String prenom) {
		
		Artiste a = collectionArtistes.getArtisteKnown(nom, prenom) ;
		if (a == null) {
			a = concertsArtistes.getArtisteKnown(nom, prenom) ;
		}
		return a ;
		
	}
	
}
