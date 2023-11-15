package com.cognizant.fecodegen.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.components.render.IRenderer;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.service.RendererFactory;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SpringBootGenerator extends SpringBootCodeGenerator  {


	private static Logger LOGGER = Logger.getLogger(SpringBootGenerator.class);
	
	public String uiName;
	
	@Autowired
	SpringBootLayoutGenerator springBootLayoutGenerator;
	
	@Autowired
	UILayout uiLayout;
	
	@Override
	public boolean generate() throws CodeGenException {

		CodeGenProperties.putCacheValue(Constants.GENERATE_LOGIN_SCREEN, request.getGenLoginScreen());
		JsonObject inlineApp = configJson.getJsonObject(Constants.APPLICATION);
		String appDefault = inlineApp.get(Constants.INLINEAPP).getAsString();
		JsonArray layout = configJson.getJsonArray(Constants.LAYOUT);
		springBootLayoutGenerator.setLayout(request);
		List<JsonObject> generatedClasses = new ArrayList<JsonObject>();
		layout.forEach(je -> {
			JsonObject config = je.getAsJsonObject();
			String type = config.get(Constants.TYPE).getAsString();
			LOGGER.debug("The type is :" + type);
			try {
				Map<String, Object> inputProps = new HashMap<String, Object>();
				inputProps.put(Constants.GENERATE_LOGIN_SCREEN, request.getGenLoginScreen());
				inputProps.put(Constants.UI_NAME, request.getUiName());
				inputProps.put(Constants.APPNAME, request.getAppName());
				inputProps.put(Constants.PACKAGE, uiLayout.getPackageName());
				inputProps.put(Constants.CLASSNAME, uiLayout.getClassName());
				inputProps.put(Constants.REQUESTURL, uiLayout.getRequestURL());
				inputProps.put(Constants.MEMBER, uiLayout.getVariables());
				inputProps.put(Constants.COMPCONFMETHODNAME, JsonUtils.toCamelCase(uiLayout.getClassName()));
				inputProps.put(Constants.COMPCONFIGBEANLIST, generatedClasses);
				IRenderer renderer = RendererFactory.getRenderer(type,  request.getCodeGenPath(), 
																new JsonDocument(config), inputProps, configJson);
				
				renderer.initializeParams(new JsonDocument(config));
				renderer.render(new JsonDocument(config.get("params").getAsJsonObject()));
			} catch (CodeGenException e) {
				LOGGER.error("Error while processing: ", e);
			}
		});
		LOGGER.debug("Cache Value: " + CodeGenProperties.getCacheInstance().getProperties());
		return false;
	}
}