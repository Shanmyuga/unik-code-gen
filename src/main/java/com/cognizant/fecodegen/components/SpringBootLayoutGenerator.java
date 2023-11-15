package com.cognizant.fecodegen.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cognizant.fecodegen.bo.CodeGenRequest;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.bo.UILayout;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SpringBootLayoutGenerator{

	private static Logger LOGGER = Logger.getLogger(SpringBootLayoutGenerator.class);
	
	protected JsonDocument configJson;
	
	public String uiName;
	
	public String appName;
	
	@Autowired
	UILayout uiLayout;
	
	public boolean setLayout(CodeGenRequest request) throws CodeGenException{
		LOGGER.info("SpringBoot Layout Generation");
		String uiParams = request.getUiLayout();
		configJson = JsonUtils.getJsonObject(uiParams);
		JsonArray layout = configJson.getJsonArray(Constants.LAYOUT);
		List<JsonObject> uiComponentList = new ArrayList<JsonObject>();
		layout.forEach(section -> {
			JsonObject config = section.getAsJsonObject();
			JsonArray uiElements = config.get(Constants.COLUMNS).getAsJsonArray();
			getVariableList(uiElements, uiComponentList);
			uiName = request.getUiName();;
			uiName = uiName.replaceAll(Constants.SPACE_REGEX, "");
			appName = request.getAppName();
			appName = appName.replaceAll(Constants.SPACE_REGEX, "");
			uiLayout.setPackageName(Constants.PACKAGENAME + appName);
			uiLayout.setRequestURL(uiName.toLowerCase());
			uiLayout.setClassName(uiName);
			uiLayout.setVariables(uiComponentList);
		});
		return true;
	}
	
	public List<JsonObject> getVariableList(JsonArray uiElements, List<JsonObject> uiComponentList){
		JsonObject uiDetails = new JsonObject();
		System.out.println(JsonUtils.toCamelCase(Constants.ID));
		uiDetails.addProperty(Constants.DATATYPE, Constants.STRING);
		uiDetails.addProperty(Constants.VARIABLE, JsonUtils.toCamelCase(Constants.ID));
		uiDetails.addProperty(Constants.METHOD, Constants.ID);
		uiComponentList.add(uiDetails);
		if (uiElements != null) {
			for (int i = 0; i < uiElements.size(); i++) {
				JsonArray uielementArr = uiElements.get(i).getAsJsonArray();
				uielementArr.forEach(uiElement -> {
					JsonObject obj = uiElement.getAsJsonObject();
					String uiElementDataType = Constants.STRING;
					String uiElementType = obj.get(Constants.TYPE).getAsString();
					if (uiElementType.equals(Constants.TEXTBOX)) {
						uiElementDataType = Constants.STRING;
					} else if (uiElementType.equals(Constants.CHECKBOX)) {
						uiElementDataType = Constants.BOOLEAN;
					} else if (uiElementType.equals(Constants.DATEPICKER)) {
						uiElementDataType = Constants.STRING;
					}
					String uiElementLabel = obj.get(Constants.LABEL).getAsString();
					JsonObject uiObject = new JsonObject();
					uiObject.addProperty(Constants.DATATYPE, uiElementDataType);
					uiObject.addProperty(Constants.VARIABLE, JsonUtils.toCamelCase(uiElementLabel));
					uiObject.addProperty(Constants.METHOD, JsonUtils.toMethodNameStandard(uiElementLabel));
					uiComponentList.add(uiObject);
				});
			}
		}
			 
		return uiComponentList;
	
	}
}
