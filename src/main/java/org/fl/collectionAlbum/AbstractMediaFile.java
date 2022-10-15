package org.fl.collectionAlbum;

public abstract class AbstractMediaFile {

	private final String source;
	private final String note;
	
	private static final String SOURCE_TITLE = "Source";
	private static final String NOTE_TITLE = "Note";
	
	protected AbstractMediaFile(String source, String note) {
		super();
		this.source = source;
		this.note = note;
	}
	
	
	public abstract String displayMediaFileDetail(String separator);
	
	public abstract String displayMediaFileSummary();
	
	protected String getSource() {
		return source;
	}
	
	protected String getNote() {
		return note;
	}
	
	protected void appendCommonMediaFileDetail(StringBuilder mediaFilesDetails, String separator) {
		mediaFilesDetails.append(getSource());
		String note = getNote();
		if ((note != null) && (!note.isEmpty())) {
			mediaFilesDetails.append(separator).append(getNote());
		}
	}
	
	protected static String getMediaFilePropertyTitles(String separator) {
		return SOURCE_TITLE + separator + NOTE_TITLE;
	}
}
