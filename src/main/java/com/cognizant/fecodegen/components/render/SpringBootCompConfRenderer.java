package com.cognizant.fecodegen.components.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;
import com.google.gson.JsonObject;

public class SpringBootCompConfRenderer extends BaseRenderer{

private static Logger LOGGER = Logger.getLogger(SpringBootCompConfRenderer.class);
	
	public static String PREFIX = "codegen.springBootCompConf"; 
	
	public SpringBootCompConfRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}
	
	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("SpringBoot Component Configuration Generation... ");
		StringBuilder content = new StringBuilder();
		StringBuilder beanContent = new StringBuilder();
		StringBuilder importsContent = new StringBuilder();
		String containerName = Constants.COMPCONFIG+".java";
		
		if(checkFileExists(containerName) == true){
			String beanTemplate = "servicefm/compconfigbean.ftl";
			Map<String, Object> beanContextVariables = getParametersMap(beanContent, jsonDoc.getJson(), beanTemplate, false);
			beanContextVariables.put(Constants.COMPCONFIGBEANLIST, getProperties().get(Constants.COMPCONFIGBEANLIST));
			beanContent.append(parser.parse(beanTemplate, beanContextVariables));
			String importsTemplate = "servicefm/compconfigimports.ftl";
			Map<String, Object> importsContextVariables = getParametersMap(importsContent, jsonDoc.getJson(), importsTemplate, false);
			importsContextVariables.put(Constants.PACKAGE, getProperties().get(Constants.PACKAGE));
			importsContextVariables.put(Constants.CLASSNAME, getProperties().get(Constants.CLASSNAME));
			importsContent.append(parser.parse(importsTemplate, importsContextVariables));
			String mergedContent = mergingFileContent(containerName, (String)getProperties().get(Constants.CLASSNAME), beanContent.toString(), importsContent.toString());
			write(mergedContent, containerName);
		}
		else{
			Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
			contextVariables.put(Constants.APPNAME, getProperties().get(Constants.APPNAME));
			contextVariables.put(Constants.PACKAGE, getProperties().get(Constants.PACKAGE));
			contextVariables.put(Constants.CLASSNAME, getProperties().get(Constants.CLASSNAME));
			contextVariables.put(Constants.COMPCONFIGBEANLIST, getProperties().get(Constants.COMPCONFIGBEANLIST));
			LOGGER.info(contextVariables.toString());
			content.append(parser.parse(templateName, contextVariables));
			write(content.toString(), containerName);
		}	
		return false;
	}

}
