package com.cognizant.fecodegen.components.render;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;

public class SpringBootMainRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(SpringBootMainRenderer.class);
	
public static String PREFIX = "codegen.springBootMain"; 
	
	public SpringBootMainRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("SpringBoot Main class Generation... ");
		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
		contextVariables.put(Constants.PACKAGE, getProperties().get(Constants.PACKAGE));
		LOGGER.info(contextVariables.toString());
		String containerName = Constants.MAIN+".java";
		content.append(parser.parse(templateName, contextVariables));
		write(content.toString(), containerName);
		return false;
	}
}