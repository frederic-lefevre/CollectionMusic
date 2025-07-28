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

package org.fl.collectionAlbum;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.ListeAlbum;
import org.fl.collectionAlbum.artistes.ArtistRole;
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

import com.fasterxml.jackson.databind.node.ObjectNode;

public class CollectionAlbumContainer {
	
	private static final Logger albumLog = Logger.getLogger(CollectionAlbumContainer.class.getName());
	
	// Liste d'artistes pour les albums
	private final ListeArtiste collectionArtistes;
	
	// Liste d'artistes pour les concerts
	private final ListeArtiste concertsArtistes;
	
	private final List<ListeArtiste> allArtistes;
	
	// Liste de tous les albums
	private final ListeAlbum collectionAlbumsMusiques;
	
	private final ListeConcert concerts;	
	private final ChronoArtistes calendrierAlbumArtistes;
	private final ChronoArtistes calendrierConcertArtistes;
	
	private final StatChrono statChronoEnregistrement;
	private final StatChrono statChronoComposition;
	
	private final LieuxDesConcerts lieuxDesConcerts ;
	
	private static CollectionAlbumContainer collectionAlbumContainer;
	
	public static CollectionAlbumContainer getEmptyInstance() {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer();
		} else {
			collectionAlbumContainer.reset();
		}
		return collectionAlbumContainer;
	}
	
	public static CollectionAlbumContainer getInstance() {
		
		if (collectionAlbumContainer == null) {
			collectionAlbumContainer = new CollectionAlbumContainer();
		}
		return collectionAlbumContainer;
	}
	
	private CollectionAlbumContainer() {
		
		collectionAlbumsMusiques = ListeAlbum.Builder.getBuilder().build();
		collectionArtistes = new ListeArtiste();
		concertsArtistes = new ListeArtiste();
		concerts = new ListeConcert();
		statChronoEnregistrement = new StatChrono();
		statChronoComposition = new StatChrono();
		calendrierAlbumArtistes = new ChronoArtistes();
		calendrierConcertArtistes = new ChronoArtistes();
		lieuxDesConcerts = new LieuxDesConcerts();
		allArtistes = new ArrayList<ListeArtiste>();
   		
   		allArtistes.add(collectionArtistes);
   		allArtistes.add(concertsArtistes);
	}

	public void addAlbum(ObjectNode arteFactJson, Path jsonFile) {
		
		Album album = new Album(arteFactJson, allArtistes, jsonFile);
		
		album.addMusicArtfactArtistesToList(collectionArtistes);
		
		// Add the album to each media files that it references
		album.getAllMediaFiles().stream()
			.map(mediaFile -> mediaFile.getMediaFilePaths())
			.filter(Objects::nonNull)
			.flatMap(mediaFileList -> mediaFileList.stream())
			.forEachOrdered(mediaFile -> mediaFile.addAlbum(album));
		
		collectionAlbumsMusiques.addAlbum(album);
		
		// Add the album to the discogs inventory if a discogs release is referenced
		if (album.hasDiscogsRelease()) {
			DiscogsInventory.linkToAlbum(album.getDiscogsLink(), album);
		} 
		
		statChronoEnregistrement.addAlbum(album.getDebutEnregistrement(), album.getFormatAlbum().getPoids());
	    statChronoComposition.addAlbum(album.getDebutComposition(), album.getFormatAlbum().getPoids());
	}
	
	public void addConcert(ObjectNode arteFactJson, Path jsonFile) { 
		
		Concert concert = new Concert(arteFactJson, allArtistes, lieuxDesConcerts, jsonFile);
		
		concert.getLieuConcert().addConcert(concert);
		concert.addMusicArtfactArtistesToList(concertsArtistes);
		
		concerts.addConcert(concert); 	
	}
	
	public void buildCalendriers() {
		
		try {
			collectionArtistes.getArtistes().forEach(artist -> calendrierAlbumArtistes.add(artist));
			concertsArtistes.getArtistes().forEach(artist -> calendrierConcertArtistes.add(artist));
		} catch (Exception e) {
    		albumLog.log(Level.SEVERE, "Exception in build calendrier ", e);
    	}
	}
	
	private ListeAlbum getAlbumsSastisfying(Predicate<Album> albumPredicate) {
		 return ListeAlbum.Builder.getBuilder()
				 .from(collectionAlbumsMusiques.getAlbums())
				 .withAlbumSatisfying(albumPredicate)
				 .build();
	}
	
	public ListeAlbum getRangementAlbums(Format.RangementSupportPhysique sPhys) {
		return getAlbumsSastisfying(album -> album.getRangement() == sPhys);
	}
	
	public ListeAlbum getAlbumsWithMediaSupport(MediaSupports mediaSupport) {
		return getAlbumsSastisfying(album -> album.hasMediaSupport(mediaSupport));
	}
	
	public ListeAlbum getAlbumsWithOnlyContentNature(ContentNature contentNature) {
		
		return getAlbumsSastisfying(
				album -> ((album.hasContentNature(contentNature)) &&
						(album.getContentNatures().size() == 1)));
	}
		
	public ListeAlbum getAlbumsWithMixedContentNature() {
		return getAlbumsSastisfying(
				album -> ((album.getContentNatures() != null) && 
						(album.getContentNatures().size() > 1) ));
	}
	
	public ListeArtiste getCollectionArtistes() {
		return collectionArtistes;
	}

	public ListeAlbum getCollectionAlbumsMusiques() {
		return collectionAlbumsMusiques;
	}

	public ListeArtiste getConcertsArtistes() {
		return concertsArtistes;
	}

	public ListeConcert getConcerts() {
		return concerts;
	}

	public ChronoArtistes getCalendrierAlbumArtistes() {
		return calendrierAlbumArtistes;
	}

	public ChronoArtistes getCalendrierConcertArtistes() {
		return calendrierConcertArtistes;
	}
	
	public StatChrono getStatChronoComposition() {
		return statChronoComposition;
	}

	public StatChrono getStatChronoEnregistrement() {
		return statChronoEnregistrement;
	}

	public LieuxDesConcerts getLieuxDesConcerts() {
		return lieuxDesConcerts;
	}
	
	public ListeAlbum getAlbumsWithAudioFile() {
		 return getAlbumsSastisfying(Album::hasAudioFiles);
	}

	public ListeAlbum getAlbumsMissingAudioFile() {
		return getAlbumsSastisfying(Album::missesAudioFile);
	}

	public ListeAlbum getAlbumsWithVideoFile() {
		return getAlbumsSastisfying(Album::hasVideoFiles);
	}

	public ListeAlbum getAlbumsMissingVideoFile() {
		return getAlbumsSastisfying(Album::missesVideoFile);
	}

	public ListeAlbum getAlbumsWithHighResAudio() {
		return getAlbumsSastisfying(Album::hasHighResAudio);
	}

	public ListeAlbum getAlbumsWithLowResAudio() {
		return getAlbumsSastisfying(album -> (album.hasAudioFiles() && !album.hasOnlyLossLessAudio()));
	}

	public ListeAlbum getAlbumsWithDiscogsRelease() {
		return getAlbumsSastisfying(Album::hasDiscogsRelease);
	}

	public ListeAlbum getAlbumsMissingDiscogsRelease() {
		return getAlbumsSastisfying(Predicate.not(Album::hasDiscogsRelease));
	}

	public ListeAlbum getAlbumsWithNoArtiste() {
		return getAlbumsSastisfying(Predicate.not(Album::hasArtiste));
	}
	
	private void reset() {
		
   		collectionAlbumsMusiques.reset();
		collectionArtistes.reset();
   		concertsArtistes.reset();   		
   		concerts.reset(); 		
   		statChronoEnregistrement.reset();
   		statChronoComposition.reset();  		
   		calendrierAlbumArtistes.reset();
   		calendrierConcertArtistes.reset();
   		lieuxDesConcerts.reset();
   		
   		allArtistes.clear();
   		allArtistes.add(collectionArtistes);
   		allArtistes.add(concertsArtistes);
	}
	
	public Artiste getArtisteKnown(String nom, String prenom) {
		
		Artiste a = collectionArtistes.getArtisteKnown(nom, prenom);
		if (a == null) {
			a = concertsArtistes.getArtisteKnown(nom, prenom);
		}
		return a;		
	}
	
	public List<Album> pickRandomAlbums(int nbAlbum) {
		return RandomAlbumPicker.pickRandomAlbums(collectionAlbumsMusiques, nbAlbum);
	}
	
	public List<Album> pickRandomAlbumsViaArtiste(int nbAlbum) {
		
		if (collectionAlbumsMusiques.getNombreAlbums() <= nbAlbum) {
			return collectionAlbumsMusiques.getAlbums();
		} else {
			return RandomAlbumPicker.pickRandomAlbumsViaArtiste(
					collectionArtistes.getArtistesSatisfying(artist -> artist.hasAnyRole(ArtistRole.AUTEUR, ArtistRole.GROUPE)), nbAlbum);
		}
	}
}
