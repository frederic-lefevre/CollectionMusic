package org.fl.collectionAlbum;

import java.util.Comparator;

import org.fl.collectionAlbum.artistes.Artiste;
import org.fl.collectionAlbum.artistes.AuteurComparator;

public class PoidsComparator  implements Comparator<Artiste> {
	
	public int compare(Artiste arg0, Artiste arg1) {
		
		double poids0 = arg0.getPoids().getPoids()  ;
		double poids1 = arg1.getPoids().getPoids() ;
		
		int ordreNom = 0 ;
		if (poids0 == poids1) {
			AuteurComparator compAuteur = new AuteurComparator();
			ordreNom = compAuteur.compare(arg0,arg1) ;	
		} else if (poids0 < poids1) {
			ordreNom = 1 ;
		} else if (poids0 > poids1) {
			ordreNom = -1 ;
		}
		return ordreNom ;
	}

}
