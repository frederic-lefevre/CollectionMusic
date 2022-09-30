package org.fl.collectionAlbum;

public class LossyAudioFile extends AbstractAudioFile {
	
	private final double bitRate;

	public LossyAudioFile(AudioFileType type, String source, double bitRate, double samplingRate, String note) {
		
		super(type, source, samplingRate, note);
		this.bitRate = bitRate;
	}

	public double getBitRate() {
		return bitRate;
	}
	
	@Override
	public String displayAudioFileSummary() {
		StringBuilder audioFilesSummary = new StringBuilder();
		audioFilesSummary.append(getType().name()).append(" ");
		audioFilesSummary.append(Double.valueOf(getBitRate()).intValue());
		return audioFilesSummary.toString();
	}
	
	@Override
	public String displayAudioFileDetail(String separator) {
		StringBuilder audioFilesDetails = new StringBuilder();
		audioFilesDetails.append(getBitRate()).append(" kbit/s").append(separator);
		appendCommonAudioFileDetail(audioFilesDetails, separator);
		return audioFilesDetails.toString();
	}
}
