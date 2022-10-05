package org.fl.collectionAlbum;

public class VideoFile extends AbstractMediaFile {

	private final int width;
	private final int height;
	private final VideoFileType type;
	
	public VideoFile(VideoFileType type, String source, int width, int height, String note) {
		super(source, note);
		this.type = type;
		this.width = width;
		this.height = height;
	}

	protected VideoFileType getType() {
		return type;
	}

	protected int getWidth() {
		return width;
	}

	protected int getHeight() {
		return height;
	}
	
	@Override
	public String displayMediaFileDetail(String separator) {
		StringBuilder videoFilesDetails = new StringBuilder();
		videoFilesDetails.append(getWidth()).append("x").append(getHeight()).append(" px").append(separator);
		videoFilesDetails.append(getType()).append(separator);
		appendCommonMediaFileDetail(videoFilesDetails, separator);
		return videoFilesDetails.toString();
	}

	@Override
	public String displayMediaFileSummary() {
		return getWidth() + "p";	
	}

}
