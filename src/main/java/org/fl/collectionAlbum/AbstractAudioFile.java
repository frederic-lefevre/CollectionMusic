package org.fl.collectionAlbum;

public abstract class AbstractAudioFile extends AbstractMediaFile {

	private final AudioFileType type;
	private final double samplingRate;
	
	private static final String TYPE_TITLE = "Type";
	private static final String SAMPLING_RATE_TITLE = "Sampling Rate";
	
	protected AbstractAudioFile(AudioFileType type, String source, double samplingRate, String note) {
		super(source, note);
		this.type = type;
		this.samplingRate = samplingRate;
	}
	
	protected AudioFileType getType() {
		return type;
	}

	protected double getSamplingRate() {
		return samplingRate;
	}
	
	protected void appendCommonAudioFileDetail(StringBuilder audioFilesDetails, String separator) {
		audioFilesDetails.append(getSamplingRate()).append(" KHz").append(separator);
		audioFilesDetails.append(getType()).append(separator);
		appendCommonMediaFileDetail(audioFilesDetails, separator);
	}
	
	public abstract boolean isHighRes();
	public abstract boolean isLossLess();
	
	protected static String getAudioFilePropertyTitles(String separator) {
		return SAMPLING_RATE_TITLE + separator + TYPE_TITLE + separator + AbstractMediaFile.getMediaFilePropertyTitles(separator);
	}
}
