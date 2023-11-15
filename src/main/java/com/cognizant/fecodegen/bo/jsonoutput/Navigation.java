package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.List;

import com.cognizant.fecodegen.bo.JsParsedComponent;

public class Navigation {
	private String element;
	private String method;
	private String action;
	private String jsMethod;
	private String id;
	private String name;
	private String enctype;
	private JsParsedComponent jsParsedComponent;
	private List<Result> nextPage;
	
	/**
	 * @return the nextPage
	 */
	public List<Result> getNextPage() {
		return nextPage;
	}

	/**
	 * @param nextPage
	 *            the nextPage to set
	 */
	public void setNextPage(List<Result> nextPage) {
		this.nextPage = nextPage;
	}
	public JsParsedComponent getJsParsedComponent() {
		return jsParsedComponent;
	}
	public void setJsParsedComponent(JsParsedComponent jsParsedComponent) {
		this.jsParsedComponent = jsParsedComponent;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getJsMethod() {
		return jsMethod;
	}
	public void setJsMethod(String jsMethod) {
		this.jsMethod = jsMethod;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnctype() {
		return enctype;
	}
	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}
}
