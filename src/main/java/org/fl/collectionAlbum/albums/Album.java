/*
 MIT License

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

package org.fl.collectionAlbum.albums;

import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fl.collectionAlbum.MusicArtefact;
import org.fl.collectionAlbum.artistes.ListeArtiste;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher;
import org.fl.collectionAlbum.disocgs.DiscogsAlbumReleaseMatcher.ReleaseMatchResult;
import org.fl.collectionAlbum.format.AbstractMediaFile;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.format.Format;
import org.fl.collectionAlbum.format.MediaSupports;
import org.fl.collectionAlbum.format.Format.RangementSupportPhysique;
import org.fl.collectionAlbum.json.AlbumParser;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;
import org.fl.collectionAlbum.utils.FuzzyPeriod;

import com.google.gson.JsonObject;

public class Album extends MusicArtefact {
	
	protected final static Logger albumLog = Logger.getLogger(Album.class.getName());
	
    private final String titre;
    
    private final FuzzyPeriod periodeEnregistrement;
    private final FuzzyPeriod periodeComposition;
    
    private final Format formatAlbum;
    private final RangementSupportPhysique rangement;
    
    private final boolean specificCompositionDates;
    
    private final Path sleevePath;
    
    private final Map<ContentNature, List<MediaFilePath>> potentialMediaFilesPath;
    
	public Album(JsonObject albumJson, List<ListeArtiste> knownArtistes, Path jsonFilePath) {

		super(albumJson, knownArtistes, jsonFilePath);

		potentialMediaFilesPath = new HashMap<>();
		Stream.of(ContentNature.values())
			.forEach(contentNature -> potentialMediaFilesPath.put(contentNature, null));
		
		titre = AlbumParser.getAlbumTitre(albumJson);
		formatAlbum = AlbumParser.getFormatAlbum(albumJson);
		
		periodeEnregistrement = AlbumParser.processPeriodEnregistrement(albumJson);
		periodeComposition = Optional.ofNullable(AlbumParser.processPeriodComposition(albumJson))
				.orElse(periodeEnregistrement);
		specificCompositionDates = (periodeComposition != periodeEnregistrement);
		
		rangement = Optional.ofNullable(AlbumParser.getRangementAlbum(albumJson))
				.orElse(formatAlbum.inferRangement());

		if (rangement == null) {
			albumLog.severe("Pas de rangement trouvé pour l'album " + getTitre());
		}
		
		sleevePath = AlbumParser.getAlbumSleevePath(albumJson);
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
    
    public boolean hasMediaFiles(ContentNature contentNature) {
    	return formatAlbum.hasMediaFiles(contentNature);
    }
    
    public boolean hasAudioFiles() {
    	return formatAlbum.hasMediaFiles(ContentNature.AUDIO);
    }
    
    public boolean hasVideoFiles() {
    	return formatAlbum.hasMediaFiles(ContentNature.VIDEO);
    }
    
    public boolean hasMediaSupport(MediaSupports mediaSupport) {
    	return formatAlbum.hasMediaSupport(mediaSupport);
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
	
	public boolean hasContentNature(ContentNature contentNature) {
		return formatAlbum.hasContentNature(contentNature);
	}
	
	public boolean hasMissingOrInvalidMediaFilePath(ContentNature contentNature) {
		return formatAlbum.hasMissingOrInvalidMediaFilePath(contentNature);
	}
	
	public boolean hasMediaFilePathNotFound(ContentNature contentNature) {
		return formatAlbum.hasMediaFilePathNotFound(contentNature);
	}
	
	public Set<ContentNature> getContentNatures() {
		return formatAlbum.getContentNatures();
	}
	
	public boolean hasProblem() {
		return missesAudioFile() ||
				missesVideoFile() ||
				Stream.of(ContentNature.values())
					.filter(contentNature -> hasContentNature(contentNature))
					.anyMatch(contentNature -> 
						hasMissingOrInvalidMediaFilePath(contentNature) || 
						hasMediaFilePathNotFound(contentNature));
	}
	
	public List<Path> getPotentialMediaFilesPaths(ContentNature contentNature) {
		
		if (potentialMediaFilesPath == null) {
			return null;
		}
		
		List<MediaFilePath> mediaPaths = potentialMediaFilesPath.get(contentNature);
		if (mediaPaths == null) {
			return null;
		} else {
			return mediaPaths.stream()
					.map(mediaFilePath -> mediaFilePath.getPath())
					.collect(Collectors.toList());
		}
		
	}
	
	public List<MediaFilePath> searchPotentialMediaFilesPaths(ContentNature contentNature) {
		
		List<MediaFilePath> potentialMediaPaths = MediaFilesInventories.getMediaFileInventory(contentNature).getPotentialMediaPath(this);
		potentialMediaFilesPath.put(contentNature, potentialMediaPaths);
		return potentialMediaPaths;
	}
	
	public ReleaseMatchResult searchPotentialDiscogsReleases() {
		return DiscogsAlbumReleaseMatcher.getPotentialReleaseMatch(this);
	}
	
	public boolean validatePotentialMediaFilePath(ContentNature contentNature) {		
		return validatePotentialMediaFilePath(potentialMediaFilesPath.get(contentNature), contentNature);
	}
	
	public List<AbstractMediaFile> getAllMediaFiles() {
		return formatAlbum.getAllMediaFiles();
	}
	
	private boolean validatePotentialMediaFilePath(List<MediaFilePath> potentialMediaFilePath, ContentNature contentNature) {
		
		if (potentialMediaFilePath == null) {
			albumLog.severe("Trying to validate " + contentNature.getNom() + " file path with null value for album " + getTitre());
			return false;
		} else if (potentialMediaFilePath.size() == 1) {
			List<? extends AbstractMediaFile> mediaFiles = getFormatAlbum().getMediaFiles(contentNature);
			if ((mediaFiles != null) && !mediaFiles.isEmpty()) {
				if (mediaFiles.size() == 1) {
					Set<MediaFilePath> mediaFilePaths = mediaFiles.get(0).getMediaFilePaths();
					if ((mediaFilePaths != null) && !mediaFilePaths.isEmpty()) {	
						if (mediaFilePaths.size() == 1){
							albumLog.info("Validate " + contentNature.getNom() + " file path with existing " + contentNature.getNom() + " file referenced for album " + getTitre());
						} else {
							albumLog.severe("Trying to validate " + contentNature.getNom() + " file path with multiple existing " + contentNature.getNom() + " files referenced for album " + getTitre());
							return false;
						}
					}
					mediaFiles.get(0).replaceMediaFilePath(potentialMediaFilePath.get(0));
					potentialMediaFilePath = null;
					return true;
					
				} else {
					albumLog.severe("Trying to validate " + contentNature.getNom() + " file path with multiple " + contentNature.getNom() + " files referenced for album " + getTitre());
					return false;
				}
			} else {
				albumLog.severe("Trying to validate " + contentNature.getNom() + " file path with no " + contentNature.getNom() + " files referenced for album " + getTitre());
				return false;
			}
		} else {
			albumLog.severe("Trying to validate " + contentNature.getNom() + " file path with multiple values for album " + getTitre());
			return false;
		}
	}
	
	public Path getCoverImage() {

		if (sleevePath != null) {
			return sleevePath;
		} else if (hasMediaFiles()) {
			return getAllMediaFiles().stream()
					.map(mediaFile -> mediaFile.getMediaFilePaths())
					.filter(Objects::nonNull)
					.flatMap(Collection::stream)
					.map(MediaFilePath::getCoverPath)
					.filter(Objects::nonNull)
					.findFirst()
					.orElse(null);
		} else {
			return null;
		}
	}
	
    @Override
    public boolean additionnalInfo() {
    	return hasAudioFiles() || hasVideoFiles() || super.additionnalInfo();
    }
}