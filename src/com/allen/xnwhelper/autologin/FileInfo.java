package com.allen.xnwhelper.autologin;


public class FileInfo {

	private int fileType;
	private String fileName;
	private String filePath;

	public FileInfo(String filepath, String filename, int type) {
		filePath = filepath;
		fileName = filename;
		fileType = type;
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getFileType() {
		return fileType;
	}

	public void setFileType(int type) {
		fileType = type;
	}
}
