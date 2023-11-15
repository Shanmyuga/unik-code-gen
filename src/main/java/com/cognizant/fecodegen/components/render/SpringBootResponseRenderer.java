package com.cognizant.fecodegen.components.render;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonObject;

public class SpringBootResponseRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(SpringBootResponseRenderer.class);
	
	public static String PREFIX = "codegen.springBootResponse"; 
	
	public SpringBootResponseRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("SpringBoot Response Generation... ");
		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
		contextVariables.put(Constants.APPNAME, getProperties().get(Constants.APPNAME));
		contextVariables.put(Constants.PACKAGE, getProperties().get(Constants.PACKAGE));
		contextVariables.put(Constants.CLASSNAME, getProperties().get(Constants.CLASSNAME));
		contextVariables.put(Constants.REQUESTURL, getProperties().get(Constants.REQUESTURL));
		contextVariables.put(Constants.MEMBER, getProperties().get(Constants.MEMBER));
		LOGGER.info(contextVariables.toString());
		String className = getProperties().get(Constants.CLASSNAME)+Constants.RESPONSE;
		String containerName = className+".java";
		JsonObject CompConfigObject = new JsonObject();
		CompConfigObject.addProperty(Constants.COMPCONFBEANNAME, className);
		CompConfigObject.addProperty(Constants.COMPCONFMETHODNAME, JsonUtils.toCamelCase(className));
		contextVariables.put(Constants.COMPCONFIGBEANLIST, ((List<JsonObject>)getProperties().get(Constants.COMPCONFIGBEANLIST)).add(CompConfigObject));
		content.append(parser.parse(templateName, contextVariables));
		write(content.toString(), containerName);
		return false;
	}
}