package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.ArrayList;
import java.util.List;

public class Action {

	private String fileName;
	private String Namespace;
	private String ParentPackage;
	private List<Result> Results;
	private Validations Validation;
	private List<Validations> Validations;
	private List<Method> methods = new ArrayList<>();

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
	 * @return the namespace
	 */
	public String getNamespace() {
		return Namespace;
	}

	/**
	 * @param namespace
	 *            the namespace to set
	 */
	public void setNamespace(String namespace) {
		Namespace = namespace;
	}

	/**
	 * @return the parentPackage
	 */
	public String getParentPackage() {
		return ParentPackage;
	}

	/**
	 * @param parentPackage
	 *            the parentPackage to set
	 */
	public void setParentPackage(String parentPackage) {
		ParentPackage = parentPackage;
	}

	/**
	 * @return the results
	 */
	public List<Result> getResults() {
		return Results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<Result> results) {
		Results = results;
	}

	/**
	 * @return the validation
	 */
	public Validations getValidation() {
		return Validation;
	}

	/**
	 * @param validation
	 *            the validation to set
	 */
	public void setValidation(Validations validation) {
		Validation = validation;
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

	/**
	 * @return the methods
	 */
	public List<Method> getMethods() {
		return methods;
	}

	/**
	 * @param methods
	 *            the methods to set
	 */
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

}
