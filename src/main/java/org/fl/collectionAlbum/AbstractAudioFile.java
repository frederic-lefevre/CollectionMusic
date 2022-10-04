package org.fl.collectionAlbum;

public abstract class AbstractAudioFile extends AbstractMediaFile {

	private final AudioFileType type;
	private final double samplingRate;
	
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
	
	public abstract String displayAudioFileDetail(String separator);
	
	public abstract String displayAudioFileSummary();
	
	protected void appendCommonAudioFileDetail(StringBuilder audioFilesDetails, String separator) {
		audioFilesDetails.append(getSamplingRate()).append(" KHz").append(separator);
		audioFilesDetails.append(getType()).append(separator);
		appendCommonMediaFileDetail(audioFilesDetails, separator);
	}
}
