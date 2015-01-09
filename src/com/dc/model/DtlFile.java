//文件数据的标准模型

package com.dc.model;

public class DtlFile {
	public String fileName;
	public String fileType;
	public String fileDate;
	public String filePath;
	public String upLoader;
	public String tagBrand;
	public String annoTate;
	public String downRank;
	public long fileSize;
	
	public DtlFile(String fileName, String fileType, long fileSize, String fileDate, 
			String filePath, String upLoader, String tagBrand, String annoTate, String downRank) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.fileDate = fileDate;
		this.filePath = filePath;
		this.upLoader = upLoader;
		this.tagBrand = tagBrand;
		this.annoTate = annoTate;
		this.downRank = downRank;
	}
}
