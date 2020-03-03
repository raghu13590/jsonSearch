package com.zendesk.search;

public class DataFile {
	
	private String fileName;
	private String path;
	private static final String BACKSLASH = "\\";
	
	public DataFile(String fileName, String path) {
		this.path = path + BACKSLASH + fileName;
		this.fileName = fileName.substring(0, fileName.lastIndexOf("."));;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
