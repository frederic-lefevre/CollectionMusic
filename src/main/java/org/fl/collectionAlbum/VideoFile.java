package org.fl.collectionAlbum;

public class VideoFile extends AbstractMediaFile {

	private final int width;
	private final int height;
	
	public VideoFile(int width, int height, String source, String note) {
		super(source, note);
		this.width = width;
		this.height = height;
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
		appendCommonMediaFileDetail(videoFilesDetails, separator);
		return videoFilesDetails.toString();
	}

	@Override
	public String displayMediaFileSummary() {
		StringBuilder videoFilesDetails = new StringBuilder();
		videoFilesDetails.append(getWidth()).append("x").append(getHeight());
		return videoFilesDetails.toString();
		
	}

}
