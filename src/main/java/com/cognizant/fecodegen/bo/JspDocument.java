/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;

import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.RegexPatterns;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class JspDocument {
	
	private String fileContent;
	
	private JsonObject document;

	public JspDocument(String fileContent) {
		document = new JsonObject();
		
		this.fileContent = fileContent;
	}

	public String getHtmlContent(JsonObject htmlObj) {
		int start = getHtmlStartPosition(htmlObj);
		int end = getHtmlEndPosition(htmlObj);
		
		return StringUtils.substring(getFileContent(), start, end);
	}
	
	public int getHtmlStartPosition(JsonObject htmlObj) {
		int end = htmlObj.get(Constants.JSP_TAGS_END_POSITION).getAsInt();
		
		return end;
	}
	
	public int getHtmlEndPosition(JsonObject htmlObj) {
		int end = htmlObj.get(Constants.JSP_TAGS_END_POSITION).getAsInt();
		
		return end;
	}
	
	public void addHtmlTag(JsonObject htmlDocument) {
		addAsArray(document, htmlDocument, Constants.JSP_TAGS_DOM);
	}

	public void addAsArray(JsonObject parentObj, JsonObject obj, String tag) {
		JsonArray dom = parentObj.getAsJsonArray(tag);
		if (dom == null) {
			dom = new JsonArray();
			parentObj.add(tag, dom);
		}
		
		dom.add(obj);
	}
	
	public void addChildHtmlTag(JsonObject parentObj, JsonObject htmlObj) {
		addAsArray(parentObj, htmlObj, Constants.JSP_TAGS_TREE);
	}

	public JsonObject createHtmlTag(String htmlLine, int start, int end) {
		JsonObject htmlDocument = null;

		String htmlTag = null;
		Matcher m = RegexPatterns.HTML_TAG_PATTERN.matcher(htmlLine);
		if (m.find()) {
			htmlTag = StringUtils.substring(m.group(), 1);
			System.out.println(htmlLine + " = " + htmlTag);
			
			htmlDocument = new JsonObject();
			htmlDocument.addProperty(Constants.JSP_TAGS_TAG_NAME, htmlTag);
			htmlDocument.addProperty(Constants.JSP_TAGS_START_POSITION, start);
			htmlDocument.addProperty(Constants.JSP_TAGS_END_POSITION, end);
			
			parseAttributes(htmlDocument, htmlLine);
		}

		return htmlDocument;
	}
	
	public void addProperty(String property, String value) {
		document.addProperty(property, value);
	}

	public void parseAttributes(JsonObject htmlObj, String htmlLine) {
		htmlLine = StringUtils.remove(htmlLine, Constants.SEPARATOR_LT);
		htmlLine = StringUtils.remove(htmlLine, Constants.SEPARATOR_GT);
		htmlLine = StringUtils.remove(htmlLine, Constants.FORWARD_SLASH);

		JsonObject attributes = new JsonObject();
		htmlObj.add(Constants.JSP_TAGS_ATTRIBUTES, attributes);
		
		StringTokenizer tokenizer = new StringTokenizer(htmlLine, " ");
		// Skip the first Token
		if (tokenizer.hasMoreTokens()) {
			tokenizer.nextToken();
		}
		
		while (tokenizer.hasMoreTokens()) {
			String attribute = tokenizer.nextToken();
			
			String attributeKey = StringUtils.substringBefore(attribute, Constants.SEPARATOR_EQUAL);
			String attributeValue = StringUtils.substringAfter(attribute, Constants.SEPARATOR_EQUAL);
			attributeValue = StringUtils.remove(attributeValue, Constants.SEPARATOR_DOUBLE_QUOTES);
			
			attributes.addProperty(attributeKey, attributeValue);
		}
	}
	
	/**
	 * @return the document
	 */
	public JsonObject getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(JsonObject document) {
		this.document = document;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return document.toString();
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

}
