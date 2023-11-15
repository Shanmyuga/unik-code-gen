package com.cognizant.fecodegen.components.render;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;

public class SpringBootPomRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(SpringBootPomRenderer.class);
	
	public static String PREFIX = "codegen.springBootPom"; 
	
	public SpringBootPomRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("SpringBoot POM Generation... ");
		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
		System.out.println(getProperties());
		contextVariables.put(Constants.APPNAME, getProperties().get(Constants.APPNAME));
		contextVariables.put(Constants.PACKAGE, getProperties().get(Constants.PACKAGE));
		contextVariables.put(Constants.CLASSNAME, getProperties().get(Constants.CLASSNAME));
		LOGGER.info(contextVariables.toString());
		String containerName = Constants.POM;
		content.append(parser.parse(templateName, contextVariables));
		write(content.toString(), containerName);
		return false;
	}
}