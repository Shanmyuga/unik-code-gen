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
public class TypeScriptMethod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1928240863869696465L;

	private String methodName;
	
	private String methodContent;

	private String methodArgs;
	
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the methodContent
	 */
	public String getMethodContent() {
		return methodContent;
	}

	/**
	 * @param methodContent the methodContent to set
	 */
	public void setMethodContent(String methodContent) {
		this.methodContent = methodContent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @return the methodArgs
	 */
	public String getMethodArgs() {
		return methodArgs;
	}

	/**
	 * @param methodArgs the methodArgs to set
	 */
	public void setMethodArgs(String methodArgs) {
		this.methodArgs = methodArgs;
	}
}
