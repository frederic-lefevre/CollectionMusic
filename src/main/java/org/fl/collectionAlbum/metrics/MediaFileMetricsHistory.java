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

package org.fl.collectionAlbum.metrics;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.fl.collectionAlbum.format.ContentNature;
import org.fl.collectionAlbum.mediaPath.MediaFilesInventories;

public class MediaFileMetricsHistory extends MetricsHistory {

	private static final String METRIC_NAME = "Evolution des fichiers media";
	
	private static final String NB_AUDIO_PATH = "nombreAudioPath";
	private static final String NB_AUDIO_FILE = "nombreAudioFile";
	private static final String NB_VIDEO_PATH = "nombreVideoPath";
	private static final String NB_VIDEO_FILE = "nombreVideoFile";
	
	// Singleton
	private static MediaFileMetricsHistory mediaFileMetricsHistory;
	
	public static MediaFileMetricsHistory buildMediaFileMetricsHistory(Path storagePath) throws IOException {
		
		if (mediaFileMetricsHistory == null) {
			mediaFileMetricsHistory = new MediaFileMetricsHistory(storagePath);
		}
		return mediaFileMetricsHistory;
	}
	
	public Metrics addPresentMediaFileMetricsToHistory(long ts) {
		return addPresentMetricsToHistory(getMediaFileMetrics(ts));
	}
	
	public void setPresentMetricsIfNew(long ts) {
		setPresentMetricsIfNew(getMediaFileMetrics(ts));
	}
	
	private Metrics getMediaFileMetrics(long ts) {	

		return new Metrics(ts, 		Map.of(
				NB_AUDIO_PATH, (double)MediaFilesInventories.getMediaFileInventory(ContentNature.AUDIO).getNbMediaPath(),
				NB_AUDIO_FILE, (double)MediaFilesInventories.getMediaFileInventory(ContentNature.AUDIO).getMediaFileNumber(),
				NB_VIDEO_PATH, (double)MediaFilesInventories.getMediaFileInventory(ContentNature.VIDEO).getNbMediaPath(),
				NB_VIDEO_FILE, (double)MediaFilesInventories.getMediaFileInventory(ContentNature.VIDEO).getMediaFileNumber()));
	}
	
	private MediaFileMetricsHistory(Path storagePath) throws IOException {
		super(storagePath, METRIC_NAME);
	}

	@Override
	public MetricAttributesList getMetricsAttributes() {
		return new MetricAttributesList(List.of(
				new MetricAttributes(NB_AUDIO_PATH, "Nombre de dossiers audio", 300), 
				new MetricAttributes(NB_AUDIO_FILE, "Nombre de fichiers audio", 300), 
				new MetricAttributes(NB_VIDEO_PATH, "Nombre de dossiers video", 300), 
				new MetricAttributes(NB_VIDEO_FILE, "Nombre de fichiers video", 300)));
	}

}
