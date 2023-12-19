/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

package org.fl.collectionAlbum.mediaPath;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.fl.collectionAlbum.RangementComparator;
import org.fl.collectionAlbum.albums.Album;

public class MediaFilePathAlbumComparator implements Comparator<MediaFilePath> {

	private final RangementComparator albumComparator;
	
	public MediaFilePathAlbumComparator() {
		albumComparator = new RangementComparator();
	}

	@Override
	public int compare(MediaFilePath o1, MediaFilePath o2) {
		
		Set<Album> as1 = o1.getAlbumSet();
		Set<Album> as2 = o2.getAlbumSet();
		
		if ((as1 == null) || (as1.isEmpty())) {
			if ((as2 == null) || (as2.isEmpty())) {
				return 0;
			} else {
				return -1;
			}
		} else if ((as2 == null) || (as2.isEmpty())) {
			return 1;
		} else {
			TreeSet<Album> ts1 = new TreeSet<>(albumComparator);
			ts1.addAll(as1);
			
			TreeSet<Album> ts2 = new TreeSet<>(albumComparator);
			ts2.addAll(as2);
			
			return albumComparator.compare(ts1.first(), ts2.first());
		}
		
	}

}
