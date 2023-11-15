/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;

/**
 * @author 238209
 *
 */
public class CodeParseRequest implements Serializable {

	/**
	 * Generated Serial Version Id
	 */
	private static final long serialVersionUID = 3267751756942287399L;

	private String fileName;
	
	private String fileContent;
	
	private String fileType;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileContent
	 */
	public String getFileContent() {
		return fileContent;
	}

	/**
	 * @param fileContent the fileContent to set
	 */
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
