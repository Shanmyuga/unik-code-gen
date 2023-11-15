package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.ArrayList;
import java.util.List;

public class Method {

	private String name;
	private List<Validations> Validations = new ArrayList<>();

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
	 * @return the validations
	 */
	public List<Validations> getValidations() {
		return Validations;
	}

	/**
	 * @param validations
	 *            the validations to set
	 */
	public void setValidations(List<Validations> validations) {
		Validations = validations;
	}

}
