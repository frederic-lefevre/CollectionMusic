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

package org.fl.collectionAlbum.albums;

import java.util.Comparator;
import java.util.stream.Stream;

import org.fl.collectionAlbum.RangementComparator;
import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class AlbumMediaFilesStatusComparator implements Comparator<Album> {
	
	private static class MediaFilesStatusCounters {
		
		long noFile;  // Abnormal : an album without corresponding media file(s)
		long noMedia;  // Normal : an album without this type of content (no video content for instance)
		long missingOrNotFoundOrInvalid; // Abnormal : an album missing a reference to the media file or referencing a media file invalid or not found
		long mediaOk; // Normal : an album references a valid media file
		long mediaFileInventoryDisconnected; // Abnormal : no access to media files	
		
		MediaFilesStatusCounters() {
			noFile = 0;
			noMedia = 0;
			missingOrNotFoundOrInvalid = 0;
			mediaOk = 0;
			mediaFileInventoryDisconnected = 0;
		}
		
		static MediaFilesStatusCounters getMediaFilesStatusCounters(Album album) {
			
			MediaFilesStatusCounters counters = new MediaFilesStatusCounters();
			
			Stream.of(ContentNature.values()).forEachOrdered(contentNature -> {
				if (!album.hasContentNature(contentNature)) {
					counters.noMedia++;
				} else if (album.hasMediaFiles(contentNature)) {
					 if (! MediaFilesInventories.getMediaFileInventory(contentNature).isConnected()) {
						 counters.mediaFileInventoryDisconnected++;					
					 } else if (album.hasMediaFilePathNotFound(contentNature) ||
								album.hasMissingOrInvalidMediaFilePath(contentNature)) {
						 counters.missingOrNotFoundOrInvalid++;
					 } else {
						 counters.mediaOk++;
					 }
				} else {
					counters.noFile++;
				}
			});
			return counters;
		}		
	}
	
	private static final RangementComparator rangementComparator = new RangementComparator();
	
	@Override
	public int compare(Album o1, Album o2) {
		
		MediaFilesStatusCounters counters1 = MediaFilesStatusCounters.getMediaFilesStatusCounters(o1);
		MediaFilesStatusCounters counters2 = MediaFilesStatusCounters.getMediaFilesStatusCounters(o2);
		
		if (Long.compare(counters1.mediaFileInventoryDisconnected, counters2.mediaFileInventoryDisconnected) != 0) {
			return Long.compare(counters1.mediaFileInventoryDisconnected, counters2.mediaFileInventoryDisconnected) ;
		} else if (Long.compare(counters1.missingOrNotFoundOrInvalid, counters2.missingOrNotFoundOrInvalid) != 0) {
			return Long.compare(counters1.missingOrNotFoundOrInvalid, counters2.missingOrNotFoundOrInvalid);
		} else if (Long.compare(counters1.noFile, counters2.noFile) != 0) {
			return Long.compare(counters1.noFile, counters2.noFile);
		} else if (Long.compare(counters1.mediaOk, counters2.mediaOk) != 0) {
			return Long.compare(counters1.mediaOk, counters2.mediaOk);
		} else if (Long.compare(counters1.noMedia, counters2.noMedia) != 0) {
			return Long.compare(counters1.noMedia, counters2.noMedia);
		} else {
			return rangementComparator.compare(o1, o2);
		}
	}
}
