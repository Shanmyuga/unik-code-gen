/**
 * 
 */
package com.cognizant.fecodegen.model;

/**
 * @author 238209
 *
 */
public class FormTemplate {

	private String baseTemplateName;
	
	private String templateName;
	
	private String templateCode;
	
	private String templateType;

	/**
	 * @return the baseTemplateName
	 */
	public String getBaseTemplateName() {
		return baseTemplateName;
	}

	/**
	 * @param baseTemplateName the baseTemplateName to set
	 */
	public void setBaseTemplateName(String baseTemplateName) {
		this.baseTemplateName = baseTemplateName;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the templateCode
	 */
	public String getTemplateCode() {
		return templateCode;
	}

	/**
	 * @param templateCode the templateCode to set
	 */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	/**
	 * @return the templateType
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType the templateType to set
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
}
