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

package org.fl.collectionAlbum.format;

import java.util.Collections;
import java.util.Set;

public enum AudioFileType {
	
	FLAC {
		
		@Override
        public boolean isLossLess() {
			return true;
		}
		
		@Override
		public Set<String> getExtensions() {
			return Set.of("flac", "FLAC");
		}
	},
	WAV {
		
		@Override
        public boolean isLossLess() {
			return true;
		}
		
		@Override
		public Set<String> getExtensions() {
			return Set.of("wav", "WAV");
		}
	},
	AIFF {
		
		@Override
        public boolean isLossLess() {
			return true;
		}
		
		@Override
		public Set<String> getExtensions() {
			return Set.of("aiff", "AIFF");
		}
	},
	MP3 {
		
		@Override
        public boolean isLossLess() {
			return false;
		}
		
		@Override
		public Set<String> getExtensions() {
			return Set.of("mp3", "MP3");
		}
	},
	M4A {
		
		@Override
        public boolean isLossLess() {
			return false;
		}
		
		@Override
		public Set<String> getExtensions() {
			return Set.of("m4a", "M4A");
		}
	};
	
	public boolean isLossLess() {
		return true;
	}

	public Set<String> getExtensions() {
		return Collections.emptySet();
	}
}
