package com.cognizant.fecodegen.components.render;

import java.util.Map;

import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;

public class SpringBootConstantsRenderer extends BaseRenderer{

private static Logger LOGGER = Logger.getLogger(SpringBootConstantsRenderer.class);
	
	public static String PREFIX = "codegen.springBootConstantsRenderer"; 
	
	public SpringBootConstantsRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("SpringBoot Constants Generation... ");
		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
		contextVariables.put(Constants.APPNAME, getProperties().get(Constants.APPNAME));
		contextVariables.put(Constants.PACKAGE, getProperties().get(Constants.PACKAGE));
		contextVariables.put(Constants.CLASSNAME, getProperties().get(Constants.CLASSNAME));
//		contextVariables.put(Constants.REQUESTURL, getProperties().get(Constants.REQUESTURL));
//		contextVariables.put(Constants.MEMBER, getProperties().get(Constants.MEMBER));
//		contextVariables.put(Constants.GENERATE_LOGIN_SCREEN, getProperties().get(Constants.GENERATE_LOGIN_SCREEN));
		LOGGER.info(contextVariables.toString());
		String containerName = Constants.CONSTANTS+".java";
		content.append(parser.parse(templateName, contextVariables));
		write(content.toString(), containerName);
		return false;
	}
}
