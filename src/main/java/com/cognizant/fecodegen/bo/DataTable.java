/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 238209
 *
 */
public class DataTable {
	
	private List<FormElement> dataTableList;
	
	private String dataTableHtmlId;
	
	private String keyField;
	
	private String valueField;

	/**
	 * @return the valueField
	 */
	public String getValueField() {
		return valueField;
	}

	/**
	 * @param valueField the valueField to set
	 */
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	/**
	 * @return the dataTableList
	 */
	public List<FormElement> getDataTableList() {
		if  (dataTableList == null) {
			dataTableList = new ArrayList<>();
		}
		return dataTableList;
	}

	/**
	 * @param dataTableList the dataTableList to set
	 */
	public void setDataTableList(List<FormElement> dataTableList) {
		this.dataTableList = dataTableList;
	}

	/**
	 * @return the dataTableHtmlId
	 */
	public String getDataTableHtmlId() {
		return dataTableHtmlId;
	}

	/**
	 * @param dataTableHtmlId the dataTableHtmlId to set
	 */
	public void setDataTableHtmlId(String dataTableHtmlId) {
		this.dataTableHtmlId = dataTableHtmlId;
	}

	/**
	 * @return the keyField
	 */
	public String getKeyField() {
		return keyField;
	}

	/**
	 * @param keyField the keyField to set
	 */
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}
}
