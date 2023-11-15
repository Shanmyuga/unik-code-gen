package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.HashMap;
import java.util.Map;

public class FormElement {

	private final String element;
	private final Map<String, String> attributes = new HashMap<>();
	private final Map<String, String> events = new HashMap<>();
	private final Map<String, String> validations = new HashMap<>();
	public FormElement(String htmlTag) {
		element = htmlTag;
	}
	public String getElement() {
		return element;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	public Map<String, String> getEvents() {
		return events;
	}
	public Map<String, String> getValidations() {
		return validations;
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n");
		buffer.append(element);
		buffer.append("\n");
		buffer.append(attributes);
		buffer.append("\n");
		buffer.append(events);
		buffer.append("\n");
		buffer.append(validations);
		buffer.append("\n");
		return super.toString() + buffer.toString();
	}
	
}
