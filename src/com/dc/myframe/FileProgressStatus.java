package com.dc.myframe;

public class FileProgressStatus {
	private long doneLength = 0;
	private long totalLength;
	
	public FileProgressStatus(long totalLength) {
		this.totalLength = totalLength;
	}
	
	public void setDoneLength(long doneLength) {
		this.doneLength = doneLength;
	}
	
	public long getDoneLength() {
		return doneLength;
	}
	
	public long getTotalLength() {
		return totalLength;
	}

	
}
