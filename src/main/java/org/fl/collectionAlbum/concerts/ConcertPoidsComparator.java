package org.fl.collectionAlbum.concerts;

import java.util.Comparator;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.AuteurComparator;

public class ConcertPoidsComparator implements Comparator<Artiste> {
	
	public int compare(Artiste arg0, Artiste arg1) {
		
		float poids0 = arg0.getNbConcert()  ;
		float poids1 = arg1.getNbConcert() ;
		
		int ordreNom = 0 ;
		if (poids0 == poids1) {
			ordreNom = (new AuteurComparator()).compare(arg0,arg1) ;	
		} else if (poids0 < poids1) {
			ordreNom = 1 ;
		} else if (poids0 > poids1) {
			ordreNom = -1 ;
		}
		return ordreNom ;
	}
}
