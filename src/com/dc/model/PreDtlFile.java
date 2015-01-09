//待审核数据的文件标准模型

package com.dc.model;

public class PreDtlFile {
	public String fileName;
	public String fileType;
	public String fileDate;
	public String upLoader;
	public String tagBrand;
	public String annoTate;
	public long fileSize;
	
	public PreDtlFile(String fileName, String fileType, long fileSize, String fileDate
			, String upLoader, String tagBrand, String annoTate) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.fileDate = fileDate;
		this.upLoader = upLoader;
		this.tagBrand = tagBrand;
		this.annoTate = annoTate;
	}
	
	
}
