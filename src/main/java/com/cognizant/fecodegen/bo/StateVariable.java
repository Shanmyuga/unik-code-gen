/**
 * 
 */
package com.cognizant.fecodegen.bo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * @author 238209
 *
 */
public class StateVariable implements Serializable, Comparable<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6658114850242859666L;

	private String key;
	private String value;
	
	private String mandatory;
	private String minLength;
	private String maxLength;
	private String email;
	private String pattern;
	
	private String label;
	private String htmlTag;
	
	public StateVariable() {
	}
	
	public StateVariable(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(String o) {
		if (key == null) {
			return -1;
		}
		
		if (StringUtils.equalsIgnoreCase(key, o)) {
			return 0;
		} else {
			return key.compareToIgnoreCase(o);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof StateVariable) {
			StateVariable that = (StateVariable) obj;

			return StringUtils.equalsIgnoreCase(key, that.getKey());
		}
		
		return false;
	}

	/**
	 * @return the mandatory
	 */
	public String getMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * @return the minLength
	 */
	public String getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the maxLength
	 */
	public String getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the htmlTag
	 */
	public String getHtmlTag() {
		return htmlTag;
	}

	/**
	 * @param htmlTag the htmlTag to set
	 */
	public void setHtmlTag(String htmlTag) {
		this.htmlTag = htmlTag;
	}
}
