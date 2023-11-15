/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.cognizant.fecodegen.utils.JsonUtils;

/**
 * @author 238209
 *
 */
public class ReactReducerRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactReducerRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactReducerRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		LOGGER.info("Creating Reducers... ");
		
		String actionName = CodeGenProperties.getCacheCompValueAsString(Constants.SECTION_NAME);
		String actionConstant = getActionConstant(actionName, "insert");
		String reducerType = "${" + actionConstant + "}";

		Map<String, Object> contextVariables = new HashMap<>();
		contextVariables.put("reducerName", JsonUtils.toCamelCase(actionName) + "Reducer");
		contextVariables.put("actionConstant", actionConstant);
		contextVariables.put("reducerType", reducerType);
		
		contextVariables.put(Constants.ACTION_FILENAME, getRelativeSavedFileName(Constants.ACTION_FILENAME));
		
		StringBuilder content = new StringBuilder();
		content.append(parser.parse(templateName, contextVariables));
		
		String reducerName = StringUtils.replace(actionName, " ", "") + "Reducer";
		CodeGenProperties.putCacheCompValue(Constants.REDUCER_NAME, reducerName);
		
		write(content.toString(), reducerName);
		CodeGenProperties.putCacheCompValue(Constants.REDUCER_FILENAME, getSavedFileName());

		createOrUpdateRootReducer(actionName, jsonDoc);
		
		return false;
	}

	@SuppressWarnings("unchecked")
	private void createOrUpdateRootReducer(String actionName, JsonDocument jsonDoc) throws CodeGenException {
		Map<String, Object> props = CodeGenProperties.getCacheInstance().getProperties();
		Map<String, Component> reactComponent = (Map<String, Component>) props.get(Constants.REACT_COMPONENTS);
		
		/*String reducerName = JsonUtils.toCamelCase(actionName) + "Reducer";
		String reducerPath = getRelativeSavedFileName(Constants.REDUCER_FILENAME);
		
		StringBuilder content = null;

		if (checkFileExists(Constants.ROOT_REDUCER_NAME)) {
			Map<String, String> placeHolderMap = new HashMap<>();
			placeHolderMap.put("PlaceHolderInsert", reducerName + ",\n");
			placeHolderMap.put("PlaceHolderImportInsert", "import " + reducerName + " from '" + reducerPath + "';\n");
			
			content = readUpdateExistingFile(Constants.ROOT_REDUCER_NAME, reducerName, placeHolderMap);
		} else {
			content = new StringBuilder();
			
			Map<String, Object> contextVariables = new HashMap<>();
			contextVariables.put(Constants.REDUCER_NAME, reducerName);
			contextVariables.put(Constants.REDUCER_FILENAME, reducerPath);

			content.append(parser.parse("fm/flows/rootreducer.ftl", contextVariables));
		}*/
		
		String rootReducerTemplateName = jsonDoc.getAsString("rootReducer");
		
		Map<String, Object> contextVariables = new HashMap<>();
		contextVariables.put(Constants.REACT_COMPONENTS, reactComponent);
		
		StringBuilder content = new StringBuilder();
		content.append(parser.parse(rootReducerTemplateName, contextVariables));
		
		write(content.toString(), Constants.ROOT_REDUCER_NAME);
	}

	private String getActionConstant(String sectionName, String operation) {
		StringBuilder builder = new StringBuilder(StringUtils.upperCase(operation));
		builder.append("_");
		builder.append(StringUtils.replace(StringUtils.upperCase(sectionName), " ", "_"));
		
		return builder.toString();
	}
}
