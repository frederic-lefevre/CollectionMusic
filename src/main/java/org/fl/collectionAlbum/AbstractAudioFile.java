package org.fl.collectionAlbum;

public abstract class AbstractAudioFile {

	private final AudioFileType type;
	private final String source;
	private final double samplingRate;
	private final String note;
	
	protected AbstractAudioFile(AudioFileType type, String source, double samplingRate, String note) {
		super();
		this.type = type;
		this.source = source;
		this.samplingRate = samplingRate;
		this.note = note;
	}
	
	protected AudioFileType getType() {
		return type;
	}

	protected String getSource() {
		return source;
	}

	protected double getSamplingRate() {
		return samplingRate;
	}

	protected String getNote() {
		return note;
	}
	
	public abstract String displayAudioFileDetail(String separator);
	
	public abstract String displayAudioFileSummary();
	
	protected void appendCommonAudioFileDetail(StringBuilder audioFilesDetails, String separator) {
		audioFilesDetails.append(getSamplingRate()).append(" KHz").append(separator);
		audioFilesDetails.append(getType()).append(separator);
		audioFilesDetails.append(getSource());
		String note = getNote();
		if ((note != null) && (!note.isEmpty())) {
			audioFilesDetails.append(separator).append(getNote());
		}
	}
}
