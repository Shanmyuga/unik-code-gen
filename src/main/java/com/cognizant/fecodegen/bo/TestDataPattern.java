/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author 238209
 *
 */
public class TestDataPattern implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 520486616495774553L;

	private boolean elementValid;
	private boolean hasErrors;
	private String errorType;
	private String inputData;
	
	/**
	 * @return the elementValid
	 */
	public boolean isElementValid() {
		return elementValid;
	}
	/**
	 * @param elementValid the elementValid to set
	 */
	public void setElementValid(boolean elementValid) {
		this.elementValid = elementValid;
	}
	/**
	 * @return the hasErrors
	 */
	public boolean isHasErrors() {
		return hasErrors;
	}
	/**
	 * @param hasErrors the hasErrors to set
	 */
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}
	/**
	 * @return the errorType
	 */
	public String getErrorType() {
		return errorType;
	}
	/**
	 * @param errorType the errorType to set
	 */
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	/**
	 * @return the inputData
	 */
	public String getInputData() {
		return inputData;
	}
	/**
	 * @param inputData the inputData to set
	 */
	public void setInputData(String inputData) {
		this.inputData = inputData;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\nTestDataPattern:" + ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
