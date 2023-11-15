/**
 * 
 */
package com.cognizant.fecodegen.model;

/**
 * @author 238209
 *
 */
public class Params {
	
	private String parameterName;
	
	private String parameterType;
	
	private String defaultValue;
	
	private String actualValue;

	/**
	 * @return the parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

	/**
	 * @param parameterName the parameterName to set
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * @return the parameterType
	 */
	public String getParameterType() {
		return parameterType;
	}

	/**
	 * @param parameterType the parameterType to set
	 */
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the actualValue
	 */
	public String getActualValue() {
		return actualValue;
	}

	/**
	 * @param actualValue the actualValue to set
	 */
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
}
