/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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
import java.util.Comparator;

import org.fl.collectionAlbum.format.MediaSupportCategories;
import org.fl.collectionAlbum.utils.TemporalUtils;

public class AlbumAcquisitionDateComparator implements Comparator<Album> {

	@Override
	public int compare(Album album1, Album album2) {

		TemporalAccessor date1 = album1.getAcquisitionDate();
		TemporalAccessor date2 = album2.getAcquisitionDate();
		
		if (date1 == null) {
			if (date2 == null) {
				
				boolean album1hasK7 = album1.getFormatAlbum().getSupportsPhysiques().contains(MediaSupportCategories.K7);
				boolean album2hasK7 = album2.getFormatAlbum().getSupportsPhysiques().contains(MediaSupportCategories.K7);
				boolean album1hasCD = album1.getFormatAlbum().getSupportsPhysiques().contains(MediaSupportCategories.CD);
				boolean album2hasCD = album2.getFormatAlbum().getSupportsPhysiques().contains(MediaSupportCategories.CD);
				
				if (album1hasK7 && !album2hasK7) {
					return 1;
				} else if (!album1hasK7 && album2hasK7) {
					return -1;
				} else if (album1hasCD && !album2hasCD) {
					return -1;
				} else if (!album1hasCD && album2hasCD) {
					return 1;
				} else {
					return 0;
				}
			} else {
				return 1;
			}
		} else if (date2 == null) {
			return -1;
		} else {
			return TemporalUtils.getRoundedLocalDate(date2).compareTo(TemporalUtils.getRoundedLocalDate(date1));
		}
	}
}
