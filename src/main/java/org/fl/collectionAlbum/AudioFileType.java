package org.fl.collectionAlbum;

public enum AudioFileType {
	
	FLAC {
		
		@Override
        public boolean isLossLess() {
			return true;
		}
	},
	WAV {
		
		@Override
        public boolean isLossLess() {
			return true;
		}
	},
	MP3 {
		
		@Override
        public boolean isLossLess() {
			return false;
		}
	};
	
	public boolean isLossLess() {
		return true;
	}

}
