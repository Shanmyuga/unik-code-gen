/**
 * 
 */
package com.cognizant.fecodegen.model;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author 238209
 *
 */
@Document(collection = "config")
public class ConfigEntity {
	
	@Id
	private BigInteger id;
	
	private String configName;
	
	private String configLanguage;
	
	private boolean defaultConfig;
	
	private BigInteger defaultConfigId;
	
	private String defaultConfigJson;
	
	private List<FormTemplate> formTemplates;
	
	private List<ResourceTemplate> resourceTemplates;

	private ComponentConfig componentConfig;
	
	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 * @return the configName
	 */
	public String getConfigName() {
		return configName;
	}

	/**
	 * @param configName the configName to set
	 */
	public void setConfigName(String configName) {
		this.configName = configName;
	}

	/**
	 * @return the configLanguage
	 */
	public String getConfigLanguage() {
		return configLanguage;
	}

	/**
	 * @param configLanguage the configLanguage to set
	 */
	public void setConfigLanguage(String configLanguage) {
		this.configLanguage = configLanguage;
	}

	/**
	 * @return the defaultConfig
	 */
	public boolean isDefaultConfig() {
		return defaultConfig;
	}

	/**
	 * @param defaultConfig the defaultConfig to set
	 */
	public void setDefaultConfig(boolean defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	/**
	 * @return the defaultConfigId
	 */
	public BigInteger getDefaultConfigId() {
		return defaultConfigId;
	}

	/**
	 * @param defaultConfigId the defaultConfigId to set
	 */
	public void setDefaultConfigId(BigInteger defaultConfigId) {
		this.defaultConfigId = defaultConfigId;
	}

	/**
	 * @return the defaultConfigJson
	 */
	public String getDefaultConfigJson() {
		return defaultConfigJson;
	}

	/**
	 * @param defaultConfigJson the defaultConfigJson to set
	 */
	public void setDefaultConfigJson(String defaultConfigJson) {
		this.defaultConfigJson = defaultConfigJson;
	}

	/**
	 * @return the formTemplates
	 */
	public List<FormTemplate> getFormTemplates() {
		return formTemplates;
	}

	/**
	 * @param formTemplates the formTemplates to set
	 */
	public void setFormTemplates(List<FormTemplate> formTemplates) {
		this.formTemplates = formTemplates;
	}

	/**
	 * @return the resourceTemplates
	 */
	public List<ResourceTemplate> getResourceTemplates() {
		return resourceTemplates;
	}

	/**
	 * @param resourceTemplates the resourceTemplates to set
	 */
	public void setResourceTemplates(List<ResourceTemplate> resourceTemplates) {
		this.resourceTemplates = resourceTemplates;
	}

	/**
	 * @return the componentConfig
	 */
	public ComponentConfig getComponentConfig() {
		return componentConfig;
	}

	/**
	 * @param componentConfig the componentConfig to set
	 */
	public void setComponentConfig(ComponentConfig componentConfig) {
		this.componentConfig = componentConfig;
	}
}
