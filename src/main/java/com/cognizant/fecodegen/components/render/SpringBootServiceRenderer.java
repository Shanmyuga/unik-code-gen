package com.cognizant.fecodegen.components.render;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;

public class SpringBootServiceRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(SpringBootServiceRenderer.class);
	
public static String PREFIX = "codegen.springBootService"; 
	
	public SpringBootServiceRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("SpringBoot Service Generation... ");
		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
		contextVariables.put(Constants.PACKAGE, getProperties().get(Constants.PACKAGE));
		contextVariables.put(Constants.CLASSNAME, getProperties().get(Constants.CLASSNAME));
		contextVariables.put(Constants.GENERATE_LOGIN_SCREEN, getProperties().get(Constants.GENERATE_LOGIN_SCREEN));
		LOGGER.info(contextVariables.toString());
		String containerName = getProperties().get(Constants.CLASSNAME)+Constants.SERVICE+".java";
		content.append(parser.parse(templateName, contextVariables));
		write(content.toString(), containerName);
		return false;
	}
}