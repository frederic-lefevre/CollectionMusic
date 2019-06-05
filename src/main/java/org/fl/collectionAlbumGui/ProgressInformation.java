package org.fl.collectionAlbumGui;

public class ProgressInformation {

	private String status;
	private String information;
	
	public ProgressInformation(String st, String info) {
		status = st ;
		information = info ;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

}
