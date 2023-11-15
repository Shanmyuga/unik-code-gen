package com.cognizant.fecodegen.components.render;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;

public class SpringBootAppPropertiesRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(SpringBootAppPropertiesRenderer.class);
	
	public static String PREFIX = "codegen.springBootAppProperties"; 
	
	public SpringBootAppPropertiesRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("SpringBoot Application Properties Generation... ");
		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
		String containerName = Constants.APPPROPERTIES;
		content.append(parser.parse(templateName, contextVariables));
		write(content.toString(), containerName);
		return false;
	}
}