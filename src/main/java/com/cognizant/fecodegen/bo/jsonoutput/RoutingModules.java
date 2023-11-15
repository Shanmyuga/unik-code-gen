package com.cognizant.fecodegen.bo.jsonoutput;

import java.util.List;
import java.util.Map;

public class RoutingModules {

	private Map<String, List<String>> componentList;

	/**
	 * @return the componentList
	 */
	public Map<String, List<String>> getComponentList() {
		return componentList;
	}

	/**
	 * @param componentList the componentList to set
	 */
	public void setComponentList(Map<String, List<String>> componentList) {
		this.componentList = componentList;
	}
}
