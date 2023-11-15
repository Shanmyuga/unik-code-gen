/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.CodeGenTemplateParser;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public abstract class Parser {

	private static Logger LOGGER = Logger.getLogger(Parser.class);

	private String fileName;
	
	private String fileContent;
	
	protected int currDocPosition = 0;
	
	protected int fileSize;

	protected Stack<JsonObject> stack;

	public Parser(String fileName) throws CodeGenException {
		this.fileName = fileName;
		this.stack = new Stack<JsonObject>();
		
		readFile();
	}
	
	private void readFile() throws CodeGenException {
		try {
			if (StringUtils.isNotBlank(fileName)) {
				
				CodeGenTemplateParser parser = new CodeGenTemplateParser();
				fileContent = parser.readFileExternal(fileName);
				fileSize = fileContent.length();
			}
		} catch (CodeGenException e) {
			LOGGER.error("Error while reading input file: " + fileName, e);
			throw e;
		}		
	}

	public void parse () {
		filter();
		
		parseContent();
		
		postProcess();
	}

	protected abstract void postProcess();
	
	protected abstract void parseContent();
	
	protected abstract void filter();

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
	 * @return the currDocPosition
	 */
	public int getCurrDocPosition() {
		return currDocPosition;
	}

	/**
	 * @param currDocPosition the currDocPosition to set
	 */
	public void setCurrDocPosition(int currDocPosition) {
		this.currDocPosition = currDocPosition;
	}

	/**
	 * @return the fileSize
	 */
	public int getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the stack
	 */
	public Stack<JsonObject> getStack() {
		return stack;
	}

	/**
	 * @param stack the stack to set
	 */
	public void setStack(Stack<JsonObject> stack) {
		this.stack = stack;
	}
	
}
