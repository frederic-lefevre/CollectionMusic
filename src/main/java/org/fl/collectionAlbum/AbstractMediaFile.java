package org.fl.collectionAlbum;

public abstract class AbstractMediaFile {

	private final String source;
	private final String note;
	
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
}
