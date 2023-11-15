package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.List;

public class Validation {

	private String element;
	private String id;
	private String name;
	private List<Validations> serverSide;

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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serverSide
	 */
	public List<Validations> getServerSide() {
		return serverSide;
	}

	/**
	 * @param serverSide
	 *            the serverSide to set
	 */
	public void setServerSide(List<Validations> serverSide) {
		this.serverSide = serverSide;
	}

}
