package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.ArrayList;
import java.util.List;

public class ParsedJsp {
	private String path;
	private String fileName;
	private final List<FormElement> forms = new ArrayList<FormElement>();
	private final List<Validation> validations = new ArrayList<Validation>();
	private final List<Navigation> navigations = new ArrayList<Navigation>();
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<FormElement> getForms() {
		return forms;
	}
	public List<Validation> getValidations() {
		return validations;
	}
	public List<Navigation> getNavigations() {
		return navigations;
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n");
		buffer.append(path);
		buffer.append("\n");
		buffer.append(fileName);
		buffer.append("\n");
		buffer.append(forms);
		buffer.append("\n");
		buffer.append(validations);
		buffer.append("\n");
		buffer.append(navigations);
		buffer.append("\n");
		return super.toString() + buffer.toString();
	}
}