package org.fl.collectionAlbum;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.albums.Album;
import org.fl.collectionAlbum.albums.AlbumCompositionComparator;
import org.fl.collectionAlbum.albums.AlbumEnregistrementComparator;
import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.AuteurComparator;

import java.util.Collections;

public class RangementComparator  implements Comparator<Album> {

	private Logger log ;
	
	public RangementComparator(Logger l) {
		log = l ;
	}
	
	public int compare(Album arg0, Album arg1) {
		
		List<Artiste> l0 = arg0.getAuteurs() ;
		List<Artiste> l1 = arg1.getAuteurs() ;
		
		if ((l0 == null) || (l0.size() == 0)) {
		// no author for album a0
			if ((l1 == null) || (l1.size() == 0)) {
			// no author for album a1 (and a0). Compare lexicographically the title
				String t0 = arg0.getTitre() ; 
				String t1 = arg1.getTitre() ;
				return t0.compareTo(t1) ;
			} else {
			    // a1 is before
				return 1 ;
			}
		} 
		
		if ((l1 == null) || (l1.size() == 0)) return -1 ;
		//	no author for album a1: a0 is before
		
		// For the 2 albums, get the author who has the highest number of albums
		PoidsComparator compPoids = new PoidsComparator();
		Collections.sort(l0, compPoids) ;
		Collections.sort(l1, compPoids) ;
		
		Artiste art0 = l0.get(0) ;
		Artiste art1 = l1.get(0) ;
		
		// Compare lexicographically the 2 authors;
		int autComp = (new AuteurComparator()).compare(art0,art1) ;
		
		if (autComp == 0) {
		// same author for the 2 albums
		    
		    // Compare the composition dates
		    int albComp = (new AlbumCompositionComparator(log)).compare(arg0, arg1) ;
		    if (albComp == 0) {
		    // same composition dates: compare the recording dates
		        // return the oldest album (recording)
				return (new AlbumEnregistrementComparator(log)).compare(arg0, arg1) ;
		    } else {
		        // return the oldest album (composition)
		        return albComp ;
		    }
		} else {
		    // return the first author lexicographically
			return autComp ;
		}
    }
}
