/*
 MIT License

Copyright (c) 2017, 2022 Frederic Lefevre

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

package org.fl.collectionAlbum.albums;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.Format.RangementSupportPhysique;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.jsonParsers.AlbumParser;
import org.fl.util.date.FuzzyPeriod;

import com.google.gson.JsonObject;

public class Album extends MusicArtefact {

    private final String titre;
    
    private final FuzzyPeriod periodeEnregistrement ;
    private FuzzyPeriod periodeComposition ;
    
    private final Format formatAlbum;
    private RangementSupportPhysique rangement;
    
    private final boolean specificCompositionDates ;
    
    public Album(JsonObject albumJson, List<ListeArtiste> knownArtistes, Logger aLog) {
    	super(albumJson, knownArtistes, aLog) ;
    	
    	titre 				  = AlbumParser.getAlbumTitre(albumJson, aLog) ;
    	formatAlbum 		  = AlbumParser.getFormatAlbum(albumJson, aLog) ;    	
    	periodeEnregistrement = AlbumParser.processPeriodEnregistrement(albumJson, aLog);        
		periodeComposition 	  = AlbumParser.processPeriodComposition(   albumJson, aLog);
		rangement			  = AlbumParser.getRangementAlbum(albumJson, aLog);
		
		if (rangement == null) {
			// pas de rangement spécifique
			rangement = formatAlbum.getRangement();
		}
		specificCompositionDates = (periodeComposition != null);
        if (!specificCompositionDates) {   
        	periodeComposition = periodeEnregistrement ;
        }
    }
    
    public String getTitre() {
        return titre;
    }

	public TemporalAccessor getDebutEnregistrement() {
    	if ((periodeEnregistrement != null) && periodeEnregistrement.isValid()) {
    		return periodeEnregistrement.getDebut() ;
    	} else {
    		return null ;
    	}
    }

    public TemporalAccessor getFinEnregistrement() {
    	if ((periodeEnregistrement != null) && periodeEnregistrement.isValid()) {
    		return periodeEnregistrement.getFin() ;
    	} else {
    		return null ;
    	}
    }

    public TemporalAccessor getDebutComposition() {
    	if ((periodeComposition != null)  && periodeComposition.isValid()) {
    		return periodeComposition.getDebut() ;
    	} else {
    		return null ;
    	}
    }

    public TemporalAccessor getFinComposition() {
    	if ((periodeComposition != null) && periodeComposition.isValid()) {
    		return periodeComposition.getFin() ;
    	} else {
    		return null ;
    	}
    }

    public Format getFormatAlbum() {
        return formatAlbum;
    }


    public RangementSupportPhysique getRangement() {
        return rangement;
    }
    
	public boolean hasSpecificCompositionDates() {
		return specificCompositionDates;
	}
	
}