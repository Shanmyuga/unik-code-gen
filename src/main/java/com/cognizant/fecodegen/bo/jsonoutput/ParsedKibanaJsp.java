package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParsedKibanaJsp {
	private String path;
	private String fileName;
	private String elementType;
	private String element;
	private String label;
	private String keyattributes;
	private String otherattributes;
	@JsonIgnore
	private List<Map<String, String>> attributes = new ArrayList<>();
	//private List<Map<String, String>> event = new ArrayList<>();
	private KibanaValidation validations = new KibanaValidation();
	private String events; 
	
	/**
	 * @return
	 */
	public String getEvents() {
		return events;
	}

	/**
	 * @param events
	 */
	public void setEvents(String events) {
		this.events = events;
	}

	
	/**
	 * @return
	 */
	public String getKeyattributes() {
		return keyattributes;
	}

	/**
	 * @param keyattributes
	 */
	public void setKeyattributes(String keyattributes) {
		this.keyattributes = keyattributes;
	}
	
	/**
	 * @return
	 */
	public String getOtherattributes() {
		return otherattributes;
	}

	/**
	 * @param otherattributes
	 */
	public void setOtherattributes(String otherattributes) {
		this.otherattributes = otherattributes;
	}

	
	
	/**
	 * @return the validations
	 */
	public KibanaValidation getValidations() {
		return validations;
	}

	/**
	 * @param validations
	 *            the validations to set
	 */
	public void setValidations(KibanaValidation validations) {
		this.validations = validations;
	}

	/**
	 * @return the events
	 *//*
	public List<Map<String, String>> getEvent() {
		return event;
	}

	*//**
	 * @param events
	 *            the events to set
	 *//*
	public void setEvent(List<Map<String, String>> event) {
		this.event = event;
	}*/

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the elementType
	 */
	public String getElementType() {
		return elementType;
	}

	/**
	 * @param elementType
	 *            the elementType to set
	 */
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	/**
	 * @return the element
	 */
	public String getElement() {
		return element;
	}

	/**
	 * @param element
	 *            the element to set
	 */
	public void setElement(String element) {
		this.element = element;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the attributes
	 */
	public List<Map<String, String>> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<Map<String, String>> attributes) {
		this.attributes = attributes;
	}

}