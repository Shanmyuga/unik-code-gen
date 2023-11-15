/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class ReactFrameworkRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactFrameworkRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactFrameworkRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("Creating Framwork files... ");
		Map<String, Object> props = CodeGenProperties.getCacheInstance().getProperties();
		Map<String, Component> reactComponent = (Map<String, Component>) props.get(Constants.REACT_COMPONENTS);
		
		Set<Entry<String, JsonElement>> templates = getFrameworkTemplates().entrySet();
		templates.forEach(entry -> {
			String fileName = entry.getKey();
			String templateName = entry.getValue().getAsString();
			if (StringUtils.startsWith(templateName, "$")) {
				templateName = StringUtils.substring(templateName, 1);
			}
			
			LOGGER.info("		Creating " + fileName + " ...");
			
			StringBuilder content = new StringBuilder();

			try {
				
				Map<String, Object> contextVariables = 
						getParametersMap(content, jsonDoc.getJson(), templateName, false);
				contextVariables.put(Constants.REACT_COMPONENTS, reactComponent);
				
				content.append(parser.parse(templateName, contextVariables));
				
				write(content.toString(), fileName);
			} catch (CodeGenException e) {
				LOGGER.error("Error while generating framework files: ", e);
			}
		});
		
		if (jsonDoc.getAsString(Constants.PUBLIC_PATH) != null) {
			CodeGenProperties.putCacheValue(Constants.PUBLIC_PATH, jsonDoc.getAsString(Constants.PUBLIC_PATH));
		}
		
		return false;
	}

	private JsonObject getFrameworkTemplates() {
		JsonObject templates = config.getJsonObject("config").get("templates").getAsJsonObject();
		
		return templates;
	}

}
