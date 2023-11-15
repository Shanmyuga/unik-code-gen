/**
 * 
 */
package com.cognizant.fecodegen.components.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;

/**
 * @author 238209
 *
 */
public class ReactContainerRenderer extends BaseRenderer {

	private static Logger LOGGER = Logger.getLogger(ReactContainerRenderer.class);
	
	public static String PREFIX = "codegen.react"; 
	
	public ReactContainerRenderer(Map<String, Object> properties) {
		super(PREFIX, properties);
	}

	@Override
	public boolean render(JsonDocument jsonDoc) throws CodeGenException {
		
		LOGGER.info("Creating UI Container... ");

		StringBuilder content = new StringBuilder();
		Map<String, Object> contextVariables = getParametersMap(content, jsonDoc.getJson(), templateName, false);
		contextVariables.put(Constants.COMPONENT_NAME, CodeGenProperties.getCacheCompValue(Constants.COMPONENT_NAME));
		contextVariables.put(Constants.COMPONENT_FILENAME, getRelativeSavedFileName(Constants.COMPONENT_FILENAME));
		contextVariables.put(Constants.ACTION_FILENAME, getRelativeSavedFileName(Constants.ACTION_FILENAME));
		contextVariables.put(Constants.ACTION_METHOD_NAME, CodeGenProperties.getCacheCompValue(Constants.ACTION_METHOD_NAME));
		
		String containerName = CodeGenProperties.getCacheCompValue(Constants.COMPONENT_NAME) + "Container";
		CodeGenProperties.putCacheCompValue(Constants.CONTAINER_NAME, containerName);
		contextVariables.put(Constants.CONTAINER_NAME, containerName);
		contextVariables.put(Constants.REDUCER_NAME, CodeGenProperties.getCacheCompValue(Constants.REDUCER_NAME));
		
		if (StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(), 
				CodeGenProperties.getCacheValueAsString(Constants.GENERATE_LOGIN_SCREEN))) {
			contextVariables.put(Constants.PAGE, Constants.LOGIN);
		} else {
			contextVariables.put(Constants.PAGE, Constants.PAGE);
		}
		
		content.append(parser.parse(templateName, contextVariables));
	
		write(content.toString(), containerName);
		CodeGenProperties.putCacheCompValue(Constants.CONTAINER_FILENAME, getSavedFileName());

		if (StringUtils.equalsIgnoreCase(Boolean.FALSE.toString(), 
				(String) properties.get(Constants.GENERATE_LOGIN_SCREEN))) {
			replaceRoutingPathInLogin(containerName);
		}
		
		return false;
	}
	
	private void replaceRoutingPathInLogin(String containerName) {

		Object firstCompName = CodeGenProperties.getCacheValue(Constants.LOGIN_ROUTING_PATH_STATUS);

		try {
			
			if (checkFileExists(Constants.LOGIN_CONTAINER)) {
				String keyName = getFirstComponentName(containerName);

				if (keyName != null || firstCompName != null) {
					String firstPathParam = "pathname: '" + keyName + "'\n";
					if (firstCompName != null) {
						firstPathParam = firstCompName.toString();
					}
					
					Map<String, String> placeHolderMap = new HashMap<>();
					placeHolderMap.put("ReplaceWithFirstPath", firstPathParam);
					StringBuilder content = 
							readUpdateExistingFile(Constants.LOGIN_CONTAINER, "test", placeHolderMap);

					write(content.toString(), Constants.LOGIN_CONTAINER);
					CodeGenProperties.putCacheValue(Constants.LOGIN_ROUTING_PATH_STATUS, firstPathParam);
				}
			}
		} catch (CodeGenException e) {
			LOGGER.error("Error while updating the login component: ", e);
		}
	}

	private String getFirstComponentName(String containerName) {
		
		try {
			String routerFileName = CodeGenProperties.getCacheValueAsString(Constants.ROUTER_FILENAME);
			if (StringUtils.isBlank(routerFileName)) {
				return null;
			}
			
			String outputFile = getOuputFileName(routerFileName + ".js");
			outputFile = StringUtils.remove(outputFile, "/container");
			String routerFile = parser.readFileExternal(outputFile);
			
			String routerCont = StringUtils.substringAfter(routerFile, "<Route path=");
			routerCont = StringUtils.substringBetween(routerCont, "'");

			if (StringUtils.isNotBlank(routerCont)) {
				return routerCont;
			} else {
				return "/" + containerName;
			}
		} catch (CodeGenException e) {
			LOGGER.error("Error while finding the homePageURL: ", e);
		}
		
		return null;
	}
}
