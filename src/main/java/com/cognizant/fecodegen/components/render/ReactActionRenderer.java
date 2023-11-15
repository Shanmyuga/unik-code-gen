/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;

/**
 * @author 238209
 *
 */
public class ReactActionRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactActionRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactActionRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("Creating Actions... ");
		
		String sectionName = CodeGenProperties.getCacheCompValueAsString(Constants.SECTION_NAME);
		String actionMethodName = getActionMethodName(sectionName, "insert");
		String actionConstant = getActionConstant(sectionName, "insert");
		String actionType = "${" + actionConstant + "}";

		LOGGER.info("Action Name: " + sectionName);
		
		Map<String, Object> contextVariables = new HashMap<>();
		contextVariables.put(Constants.ACTION_METHOD_NAME, actionMethodName);
		contextVariables.put("actionConstant", actionConstant);
		contextVariables.put("actionType", actionType);
		contextVariables.put(Constants.SERVICE_FILENAME, getRelativeSavedFileName(Constants.SERVICE_FILENAME));
		
		StringBuilder content = new StringBuilder();
		content.append(parser.parse(templateName, contextVariables));
		
		String actionName = JsonUtils.toCamelCase(sectionName) + "Action";
		write(content.toString(), actionName);
		
		CodeGenProperties.putCacheCompValue(Constants.ACTION_FILENAME, getSavedFileName());
		CodeGenProperties.putCacheCompValue(Constants.ACTION_NAME, actionName);
		CodeGenProperties.putCacheCompValue(Constants.ACTION_METHOD_NAME, actionMethodName);
		CodeGenProperties.putCacheCompValue(Constants.ACTION_CONSTANT, actionConstant);
		CodeGenProperties.putCacheCompValue(Constants.ACTION_TYPE, actionType);
		
		return false;
	}

	private String getActionConstant(String sectionName, String operation) {
		StringBuilder builder = new StringBuilder(StringUtils.upperCase(operation));
		builder.append("_");
		builder.append(StringUtils.replace(StringUtils.upperCase(sectionName), " ", "_"));
		
		return builder.toString();
	}

	private String getActionMethodName(String sectionName, String operation) {
		StringBuilder builder = new StringBuilder(operation);
		builder.append(StringUtils.replace(sectionName, " ", ""));
		
		return builder.toString();
	}
}
