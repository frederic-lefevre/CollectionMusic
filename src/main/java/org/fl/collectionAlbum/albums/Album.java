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

import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import org.fl.collectionAlbum.Format;
import org.fl.collectionAlbum.Format.ContentNature;
import org.fl.collectionAlbum.Format.RangementSupportPhysique;
import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.jsonParsers.AlbumParser;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.fl.util.date.FuzzyPeriod;

import com.google.gson.JsonObject;

public class Album extends MusicArtefact {

    private final String titre;
    
    private final FuzzyPeriod periodeEnregistrement;
    private FuzzyPeriod periodeComposition;
    
    private final Format formatAlbum;
    private RangementSupportPhysique rangement;
    
    private final boolean specificCompositionDates;
    
    private List<Path> potentialAudioFilesPath;
    private List<Path> potentialVideoFilesPath;
    
	public Album(JsonObject albumJson, List<ListeArtiste> knownArtistes, Path jsonFilePath) {

		super(albumJson, knownArtistes, jsonFilePath);

		potentialAudioFilesPath = null;
		potentialVideoFilesPath = null;
		
		titre = AlbumParser.getAlbumTitre(albumJson);
		formatAlbum = AlbumParser.getFormatAlbum(albumJson);
		periodeEnregistrement = AlbumParser.processPeriodEnregistrement(albumJson);
		periodeComposition = AlbumParser.processPeriodComposition(albumJson);
		rangement = AlbumParser.getRangementAlbum(albumJson);

		if (rangement == null) {
			// pas de rangement spécifique
			rangement = formatAlbum.getRangement();
		}
		specificCompositionDates = (periodeComposition != null);
		if (!specificCompositionDates) {
			periodeComposition = periodeEnregistrement;
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

    public boolean hasMediaFiles() {
    	return formatAlbum.hasMediaFiles();
    }
    
    public boolean hasAudioFiles() {
    	return formatAlbum.hasAudioFiles();
    }
    
    public boolean hasVideoFiles() {
    	return formatAlbum.hasVideoFiles();
    }
    
    public RangementSupportPhysique getRangement() {
        return rangement;
    }
    
	public boolean hasSpecificCompositionDates() {
		return specificCompositionDates;
	}
	
	public boolean missesAudioFile() {
		return (hasContentNature(ContentNature.AUDIO) && (!hasAudioFiles()));
	}
	
	public boolean missesVideoFile() {
		return (hasContentNature(ContentNature.VIDEO) && (!hasVideoFiles()));
	}
	
	public boolean hasOnlyLossLessAudio() {
		return formatAlbum.hasOnlyLossLessAudio();
	}
	
	public boolean hasHighResAudio() {
		return formatAlbum.hasHighResAudio();
	}
	
	public boolean hasContentNature(Format.ContentNature contentNature) {
		return formatAlbum.hasContentNature(contentNature);
	}
	
	public boolean hasMissingOrInvalidMediaFilePath() {
		return formatAlbum.hasMissingOrInvalidMediaFilePath();
	}
	
	public boolean hasMediaFilePathNotFound() {
		return formatAlbum.hasMediaFilePathNotFound();
	}
	
	public List<Path> getPotentialAudioFilesPaths() {
		return potentialAudioFilesPath;
	}
	
	public List<Path> getPotentialVideoFilesPaths() {
		return potentialVideoFilesPath;
	}
	
	public List<Path> searchPotentialAudioFilesPaths() {
		potentialAudioFilesPath = MediaFilesInventories.getAudioFileInventory().getPotentialMediaPath(this);
		return potentialAudioFilesPath;
	}
	
	public List<Path> searchPotentialVideoFilesPaths() {
		potentialVideoFilesPath = MediaFilesInventories.getVideoFileInventory().getPotentialMediaPath(this);
		return potentialVideoFilesPath;
	}
	
    @Override
    public boolean additionnalInfo() {
    	return hasAudioFiles() || hasVideoFiles() || super.additionnalInfo();
    }
}