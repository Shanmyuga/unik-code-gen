/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 238209
 *
 */
public class TypeScriptModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9010871444468876280L;

	private String modelName;
	
	private Map<String, String> attributes;

	public void addAttribute(String key, String value) {
		if (attributes == null) {
			attributes = new HashMap<>();
		}
		
		attributes.put(key, value);
	}
	
	/**
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * @param modelName the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
