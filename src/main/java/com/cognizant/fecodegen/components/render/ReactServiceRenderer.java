/**
 * 
 */
package com.cognizant.fecodegen.components.render;

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
public class ReactServiceRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactServiceRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactServiceRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("Creating Services... ");
		
		String sectionName = CodeGenProperties.getCacheCompValueAsString(Constants.SECTION_NAME);
		String actionMethodName = getActionMethodName(sectionName, "insert");
		LOGGER.info("Rendering Services: " + jsonDoc);
		
		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = 
				getParametersMap(content, jsonDoc.getJson(), templateName, false);
		contextVariables.put(Constants.ACTION_METHOD_NAME, actionMethodName);
		
		content.append(parser.parse(templateName, contextVariables));
		write(content.toString(), JsonUtils.toCamelCase(sectionName) + "Service");
		CodeGenProperties.putCacheCompValue(Constants.SERVICE_FILENAME, getSavedFileName());
		CodeGenProperties.putCacheCompValue(Constants.ACTION_METHOD_NAME, actionMethodName);
		
		return false;
	}

	private String getActionMethodName(String sectionName, String operation) {
		StringBuilder builder = new StringBuilder(operation);
		builder.append(StringUtils.replace(sectionName, " ", ""));
		
		return builder.toString();
	}
}
