package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.List;
import java.util.Map;

public class Row {

	private List<Map<String, String>> columns;

	/**
	 * @return the columns
	 */
	public List<Map<String, String>> getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(List<Map<String, String>> columns) {
		this.columns = columns;
	}

}
