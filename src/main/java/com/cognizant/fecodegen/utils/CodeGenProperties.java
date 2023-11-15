/**
 * 
 */
package com.cognizant.fecodegen.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.Component;

/**
 * @author 238209
 *
 */
public class CodeGenProperties {

	private static Logger logger = Logger.getLogger(CodeGenProperties.class);
	
	private Map<String, Object> properties;
	
	private static CodeGenProperties INSTANCE;
	
	private static CodeGenProperties CACHE_INSTANCE;
	
	public CodeGenProperties(boolean loadProps) {
		properties = new HashMap<>();

		logger.debug("Cache Instantiated...");
		/*if (loadProps) {
			try {
				InputStream inStream = 
						getClass().getClassLoader()
						.getResourceAsStream("application.properties");

				//properties.load(inStream);
			} catch (IOException e) {
				logger.error("Error while loading properties file.", e);
				throw new CodeGenerationException(e);
			}
		}*/
	}
	
	public static CodeGenProperties getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CodeGenProperties(true);
		}
		
		return INSTANCE;
	}
	
	public static CodeGenProperties getCacheInstance() {
		if (CACHE_INSTANCE == null) {
			CACHE_INSTANCE = new CodeGenProperties(false);
		}
		
		return CACHE_INSTANCE;
	}

	public static String getCacheCompValue (String key) {
		String currentComponent = 
				(String) getCacheInstance().getProperties().get(Constants.CURRENT_COMPONENT_NAME);
		
		if (StringUtils.isNotBlank(currentComponent)) {
			Component component = getReactComponent(currentComponent);
			
			return getComponentValue(component, key);
		} else {
			logger.info("Cannot find the current component..");
		}
		
		return null;
	}
	
	private static String getComponentValue(Component component, String key) {
		String value = null;
		
		switch (key) {
		case Constants.ACTION_FILENAME:
			value = component.getActionFileName();
			break;
		case Constants.ACTION_METHOD_NAME:
			value = component.getActionMethodName();
			break;
		case Constants.ACTION_NAME:
			value = component.getActionName();
			break;
		case Constants.SERVICE_FILENAME:
			value = component.getServiceFileName();
			break;
		case Constants.CONTAINER_FILENAME:
			value = component.getContainerFileName();
			break;
		case Constants.CONTAINER_NAME:
			value = component.getContainerName();
			break;
		case Constants.COMPONENT_NAME:
			value = component.getComponentName();
			break;
		case Constants.COMPONENT_FILENAME:
			value = component.getComponentFileName();
			break;
		case Constants.REDUCER_NAME:
			value = component.getReducerName();
			break;
		case Constants.REDUCER_FILENAME:
			value = component.getReducerFileName();
			break;
		case Constants.SECTION_NAME:
			value = component.getSectionName();
			break;
		case Constants.ACTION_TYPE:
			value = component.getActionType();
			break;
		case Constants.REDUCER_TYPE:
			value = component.getReducerType();
			break;
		case Constants.ACTION_CONSTANT:
			value = component.getActionConstant();
			break;
		default:
			logger.info("Value not set in Component..");
		}

		return value;
	}

	public static Component getCurrentReactComponent () {
		String currentComponent = 
				(String) getCacheInstance().getProperties().get(Constants.CURRENT_COMPONENT_NAME);
		
		if (StringUtils.isNotBlank(currentComponent)) {
			Component component = getReactComponent(currentComponent);
			
			return component;
		} 

		return null;
	}
	
	public static void putCacheCompValue (String key, Object value) {
		String currentComponent = 
				(String) getCacheInstance().getProperties().get(Constants.CURRENT_COMPONENT_NAME);
		
		if (StringUtils.isNotBlank(currentComponent)) {
			Component component = getReactComponent(currentComponent);
			String keyValue = (String) value;
			
			setComponentValue(component, key, keyValue);
		} else {
			logger.info("Cannot find the current component..");
		}
	}

	/**
	 * @param component
	 * @param key
	 * @param keyValue
	 */
	protected static void setComponentValue(Component component, String key, String keyValue) {
		switch (key) {
		case Constants.ACTION_FILENAME:
			component.setActionFileName(keyValue);
			break;
		case Constants.ACTION_METHOD_NAME:
			component.setActionMethodName(keyValue);
			break;
		case Constants.ACTION_NAME:
			component.setActionName(keyValue);
			break;
		case Constants.SERVICE_FILENAME:
			component.setServiceFileName(keyValue);
			break;
		case Constants.CONTAINER_FILENAME:
			component.setContainerFileName(keyValue);
			break;
		case Constants.CONTAINER_NAME:
			component.setContainerName(keyValue);
			break;
		case Constants.COMPONENT_NAME:
			component.setComponentName(keyValue);
			break;
		case Constants.COMPONENT_FILENAME:
			component.setComponentFileName(keyValue);
			break;
		case Constants.REDUCER_NAME:
			component.setReducerName(keyValue);
			break;
		case Constants.REDUCER_FILENAME:
			component.setReducerFileName(keyValue);
			break;
		case Constants.SECTION_NAME:
			component.setSectionName(keyValue);
			break;
		case Constants.ACTION_TYPE:
			component.setActionType(keyValue);
			break;
		case Constants.REDUCER_TYPE:
			component.setReducerType(keyValue);
			break;
		case Constants.ACTION_CONSTANT:
			component.setActionConstant(keyValue);
			break;
		case "isTab":
			component.setIsTab(keyValue);
			break;
		default:
			logger.info("Value not set in Component..");
		}
	}

	/**
	 * @param compName
	 * @return 
	 */
	protected static Component getReactComponent(String compName) {
		Map<String, Component> components = getReactComponents();
		
		Component component = components.get(compName);
		if (component == null) {
			component = new Component();
			components.put(compName, component);
		}
		
		return component;
	}

	/**
	 * @return 
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected static Map<String, Component> getReactComponents() {
		Map<String, Component> components = 
				(Map<String, Component>) getCacheInstance().getProperties().get(Constants.REACT_COMPONENTS);
		if (components == null) {
			components = new HashMap<>();
			getCacheInstance().getProperties().put(Constants.REACT_COMPONENTS, components);
		}
		
		return components;
	}

	public static void putCacheValue (String key, Object value) {
		getCacheInstance().getProperties().put(key, value);
	}

	public static String getCacheCompValueAsString (String key) {
		return (String) getCacheCompValue(key);
	}

	public static String getCacheValueAsString (String key) {
		return (String) getCacheValue (key);
	}
	
	public static Object getCacheValue (String key) {
		return getCacheInstance().getProperties().get(key);
	}
	
	public static Object getValue (String key) {
		return getInstance().getProperties().get(key);
	}

	public static Map<String, Object> getProps () {
		return getInstance().getProperties();
	}
	
	public static Map<String, Object> getCacheProps () {
		return getCacheInstance().getProperties();
	}
	
	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
