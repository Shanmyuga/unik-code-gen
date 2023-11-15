/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 244898
 *
 */
public class TabComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5957131467947563098L;

	private Map<String, String> tabCompMap;

	private String componentName;

	private String sectionName;

	private String htmlID;

	/**
	 * @return the htmlID
	 */
	public String getHtmlID() {
		return htmlID;
	}

	/**
	 * @param htmlID the htmlID to set
	 */
	public void setHtmlID(String htmlID) {
		this.htmlID = htmlID;
	}

	/**
	 * @return the tabCompMap
	 */
	public Map<String, String> getTabCompMap() {
		return tabCompMap;
	}

	/**
	 * @param tabCompMap the tabCompMap to set
	 */
	public void setTabCompMap(Map<String, String> tabCompMap) {
		this.tabCompMap = tabCompMap;
	}

	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * @return the sectionName
	 */
	public String getSectionName() {
		return sectionName;
	}

	/**
	 * @param sectionName the sectionName to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

}
