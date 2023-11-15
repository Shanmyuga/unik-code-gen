/**
 * 
 */
package com.cognizant.fecodegen.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.cognizant.fecodegen.bo.CodeGenConfig;
import com.cognizant.fecodegen.bo.Component;
import com.cognizant.fecodegen.bo.JsonDocument;
import com.cognizant.fecodegen.components.render.IRenderer;
import com.cognizant.fecodegen.exception.CodeGenException;
import com.cognizant.fecodegen.service.RendererFactory;
import com.cognizant.fecodegen.utils.CodeGenProperties;
import com.cognizant.fecodegen.utils.Constants;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author 238209
 *
 */
public class ReactJsGenerator extends CodeGenerator {

	private static Logger LOGGER = Logger.getLogger(ReactJsGenerator.class);
	
	@Override
	public boolean generate() throws CodeGenException {

		readAndLoadCacheProps();
		
		CodeGenProperties.putCacheValue(Constants.GENERATE_LOGIN_SCREEN, request.getGenLoginScreen());
		JsonArray layout = configJson.getJsonArray("layout");
		layout.forEach(je -> {
			JsonObject config = je.getAsJsonObject();
			String type = config.get("type").getAsString();
			
			LOGGER.debug("The type is :" + type);
			try {
				Map<String, Object> inputProps = new HashMap<String, Object>();
				inputProps.put(Constants.GENERATE_LOGIN_SCREEN, request.getGenLoginScreen());
				inputProps.put(Constants.UI_NAME, request.getUiName());
				inputProps.put(Constants.APPNAME, request.getAppName());
				if (StringUtils.isNotEmpty(request.getComponentName())) {
					inputProps.put(Constants.COMPONENT_NAME, request.getComponentName());
				}
				if (StringUtils.isNotEmpty(request.getModuleName())) {
					inputProps.put(Constants.MODULE_NAME, request.getModuleName());
				}
				if (StringUtils.isNotEmpty(request.getUserDefinedPath())) {
					inputProps.put(Constants.USER_DEFINED_PATH, request.getUserDefinedPath());
				}

				IRenderer renderer = RendererFactory.getRenderer(type,  request.getCodeGenPath(), 
							new JsonDocument(config), inputProps, configJson);
				
				renderer.initializeParams(new JsonDocument(config));
				renderer.render(new JsonDocument(config.get("params").getAsJsonObject()));
			} catch (CodeGenException e) {
				LOGGER.error("Error while processing: ", e);
			}
		});
		
		LOGGER.debug("Cache Value: " + CodeGenProperties.getCacheInstance().getProperties());
		
		writeCacheProps();
		CodeGenProperties.putCacheValue(Constants.LOGIN_ROUTING_PATH_STATUS, null);
		
		return false;
	}

	/**
	 * @throws CodeGenException 
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void writeCacheProps() throws CodeGenException {
		FileOutputStream fos = null;
		
		try {
			String confPath = request.getCodeGenPath() + "/" + Constants.CODE_GEN_CONF;
			fos = new FileOutputStream(new File(confPath));
			
			Map<String, Object> props = CodeGenProperties.getCacheInstance().getProperties();
			CodeGenConfig config = new CodeGenConfig();
			config.setProps(props);
			config.setReactComponents((Map<String, Component>) props.get(Constants.REACT_COMPONENTS));
			
			props.remove(Constants.REACT_COMPONENTS);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(fos, config);
		} catch (JsonGenerationException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		} catch (JsonMappingException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		} catch (FileNotFoundException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		} catch (IOException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	protected void readAndLoadCacheProps() throws CodeGenException {
		FileOutputStream fos = null;
		
		try {
			String confPath = request.getCodeGenPath() + "/" + Constants.CODE_GEN_CONF;
			
			File confFile = new File(confPath);
			
			if (confFile.exists()) {
				ObjectMapper mapper = new ObjectMapper();
				CodeGenConfig config = mapper.readValue(confFile, CodeGenConfig.class);

				Map<String, Object> props = config.getProps();
				props.put(Constants.REACT_COMPONENTS, config.getReactComponents());

				CodeGenProperties.getCacheInstance().setProperties(props);
			}
		} catch (JsonGenerationException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		} catch (JsonMappingException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		} catch (FileNotFoundException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		} catch (IOException e) {
			LOGGER.error(e);
			throw new CodeGenException(e);
		}  finally {
			IOUtils.closeQuietly(fos);
		}
	}
}
