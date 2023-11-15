/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author 238209
 *
 */
public class TypeScriptDeclarations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3523063651155214501L;

	private String variableName;
	
	private String dataType;
	
	private String defaultValue;
	
	private String actualValue;
	
	private String dataTableProps;
	
	private String annotation;

	/**
	 * @return the variableName
	 */
	public String getVariableName() {
		return variableName;
	}

	/**
	 * @param variableName the variableName to set
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	 * @return the annotation
	 */
	public String getAnnotation() {
		return annotation;
	}

	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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

	/**
	 * @return the dataTableProps
	 */
	public String getDataTableProps() {
		return dataTableProps;
	}

	/**
	 * @param dataTableProps the dataTableProps to set
	 */
	public void setDataTableProps(String dataTableProps) {
		this.dataTableProps = dataTableProps;
	}
	
}
