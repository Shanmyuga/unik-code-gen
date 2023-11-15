package com.cognizant.fecodegen.bo;

import java.util.List;

import com.google.gson.JsonObject;

public class UILayout {
	
	private String packageName;
	
	private String className;
	
	private List<JsonObject> variables;
	
	private String requestURL;
	
	public UILayout(){}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<JsonObject> getVariables() {
		return variables;
	}

	public void setVariables(List<JsonObject> uiComponentList) {
		this.variables = uiComponentList;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	
}
