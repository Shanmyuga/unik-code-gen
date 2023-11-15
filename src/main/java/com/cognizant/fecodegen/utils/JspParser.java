/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.JspDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.service.ElasticSearchService;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class JspParser extends Parser {

	@Autowired
	protected ElasticSearchService elasticService;
	
	protected JspDocument document;
	public JspParser(String fileName) throws CodeGenException {
		super(fileName);
		
		document =  new JspDocument(getFileContent());
	}

	@Override
	protected void postProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void parseContent() {
		Matcher htmlLineStartMatcher = RegexPatterns.HTML_TAG_LINE_PATTERN.matcher(getFileContent());
		String htmlLine = null;
		String html = null;


		JsonObject htmlObj = null;
		JsonObject parentObj = null;

		while (currDocPosition < fileSize) {
			if (htmlLineStartMatcher.find(currDocPosition)) {
				htmlLine = htmlLineStartMatcher.group();
				htmlObj = document.createHtmlTag(htmlLine, 
												 htmlLineStartMatcher.start(),
												 htmlLineStartMatcher.end());

				if (stack.empty()) {
					document.addHtmlTag(htmlObj);
				} else {
					parentObj = stack.pop();

					document.addChildHtmlTag(parentObj, htmlObj);
					stack.push(parentObj);
				}

				currDocPosition = htmlLineStartMatcher.end();

				html = StringUtils.substring(getFileContent(), htmlLineStartMatcher.start());
				if (isHtmlTagContainChild(htmlObj.get("tag_name").getAsString(), html, htmlLine)) {
					stack.push(htmlObj);
				} else { 
					processAndCleanStack(htmlObj, htmlLine);
				}
			}

			
			if (htmlLineStartMatcher.hitEnd()) {
				break;
			}
			System.out.println("Json Document=" + document);
			//System.out.println("currDoc=" + currDocPosition + " : fileSize=" + fileSize);
		}
		
		//Stack stack = new Stack();
		//stack.p
		
		System.out.println("Json Document=" + document);
		
	}

	private boolean isHtmlTagContainChild(String htmlTag, String html, String htmlLine) {
		if (StringUtils.endsWith(StringUtils.trim(htmlLine), Constants.JSP_TAGS_END_TAG_SYNTAX)) {
			return false;
		}
		
		Matcher htmlStartTagMatcher = RegexPatterns.HTML_TAG_PATTERN.matcher(html);
		Matcher htmlEndTagMatcher = RegexPatterns.HTML_END_TAG_PATTERN.matcher(html);
		
		int startTagPosition = 0;
		int endTagPosition = 0;
		if (htmlStartTagMatcher.find(htmlTag.length() + 1)) {
			startTagPosition = htmlStartTagMatcher.start();
		}
		
		if (htmlEndTagMatcher.find()) {
			endTagPosition = htmlEndTagMatcher.start();
		}
		
		System.out.println("Tag=" + htmlTag + " : start=" + startTagPosition + " :end=" + endTagPosition);
		return (startTagPosition < endTagPosition)? true: false;
	}

	private void processAndCleanStack(JsonObject htmlObj, String htmlLine) {
		while (stack.empty() == false) {
			JsonObject parentObj = stack.pop();

			int start = 0;
			Matcher htmlStartTagMatcher = RegexPatterns.HTML_TAG_PATTERN.matcher(getFileContent());
			String nextHtmlTag = "";
			if (htmlStartTagMatcher.find(currDocPosition)) {
				start = htmlStartTagMatcher.start();
				nextHtmlTag = htmlStartTagMatcher.group();
			}

			int end = 0;
			String htmlTag = parentObj.get(Constants.JSP_TAGS_TAG_NAME).getAsString();
			String htmlEndPattern = "</[ ]*" + htmlTag + "[ ]*>"; 
			Matcher htmlEndTagMatcher = Pattern.compile(htmlEndPattern).matcher(getFileContent());
			if (htmlEndTagMatcher.find(currDocPosition)) {
				end = htmlEndTagMatcher.start();
			}
			
			System.out.println("processAndCleanStack: next start Tag=" + nextHtmlTag
											+ " Tag Position=" + start 
											+ " parent start Tag=" + htmlTag
											+ " : End Tag Position=" + end);
 			if (start < end) {
				stack.push(parentObj);
				break;
			}
		}
	}

	@Override
	protected void filter() {
		setFileContent(StringUtils.removePattern(getFileContent(), RegexPatterns.COMMENTS_REGEX));
		setFileSize(getFileContent().length());
	}

}
