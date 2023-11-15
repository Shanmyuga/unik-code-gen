/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 238209
 *
 */
public class CodeGenConfig implements Serializable {

	/**
	 * Serial Version Number
	 */
	private static final long serialVersionUID = -6307672346422423790L;

	private Map<String, Object> props;
	
	private Map<String, Component> reactComponents;

	/**
	 * @return the reactComponents
	 */
	public Map<String, Component> getReactComponents() {
		return reactComponents;
	}

	/**
	 * @param reactComponents the reactComponents to set
	 */
	public void setReactComponents(Map<String, Component> reactComponents) {
		this.reactComponents = reactComponents;
	}

	/**
	 * @return the props
	 */
	public Map<String, Object> getProps() {
		return props;
	}

	/**
	 * @param props the props to set
	 */
	public void setProps(Map<String, Object> props) {
		this.props = props;
	}
	
}
