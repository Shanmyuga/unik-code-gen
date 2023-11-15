/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;

/**
 * @author 238209
 *
 */
public class Route implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7946701058214232529L;

	private String path;

	private String component;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the component
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}

}
